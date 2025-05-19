package com.library.userservice.dto;

import com.library.userservice.utils.constant.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 3, message = "Full name must be at least 3 character")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Phone number must be a valid Vietnamese format starting with 0")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(min = 3, message = "Address must be at least 3 characters")
    private String address;

    @NotNull(message = "User role is required")
    private Role role;
}
