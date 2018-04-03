# afterburner-java
Simple self-running test jar to use in load test environments to tune and explore monitor and analysis tools.

# functions

## delay
Call an endpoint that will delay a call for the given duration. Default duration is 100 milliseconds.
The duration can either be given in milliseconds or in [ISO 8601 duration](https://en.wikipedia.org/wiki/ISO_8601#Durations) by starting with P.

If you increase the response time high enough, you are likely to see timeout issues.

Examples:
* `/delay?duration=PT1.5s` delay for 1500 millis
* `/delay?duration=150` delay for 150 millis

## memory
Call this endpoint to increase memory usage. 

If you increase the memory usage continuously or with a high
number of objects you will likely see a OutOfMemory related issues.

Examples:
* `/memory/grow` grows an in memory structure with default values
* `/memory/grow?objects=5&items=5` grows structure with the given values
* `/memory/clear` clears the memory structure and memory will be freed

## cpu
Call this endpoint to burn some cpu cycles using matrix calculations.
You can perform an identity check on a simple magic square. 

Be careful with sizes above 500, 
the response times and cpu effort will increase significantly above these sizes.

Note that the current implementation is doing the calculation within one thread (core).
You can exercise multiple cores by calling multiple concurrent requests.

Examples:
* `/cpu/magic-identity-check` checks identity and simple magic square multiplication with default size 10
* `/cpu/magic-identity-check?matrixSize=100` check for size 100, currently takes around 100 milli sec on a simple laptop
* `/cpu/magic-identity-check?matrixSize=600` check for size 600, currently takes 2.5 sec on a simple laptop
* `/cpu/magic-identity-check?matrixSize=0` get a 500 error (might also be handy for testing)

# properties
* `-Dafterburner.name=Angry-Afterburner` provide a name for the instance
* `--server.port=8090` use different port (default 8080)
* `export SERVER_PORT=8090` use different port via env variable 

##### credits
* fire favicon from [freefavicon](http://www.freefavicon.com)
