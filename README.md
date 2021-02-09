![revolut image](https://media-exp1.licdn.com/dms/image/C560BAQFaYlZ9k36fXQ/company-logo_200_200/0/1548754935584?e=2159024400&v=beta&t=WVzlz3uRU0OxTiJ46ciKYBME3sAzv21ngRahryryOig)


## Analyse

Apart from the logic to sell and restock books using in-memory storage (which should be straightforward), the main problem that I see in this assessment is how can we handle concurrent book purchases safely? For a book shop, it is not acceptable to have an unsafe and unpredictable store across multiple distributed systems and the stock has to be consistent.

## Solution & Approach

I took a TDD approach to write a Unit Test and assert that my initial store budget is showing some random assertions and unpredictable results, when performing parallel requests.


From there, I looked back into optimistic/pessimistic locking, Java synchronized, CountDownLatch classes... and so on.

### Dependencies:
- Spring Boot: Basic REST API for selling books and generating shop report.
- JUnit: To write all my Unit Tests.
- RestAssured: Testing REST services.

### Build the project

- Run `mvn clean package` to build the JAR file.
- Then `java -jar ./target/bookshop-0.0.1-SNAPSHOT.jar` to spin up the embedded server

### How to test?

#### Method 1) Use Postman:

- Simply import the collection file `src/main/resources/static/my-tutor.postman_collection.json` into Postman.

#### Method 2) CURL request:

/GET report: `curl --location --request GET 'http://localhost:8080/api/v1/shop/report'`

/POST order: `curl --location --request POST 'http://localhost:8080/api/v1/shop/order?bookType=A&quantity=4'`


The REST API is accepting those calls:

- /POST order: Order a new book given a valid book type and positive quantity.
- /GET report: Return the current store report.

### Retrospective:

If I had more time, and the requirement to make it work for real customers, I will:

- Use a NoSQL or real DB instead of in-memory HashMap storage.
- Not use a blocking synchronized to make sure that the store data are consistent. (synchronized is blocking the thread until it is finished).
- Use CompletableFuture or Akka Actor-model to have a non-blocking & thread-safe store.
- Build a UI with React to make it user-friendly.

### Contact

email: nizar.bousebsi@gmail.com


