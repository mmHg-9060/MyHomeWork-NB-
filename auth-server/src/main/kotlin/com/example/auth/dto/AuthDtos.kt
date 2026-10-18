package com.example.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignUpRequest(
    @field:NotBlank(message = "아이디는 필수입니다.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]{3,8}$",
        message = "아이디는 영문자와 숫자를 조합한 3~8자여야 합니다."
    )
    val id: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{5,15}$",
        message = "비밀번호는 영문자, 숫자 중 최소 1자와 특수문자(!@#$%^&*) 최소 1자를 포함한 5~15자여야 합니다."
    )
    val password: String,

    @field:NotBlank(message = "확인용 비밀번호는 필수입니다.")
    val confirmPassword: String
)

data class LoginRequest(
    @field:NotBlank(message = "아이디는 필수입니다.")
    val id: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String
)

data class ApiResponse(
    val message: String
)
