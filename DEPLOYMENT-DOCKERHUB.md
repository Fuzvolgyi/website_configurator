# Deployment Guide — GitHub Container Registry (ghcr.io)

This guide deploys Casa Gabriel using pre-built images from GitHub Container Registry.
No source code is needed on the server — only the compose file, Caddyfile, and `.env`.

## Overview

```
Dev machine                          ghcr.io                          VPS
───────────                          ───────                          ───────────
docker build → docker push ────→  private packages  ←──── docker pull ← docker compose up
```

## Prerequisites

- GitHub account
- Personal Access Token (PAT) with `write:packages` and `read:packages` scope
- Docker installed on your dev machine and the VPS
- Domain name pointed to your VPS IP (for HTTPS)

## Step 0: Create GitHub Personal Access Token

1. Go to https://github.com/settings/tokens?type=beta (Fine-grained tokens)
   - Or classic tokens: https://github.com/settings/tokens/new
2. For classic tokens, select scopes: `write:packages`, `read:packages`, `delete:packages`
3. Copy the token — you'll use it as your password for `docker login`

## Step 1: Build & Push Images (Dev Machine)

### Login to ghcr.io

```bash
echo "YOUR_GITHUB_PAT" | docker login ghcr.io --username YOUR_GITHUB_USERNAME --password-stdin
```

### Build and push all images

```bash
# Set your GitHub username (lowercase!)
export GITHUB_USERNAME=yourusername

# Backend (website_configurator)
cd website_configurator
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest

# Frontend (casa_gabriel_ui)
cd ../casa_gabriel_ui
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest
```

### Tagged releases (recommended for rollbacks)

```bash
export VERSION=1.0.0

# Backend
cd website_configurator
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:$VERSION .
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:$VERSION
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest

# Frontend
cd ../casa_gabriel_ui
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:$VERSION .
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:$VERSION
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest
```

### Make packages private (first push only)

After your first push, packages are public by default. To make them private:
1. Go to https://github.com/YOUR_USERNAME?tab=packages
2. Click each package → Package settings → Danger Zone → Change visibility → Private

## Step 2: Server Setup (One-time)

### Install Docker

```bash
ssh root@YOUR_VPS_IP

# Install Docker
curl -fsSL https://get.docker.com | sh

# Create deploy user
adduser deploy
usermod -aG docker deploy
```

### Create deployment directory

```bash
su - deploy
mkdir -p ~/casa-gabriel
cd ~/casa-gabriel
```

### Create docker-compose.hub.yml

Create this file on the server (or copy from dev machine):

```yaml
services:
  caddy:
    image: caddy:2-alpine
    ports:
      - "80:80"
      - "443:443"
      - "443:443/udp"
    volumes:
      - ./Caddyfile:/etc/caddy/Caddyfile:ro
      - frontend_dist:/srv:ro
      - caddy_data:/data
      - caddy_config:/config
    environment:
      - DOMAIN=${DOMAIN}
    depends_on:
      app:
        condition: service_healthy
      frontend:
        condition: service_completed_successfully
      minio:
        condition: service_started

  frontend:
    image: ghcr.io/${GITHUB_USERNAME}/casa-gabriel-ui:${TAG:-latest}
    volumes:
      - frontend_dist:/output
    entrypoint: ["sh", "-c", "cp -r /usr/share/nginx/html/* /output/"]

  app:
    image: ghcr.io/${GITHUB_USERNAME}/casa-gabriel-backend:${TAG:-latest}
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}
      - DB_USERNAME=${POSTGRES_USER}
      - DB_PASSWORD=${POSTGRES_PASSWORD}
      - STORAGE_ENDPOINT=${STORAGE_ENDPOINT}
      - STORAGE_ACCESS_KEY=${STORAGE_ACCESS_KEY}
      - STORAGE_SECRET_KEY=${STORAGE_SECRET_KEY}
      - STORAGE_BUCKET_NAME=${STORAGE_BUCKET_NAME}
      - STORAGE_PUBLIC_URL=${STORAGE_PUBLIC_URL}
      - STORAGE_REGION=${STORAGE_REGION}
      - SERVER_PORT=${SERVER_PORT}
      - DEEPL_API_KEY=${DEEPL_API_KEY}
      - TRANSLATION_TARGET_LANGS=${TRANSLATION_TARGET_LANGS}
    depends_on:
      db:
        condition: service_healthy
      minio-init:
        condition: service_completed_successfully
    healthcheck:
      test: ["CMD-SHELL", "wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1"]
      interval: 10s
      timeout: 5s
      start_period: 30s
      retries: 5

  db:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "127.0.0.1:5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER}"]
      interval: 5s
      timeout: 5s
      retries: 5

  minio:
    image: minio/minio:latest
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: ${MINIO_ROOT_USER}
      MINIO_ROOT_PASSWORD: ${MINIO_ROOT_PASSWORD}
    ports:
      - "127.0.0.1:9001:9001"
    volumes:
      - miniodata:/data
    healthcheck:
      test: ["CMD", "mc", "ready", "local"]
      interval: 5s
      timeout: 5s
      retries: 5

  minio-init:
    image: minio/mc:latest
    entrypoint: >
      /bin/sh -c "
      mc alias set local http://minio:9000 ${MINIO_ROOT_USER} ${MINIO_ROOT_PASSWORD};
      mc mb --ignore-existing local/${STORAGE_BUCKET_NAME};
      mc anonymous set download local/${STORAGE_BUCKET_NAME};
      exit 0;
      "
    depends_on:
      minio:
        condition: service_healthy

volumes:
  pgdata:
  miniodata:
  frontend_dist:
  caddy_data:
  caddy_config:
```

### Copy files to server

From your dev machine:

