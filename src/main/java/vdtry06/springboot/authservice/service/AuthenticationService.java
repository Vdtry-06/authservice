package vdtry06.springboot.authservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdtry06.springboot.authservice.dto.request.AuthenticationRequest;
import vdtry06.springboot.authservice.exception.AppException;
import vdtry06.springboot.authservice.exception.ErrorCode;
import vdtry06.springboot.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    // xác thực người dùng
    public boolean authenticate(AuthenticationRequest request) {
        // tìm người dùng trong csdl
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // so sánh mật khẩu: mk có độ mạnh càng lớn càng tốn bộ nhớ
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
