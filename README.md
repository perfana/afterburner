# afterburner-java
Simple self-running test jar to use in load test environments to tune and explore monitor and analysis tools.

# functions

## delay
Call an endpoint that will delay a call for the given duration. Default duration is 100 milliseconds.
The duration can either be given in milliseconds or in [ISO 8601 duration](https://en.wikipedia.org/wiki/ISO_8601#Durations) by starting with P.

Examples:
* `/delay?duration=PT1.5s` delay for 1500 millis
* `/delay?duration=150` delay for 150 millis

## memory
Call this endpoint to increase memory usage.

Examples:
* `/memory/grow` grows an in memory structure with default values
* `/memory/grow?objects=5&items=5` grows structure with the given values
* `/memory/clear` clears the memory structure and memory will be freed

# properties
* `-Dafterburner.name=Angry-Afterburner` provide a name for the instance
* `--server.port=8090` use different port (default 8080)
* `export SERVER_PORT=8090` use different port via env variable 

##### credits
* fire favicon from [freefavicon](http://www.freefavicon.com)
