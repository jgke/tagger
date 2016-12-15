# Tagger

![Travis badge](https://travis-ci.com/jgke/tagger.svg?token=RqmiQF9vVBY1xsdDcmg4&branch=master)

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
