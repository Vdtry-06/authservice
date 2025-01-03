package vdtry06.springboot.authservice.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vdtry06.springboot.authservice.dto.request.UserCreationRequest;
import vdtry06.springboot.authservice.dto.request.UserUpdationRequest;
import vdtry06.springboot.authservice.dto.response.ApiResponse;
import vdtry06.springboot.authservice.dto.response.UserResponse;
import vdtry06.springboot.authservice.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return ApiResponse.<UserResponse>builder().data(userService.createUser(request)).build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdationRequest request) {
        return ApiResponse.<UserResponse>builder().data(userService.updateUser(userId, request)).build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder().data(userService.getUsers()).build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder().data(userService.getUser(userId)).build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().data(String.format("User %s has been deleted!", userId)).build();
    }
}
