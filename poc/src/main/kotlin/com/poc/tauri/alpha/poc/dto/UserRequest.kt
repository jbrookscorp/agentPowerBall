package com.poc.tauri.alpha.poc.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRequest(
    val name: String,
    val email: String,
    val age: Int,
    @JsonProperty("company_id") val companyId: Long
)
