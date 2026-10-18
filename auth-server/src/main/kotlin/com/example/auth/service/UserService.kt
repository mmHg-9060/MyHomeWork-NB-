package com.example.auth.service

import com.example.auth.dto.LoginRequest
import com.example.auth.dto.SignUpRequest
import com.example.auth.entity.SessionEntity
import com.example.auth.entity.UserEntity
import com.example.auth.repository.SessionRepository
import com.example.auth.repository.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val passwordEncoder: PasswordEncoder
) {
    companion object {
        const val SESSION_USER_KEY = "LOGGED_IN_USER_ID"
    }

    @Transactional
    fun signUp(request: SignUpRequest) {
        if (request.password != request.confirmPassword) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호와 확인용 비밀번호가 일치하지 않습니다.")
        }

        if (userRepository.existsById(request.id)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.")
        }

        val user = UserEntity(
            id = request.id,
            password = passwordEncoder.encode(request.password)
        )
        userRepository.save(user)
    }

    @Transactional
    fun login(request: LoginRequest, httpSession: HttpSession) {
        val user = userRepository.findById(request.id)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.")
        }

        httpSession.setAttribute(SESSION_USER_KEY, user.id)

        val sessionEntity = SessionEntity(
            sessionId = httpSession.id,
            userId = user.id,
            createdAt = LocalDateTime.now(),
            expiresAt = LocalDateTime.now().plusMinutes(30)
        )
        sessionRepository.save(sessionEntity)
    }

    @Transactional
    fun logout(httpSession: HttpSession) {
        val userId = getAuthenticatedUserId(httpSession)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.")

        sessionRepository.deleteById(httpSession.id)
        httpSession.invalidate()
    }

    @Transactional
    fun withdraw(httpSession: HttpSession) {
        val userId = getAuthenticatedUserId(httpSession)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.")

        sessionRepository.deleteByUserId(userId)
        userRepository.deleteById(userId)
        httpSession.invalidate()
    }

    @Transactional(readOnly = true)
    fun getAuthenticatedUserId(httpSession: HttpSession): String? {
        val sessionUserId = httpSession.getAttribute(SESSION_USER_KEY) as? String ?: return null
        val savedSession = sessionRepository.findById(httpSession.id).orElse(null) ?: return null

        if (savedSession.expiresAt.isBefore(LocalDateTime.now()) || savedSession.userId != sessionUserId) {
            return null
        }

        if (!userRepository.existsById(sessionUserId)) {
            return null
        }

        return sessionUserId
    }
}
