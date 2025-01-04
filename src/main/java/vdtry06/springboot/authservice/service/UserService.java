package vdtry06.springboot.authservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.request.UserUpdationRequest;
import vdtry06.springboot.authservice.dto.response.UserResponse;
import vdtry06.springboot.authservice.entity.User;
import vdtry06.springboot.authservice.exception.AppException;
import vdtry06.springboot.authservice.exception.ErrorCode;
import vdtry06.springboot.authservice.mapper.UserMapper;
import vdtry06.springboot.authservice.repository.UserRepository;

import java.util.List;

import static vdtry06.springboot.authservice.exception.ErrorCode.USER_NOT_EXISTED;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (userRepository.existsByPassword(request.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("User created: " + user);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (userRepository.existsByPassword(request.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        userMapper.updateUser(user, request);

        log.info("Updating user with id: " + userId);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String id) {
        log.info("Getting user with id: " + id);
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(USER_NOT_EXISTED)));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            log.info("User not existed!");
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        log.info("Deleting user {}", id);
        userRepository.deleteById(id);
    }
}
