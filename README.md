# Easy notes API <!-- omit from toc -->

Api REST de CRUD de notas usando Spring Boot e MariaDB. Projeto feito para estudar Spring Hateoas.

## Features

- [x] Crud completo de notas
- [x] Respostas usando hateoas

## Contrato da API

``` yaml
openapi: 3.0.1
info:
  title: OpenAPI definition
  version: v0
servers:
- url: http://localhost:8080
  description: Generated server url
paths:
  /v1/notes/{id}:
    get:
      tags:
      - notes-controller
      operationId: getNoteById
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: '#/components/schemas/NoteDetailsDto'
    put:
      tags:
      - notes-controller
      operationId: updateNote
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/NoteForm'
        required: true
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: '#/components/schemas/NoteDto'
    delete:
      tags:
      - notes-controller
      operationId: deleteNote
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      responses:
        "200":
          description: OK
  /v1/notes:
    get:
      tags:
      - notes-controller
      operationId: getAllNotes
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: '#/components/schemas/CollectionModelNoteDto'
    post:
      tags:
      - notes-controller
      operationId: createNote
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/NoteForm'
        required: true
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: '#/components/schemas/NoteDto'
  /v1:
    get:
      tags:
      - root-controller
      operationId: root
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: '#/components/schemas/EntryPointDto'
components:
  schemas:
    NoteForm:
      required:
      - content
      - title
      type: object
      properties:
        title:
          type: string
        content:
          type: string
    Link:
      type: object
      properties:
        rel:
          type: string
        href:
          type: string
        hreflang:
          type: string
        media:
          type: string
        title:
          type: string
        type:
          type: string
        deprecation:
          type: string
        profile:
          type: string
        name:
          type: string
    NoteDto:
      type: object
      properties:
        title:
          type: string
        content:
          type: string
        links:
          type: array
          items:
            $ref: '#/components/schemas/Link'
    EntryPointDto:
      type: object
      properties:
        links:
          type: array
          items:
            $ref: '#/components/schemas/Link'
    CollectionModelNoteDto:
      type: object
      properties:
        links:
          type: array
          items:
            $ref: '#/components/schemas/Link'
        content:
          type: array
          items:
            $ref: '#/components/schemas/NoteDto'
    NoteDetailsDto:
      type: object
      properties:
        title:
          type: string
        content:
          type: string
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time
        links:
          type: array
          items:
            $ref: '#/components/schemas/Link'
```
## Endpoints da Aplicação

### /v1/notes/{id}

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | long   |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | long   |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | long   |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /v1/notes

#### GET

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /v1

#### GET

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

Coleção para testar os endpoints da aplicação

[![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=easy-notes-api&uri=https%3A%2F%2Fraw.githubusercontent.com%2Fgasfgrv%2Feasy-notes-api%2Fmaster%2Feasy-notes%2Fcollection.json)

## Pré-requisitos e como rodar a aplicação/testes

Criar arquivo docker-compose.yaml:

```yaml
version: "3.7"
services:
  mariadb:
    container_name: notes_db
    image: mariadb:10.7
    restart: unless-stopped
    networks:
      - easy-notes-network
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_PASSWORD: root
      MYSQL_USER: root
      MYSQL_DATABASE: notes-api

networks:
  easy-notes-network:
    driver: bridge
```
Rodar os seguintes comados

```sh
# Subir container do MariaDB
docker-compose up -d

# Baixar a imagem
docker pull gustosilva/easy-notes-api:latest

# Gerar o containter
docker run --network easy-notes_easy-notes-network \
  --env DB_USER=root \
  --env DB_PASSWORD=root \
  -p 8080:8080 gustosilva/easy-notes-api:latest
```

## Tecnologias utilizadas

Projeto feito usando **Java 1.8** e **Maven 3.8** como ferramenta de build.

## Dependencias

- spring-boot-starter-data-jpa
- spring-boot-starter-web
- slf4j-api
- logback-classic
- logback-core
- spring-boot-devtools
- spring-boot-starter-test
- spring-boot-starter-hateoas
- spring-boot-starter-validation
- mariadb-java-client
- modelmapper
- springdoc-openapi-ui

## Autor

<div>
    <p>Feito por Gustavo Silva:</p>
    <a href="https://www.linkedin.com/in/gustavo-silva-69b84a15b/"><img src="https://img.shields.io/badge/linkedin-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" height=25></a>
    <a href="https://discordapp.com/users/616994765065420801"><img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" height=25></a>
    <a href="mailto:gustavoalmeidasilva41@gmail.com"><img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" height=25></a>
    <a href="mailto:gustavo_almeida11@hotmail.com"><img src="https://img.shields.io/badge/Microsoft_Outlook-0078D4?style=for-the-badge&logo=microsoft-outlook&logoColor=white" height=25></a>
</div>