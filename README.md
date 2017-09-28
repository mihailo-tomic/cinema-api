# Cinema Api Project

This project is a showcase of the use of back-end and front-end technologies. The goal is to create a simple API for a movie theater web site.
 
 #Requirements
 
 * Java 1.8
 * Gradle
 * Git
 * MySQL
 
 #Setup
 
 * Clone the repository from Github using Git ``git clone https://github.com/mihailo-tomic/cinema-api.git``
 * (Optional) Import the project into your IDE (Eclipse, IntelliJ, NetBeans...)
 * Update the ``src/main/resource/application.properties`` and ``src/main/resources/db/flyway.prperties`` with your MySQL database credentials
 
 #Building and starting the project
 
 The required MySQL database is created using the Flyway plugin. All structural changes to the database are added to the ``src/main/resources/db/migration`` directory. To create or update the database, run ``gradle flywayMigrate``.
 
 To build the project, run ``gradle build``.
 
 To start the project, run ``gradle bootRun``.
 
 >> Note: All commands are run from the project's root directory
 
 #API endpoints
 
 ###Movie
 
 Endpoints for CRUD operations:
 
 * **/api/cinema/movies{?page,size,sort}** - Retrieves all movies
    
    Method: **GET**
    
    Parameters: 
    * page
    * size
    * sort
    
    Example request: 
    
    ``curl -X GET http://localhost:8080/api/cinema/movies?page=0&size=20``
    
    Example response: 
    
    Content Type: **application/hal+json**
        
    Http status: **200 (OK)**
    
    
    ````
    {
          "_embedded": {
              "movies": [
                  {
                      "title": "Gospodar Prstenova: Druzina Prstena",
                      "originalTitle": "The Lord of the Rings: The Fellowship of the Ring",
                      "duration": 178,
                      "director": "Peter Jackson",
                      "premiereDate": "2001-12-19T00:00:00.000+0000",
                      "_links": {
                          "self": {
                              "href": "http://localhost:8080/cinema/api/movies/18"
                          },
                          "movie": {
                              "href": "http://localhost:8080/cinema/api/movies/18"
                          },
                          "genres": {
                              "href": "http://localhost:8080/cinema/api/movies/18/genres"
                          },
                          "actors": {
                              "href": "http://localhost:8080/cinema/api/movies/18/actors"
                          }
                      }
                  }
              ]
          },
          "_links": {
              "self": {
                  "href": "http://localhost:8080/cinema/api/movies"
              },
              "profile": {
                  "href": "http://localhost:8080/cinema/api/profile/movies"
              },
              "search": {
                  "href": "http://localhost:8080/cinema/api/movies/search"
              }
          },
          "page": {
              "size": 20,
              "totalElements": 1,
              "totalPages": 1,
              "number": 0
          }
      }```
    
 * **/api/cinema/movies/{movie_id}** - Retrieves a single movie with the given id
 
    Method: **GET**
    
    Path parameters:
    * movie_id (Number)
    
    Example request: 
    
    ``curl -X GET http://localhost:8080/api/cinema/movies/42``
    
    Example response: 
    
    Content Type: **application/hal+json**
        
    HttpStatus: **200 (OK)**
    
    ```
    {
      "title": "Gospodar Prstenova: Druzina Prstena",
      "originalTitle": "The Lord of the Rings: The Fellowship of the Ring",
      "duration": 178,
      "director": "Peter Jackson",
      "premiereDate": "2001-12-19T00:00:00.000+0000",
      "_links": {
        "self": {
          "href": "http://localhost:8080/cinema/api/movies/42"
        },
        "movie": {
          "href": "http://localhost:8080/cinema/api/movies/42"
        },
        "genres": {
          "href": "http://localhost:8080/cinema/api/movies/42/genres"
        },
        "actors": {
          "href": "http://localhost:8080/cinema/api/movies/42/actors"
        }
      }
    }
    ```
    
 * **/api/cinema/movies** - Create a new movie from a JSON payload
 
    Method: **POST**
    
    Example request: 
    
    ```
    curl -X POST http://localhost:8080/api/cinema/movies -d "
    {
        "title" : "Gospodar Prstenova: Dve Kule",
        "originalTitle" : "The Lord of the Rings: Two Towers",
        "duration" : "179",
        "director" : "Peter Jackson",
        "premiereDate" : "2002-12-18T00:00:00.000+0000"
    }
    " 
    ```
    
    Example response: 
    
    Content Type: **application/json**
        
    Http status: **201 (Created)**
    
    ```
    ```
 
 * **/api/cinema/movies/{movie_id}** - Update the movie with the given id
 
    Method: **PUT**
    
    Path parameters: 
    * movie_id
    
    Content Type: **application/json**

    Example request: 
    
    ``curl -X PUT http://localhost:8080/api/cinema/movies/42``

    Example response: 

    Http status: **204 (No Content)**
 
 * **/api/cinema/movies/{movie_id}** -
  
    Method: **PATCH**
    
    Path parameters:
    * movie_id
    
    Content Type: **application/json**

    Example request:
    
    ``curl -X PATCH http://localhost:8080/api/cinema/movies/42``

    Example response: 

    Http status: **204 (No Content)**
  
 * **/api/cinema/movies/{movie_id}** - Delete the movie with the given id
 
    Method: **DELETE**
    
    Path parameters:
    * movie_id
    
    Example request: 
    
    ``curl -X DELETE http://localhost:8080/api/cinema/movies/42``
    
    Example response: 
    
    Http status: **204 (No Content)**
 
 Search endpoints: 
 
 * **/api/cinema/movies/search/title?title={movie_title}**
 
    Method: **GET**
  
    Parameters:
    * title - Movie title
    
    Example request:
    
    ``curl -X GET http://localhost:8080/cinema/api/movies/search/title?title=Gospodar%20Prstenova:%20Druzina%20Prstena``
    
    Example response: 
    
    Http status: **200 (OK)**
    
    ```
      {
        "_embedded": {
          "movies": [
            {
              "title": "Gospodar Prstenova: Druzina Prstena",
              "originalTitle": "The Lord of the Rings: The Fellowship of the Ring",
              "duration": 178,
              "director": "Peter Jackson",
              "premiereDate": "2001-12-19T00:00:00.000+0000",
              "_links": {
                "self": {
                  "href": "http://localhost:8080/cinema/api/movies/42"
                },
                "movie": {
                  "href": "http://localhost:8080/cinema/api/movies/42"
                },
                "genres": {
                  "href": "http://localhost:8080/cinema/api/movies/42/genres"
                },
                "actors": {
                  "href": "http://localhost:8080/cinema/api/movies/42/actors"
                }
              }
            }
          ]
        },
        "_links": {
          "self": {
            "href": "http://localhost:8080/cinema/api/movies/search/title?title=Gospodar%20Prstenova%3A%20Druzina%20Prstena"
          }
        }
      }
    ```
    
 
 * **/api/cinema/movies/search/upcoming?date={premiere_date}**
 
    Method: **GET**
    
    Parameters:
    * date - starting date in yyyy-MM-dd format
    
    Example request: 
    
    ``curl -X GET http://localhost:8080/api/cinema/movies/search/upcoming?date=2017-10-00``
    
    Example response:
    
    Http status: **200 (OK)**
 
 * **/api/cinema/movies/search/upcomingRange?fromDate={start_date}&toDate={end_date}**
 
    Method: **GET**
    
    Parameters:
    * fromDate - starting date in yyyy-MM-dd format 
    * toDate - ending date in yyyy-MM-dd format
    
    Example request: 
    
    ``curl -X GET http://localhost:8080/api/cinema/movies/search/upcomingRange?fromDate=2017-10-00&toDate=2017-11-00``
    
    Example response: 
 
     Http status: **200 (OK)**
 
 #Tests
 
 To start the tests, run ``gradle test``.