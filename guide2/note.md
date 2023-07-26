# Scheduling Tasks

## What You Will Build
   
@Scheduled 애노테이션을 사용해서 매 5초마다 현재 시간을 출력하는 애플리케이션을 만들어보겠습니다.      

## Let's Do it

````java
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
//        log.info("The time is now {}", dateFormat.format(new Date()));
        log.info("The time is now {}", LocalDateTime.now().format(dateFormat));
    }
}
````
 
위와 같이 ScheduledTasks 클래스를 만든다. ScheduledTasks는 스프링 빈으로 등록되어야 합니다.          
log를 사용하는데 롬복을 사용하지 않았기 때문에 LoggerFactory를 이용해서 log를 사용하였습니다.         
공식문서에서는 Date를 사용했지만 LocalDateTime를 사용하는 것이 더 좋을 것 같아서 변경하였습니다.      

이제 반복해서 실행할 메소드를 만들 때 @Scheduled(fixedRate = 5000)를 사용하였습니다.       
ms 단위이기 때문에 5초를 나타낸다. 이외에도 fixedDelay, cron 과 같은 옵션이 있습니다.     

한 가지 일이 더 남았는데 공식 문서에 적혀있는 문장을 가져와보면     
The @EnableScheduling annotation ensures that a background task executor is created.   
Without it, nothing gets scheduled.   
즉 @EnableScheduling 애노테이션을 사용해야 위에서 정의한 메소드가 백그라운드 태스크로 인식하고 실행될 수 있습니다.         

## Guide2Application

````java
@EnableScheduling
@SpringBootApplication
public class Guide2Application {

	public static void main(String[] args) {
		SpringApplication.run(Guide2Application.class, args);
	}

}
````

공식문서에는 이렇게 @SpringBootApplication과 같이 썼지만 아예 별도의 클래스를 만들어서 설정하는 것도 방법입니다.      

## ScheduledConfig

````java
@EnableScheduling
@Configuration
public class ScheduledConfig {
}
````

Scheduled Tasks는 테스트하기 어렵다. 24시간에 한번씩 실행되는 작업이라고 테스트를 24시간 돌릴 수 없습니다.       
여기서는 5초마다 한번씩 작업이 일어나기 때문에 공식문서에서 제안하는 awaitility 라이브러리를 사용해서 테스트를 작성해보겠습니다.            

build.gradle에 아래와 같이 의존성을 추가합니다.     

````
testImplementation 'org.awaitility:awaitility:4.2.0'
````

## Guide2ApplicationTests

````java
@SpringBootTest
class Guide2ApplicationTests {

	@SpyBean
	ScheduledTasks scheduledTasks;

	@Test
	@DisplayName("스케쥴 태스크에 의해 15초 이내에 최소 2번이상 호출되었음을 확인합니다")
	void test() {
		await().atMost(15, TimeUnit.SECONDS).untilAsserted(
				() -> verify(scheduledTasks, atLeast(2)).reportCurrentTime());
	}
}
````

테스트가 성공적으로 수행되었습니다.            

## 요약

1. 반복적으로 수행되는 작업을 해보자.
2. ScheduledTasks 클래스를 먼저 스프링 빈으로 등록한다.
3. 메소드에 수행할 시간 or 시간 간격을 작성한다.
4. @EnableScheduling을 @SpringBootApplication와 같이 붙이거나 별도의 Config 클래스에 붙이자.
5. 테스트 코드를 작성할 필요가 있다면 awaitility 라이브러리를 이용하자

[GUIDE 2 - 예제 코드](https://github.com/320Hwany/spring-guide/tree/main/guide2/src/main/java/guide2)         
[GUIDE 2 - 학습 테스트](https://github.com/320Hwany/spring-guide/tree/main/guide2/src/test/java/guide2)           