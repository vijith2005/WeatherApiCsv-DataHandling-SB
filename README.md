Weather Data System — Spring Boot + MongoDB Implementation

A Spring Boot + MongoDB application that ingests historical weather data from CSV files and exposes REST APIs for querying, pagination, and analytics.

TECH STACK

Java 17+
Spring Boot
Spring Data MongoDB
OpenCSV
MongoDB
Maven

BASE URL

http://localhost:8077/api

API ENDPOINTS

Upload CSV File

Description:
Uploads a CSV file and stores weather data into MongoDB.

Endpoint:
POST /api/upload

Request Type:
multipart/form-data

Request Parameter:
file → CSV File

Response (Success - 201 Created):
CSV file uploaded and data saved successfully!

Response (Error - 400 Bad Request):
File is empty

Get Weather By ID

Description:
Fetch a single weather record using its MongoDB ID.

Endpoint:
GET /api/{id}

Example:
GET /api/65f123abc456def789

Response:
Returns a WeatherData document.

Get All Weather Data (With Pagination)

Description:
Retrieves all weather records with pagination support.

Endpoint:
GET /api/weather?page=0&size=10

Query Parameters:

page (default: 0)

size (default: 10)

Example:
GET /api/weather?page=1&size=5

Response:
Returns paginated weather data (Spring Page object).

Get Weather Data By Month

Description:
Retrieves weather records filtered by month.

Endpoint:
GET /api/month?month=MM

Example:
GET /api/month?month=11

Response:
Returns a list of WeatherData documents for the specified month.

Monthly Temperature Statistics By Year

Description:
Provides monthly statistics (max, min, median temperature) for a given year.

Endpoint:
GET /api/stats/{year}

Example:
GET /api/stats/1996

Response Format:
{
"1": {
"maxTemp": 32.0,
"minTemp": 18.0,
"medianTemp": 25.0
},
"2": {
"maxTemp": 34.0,
"minTemp": 19.0,
"medianTemp": 27.0
}
}

Key:

Outer Key → Month number

Inner Map → Temperature statistics

TESTING FLOW

Upload CSV file using /api/upload

Fetch paginated records using /api/weather

Fetch record by ID using /api/{id}

Filter records by month using /api/month

Retrieve monthly statistics using /api/stats/{year}

MONGODB CONFIGURATION (application.properties)

spring.data.mongodb.uri=mongodb://localhost:27017/weatherdb
server.port=8077

HOW TO RUN THE PROJECT

Clone the repository:
git clone https://github.com/your-username/weather-data-system.git

Navigate to project directory:
cd weather-data-system

Build the project:
mvn clean install

Run the application:
mvn spring-boot:run

Application runs at:
http://localhost:8077

PROJECT STRUCTURE

controller/ → REST APIs
service/ → Business logic
repository/ → MongoDB interaction
entity/ → WeatherData document model

FEATURES

CSV Upload

MongoDB Storage

Pagination Support

Month-Based Filtering

Yearly Monthly Temperature Statistics

Clean REST API Design

Proper HTTP Status Handling (ResponseEntity)
