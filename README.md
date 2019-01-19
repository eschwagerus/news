# Upday Java Backend Assignment (news/articles)

This module implements the assigned backend tasks:

* algorithm: Flatten an array of nested integer arrays (See utility class ArrayUtil & ArrayUtilTest
* coding: The news articles spring-boot app

## Some notes on implementation:

* build using maven 3.3.9 and higher
* uses Java 8
* SpringBoot latest version
* includes an embedded mongo db
* offers a test- frontend / API page where you can try all methods and find the API documentation (jsondoc)

## How to build, test and run the app:

* build the app:              mvn clean install
* run the integration tests:  mvn clean verify
* start springboot:           mvn spring-boot:run
* generate jacoco report:     mvn jacoco:report             (see /target/site/jacoco/...)

When the app is started, open your browser at http://localhost:8080/jsondoc-ui.html?url=jsondoc
The actual API documentation can be found here!

## What is not included / Things to think about

* The app has no access control or security features / personalized user behaviour
* The data structure is kept very simple and document oriented
* There is no form of caching activated - which would be essential for high traffic to deal with
* For actual deployment, settings for mongodb have to be changed
* As the jacoco report shows, not all Error- cases have been tested. This was left out because I
  think the existing tests show easily how I would do it
* Sonar integration to track status of clean code is not included