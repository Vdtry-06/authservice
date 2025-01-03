package vdtry06.springboot.authservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class) // Liên kết với EmailValidator
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {

    // Thông báo lỗi khi không hợp lệ
    String message() default "Email is not valid";

    // Các nhóm kiểm tra
    Class<?>[] groups() default {};

    // Tải dữ liệu bổ sung
    Class<? extends Payload>[] payload() default {};
}