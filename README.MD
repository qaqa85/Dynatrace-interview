Currency Exchange App
=====================

This application works with nbp api to retrieve information about 
exchange rate between PLN and other foreign currencies.

Installation
------------
___

Manual
1. build an application using gradle
```
./gradlew build
```
2. run the application from terminal using jar and Java 17+
```
java -jar build/libs/currency-0.0.1-SNAPSHOT.jar
```

Docker
1. From project folder execute command to build an image
```
 docker build . -t crrency_app
```
2. To run an image simply run command below. The image will listen on localhost:8080 
```
 docker run crrency_app -p 8080:8080
```
Usage
-----
___

This is a simple application working on `http://localhost:8080`.
Api is available on `http://localhost:8080/api/v1/exchanges`

Swagger
* To run swagger go to [swagger.ui](http://localhost:8080/swagger)

Curl examples
1. Get the max and min average value in last 200 quotations (USD/PLN)
```
curl --location --request GET http://localhost:8080/api/v1/exchanges/average/USD/last/200
```
2. Get the average ratio (USD/PLN) from 2023-04-24
```
curl --location --request GET http://localhost:8080/api/v1/exchanges/average/USD/date/2023-04-24
```
3. Get the major difference between the buy and ask rate in last 200 quotations (USD/PLN)
```
curl --location --request GET http://localhost:8080/api/v1/exchanges/buy-ask/USD/last/200
```