```bash
scp website_configurator/Caddyfile deploy@YOUR_VPS_IP:~/casa-gabriel/Caddyfile
scp website_configurator/.env.example deploy@YOUR_VPS_IP:~/casa-gabriel/.env.example
```

### Server folder structure

```
~/casa-gabriel/
├── docker-compose.yml    (the hub version above)
├── Caddyfile
└── .env                  (created from .env.example)
```

### Configure environment

```bash
cd ~/casa-gabriel
cp .env.example .env
nano .env
```

Required `.env` values:

```env
# GitHub Container Registry
GITHUB_USERNAME=yourusername
TAG=latest

# Domain (for Caddy HTTPS)
DOMAIN=yourdomain.com

# Database
POSTGRES_DB=website_configurator
POSTGRES_USER=casa_gabriel_db
POSTGRES_PASSWORD=strong-random-password-here

# MinIO / S3
MINIO_ROOT_USER=strong-random-username
MINIO_ROOT_PASSWORD=strong-random-password
STORAGE_ENDPOINT=http://minio:9000
STORAGE_ACCESS_KEY=strong-random-key
STORAGE_SECRET_KEY=strong-random-secret
STORAGE_BUCKET_NAME=website-configurator
STORAGE_PUBLIC_URL=/storage
STORAGE_REGION=us-east-1

# App
SERVER_PORT=8080

# Translation (DeepL)
DEEPL_API_KEY=your-deepl-api-key
TRANSLATION_TARGET_LANGS=EN,ES,HU
```

### Login to ghcr.io on server

```bash
echo "YOUR_GITHUB_PAT" | docker login ghcr.io --username yourusername --password-stdin
```

Docker stores credentials in `~/.docker/config.json` — you only need to do this once.

## Step 3: Deploy

```bash
cd ~/casa-gabriel
docker compose up -d
```

Docker pulls the private images from ghcr.io and starts everything. Caddy automatically obtains a TLS certificate.

### Verify

```bash
# Check all services
docker compose ps

# Test endpoints
curl https://yourdomain.com/actuator/health
curl -I https://yourdomain.com/
```

## Updating the Deployment

On your **dev machine**, build and push new images:

```bash
export GITHUB_USERNAME=yourusername

# Backend
cd website_configurator
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest

# Frontend
cd ../casa_gabriel_ui
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest
```

On the **VPS**, pull and restart:

```bash
cd ~/casa-gabriel
docker compose pull
docker compose up -d
```

### Deploy a specific version

```bash
# Dev machine
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:1.2.0 .
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:1.2.0

# VPS: update TAG in .env
nano .env  # TAG=1.2.0
docker compose pull
docker compose up -d
```

### Rollback

```bash
nano .env  # TAG=previous-version
docker compose pull
docker compose up -d
```

## Quick Deploy Script (Dev Machine)

Save as `deploy.sh` in your project root:

```bash
#!/bin/bash
set -e

GITHUB_USERNAME="${GITHUB_USERNAME:?Set GITHUB_USERNAME}"
TAG="${1:-latest}"
VPS_HOST="${VPS_HOST:?Set VPS_HOST}"

echo "Building images with tag: $TAG"

# Build all
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:$TAG ./website_configurator
docker build -t ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:$TAG ./casa_gabriel_ui

# Also tag as latest
docker tag ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:$TAG ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest
docker tag ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:$TAG ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest

# Push all
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:$TAG
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-backend:latest
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:$TAG
docker push ghcr.io/$GITHUB_USERNAME/casa-gabriel-ui:latest

# Deploy on VPS
ssh deploy@$VPS_HOST "cd ~/casa-gabriel && docker compose pull && docker compose up -d"

echo "Deployed version $TAG"
```

Usage:

```bash
export GITHUB_USERNAME=yourusername
export VPS_HOST=your.vps.ip
./deploy.sh 1.0.0
```

## Useful Commands (VPS)

```bash
cd ~/casa-gabriel

# View logs
docker compose logs -f app
docker compose logs -f caddy

# Restart a service
docker compose restart app

# Resource usage
docker stats

# Access database
docker exec -it casa-gabriel-db-1 psql -U postgres -d website_configurator

# Backup database
docker exec casa-gabriel-db-1 pg_dump -U postgres website_configurator > backup_$(date +%Y%m%d).sql

# Backup MinIO data
docker run --rm -v casa-gabriel_miniodata:/data -v $(pwd):/backup alpine tar czf /backup/minio_backup_$(date +%Y%m%d).tar.gz /data

# Clean up old images
docker image prune -f
```

## Firewall

```bash
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp
ufw enable
```

## Accessing Database Remotely (DBeaver)

DB port is bound to `127.0.0.1` on the VPS — not exposed to the internet. Use an SSH tunnel.

### DBeaver built-in SSH tunnel

1. New Connection → PostgreSQL
2. **Main tab:**
   - Host: `127.0.0.1`
   - Port: `5432`
   - Database: `website_configurator`
   - User: from your `.env` (`POSTGRES_USER`)
   - Password: from your `.env` (`POSTGRES_PASSWORD`)
3. **SSH tab:**
   - Enable SSH tunnel
   - Host: your VPS IP
   - Port: `22`
   - User: `deploy`
   - Authentication: password or key
4. Test Connection

### Manual SSH tunnel

```bash
ssh -L 15432:127.0.0.1:5432 deploy@YOUR_VPS_IP
```

Then connect to `localhost:15432` in DBeaver.

## MinIO Console Access

MinIO console is bound to `127.0.0.1:9001`. Access via SSH tunnel:

```bash
ssh -L 9001:127.0.0.1:9001 deploy@YOUR_VPS_IP
```

Then open `http://localhost:9001` in your browser. Login with `MINIO_ROOT_USER` / `MINIO_ROOT_PASSWORD`.
