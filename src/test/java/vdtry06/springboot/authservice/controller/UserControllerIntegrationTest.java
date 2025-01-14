package vdtry06.springboot.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.response.UserResponse;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:9.0.1-oraclelinux9");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driveClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate birthday;

    // chạy đầu tiên
    @BeforeEach
    void initData() {
        request = UserCreationRequest.builder()
                .username("trong")
                .password("12345678")
                .email("tunhoipro0306@gmail.com")
                .firstName("Trong")
                .lastName("Vuong")
                .dateOfBirth(LocalDate.of(2004, 6, 3))
                .build();
        userResponse = UserResponse.builder()
                .id("41b78ff154ba")
                .username("trong")
                .email("tunhoipro0306@gmail.com")
                .firstName("Trong")
                .lastName("Vuong")
                .dateOfBirth(LocalDate.of(2004, 6, 3))
                .build();
    }

    @Test
        //
    void createUser_validRequest_success() throws Exception {
        // GIVEN: Chuẩn bị dữ liệu đầu vào (request) và giả lập hành vi
        // của userService.createUser để trả về kết quả (userResponse).
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(request);

        // WHEN: Gửi yêu cầu HTTP POST đến API với dữ liệu JSON trong body.
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))

                // THEN: Xác minh rằng response trả về có trạng thái HTTP và nội dung JSON đúng như mong đợi.
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.username").value("trong"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.firstName").value("Trong"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.lastName").value("Vuong"));

        //        log.info("Data: {}", response.andReturn().getResponse().getContentAsString());
    }
}