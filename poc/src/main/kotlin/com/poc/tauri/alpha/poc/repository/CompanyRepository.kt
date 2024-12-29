package com.poc.tauri.alpha.poc.repository

import com.poc.tauri.alpha.poc.model.Company
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.CrudRepository

interface CompanyRepository: CrudRepository<Company, Long> {
    fun findByNameContaining(name: String): Flow<Company>
}