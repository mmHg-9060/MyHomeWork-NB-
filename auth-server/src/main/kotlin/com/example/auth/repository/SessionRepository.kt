package com.example.auth.repository

import com.example.auth.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : JpaRepository<SessionEntity, String> {
    fun deleteByUserId(userId: String)
}
