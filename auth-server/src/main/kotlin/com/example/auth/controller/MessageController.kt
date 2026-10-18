package com.example.auth.controller

import com.example.auth.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(
    private val userService: UserService
) {

    @GetMapping("/message")
    fun getMessage(httpSession: HttpSession): ResponseEntity<String> {
        val userId = userService.getAuthenticatedUserId(httpSession)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return ResponseEntity.ok("Hello World")
    }
}
