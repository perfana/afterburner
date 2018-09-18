# afterburner-java
Simple self-running test jar to use in load test environments to tune and explore monitor and analysis tools.

# Start
Start in the afterburner-java directory:
    
    mvn spring-boot:run
    
# functions

## delay
Call an endpoint that will delay a call for the given duration. Default duration is 100 milliseconds.
The duration can either be given in milliseconds or in [ISO 8601 duration](https://en.wikipedia.org/wiki/ISO_8601#Durations) by starting with P.

If you increase the response time high enough, you are likely to see timeout issues.

Examples:
* `/delay?duration=PT1.5s` delay for 1500 millis
* `/delay?duration=150` delay for 150 millis

## memory
Call this endpoint to increase memory usage to simulate a memory leak. 

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

## remote call
Call another remote http(s) endpoint. Use `path` to specify the path to be called on the remote
instance. 

The base url is specified via the `afterburner.remote.call.base_url` property. The default value is
`http://localhost:8080` so it will call itself.

The default remote client is Apache HttpClient. With the `type` request parameter you can switch to OkHttp.

Examples:
* `/remote/call?path=delay` call delay on remote afterburner via HttpClient
* `/remote/call?path=delay&type=okhttp` call delay on remote afterburner via OkHttp
* `/remote/call?path=remote/call?path=delay` call remote afterburner to call remote afterburner to call delay
* `/remote/call?type=okhttp&path=remote/call?path=delay` same, but first call via OkHttp and second via HttpClient

note: do not forget to escape & with a backslash using bash and curl

# properties
* `-Dafterburner.name=Angry-Afterburner` provide a name for the instance
* `-Dafterburner.remote.call.base_url=https://my.remote.site:1234` connect remote calls to this base url
* `export AFTERBURNER_REMOTE_CALL_BASE_URL=https://my.remote.site:1234` provide base url via environment variable
* `--server.port=8090` use different port (default 8080)
* `export SERVER_PORT=8090` use different port via env variable

##### credits
* fire favicon from [freefavicon](http://www.freefavicon.com)
