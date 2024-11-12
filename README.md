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

Setup ```IDeaSClientFactory``` can be done via yaml

```yaml
ideas:
  client:
    url: https://pmsinbound-internal.stage.ideasrms.com
    retry:
      period: 5
      maxPeriod: 30
      maxAttempts: 6
```
Then, enable IDeaS ConfigurationProperties classes:
```java
@EnableConfigurationProperties(value = {IDeaSProperties.class, IDeaSRetryProperties.class})
public class YourConfigClass {
    //...
}
```

And inject ```IDeaSClientFactory``` as Spring's bean:

```java
@Autowired
IDeaSClientFactory factory;
```

Send reservation example

```java
var reservationClient = iDeaSClientFactory.<IDeaSReservationV1ApiClient>build(IDeaSDataType.RESERVATION);
reservationClient.saveReservations(Arrays.asList(getNucleusReservation()));
```

(For usage example can also take a look at ```IDeaSClientFactoryTest```)

## Authentication

Once you will request clientId + clientSecret from IDeaS at cloudsolutions@ideas.com you will be able to reach out to IDeaS APIs

Have to define these values in application.yaml:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          pmsInboundAuth:
            client-id: ${CLIENT_ID} # <------------
            client-secret: ${CLIENT_SECRET} # <------------
            authorization-grant-type: client_credentials
            client-authentication-method: post
            scope:
              - com.ideas.api/pms
        provider:
          pmsInboundAuth:
            token-uri: https://fds.stage.ideasrms.com/api/uis/external_m2m/oauth2/token
```

ClientCode and PropertyCode defined in entities sent to IDeaS APIs should be equal to codes dedicated to hotel,
otherwise access will be denied

There is dynamic request interceptor defined in application context that is responsible for setting 
Authorization header based on clientId and clientSecret provided by application.yaml file. 
No additional configuration is needed

ClientId and ClientSecret are depend on environment: STAGE is for testing purposes whereas ```https://fds.ideasrms.com/api/uis/external_m2m/oauth2/token```
and ```https://pmsinbound-internal.stage.ideasrms.com``` - PROD

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)

test