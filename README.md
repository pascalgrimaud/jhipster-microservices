# Microservices with JHipster

[![jhipster](_images/logo-jhipster.png)](http://jhipster.github.io/)[![netflix](_images/logo-netflix-oss.png)](https://netflix.github.io/)

## Description

Simple project using a **Microservices Architecture**.


## How to run

Clone the project:

```
git clone https://github.com/pascalgrimaud/jhipster-microservices.git
```

Start the registry:

```
cd jhipster-registry
./mvnw
```

Start the gateway:
```
cd gateway
./mvnw
```

Start the microservice:

```
cd micro
./mvnw
```

Access to the registry: [http://localhost:8761](http://localhost:8761)

Access to the gateway: [http://localhost:8080](http://localhost:8080)


## Thanks to

* [JHipster Generator](https://github.com/jhipster/generator-jhipster)

* [JHipster Registry](https://github.com/jhipster/jhipster-registry)

* Ideas from [PierreBesson/jhipster-circuit-breaker-demo](https://github.com/PierreBesson/jhipster-circuit-breaker-demo)

* The documentation on [Spring Clound Netflix](http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)

* The project [OpenFeign/Feign](https://github.com/OpenFeign/feign)

* The article about [Feign Client](http://blog.ippon.fr/2016/09/21/feign-encore-un-client-http/) (in French)

* The article about [JSON Web Tokens With Spring Cloud Microservices](https://keyholesoftware.com/2016/06/20/json-web-tokens-with-spring-cloud-microservices/)
