# URL Shortener

A simple URL Shortener web application built using **Spring Boot** and **MongoDB**. The application converts long URLs into short, unique links and redirects users to the original URL when the shortened link is accessed.

---

## Features

- Shorten any valid URL
- Generate unique short codes using SHA-256 hashing
- Store URL mappings in MongoDB
- Redirect shortened URLs to the original destination
- RESTful API implementation
- Collision handling for duplicate short codes

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data MongoDB
- Maven
- MongoDB

---

## Project Structure

```
src
├── main
│   ├── java
│   │   ├── Controller
│   │   ├── DTO
│   │   ├── Entity
│   │   ├── Repository
│   │   ├── Service
│   │   └── UrlShortenerApplication.java
│   └── resources
│       └── application.properties
└── test
```

---

## Prerequisites

- Java 17 or above
- Maven
- MongoDB running locally on port **27017**

---

## Configuration

Update `application.properties` if required.

```properties
server.port=8080

app.base-url=http://localhost:8080

spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=urlshortener
```

---

## Running the Application

### 1. Clone the repository

```bash
git clone <repository-url>
cd URLShortener
```

### 2. Start MongoDB

Make sure MongoDB is running locally.

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The server starts at:

```
http://localhost:8080
```

---

## API Endpoints

### Shorten a URL

**POST** `/shorten`

#### Request Body

```json
{
  "longUrl": "https://www.google.com"
}
```

#### Response

```json
{
  "shortUrl": "http://localhost:8080/Ab12Cd"
}
```

---

### Redirect to Original URL

**GET**

```
/{shortCode}
```

Example:

```
GET http://localhost:8080/Ab12Cd
```

Returns:

```
HTTP 302 Found
```

and redirects the browser to the original URL.

---

## How It Works

1. User submits a long URL.
2. The application generates a SHA-256 hash of the URL.
3. The first few characters of the encoded hash are used as the short code.
4. If a collision occurs, the code length is increased or a random character is appended.
5. The mapping is stored in MongoDB.
6. Accessing the shortened URL retrieves the original URL and redirects the user.

---

## Example Workflow

**Input**

```
https://www.google.com
```

↓

**Generated Short URL**

```
http://localhost:8080/Ab12Cd
```

↓

Opening the shortened URL redirects the user to:

```
https://www.google.com
```

---

## Future Enhancements

- Custom short URLs
- URL expiration
- Click analytics
- User authentication
- QR code generation
- Rate limiting

---

## Author

**Chandrini Kalaivanan**

Software Engineering Assignment – URL Shortener using Spring Boot and MongoDB.
