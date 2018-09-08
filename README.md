[![Build Status](https://travis-ci.org/smarakhov/soccer-stats-analyser.svg?branch=master)](https://travis-ci.org/smarakhov/soccer-stats-analyser)

# soccer-stats-analyser
Soccer Match Stats Analyser

## Assumptions - Environment
- Java 8 (Java 9 might have a negative effect on parsing Lombok annotated classes)
- Gradle is required. If not available, please follow <https://gradle.org/install/> to install

## Assumptions - Application Requirements
- Events are always coming in correct order 
- After scoring a goal, there will always be a POSSESS event for the opposite team

## Questions
- Why can't I use Spring? I like dependency injection

## Repository
https://github.com/smarakhov/soccer-stats-analyser

## CI
https://travis-ci.org/smarakhov/soccer-stats-analyser

## Trello Board
https://trello.com/b/kOwcsnbX/soccer-stats-analyser

## To run tests 
`gradle clean check`

### With coverage
`gradle clean check jacocoTestReport jacocoTestCoverageVerification`

This will generate following reports:
- coverage report in `./build/reports/jacoco/test/html/index.html`
- test report in `./build/reports/tests/test/index.html`  

## To run application 
`./gradlew run --args="./src/main/resources/match-events.csv 00:11"`
