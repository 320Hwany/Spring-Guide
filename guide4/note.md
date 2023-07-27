# Accessing Relational Data using JDBC with Spring

## What We Will build

Spring의 JdbcTemplate을 이용해서 관계형 데이터베이스를 이용하는 애플리케이션을 만들어보겠습니다.       

## Let's Do it

Dependencies는 JDBC API and H2 Database를 선택해줍니다.    

### Customer

````java
public class Customer {

    private long id;
    private String firstName, lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    ...
}
````

### Guide4Application

````java
@SpringBootApplication
public class Guide4Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Guide4Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Guide4Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        log.info("Creating tables");

        // First, install some DDL by using the execute method of JdbcTemplate.
        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // Second, take a list of strings and, by using Java 8 streams,
        // split them into firstname/lastname pairs in a Java array
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(toList());

        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?, ?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{"Josh"},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
                        rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }
}
````

JdbcTemplate를 사용하기 위해 의존관계 주입을 해줍니다.    
JdbcTemplate의 execute 메소드를 이용해 DROP, CREATE DDL 명령어를 실행해줍니다.    
JdbcTemplate의 update 메소드를 사용하여 insert 문을 처리할 수 있지만 여러 개의 작업은 batchUpdate를 사용하여   
성능을 높일 수 있습니다.    
JdbcTemplate의 query 메소드를 이용하여 select 문을 처리할 수 있습니다.   
메소드의 결과를 처리하기 위해서 RowMapper와 같은 것을 사용해야 하지만 여기서는 람다 표현식을 이용하여 더 간결하게 작성했습니다.     

[GUIDE 4 - 예제 코드](https://github.com/320Hwany/spring-guide/tree/main/guide4/src/main/java/guide4)               