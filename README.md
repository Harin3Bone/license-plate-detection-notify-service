# License plate detection notify service

## Description
This repository create for notify user to Discord when receive APIs request from license plate detection LLM system.

In this service are provide for Deep Learning and Application subject, at Kasetsart University.

## Features
- Message broker with RabbitMQ.
- Store image with MinIO.
- Log history with PostgreSQL.
- Notify user with Discord.

## Prerequisites
1. Docker and Docker Compose
2. Discord account, Discord server, Discord text channel and Discord bot.

## Installation (Demo)
For security reason, this repository won't provide any real API key, Discord token or URL.
You can create your own mock up by following the steps below:

### Discord Setup
1. If you don't have Discord account, please create one at [Discord](https://discord.com/).  
Once you've an account please create a Discord server and a text channel in that server.
2. If you don't have Discord bot, please create one at [Discord Developer](https://discord.com/developers/docs/intro).  
Once you've created a bot, please invite the bot to your server with appropriate permissions below.
     - Send Messages
     - Send Messages in Threads
     - Send TTS Messages

### PostgreSQL Setup
1. Base on `docker-compose.yml` file. At line 10-12 you may see credentials for PostgreSQL database,    
you can change it as you want or else just leave it as default to **postgres**
    ```yml
    environment:
      POSTGRES_USER: ${POSTGRES_ROOT_USER:-postgres}
      POSTGRES_PASSWORD: ${POSTGRES_ROOT_PASSWORD:-postgres}
    ```
2. Modify script `/script/postgres/04_init_user.sql` and `/script/postgres/run.sh` to create your own database user.
3. Run command below to start PostgreSQL container.
    ```bash
    docker compose up -d postgres
    ```
4. Run command below to access PostgreSQL container.
    ```bash
    docker exec -it dl_minio /bin/bash
    ```
5. Execute script inside PostgreSQL container to create database and table.
    ```bash
    /var/lib/scripts/run.sh
    ```
6. Once you've done, exit from PostgreSQL container.
    ```bash
    exit
    ```
### RabbitMQ Setup
1. Base on `docker-compose.yml` file. At line 21-23 you may see credentials for RabbitMQ,    
you can change it as you want or else just leave it as default to **rabbitmq**
   ```yml
    environment:
      RABBITMQ_DEFAULT_USER: ${RABBIT_ROOT_USER:-rabbitmq}
      RABBITMQ_DEFAULT_PASS: ${RABBIT_ROOT_PASSWORD:-rabbitmq}
   ```
2. Modify script `/script/rabbitmq/run.sh` to create your own user.
3. Run command below to start RabbitMQ container.
   ```bash
   docker compose up -d rabbitmq
   ```
4. Run command below to access RabbitMQ container.
   ```bash
   docker exec -it dl_rabbitmq /bin/bash
   ```
5. Execute script inside RabbitMQ container to create user and set permission.
   ```bash
   /var/lib/scripts/run.sh
   ```
6. Once you've done, exit from RabbitMQ container.
   ```bash
   exit
   ```

### MinIO Setup
1. Base on `docker-compose.yml` file. At line 33-35 you may see credentials for MinIO,
you can change it as you want or else just leave it as default to **minioadmin** and **miniopassword**
   ```yml
    environment:
      MINIO_ROOT_USER: ${MINIO_ROOT_USER:-minioadmin}
      MINIO_ROOT_PASSWORD: ${MINIO_ROOT_PASSWORD:-miniopassword}
   ```
2. Modify script `/script/minio/run.sh` to create your own user.
3. Run command below to start MinIO container.
   ```bash
   docker compose up -d minio
   ```
4. Run command below to access MinIO container.
   ```bash
   docker exec -it dl_minio /bin/bash
   ```
5. Execute script inside MinIO container to create bucket.
   ```bash
   /var/lib/scripts/run.sh
   ```
6. Once you've done, exit from MinIO container.
   ```bash
   exit
    ```

### Notify Service Setup
1. Base on `docker-compose.yml` file. At line 51-52 you may use super-user of PostgreSQL or your own user from your postgreSQL setup.
   ```yml
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER:-postgres}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD:-postgres}
   ```
2. Base on `docker-compose.yml` file. At line 55-56 you may use administrator of RabbitMQ or your own user from your RabbitMQ setup.
   ```yml
      RABBIT_USERNAME: ${RABBIT_USER:-rabbitmq}
      RABBIT_PASSWORD: ${RABBIT_PASSWORD:-rabbitmq}
   ```
3. Base on `docker-compose.yml` file. At line 58-59 you may use super-user of MinIO or your own user from your MinIO setup.
   ```yml
      MINIO_ACCESSKEY: ${MINIO_USER:-minioadmin}
      MINIO_SECRETKEY: ${MINIO_PASSWORD:-miniopassword}
   ```
4. Base on `docker-compose.yml` file. At line 61-64 you need to set your own API key, Discord channel ID and Discord bot token.
   ```yml
      API_KEY_NAME: ${API_KEY}
      API_KEY_VALUE: ${API_VALUE}
      DISCORD_CHANNEL: ${DISCORD_CHANNEL_ID}
      DISCORD_TOKEN: ${DISCORD_BOT_TOKEN}
   ```
5. Once you've done all the steps above, run command below to start notify service container.
   ```bash
   docker compose up -d app
   ```

## References

- [Docker](https://docs.docker.com/)
- [Rancher](https://rancherdesktop.io/)
- [Discord Developer](https://discord.com/developers/docs/intro)
- [Discord4J](https://github.com/Discord4J/Discord4J)
- [Spring Boot with MinIO](https://www.baeldung.com/minio)
- [Spring Boot with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq)