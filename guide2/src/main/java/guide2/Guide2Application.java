package guide2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Guide2Application {

	public static void main(String[] args) {
		SpringApplication.run(Guide2Application.class, args);
	}

}
