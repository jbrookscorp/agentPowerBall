package com.poc.tauri.alpha.poc.dto

data class IdNameTypeResponse(
    val id: Long,
    val name: String,
    val type: ResultType
)

enum class ResultType {
    USER,
    COMPANY
}
