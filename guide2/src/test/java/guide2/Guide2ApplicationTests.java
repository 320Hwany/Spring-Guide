package guide2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.*;
import static org.mockito.Mockito.*;

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
