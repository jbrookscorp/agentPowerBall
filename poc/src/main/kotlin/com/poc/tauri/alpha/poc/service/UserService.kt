package com.poc.tauri.alpha.poc.service

import com.poc.tauri.alpha.poc.model.User
import com.poc.tauri.alpha.poc.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService (private val userRepository: UserRepository){

    suspend fun saveUser(user: User): User? = withContext(Dispatchers.IO) {
        userRepository.byEmail(user.email)
    }
        .firstOrNull()
        ?.let {throw ResponseStatusException(HttpStatus.CONFLICT, "User already exists") }
        ?: withContext(Dispatchers.IO) {
            userRepository.save(user)
        }

    suspend fun findAllUsers(): MutableIterable<User> =
        withContext(Dispatchers.IO) {
            userRepository.findAll()
        }

    suspend fun findById(id: Long): Optional<User> = withContext(Dispatchers.IO) {
        userRepository.findById(id)
    }

    suspend fun deleteById(id: Long) {
        val foundUser = withContext(Dispatchers.IO) {
            userRepository.findById(id)
        }
        if (foundUser == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        } else {
            withContext(Dispatchers.IO) {
                userRepository.deleteById(id)
            }
        }
    }

    suspend fun updateById(id: Long, reqUser: User): User {
        val foundUser = withContext(Dispatchers.IO) {
            userRepository.findById(id)
        }
        return if (foundUser == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        } else {
            withContext(Dispatchers.IO) {
                userRepository.save(reqUser.copy(id = id))
            }
        }
    }

    suspend fun findByCompanyId(companyId: Long): Flow<User> =
        withContext(Dispatchers.IO) {
            userRepository.findByCompanyId(companyId)
        }

    suspend fun findByNameContaining(name: String): Flow<User> =
        withContext(Dispatchers.IO) {
            userRepository.findByNameContaining(name)
        }
}