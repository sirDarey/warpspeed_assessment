# RUNNING THE PROTOTYPE

* Clone this project and open in an IDE (e.g. Intellij IDEA)
* Locate the main application file ```/src/main/java/com.warpspeed.sirdarey.fraud_detection/FraudDetectionApplication.java```
* Right click and run the program
* Application runs on port: ```9090```


# TESTING THE PROTOTYPE
You can test via 2 endpoints:
  * ```GET http://localhost:9090/api/test``` 
    * This can be accessed via a browser or an API testing tool such as ```postman```.
    * This endpoint doesn't give room to run custom tests.
    * The default test data equivalent used internally by this endpoint is located in the file: ```/src/main/resources/test_data.json```.
    
  * ```POST http://localhost:9090/api/test``` ```Content-Type: application/json```
    * Here, you can run custom test data. Format must be exactly as the ```test_data.json```


# ALGORITHM ANALYSIS
```NOTE:``` An InMemoryDB is used to temporarily save data. <br>

  #### ASSUMPTIONS
* This is a standalone microservices and not a library or dependency package 
* Timestamp is in seconds 
* Max back and forth to detect PING-PONG is 1 cycle; i.e. Similar transactions must not occur more than once in each of the 2 services involved, else PING-PONG will be detected from the 2nd cycle 
* PING-PONG activity is assumed to be achievable by just one user where amount is the same in the detected events; Also, the frequency of occurence of similar events must be the same in both services 
* Transaction events are assumed to be in bulk (at least 2 or more events)



### Detecting if amount exceeds 5 times the average of previous transactions in the last 24 hours
Here, an SQL is made to get the average of all transactions amount within the last 24 hours from the timestamp of the current event being processed.
If specified criterion is true, event is flagged.

  * Time Complexity: O(1) (Without considering db processing)
  * Space Complexity: O(1)

### Detecting if transactions are done in more than 3 services within a 5-minute window
Here, an SQL is made to get the list of service IDs of this user's transactions within the last 5 minutes from the current event's timestamp.
The result is stored in a ```Set``` Data Structure to  avoid duplicates. If the size of the set is more than 3, the event is flagged.

  * Time Complexity: O(1) (Without considering db processing)
  * Space Complexity: O(1) (Since the highest number of services is same as the available services that process transactions which is constant)

### Detecting if transaction is a PING-PONG Activity
Here, an SQL is made to get the ```list``` of service IDs of this user's transactions within the last 10 minutes from the current event's timestamp.
The ```list``` is mapped to a ```Map``` Data Structure where the value is the frequency of each service in the ```list``` from the database.
The frequency of each service is then stored in an array and each frequency is compared against others using a ```nested for loop```. 
In the ```inner loop```, if the difference between the frequencies of both indexes ```i``` and ```j``` are same, then the event is flagged.
This appears to be an O(n^2) algorithm, but it's not because, the worst case scenario of the number of services is constant.

  * Time Complexity: O(n) (Without considering db processing)
  * Space Complexity: O(n)
  * n - number of the user's transactions within the 10-minute window

### Handling out-of-order events
This was handled by implementing a sorting mechanism using the timestamp of the events in the stream. <br>
Also, the database queries are bounded by the timeframes of the current event and the required window needed.


# EXPECTED RESULT
When the ```test_data.json``` file is used for testing, the expected result is given in the file
```/src/main/resources/expected_result.json```