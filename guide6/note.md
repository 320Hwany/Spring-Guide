# Authenticating a User with LDAP

## What We Will build

Spring Security의 임베디드 Java 기반 LDAP 서버로 보호되는 간단한 웹 애플리케이션을 빌드합니다.     

## Let's Do it

Dependencies는 Spring Web을 선택해줍니다.       

Spring Security 설정을 위해 build.gradle에 다음과 같이 추가해줍니다.   

````
implementation("org.springframework.boot:spring-boot-starter-security")
implementation("org.springframework.ldap:spring-ldap-core")
implementation("org.springframework.security:spring-security-ldap")
implementation("com.unboundid:unboundid-ldapsdk")
````

첫 번째 줄부터 각각 Spring Security 기본 설정을 포함하는 스타터 모듈      
Spring 프레임워크를 사용하여 LDAP 기반 서비스와 상호 작용하는데 필요한 핵심 라이브러리    
Spring Security를 사용하여 LDAP 인증을 구현하는데 필요한 라이브러리      
LDAP 연결과 검색 작업을 수행하는 데 사용되는 라이브러리를 나타냅니다.    

## WebSecurityConfig

````java
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    ...
}
````

filterChain은 자바 기반의 Spring Security를 나타내고 있습니다.    
이 코드는 Spring Security 필터 체인을 구성하는 메소드입니다.   

````java
@Configuration
public class WebSecurityConfig {

    ...

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");
    }
}
````

이 코드는 AuthenticationManagerBuilder를 사용하여 LDAP 기반의 인증을 구성하는 메소드입니다.   
LDAP는 디렉토리 서비스 프로토콜로 사용자 및 그룹 정보와 같은 계층적 데이터를 저장하는데 사용합니다.    

[GUIDE 6 - 예제 코드](https://github.com/320Hwany/spring-guide/tree/main/guide6/src/main/java/guide6)        
