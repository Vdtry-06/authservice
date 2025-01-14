package vdtry06.springboot.authservice.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.response.UserResponse;
import vdtry06.springboot.authservice.entity.User;
import vdtry06.springboot.authservice.exception.AppException;
import vdtry06.springboot.authservice.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate birthday;
    private User user;

    @BeforeEach
    void initData() {
        birthday = LocalDate.of(2004, 6, 3);

        request = UserCreationRequest.builder()
                .username("trong")
                .password("12345678")
                .firstName("Trong")
                .lastName("Vuong")
                .dateOfBirth(birthday)
                .build();

        userResponse = UserResponse.builder()
                .id("41b78ff154ba")
                .username("trong")
                .firstName("Trong")
                .lastName("Vuong")
                .dateOfBirth(birthday)
                .build();

        user = User.builder()
                .id("41b78ff154ba")
                .username("trong")
                .firstName("Trong")
                .lastName("Vuong")
                .dateOfBirth(birthday)
                .build();
    }

    @Test
        // Kiểm tra trường hợp tạo người dùng thành công khi tên người dùng chưa tồn tại.
    void createUser_validRequest_success() {
        // GIVEN: Tên người dùng chưa tồn tại trong cơ sở dữ liệu (existsByUsername trả về false).
        // Khi lưu người dùng mới (save), đối tượng trả về là user.
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN: Gọi phương thức createUser của userService với yêu cầu đã chuẩn bị.
        var response = userService.createUser(request);

        // THEN: Xác minh kết quả trả về
        Assertions.assertThat(response.getId()).isEqualTo("41b78ff154ba");
        Assertions.assertThat(response.getUsername()).isEqualTo("trong");
    }

    @Test
        // Kiểm tra trường hợp tạo người dùng thất bại khi tên người dùng đã tồn tại.
    void createUser_userExisted_fail() {
        // GIVEN: Chuẩn bị dữ liệu đầu vào và giả lập hành vi của `userRepository`.
        // Giả lập rằng tên người dùng đã tồn tại trong cơ sở dữ liệu.
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN: Gọi phương thức `createUser` với yêu cầu đã chuẩn bị.
        // Ghi nhận ngoại lệ `AppException` xảy ra khi người dùng đã tồn tại.
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // THEN: THEN: Xác minh rằng ngoại lệ chứa mã lỗi mong đợi.
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    @WithMockUser(username = "trong")
    void getMyInfo_valid_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        Assertions.assertThat(response.getUsername()).isEqualTo("trong");
        Assertions.assertThat(response.getId()).isEqualTo("41b78ff154ba");
    }

    @Test
    @WithMockUser(username = "trong")
    void getMyInfo_userNotFound_error() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1010);
    }
}
