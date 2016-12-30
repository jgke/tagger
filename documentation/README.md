Tagger documentation
====================

Tagger is a small service which can be used to upload links and tag them with
tags. Tagger works in the Heroku cloud using PostgreSQL as the backing
database. The software is a Spring-boot stack, using Cobertura for coverage,
spring-data-jpa for JPA backing, Junit and HtmlUnit for testing and Liquibase
for migrations.

Feature overview:
- Users can submit links
- Users can tag submitted links
- Users can comment on links
- Tags can be searched with a simple syntax
   - "cat cute !dog" will match all links tagged with both "cat" and "cute",
     but do not include the tag "dog"
- Browsing and searching does not require authentication, but posting new links
  does
- Normal users can only delete their own links
    - Admin users can delete all links
- All users can manage tags
- Complete Travis-Heroku integration
  - Commits in master go automatically into Travis, and if it passes, to Heroku
  - Liquibase (or rather, raw SQL) is used for database migrations

Setting up:
-----------
Create a postgres database (you need postgresql up and running)
```shell
psql -c "create database tagger;"
```
Run the software
```shell
export JDBC_DATABACE_URL=jdbc:postgresql:tagger
mvn spring-boot:run
```
Do changes and test them
```shell
mvn test
```
Commit & push (the commit will go through Travis to Heroku)

Todo features:
--------------

- [ ] Error messages from registering with duplicate data
- [ ] Better UI for tags, the current one is slightly buggy with longer tags
- [ ] Better tests, currently the system runs on some unit tests and system tests
- [ ] Thumbnailing the search page
- [ ] Link ratings, in the thumbs up/down style
- [ ] Profile pages, including list of submitted links, password changes...
- [ ] Caching, the app is pretty fast right now but it won't scale hard...
- [ ] Database indices, same as above.
- [ ] Tag searching on database level, current implementation is Java-based
- [ ] Attach user information to tags, leading to...
- [ ] List users based on links, tags, comments and ratings
- [ ] Suggested tags based on existing tags (If tagged with 'cat', suggest 'cute')
