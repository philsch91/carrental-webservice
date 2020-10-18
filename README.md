# Car Rental Webservice

## Compile

1. `mvn package`

## Run

1. `java -jar target/carrentalwebservice-0.0.1-SNAPSHOT.jar`

## Build and Run Container

1. `docker build -t ps/carrental-webservice .`
2. `docker run -p 443:443 ps/carrental-webservice`

## Push Container

1. `docker tag ps/carrental-webservice <registry-name>/ps/carrental-webservice:latest`
2. `docker login <registry-name>`
3. `docker push <registry-name>/ps/carrental-webservice:latest`
4. `docker logout <registry-name>`

## Create Certificate
`keytool -genkeypair -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore se-carrental.p12 -validity 730`

