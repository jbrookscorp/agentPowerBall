package com.poc.tauri.alpha.poc.controller

import com.poc.tauri.alpha.poc.dto.IdNameTypeResponse
import com.poc.tauri.alpha.poc.dto.ResultType
import com.poc.tauri.alpha.poc.model.Company
import com.poc.tauri.alpha.poc.model.User
import com.poc.tauri.alpha.poc.service.CompanyService
import com.poc.tauri.alpha.poc.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Service
@RequestMapping("/api/v1/search")
class SearchController (
    private val userService: UserService,
    private val companyService: CompanyService){

    @GetMapping
    suspend fun searchByName(@RequestParam("query") query: String) : Flow<Any> {
        val userFlow = userService.findByNameContaining(query)
            .map { user: User -> user.toIdNameTypeResponse() }
        val companyFlow = companyService.findByName(query)
            .map { company: Company -> company.toIdNameTypeResponse() }

        return merge (userFlow, companyFlow)
    }
}

private fun User.toIdNameTypeResponse() : IdNameTypeResponse {
    return IdNameTypeResponse(
        id = this.id!!,
        name = this.name,
        type = ResultType.USER
    )
}

private fun Company.toIdNameTypeResponse() : IdNameTypeResponse {
    return IdNameTypeResponse(
        id = this.id!!,
        name = this.name,
        type = ResultType.COMPANY
    )
}