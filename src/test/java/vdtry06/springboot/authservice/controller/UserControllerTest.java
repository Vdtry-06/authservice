package vdtry06.springboot.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.response.UserResponse;
import vdtry06.springboot.authservice.service.UserService;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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

        when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN: Gửi yêu cầu HTTP POST đến API với dữ liệu JSON trong body.
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))

                // THEN: Xác minh rằng response trả về có trạng thái HTTP và nội dung JSON đúng như mong đợi.
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.id").value("41b78ff154ba"));
    }

    @Test
        //
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN: dự đoán đầu vào đầu ra xảy ra như vậy
        request.setUsername("tro");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // WHEN: khi request API
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1005"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 5 characters"));

        // THEN: nhận response
    }
}