# afterburner-java
Simple self-running test jar to use in load test environments to tune and explore monitor and analysis tools.

# functions

## delay
Call an endpoint that will delay a call for the given duration. Default duration is 100 milliseconds.
The duration can either be given in milliseconds or in [ISO 8601 duration](https://en.wikipedia.org/wiki/ISO_8601#Durations) by starting with P.

Examples:
* `/delay?duration=PT1.5s` delay for 1500 millis
* `/delay?duration=150` delay for 150 millis

##### credits
* fire favicon from [freefavicon](http://www.freefavicon.com)
