# NBCUniversal SQE Craft Demonstration Challenge
## Used technologies and approaches 
* TestNG
* Data driven testing

## Install
* get maven
* `mvn install`

## Testcases - Areas
### Content 
* Test keys of results
    * if every key is present in every record
* Test data
    * If values is correct - test via regex
    * for example: date is correct, id is int, URL is valid url
    * URL are valid and user is able to download it or stream. 
### URL Params and results
* Test **query** (every possibilities)
    * Testing normal input, corner cases, default value (q is not defined)
    * Examples: `Apollo`, `Voyager`, `123456`, `null`, `{empty}`
* Test **limit** 
     * Testing inputs - correct `(>0)`, incorrect, default value (limit is not defined)
     * Examples: `1`, `10`, `0`, `-50`, `null`, `JakeTheHuman`
* Test **api_key**
    * Testing inputs - correct, incorrect, default value (api_key is not defined)
    * Examples: correct: `DEMO_KEY`, incorrect: `FinnTheDog`
    
### Security
* Load Testing:
    * Service should throws `Error 429` after too many requests (30 per hour for `DEMO_KEY`, 1000 for generated key)

### Performance
* Server should responds on time (time in milliseconds - 500 milliseconds is too much)


## Results of tests
```
Results :

Failed tests:   
  test_limit(TestParams): Server returned HTTP response code: 500 for URL: https://api.nasa.gov/planetary/sounds?&api_key=bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma&limit=-10
  test_limit(TestParams): Server returned HTTP response code: 500 for URL: https://api.nasa.gov/planetary/sounds?&api_key=bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma&limit=limit
  test_limit(TestParams): Server returned HTTP response code: 500 for URL: https://api.nasa.gov/planetary/sounds?&api_key=bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma&limit=What
  test_limit(TestParams): Server returned HTTP response code: 500 for URL: https://api.nasa.gov/planetary/sounds?&api_key=bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma&limit=$%^&*(
  test_query_filter(TestParams): Filter on word "Voyager" is not in every record. (..)
  test_query_filter(TestParams): Filter on word "Apollo" is not in every record. (..)
  test_query_filter(TestParams): Filter on word "123" is not in every record. (..)
  test_query_filter(TestParams): Filter on word "null" is not in every record. (..)

Tests run: 30, Failures: 8, Errors: 0, Skipped: 0
```
### Discovered errors
* `test_limit` fails in one testcase, because server doesn't handle negative digits.
* `test_limit` fails 3times, because server doesn't handle string as value. It accepts only (positive) digits.
* `test_query_filter` fails due no-working filtering. It returns records, where is not a key word - error.