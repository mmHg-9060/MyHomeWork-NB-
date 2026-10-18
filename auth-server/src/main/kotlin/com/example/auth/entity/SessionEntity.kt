package com.example.auth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_sessions")
class SessionEntity(
    @Id
    @Column(name = "session_id", nullable = false, length = 128)
    val sessionId: String,

    @Column(name = "user_id", nullable = false, length = 8)
    val userId: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime = LocalDateTime.now().plusMinutes(30)
)
