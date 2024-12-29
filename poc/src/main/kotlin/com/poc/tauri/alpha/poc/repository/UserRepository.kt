package com.poc.tauri.alpha.poc.repository

import com.poc.tauri.alpha.poc.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
    fun findByNameContaining(name: String): Flow<User>
    fun findByCompanyId(companyId: Long): Flow<User>

    @Query("SELECT * FROM application.user WHERE email = :email")
    fun byEmail(email: String): Flow<User>
}