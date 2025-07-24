package com.lucas.plinks;

import com.lucas.plinks.exception.InvalidLinkException;
import com.lucas.plinks.exception.LinkAlreadyExpiresException;
import com.lucas.plinks.exception.SlugAlreadyExistsException;
import com.lucas.plinks.exception.SlugNotFoundException;
import io.nayuki.qrcodegen.QrCode;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkResponseDTO shortUrl(LinkRequestDTO requestDTO) throws IOException {
        if (!requestDTO.url().startsWith("https://") || isInvalidLink(requestDTO.url()))
            throw new InvalidLinkException();

        String slug;

        if (requestDTO.slug() == null) {
            slug = getRandomAndUniqueSlug();
        } else {
            slug = requestDTO.slug().replace(" ", "");
            if (linkRepository.existsBySlug(slug)) throw new SlugAlreadyExistsException();
        }

        String shortenedLink = Constants.BASE_URL + "/" + slug;

        LocalDateTime expiresAt = requestDTO.expiresAfter() != null ? LocalDateTime.now().plusSeconds(requestDTO.expiresAfter()) : null;
        byte[] qrCode = Boolean.TRUE.equals(requestDTO.generateQrCode()) ? generateQrCode(shortenedLink) : null;

        Link link = new Link(null, requestDTO.url(), slug, expiresAt, qrCode, 0);

        linkRepository.save(link);

        return new LinkResponseDTO(shortenedLink, qrCode);
    }

    private String getRandomAndUniqueSlug() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String slug;

        do {
            StringBuilder randomSlug = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < Constants.SLUG_MAX_SIZE; i++) {
                char randomChar = chars.charAt(random.nextInt(chars.length()));
                randomSlug.append(randomChar);
            }
            slug = randomSlug.toString();
        } while (linkRepository.existsBySlug(slug));
        return slug;
    }

    public String redirect(String slug) {
        Optional<Link> optionalLink = linkRepository.findBySlug(slug);

        if (optionalLink.isEmpty())
            throw new SlugNotFoundException();

        Link link = optionalLink.get();

        if (link.expiresAt != null && link.expiresAt.isBefore(LocalDateTime.now()))
            throw new LinkAlreadyExpiresException(link.expiresAt);


        link.clicks = link.clicks + 1;
        linkRepository.save(link);

        return link.url;
    }

    private boolean isInvalidLink(String link) {
        try {
            new URL(link);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public int getClicksFromShortenedLink(SlugRequestDTO slug) {
        Optional<Link> link = linkRepository.findBySlug(slug.slug().replace(" ", ""));
        if (link.isEmpty()) throw new SlugNotFoundException();
        return link.get().clicks;
    }

    private byte[] generateQrCode(String url) throws IOException {
        QrCode qrCode = QrCode.encodeText(url, QrCode.Ecc.MEDIUM);
        return toImage(qrCode, 4, 10, 0xFFFFFF, 0x000000);
    }

    public byte[] toImage(QrCode qr, int scale, int border, int lightColor, int darkColor) throws IOException {
        Objects.requireNonNull(qr);
        if (scale <= 0 || border < 0) {
            throw new IllegalArgumentException("Value out of range");
        }
        if (border > Integer.MAX_VALUE / 2 || qr.size + border * 2L > Integer.MAX_VALUE / scale) {
            throw new IllegalArgumentException("Scale or border too large");
        }

        BufferedImage result = new BufferedImage(
                (qr.size + border * 2) * scale,
                (qr.size + border * 2) * scale,
                BufferedImage.TYPE_INT_RGB
        );
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / scale - border, y / scale - border);
                result.setRGB(x, y, color ? darkColor : lightColor);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(result, "png", baos);

        return baos.toByteArray();
    }
}
