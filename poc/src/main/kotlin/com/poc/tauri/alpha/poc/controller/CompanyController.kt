package com.poc.tauri.alpha.poc.controller

import com.poc.tauri.alpha.poc.dto.CompanyRequest
import com.poc.tauri.alpha.poc.dto.CompanyResponse
import com.poc.tauri.alpha.poc.dto.UserResponse
import com.poc.tauri.alpha.poc.model.Company
import com.poc.tauri.alpha.poc.model.User
import com.poc.tauri.alpha.poc.service.CompanyService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/company")
class CompanyController(private val companyService: CompanyService) {

        @PostMapping
        suspend fun createCompany(@RequestBody companyRequest: CompanyRequest): CompanyResponse =
            companyService.saveCompany(company = companyRequest.toModel())
                ?.toResponse()
                ?: throw ResponseStatusException(HttpStatus.CONFLICT, "Company already exists")

        @GetMapping
        suspend fun findAllCompanies(): List<CompanyResponse> {
            val companies = companyService.findAllCompanies()
            return companies.map { company: Company -> company.toResponse() }
        }

        @GetMapping("/{id}")
        suspend fun findCompanyById(@PathVariable id: Long): CompanyResponse =
            companyService.findById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found") }
                .toResponse()

        @DeleteMapping("/{id}")
        suspend fun deleteCompanyById(@PathVariable id: Long) {
            companyService.deleteByCompanyId(id)
        }

        @PutMapping("/{id}")
        suspend fun updateCompanyById(@PathVariable id: Long, @RequestBody companyRequest: CompanyRequest): CompanyResponse =
            companyService.updateByCompanyId(id, companyRequest.toModel()).toResponse()

        private fun CompanyRequest.toModel(): Company =
            Company(
                name = this.name,
                address = this.address
            )

        private fun Company.toResponse(users: List<User> = emptyList()): CompanyResponse =
            CompanyResponse(
                id = this.id!!,
                name = this.name,
                address = this.address,
                users = users.map { user: User -> user.toResponse() }
            )

        private fun User.toResponse(): UserResponse =
            UserResponse(
                id = this.id!!,
                name = this.name,
                email = this.email,
                age = this.age
            )
}



