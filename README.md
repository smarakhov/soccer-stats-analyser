[![Build Status](https://travis-ci.org/smarakhov/soccer-stats-analyser.svg?branch=master)](https://travis-ci.org/smarakhov/soccer-stats-analyser)

# soccer-stats-analyser
Soccer Match Stats Analyser

## Assumptions
- Java 8 (Java 9 might have a negative effect on parsing Lombok annotated classes)
- Gradle is required. If not available, please follow <https://gradle.org/install/> to install

## Questions
- Why can't I use Spring? I like dependency injection

## Repository
https://github.com/smarakhov/soccer-stats-analyser

## CI
https://travis-ci.org/smarakhov/soccer-stats-analyser

## Trello Board
https://trello.com/b/kOwcsnbX/soccer-stats-analyser

## To run tests 
`gradle clean test`

## To run application 
`./gradlew run --args="./src/main/resources/match-events.csv 00:11"`
