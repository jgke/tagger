# Tagger

[![Travis badge](https://travis-ci.org/jgke/tagger.svg?branch=master)](https://travis-ci.org/jgke/tagger)
[![Codebeat badge](https://codebeat.co/badges/37b5241f-87f1-4d42-9b39-05fb3abfc61f)](https://codebeat.co/projects/github-com-jgke-tagger)

An image tagging service backend. The app is running at [Heroku](http://tagger.jgke.fi).
Api documentation is [here](http://tagger.jgke.fi/swagger-ui).

## Running

```
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/tagger mvn spring-boot:run
```

## Testing

```
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/tagger mvn test
```
