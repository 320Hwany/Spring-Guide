package guide2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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