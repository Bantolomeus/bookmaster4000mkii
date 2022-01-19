# Bookmaster 4000 MARK II _(bookmaster4000mkii)_

![CI passing](https://github.com/Bantolomeus/bookmaster4000mkii/actions/workflows/ci.yml/badge.svg)

> Improve your reading habits. Api to document your books read and your daily reading progress.
>
> Featuring: Kotlin :cookie: Spring :cookie: ReScript

### Install

`$ mvn package` to compile, test and build (including frontend)

`$ mvn test` to execute unit tests separately.

**Develop Frontend**

`$ npm install` to get all UI dependencies

You want to keep running two processes
1. `npm run re:start`
2. and in another tab `npm run server`

**Dependencies**

- Apache Maven 3.6
- Kotlin 1.6
- Java 1.8
- node 12.20
- npm 6.14

### Usage

`$ java -jar target/bookmaster4000mkii-[VERSION]-SNAPSHOT.jar`

You should see an output similar to this

```
...
2018-05-25 18:03:39.586   s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2018-05-25 18:03:39.591   c.b.Bookmaster4000mkiiApplicationKt      : Started Bookmaster4000mkiiApplicationKt in 3.535 seconds (JVM running for 4.09)
```

The app is now running on `localhost:8080/`

### API

Find a description of the api endpoints under [`src/main/kotlin/com/bantolomeus/controller`](src/main/kotlin/com/bantolomeus/controller).

I recommend a tool like Postman to set up the http calls. For detailed Api call description, see scripts in the API directory.

### CI

Every commits gets built, tested and quality checked by a GitHub workflow.

For downloading CI workflow artifacts, go to the [repo's action tab](https://github.com/Bantolomeus/bookmaster4000mkii/actions) and download from a specific job's _Artifacts_ section.

[Codacy](https://github.com/marketplace/actions/codacy-analysis-cli) does the code quality check. Find results in the [repo's security tab](https://github.com/Bantolomeus/bookmaster4000mkii/security/code-scanning).

### Maintainers

- [Bantolomeus](https://github.com/Bantolomeus)
- [Husterknupp](https://github.com/Husterknupp)

### Contribute

Please check out the repo, use it yourself and come back with feature requests/bug tickets

Also on any open issue we would be happy to get some help. So far they're not labeled with a 'good for beginner' label.

Since this is also about learning Kotlin, please also leave remarks on the coding style used here (in form of comments on the commits or of opening issues).

### License

Apache License Version 2.0
