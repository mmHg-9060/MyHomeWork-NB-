package com.example.auth.repository

import com.example.auth.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findById(id: String): UserEntity?
    fun existsById(id: String): Boolean
    fun deleteById(id: String)
}
