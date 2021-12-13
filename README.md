# Bookmaster 4000 MARK II _(bookmaster4000mkii)_

[![Build Status](https://travis-ci.org/Bantolomeus/bookmaster4000mkii.svg?branch=master)](https://travis-ci.org/Bantolomeus/bookmaster4000mkii) 
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.bantolomeus%3Abookmaster4000mkii&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.bantolomeus:bookmaster4000mkii)

> Improve your reading habits. Api to document your books read and your daily reading progress. 
>
> Featuring: Kotlin :cookie: Spring :cookie: json 

### Install

`$ mvn package` to compile, test and build (including frontend)

`$ mvn test` to execute unit tests separately.

**Develop Frontend**

`$ npm install` to get all UI dependencies

`$ npm start` to have webpack bundle everything. It will provide a live reload web server. `localhost:4002/` to open the UI (it expects a backend though).

**Dependencies**

* Apache Maven 3.6.x
* Kotlin 1.2.x
* Java 1.8.x
* node 12.20
* npm 6.14.x

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

### Maintainers

* [Bantolomeus](https://github.com/Bantolomeus)
* [Husterknupp](https://github.com/Husterknupp)

### Contribute

Please check out the repo, use it yourself and come back with feature requests/bug tickets 

Also on any open issue we would be happy to get some help. So far they're not labeled with a 'good for beginner' label.

Since this is also about learning Kotlin, please also leave remarks on the coding style used here (in form of comments on the commits or of opening issues).

### License

Apache License Version 2.0
