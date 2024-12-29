package com.poc.tauri.alpha.poc.controller

import com.poc.tauri.alpha.poc.dto.UserRequest
import com.poc.tauri.alpha.poc.dto.UserResponse
import com.poc.tauri.alpha.poc.model.User
import com.poc.tauri.alpha.poc.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping
    suspend fun createUser(@RequestBody userRequest: UserRequest): UserResponse =
        userService.saveUser(user = userRequest.toModel())
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.CONFLICT, "User already exists")

    @GetMapping
    suspend fun findAllUsers(): List<UserResponse> {
        val users = userService.findAllUsers()
        return users.map { user: User -> user.toResponse() }
    }

    @GetMapping("/{id}")
    suspend fun findUserById(@PathVariable id: Long): UserResponse =
        userService.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
            .toResponse()

    @DeleteMapping("/{id}")
    suspend fun deleteUserById(@PathVariable id: Long) {
        userService.deleteById(id)
    }

    @PutMapping("/{id}")
    suspend fun updateUserById(@PathVariable id: Long, @RequestBody userRequest: UserRequest): UserResponse =
        userService.updateById(id, userRequest.toModel()).toResponse()

    private fun UserRequest.toModel(): User =
        User(
            name = this.name,
            email = this.email,
            age = this.age,
            companyId = this.companyId
        )

    private fun User.toResponse(): UserResponse =
        UserResponse(
            id = this.id!!,
            name = this.name,
            email = this.email,
            age = this.age
        )
}