package com.poc.tauri.alpha.poc.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("application.user")
data class User (
    @Id val id: Long? = null,
    val name: String,
    val email: String,
    val age: Int,
    val companyId: Long
)
