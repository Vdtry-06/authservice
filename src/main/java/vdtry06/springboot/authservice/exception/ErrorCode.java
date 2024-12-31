package vdtry06.springboot.authservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    INVALID_KEY(1001, "Uncategorized Exception"),
    USER_EXISTED(1002, "User already exists"),
    USERNAME_INVALID(1003, "Username must be at least {min} characters"),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters"),
    EMAIL_INVALID(1005, "Email is not valid"),
    INVALID_BIRTHDAY(1006, "Your age must be at least {min}"),
    USER_NOT_EXISTED(1007, "User not exists"),
    UNAUTHENTICATED(1008, "Unauthenticated"),
    UNAUTHORIZED(1009, "You do not have permission"),
    USER_NOT_FOUND(1010, "User not found"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;


}
