# Casa Gabriel — Website Configurator

Spring Boot backend + Angular frontend with inline CMS, served behind Caddy (automatic HTTPS) with MinIO (S3-compatible) object storage and DeepL AI translation.

## Architecture

```
Browser → Caddy:443 (HTTPS, auto Let's Encrypt)
            ├── /              → Angular SPA (static files)
            ├── /api/          → Spring Boot backend
            ├── /storage/      → MinIO (public image access)
            └── /actuator/     → Health endpoint
```

## Features

- **Inline CMS** — edit text and images directly on the page preview
- **AI Translation** — edit in one language, auto-translate to all others via DeepL
- **Image Management** — upload, confirm/revert, stored in S3-compatible storage
- **Multi-language** — English, Spanish, Hungarian (configurable)
- **Sites Management** — activate/deactivate pages
- **Docker Compose** — full production stack with Caddy, PostgreSQL, MinIO

## Prerequisites

- Docker & Docker Compose v2
- Git
- A domain name pointed to your server (for HTTPS in production)
- DeepL API key (free tier: https://www.deepl.com/pro-api) — optional, manual translation fallback available

## Project Structure

```
casa_gabriel/
├── casa_gabriel_ui/         # Angular 19 frontend
│   ├── Dockerfile
│   └── src/
├── website_configurator/    # Spring Boot 3 backend
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── Caddyfile
│   ├── .env.example
│   └── src/
```

---

## Local Development (Docker Compose)

### 1. Clone and navigate

```bash
git clone <repo-url>
cd casa_gabriel/website_configurator
```

### 2. Create environment file

```bash
cp .env.example .env
```

For local dev, set `DOMAIN=localhost`:

```env
POSTGRES_DB=website_configurator
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin
STORAGE_ENDPOINT=http://minio:9000
STORAGE_ACCESS_KEY=minioadmin
STORAGE_SECRET_KEY=minioadmin
STORAGE_BUCKET_NAME=website-configurator
STORAGE_PUBLIC_URL=/storage
STORAGE_REGION=us-east-1
SERVER_PORT=8080
DOMAIN=localhost
DEEPL_API_KEY=your-deepl-api-key-here
TRANSLATION_TARGET_LANGS=EN,ES,HU
```

### 3. Start everything

```bash
docker compose up --build
```

### 4. Access

| Service | URL |
|---------|-----|
| Application | http://localhost |
| Admin Panel | http://localhost/admin |
| API | http://localhost/api/ |
| Swagger UI | http://localhost/api/swagger-ui.html |
| Uploaded images | http://localhost/storage/{filename} |

### 5. Stop

```bash
docker compose down
```

To also remove volumes (wipes DB and uploaded files):

```bash
docker compose down -v
```

---

## Local Development (without Docker — backend only)

### Prerequisites

- Java 17+
- Node 22+ and npm
- PostgreSQL running on localhost:5432
- MinIO running on localhost:9000

### Run only infra with Docker

```bash
docker compose up db minio minio-init -d
```

### Run backend

```bash
./gradlew bootRun
```

Backend starts on `http://localhost:8080`.

### Run frontend

```bash
cd ../casa_gabriel_ui
npm install
npm start
```

Angular dev server starts on `http://localhost:4200`.

---

## VPS Deployment (Production)

### 1. Server requirements

- Linux VPS (Ubuntu 22.04+ recommended)
- Docker & Docker Compose v2 installed
- At least 2GB RAM, 20GB disk
- Ports 80 and 443 open in firewall
- A domain name with DNS A record pointed to your VPS IP

### 2. Upload project to VPS

```bash
scp -r casa_gabriel/ user@your-vps-ip:/opt/casa_gabriel
```

Or clone from git:

```bash
ssh user@your-vps-ip
cd /opt
git clone <repo-url> casa_gabriel
```

### 3. Configure environment

```bash
cd /opt/casa_gabriel/website_configurator
cp .env.example .env
nano .env
```

Set production values:

```env
POSTGRES_DB=website_configurator
POSTGRES_USER=casa_gabriel_db
POSTGRES_PASSWORD=<strong-random-password>
MINIO_ROOT_USER=<strong-random-username>
MINIO_ROOT_PASSWORD=<strong-random-password>
STORAGE_ENDPOINT=http://minio:9000
STORAGE_ACCESS_KEY=<strong-random-key>
STORAGE_SECRET_KEY=<strong-random-secret>
STORAGE_BUCKET_NAME=website-configurator
STORAGE_PUBLIC_URL=/storage
STORAGE_REGION=us-east-1
SERVER_PORT=8080
DOMAIN=yourdomain.com
DEEPL_API_KEY=<your-deepl-api-key>
TRANSLATION_TARGET_LANGS=EN,ES,HU
```

### 4. Build and start

```bash
docker compose up --build -d
```

Caddy will automatically obtain a Let's Encrypt TLS certificate, redirect HTTP→HTTPS, and renew before expiry.

### 5. Verify

```bash
docker compose ps
curl https://yourdomain.com/actuator/health
```

---

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `POSTGRES_DB` | Database name | website_configurator |
| `POSTGRES_USER` | DB username | postgres |
| `POSTGRES_PASSWORD` | DB password | postgres |
| `MINIO_ROOT_USER` | MinIO admin user | minioadmin |
| `MINIO_ROOT_PASSWORD` | MinIO admin password | minioadmin |
| `STORAGE_ENDPOINT` | Internal S3 endpoint | http://minio:9000 |
| `STORAGE_ACCESS_KEY` | S3 access key | minioadmin |
| `STORAGE_SECRET_KEY` | S3 secret key | minioadmin |
| `STORAGE_BUCKET_NAME` | S3 bucket name | website-configurator |
| `STORAGE_PUBLIC_URL` | Public URL prefix for images | /storage |
| `STORAGE_REGION` | S3 region | us-east-1 |
| `SERVER_PORT` | Backend port | 8080 |
| `DOMAIN` | Domain for Caddy HTTPS | localhost |
| `DEEPL_API_KEY` | DeepL API key for translations | (empty) |
| `TRANSLATION_TARGET_LANGS` | Comma-separated language codes | EN,ES,HU |

---

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/admin/sites` | Get all sites |
| PUT | `/api/admin/sites/{id}` | Update site |
| GET | `/api/admin/texts?siteId=` | Get texts by site |
| POST | `/api/admin/texts` | Create text |
| PUT | `/api/admin/texts/{id}` | Update text |
| DELETE | `/api/admin/texts/{id}` | Delete text |
| POST | `/api/admin/texts/translate` | Save with AI translation |
| POST | `/api/admin/texts/saveAll` | Save all languages manually |
| GET | `/api/admin/pictures?siteId=` | Get pictures by site |
| POST | `/api/admin/pictures` | Create picture |
| POST | `/api/admin/pictures/upload` | Upload picture file |
| DELETE | `/api/admin/pictures/{id}` | Delete picture |
| POST | `/api/admin/siteImages/upload` | Upload site image (staged) |
| POST | `/api/admin/siteImages/confirm` | Confirm staged image |
| POST | `/api/admin/siteImages/revert` | Revert staged image |
| GET | `/api/admin/siteImages` | Get all site images |
| GET | `/api/public/texts?lang=` | Get text overrides by language |

---

## Useful Commands

```bash
# Rebuild a single service
docker compose up --build app -d

# View logs
docker compose logs -f caddy
docker compose logs -f app

# Restart a service
docker compose restart app

# Access database
docker compose exec db psql -U postgres -d website_configurator

# Force certificate renewal
docker compose exec caddy caddy reload --config /etc/caddy/Caddyfile
```

---

## Backup

### Database

```bash
docker compose exec db pg_dump -U postgres website_configurator > backup_$(date +%Y%m%d).sql
```

### MinIO data

```bash
docker run --rm -v website_configurator_miniodata:/data -v $(pwd):/backup alpine tar czf /backup/minio_backup_$(date +%Y%m%d).tar.gz /data
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `app` fails to start | Check logs: `docker compose logs app`. Likely DB not ready — increase `start_period`. |
| Images return 403 | `minio-init` may not have run. Check: `docker compose logs minio-init`. |
| Frontend shows blank page | Check `docker compose logs frontend` — Angular build may have failed. |
| Certificate not issued | Ensure DNS A record points to VPS IP and ports 80/443 are open. |
| Translation fails | Check `DEEPL_API_KEY` is set. Manual fallback is available. |
| 500 on image upload | Ensure MinIO is running and bucket exists: `docker compose up minio-init`. |
