# Messaging with Redis

## What We Will build

StringRedisTemplate을 이용하여 문자열 메세지를 보여주는 애플리케이션을 빌드합니다.      
MessageListenerAdapter를 이용하여 메세지를 구독하는 POJO 구독자를 만들어봅시다.          

## Let's Do it

먼저 Redis Server를 실행해줍니다.      

````
brew install redis
redis-server
````

다음으로 Dependencies는 Spring Data Redis를 선택해줍니다.    

### Receiver - 메세지를 받을 POJO 구독자

````java
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}
````

### RedisConfig

````java
@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
````
 
listenerAdapter 메소드에 정의된 bean은 컨테이너에 정의된 MessageListenerContainer에 MessageListener로 등록되어     
채팅에 대한 메세지를 수신합니다.     
이때 Receiver 클래스는 POJO이기 때문에 MessageListenerAdapter로 감싸서 등록합니다.   
그리고 메세지를 전송하기 위해서는 Redis template가 필요하고 여기서는 StringRedisTemplate를 빈으로 등록하였습니다.      

````java
@SpringBootApplication
public class Guide7Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext ctx = SpringApplication.run(Guide7Application.class, args);

		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		Receiver receiver = ctx.getBean(Receiver.class);

		while (receiver.getCount() == 0) {
			LOGGER.info("Sending message...");
			template.convertAndSend("chat", "Hello from Redis!");
			Thread.sleep(500L);
		}

		System.exit(0);
	}

}
````

[GUIDE 7 - 예제 코드](https://github.com/320Hwany/spring-guide/tree/main/guide7/src/main/java/guide7)          