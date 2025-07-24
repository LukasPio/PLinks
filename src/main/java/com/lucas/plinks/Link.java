package com.lucas.plinks;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "links")
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String url;

    @Column(nullable = false, unique = true)
    String slug;

    @Column(name = "expires_at")
    LocalDateTime expiresAt;

    @Lob
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "qr_code")
    byte[] qrCode;

    @Column(nullable = false)
    int clicks;
}