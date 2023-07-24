package guide1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class Guide1ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("파라미터가 없는 경우 기본 값을 포함하여 반환합니다")
    void test1() throws Exception {
        // expected
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value("Hello, World!"));
    }

    @Test
    @DisplayName("파라미터가 있는 경우 그 값을 포함하여 반환합니다")
    void test2() throws Exception {
        // given1
        mockMvc.perform(get("/greeting").param("name", "이름"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value("Hello, 이름!"));
    }
}