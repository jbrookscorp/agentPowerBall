package com.poc.tauri.alpha.poc.service

import com.poc.tauri.alpha.poc.model.Company
import com.poc.tauri.alpha.poc.repository.CompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class CompanyService(private val companyRepository: CompanyRepository) {
    suspend fun findByName(name: String): Flow<Company> {
        return withContext(Dispatchers.IO) {
            companyRepository.findByNameContaining(name)
        }
    }

    suspend fun findAllCompanies(): MutableIterable<Company> {
        return withContext(Dispatchers.IO) {
            companyRepository.findAll()
        }
    }

    suspend fun findById(id: Long): Optional<Company> {
        return withContext(Dispatchers.IO) {
            companyRepository.findById(id)
        }
    }

    suspend fun saveCompany(company: Company): Company {
        return withContext(Dispatchers.IO) {
            companyRepository.save(company)
        }
    }

    suspend fun deleteByCompanyId(id: Long) {
        val foundCompany = withContext(Dispatchers.IO) {
            companyRepository.findById(id)
        }
        if (foundCompany == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")
        } else {
            withContext(Dispatchers.IO) {
                companyRepository.deleteById(id)
            }
        }
    }

    suspend fun updateByCompanyId(id: Long, reqCompany: Company): Company {
        val foundCompany = withContext(Dispatchers.IO) {
            companyRepository.findById(id)
        }
        return if (foundCompany == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")
        } else {
            withContext(Dispatchers.IO) {
                companyRepository.save(reqCompany.copy(id = id))
            }
        }
    }
}