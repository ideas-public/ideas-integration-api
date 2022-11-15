# IDeaS Public Integration Api

Feign Java client for accessing IDeaS Public Integration API's

## Prerequisites

    1. Request client and secret from IDeaS at cloudsolutions@ideas.com
    2. Request IDeaS to create vendor/client/property (manually IDeaS process for now can automate in future)


## Build

```bash
./gradlew clean build
```

## Usage

Setup client factory, see IDeaSClientFactoryTest for example

```java

var iDeaSClientFactory = new IDeaSClientFactory(
                getObjectMapper(),
                Logger.Level.BASIC,
                new IDeaSRetryProperties(Duration.ofSeconds(5), Duration.ofSeconds(30), 6),
                new IDeaSProperties("https://pmsinbound-internal.stage.ideasrms.com"));

```

Send reservation example

```java
var reservationClient = iDeaSClientFactory.<IDeaSReservationV1ApiClient>build(IDeaSDataType.RESERVATION);
reservationClient.saveReservations(Arrays.asList(getNucleusReservation()));
```

Setup can also be done via yaml

```yaml
ideas:
  client:
    url: https://pmsinbound-internal.stage.ideasrms.com
    retry:
      period: 5
      maxPeriod: 30
      maxAttempts: 6
```

##TODO auth...

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)