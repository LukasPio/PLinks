# PLinks

API REST de encurtamento de URLs desenvolvida em **Java 21 e Spring Boot 3.5**. Cria links curtos com identificadores aleatórios ou personalizados, redireciona para a URL original, contabiliza acessos e permite definir expiração. Também pode gerar um QR Code para o link criado.

## O que foi implementado

- Validação de URL HTTPS e verificação de colisão de slug.
- Slug aleatório de oito caracteres ou slug informado na requisição.
- Expiração opcional em segundos, com resposta de link expirado.
- Contagem de redirecionamentos e consulta de cliques.
- QR Code codificado como PNG e retornado em bytes (Base64 no JSON).
- Persistência com Spring Data JPA/PostgreSQL e migrações com Flyway.
- Tratamento centralizado para URL inválida, slug duplicado, inexistente e expirado.

O código separa controller, serviço, repositório, DTOs e exceções em `src/main/java/com/lucas/plinks/`. As migrações estão em `src/main/resources/db/migration/`.

## Rotas

| Método | Caminho | Resultado |
| --- | --- | --- |
| `POST` | `/short` | Cria o link e retorna `shortenedUrl` e, se solicitado, `qrCode` |
| `GET` | `/{slug}` | Responde `302 Found` com o destino no cabeçalho `Location` |
| `POST` | `/clicks` | Consulta a contagem de acessos de um slug |

Exemplo de criação:

```bash
curl -s -X POST http://localhost:8080/short \
  -H 'Content-Type: application/json' \
  -d '{"url":"https://example.com","slug":"exemplo","expiresAfter":3600,"generateQrCode":false}'
```

`slug`, `expiresAfter` e `generateQrCode` são opcionais. Sem slug informado, a API gera um identificador. `expiresAfter` é medido em segundos. Para consultar cliques:

```bash
curl -s -X POST http://localhost:8080/clicks \
  -H 'Content-Type: application/json' \
  -d '{"slug":"exemplo"}'
```

O endereço base dos links é `http://localhost:8080` no código atual; este projeto está configurado para demonstração local.

## Executar localmente

Requisitos: **JDK 21**, Docker com Compose e Maven (ou Maven Wrapper). Com a porta 5432 livre:

```bash
docker compose up -d
./mvnw spring-boot:run
```

O Compose inicia um PostgreSQL para desenvolvimento; `application.yml` contém valores locais correspondentes. Antes de publicar em outro ambiente, configure credenciais próprias e revise o endereço base e a configuração de CORS. O projeto não inclui uma implantação pública pronta.

## Escopo

A API demonstra modelagem, persistência, migrações e respostas HTTP. Ela não implementa contas de usuário, autenticação ou métricas por usuário. O [código da interface web](https://github.com/LukasPio/PLinksFrontEnd) está em repositório separado.
