package vdtry06.springboot.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // id = chuoi ki tu
    String id;
    String username;
    String password;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;

    @ManyToMany
    Set<Role> roles;
}
