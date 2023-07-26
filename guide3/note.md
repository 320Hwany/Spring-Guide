# Consuming a RESTful Web Service

## What You Will Build

스프링의 RestTemplate을 이용해서 다른 애플리케이션인 http://localhost:8080/api/random 에 요청을 해서    
랜덤 문장을 하나 받아오는 것을 만들어보겠습니다.    

## Let's do it

먼저 https://github.com/spring-guides/quoters 을 git clone 받아와야합니다.    
미리 구현된 애플리케이션을 실행하고 이곳에 RestTemplate을 이용해 요청을 합니다.       

## Quote

````java
@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote(
        String type,
        Value value
) {
}
````

## Value

````java
@JsonIgnoreProperties(ignoreUnknown = true)
public record Value(
        Long id,
        String quote
) {
}
````

JsonIgnoreProperties를 사용한 이유는 이 유형에 바인딩되지 않은 모든 속성을 무시하기 위해서입니다.       

## ConsumingRestApplication

````java
@SpringBootApplication
public class Guide3Application {

	private static final Logger log = LoggerFactory.getLogger(Guide3Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Guide3Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Quote quote = restTemplate.getForObject(
					"http://localhost:8080/api/random", Quote.class);
			log.info(quote.toString());
		};
	}
}
````

RestTemplate을 빈으로 등록하고 주입을 받아서 사용하였습니다.    
또한 이때 RestTemplateBuilder를 이용해서 RestTemplate의 기본 설정 값을 미리 줄 수 있습니다.   

CommandLineRunner는 애플리케이션이 시작될 때 자동으로 CommandLineRunner에 등록된 코드 블록을 실행하게 합니다.   
CommandLineRunner 보다 조금 더 기능이 많은 ApplicationRunner을 사용할 수도 있습니다.    

이때 하나의 컴퓨터에서 8080 포트가 겹치면 실행이 되지 않으므로 server.port=8081로 설정하여 포트 충돌을 피합니다.   
참고로 이때 server.port=0 으로 설정하면 사용하고 있지 않은 랜덤한 포트가 적용됩니다.     

[GUIDE 3 - 예제 코드](https://github.com/320Hwany/spring-guide/tree/main/guide3/src/main/java/guide3)     