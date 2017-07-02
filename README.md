# NBCUniversal SQE Craft Demonstration Challenge
## Used technologies and approaches 
* TestNG
* Data driven testing


## Testcases - Areas
### Content 
* Test **query** (every possibilities)
    * Testing normal input, corner cases, default value (q is not defined)
    * Examples: `Apollo`, `Voyager`, `123456`, `null`, `{empty}`
* Test **limit** 
     * Testing inputs - correct `(>0)`, incorrect, default value (limit is not defined)
     * Examples: `1`, `10`, `0`, `-50`, `null`, `JakeTheHuman`
* Test **api_key**
    * Testing inputs - correct, incorrect, default value (api_key is not defined)
    * Examples: correct: `DEMO_KEY`, incorrect: `FinnTheDog`
* Every record should have all params like `id`, `title`, `duration`, `download_url`
* Every record should have all values correct - `id` is `int`, `title` is `String`, `duration` is `int`, 
`download_url` is `String`
    
### Security
* Load Testing:
    * Service should throws `Error 429` after too many requests

### Performance
* Server should responds on time (time in milliseconds - 500 milliseconds is too much)
