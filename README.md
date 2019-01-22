# Java Backend Assignment (news/articles)

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

## Check reports:

* generate jacoco report:     mvn jacoco:report             (see /target/site/jacoco/...)
* generate surefire-report:   mvn surefire-report:report
* mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=2df9164fb22312cc640034732ea26327a9c58440

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



# Taskdescription:

# Algorithm
Write some code, that will flatten an array of arbitrarily nested arrays of integers into a flat array of
integers. e.g. [[1,2,[3]],4] -> [1,2,3,4].

## Coding
This is the situation: We are a publishing company that created an app for reading news articles.
To serve data to the app we need a backend to provide RESTful APIs for the following use cases:

* allow an editor to create/update/delete an article
* display one article
* list all articles for a given author
* list all articles for a given period
* find all articles for a specific keyword

Each API should only return the data that is really needed to fulfill the use case.

* An article usually consists of the following information:
* header
* short description
* text
* publish date
* author(s)
* keyword(s)

## Hints:
* Use the Java technology you are most comfortable with (e.g. spring-boot).
* The data does not need to be persisted after the application is shut down.
* Using Kotlin would be a plus.

## How we review
Your application/program will be reviewed by at least two of our engineers. We do take into
consideration your experience level.
We value quality over feature-completeness. It is fine to leave things aside provided you call them
out in your project's README. The goal of this code sample is to help us identify what you consider
production-ready code. You should consider this code ready for final review with your colleague, i.e.
this would be the last step before deploying to production.
The aspects of your code we will assess include:

* Architecture: how clean is you interface?
* Clarity: does the README clearly and concisely explains the problem and solution? Are
technical tradeoffs explained?
* Correctness: does the application/program do what was asked? If there is anything missing,
does the README explain why it is missing?
* Code quality: is the code simple, easy to understand, and maintainable? Are there any code
smells or other red flags? Does object-oriented code follows principles such as the single
responsibility principle? Is the coding style consistent with the language's guidelines? Is it
consistent throughout the codebase?
* Security: are there any obvious vulnerability?
* Testing: how thorough are the automated tests? Will they be difficult to change if the
requirements of the application were to change? Are there some unit and some integration
tests?
* We're not looking for full coverage (given time constraint) but just trying to get a feel for your
testing skills.
* Technical choices: do choices of libraries, databases, architecture etc. seem appropriate for
the chosen application?