# Building a RESTful Web Service

## What You Will Build

http://localhost:8080/greeting URL로 요청을 하면 다음과 같이 반환함

````json
{"id":1,"content":"Hello, World!"}
````

이때 name 쿼리 파라미터 값에 따라 content 값이 달라지게 함

http://localhost:8080/greeting?name=User 요청시 다음과 같이 반환함

````json
{"id":1,"content":"Hello, User!"}
````

## Let's do it

### Greeting

순수하게 데이터를 보유하기 위한 특수한 종류의 클래스인 record 사용
````java
public record Greeting(
        long id,
        String content
) {
}
````

### GreetingController

````java
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
````

위와 같이 String.format을 사용할 수 있다.    
AtomicLong은 Long 자료형을 갖고 있는 Wrapping 클래스이며     
멀티 쓰레드에서도 synchronized보다 적은 비용으로 동시성을 보장할 수 있다.

@RestController는 @Controller + @ResponseBody 이다.       
returns a domain object instead of a view.      
뷰를 반환하는 대신에 도메인 객체를 반환한다.

The Greeting object must be converted to JSON. Thanks to Spring’s HTTP message converter support,
you need not do this conversion manually. Because Jackson 2 is on the classpath,
Spring’s MappingJackson2HttpMessageConverter is automatically chosen to convert the Greeting instance to JSON.

스프링의 MappingJackson2HttpMessageConverter가 자동으로 객체를 JSON으로 변환해준다.

## Build an executable JAR

Building an executable jar makes it easy to ship, version,
and deploy the service as an application throughout the development lifecycle,
across different environments, and so forth.

실행 가능한 jar 파일로 빌드하여 다양한 환경에서 배포할 수 있도록 해보자.

````
./gradlew build
````

/build/libs 에 jar 파일 생성 후 다음과 같이 실행할 수 있음

````
java -jar guide1-0.0.1-SNAPSHOT.jar
````
