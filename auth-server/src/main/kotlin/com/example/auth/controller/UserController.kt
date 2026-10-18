package com.example.auth.controller

import com.example.auth.dto.ApiResponse
import com.example.auth.dto.LoginRequest
import com.example.auth.dto.SignUpRequest
import com.example.auth.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup", "/api/auth/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<ApiResponse> {
        userService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse("회원가입이 완료되었습니다."))
    }

    @PostMapping("/login", "/api/auth/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        httpSession: HttpSession
    ): ResponseEntity<ApiResponse> {
        userService.login(request, httpSession)
        return ResponseEntity.ok(ApiResponse("로그인이 완료되었습니다."))
    }

    @PostMapping("/logout", "/api/auth/logout")
    fun logout(httpSession: HttpSession): ResponseEntity<ApiResponse> {
        userService.logout(httpSession)
        return ResponseEntity.ok(ApiResponse("로그아웃이 완료되었습니다."))
    }

    @DeleteMapping("/withdraw", "/api/auth/withdraw")
    fun withdrawDelete(httpSession: HttpSession): ResponseEntity<ApiResponse> {
        userService.withdraw(httpSession)
        return ResponseEntity.ok(ApiResponse("회원탈퇴가 완료되었습니다."))
    }

    @PostMapping("/withdraw", "/api/auth/withdraw")
    fun withdrawPost(httpSession: HttpSession): ResponseEntity<ApiResponse> {
        userService.withdraw(httpSession)
        return ResponseEntity.ok(ApiResponse("회원탈퇴가 완료되었습니다."))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse> {
        val errorMessage = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "입력값이 올바르지 않습니다."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse(errorMessage))
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(ex.statusCode).body(ApiResponse(ex.reason ?: "요청 처리 중 오류가 발생했습니다."))
    }
}
