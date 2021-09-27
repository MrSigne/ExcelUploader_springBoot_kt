package com.example.excelUploader.repository

import com.example.excelUploader.model.Role
import com.example.excelUploader.model.UserDB
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository <UserDB, Long>{

    fun findByEmailIgnoreCase(email: String): UserDB?
    fun findByUsernameIgnoreCase(username: String): UserDB?
    fun findByUsernameIgnoreCaseAndRole(username: String, role: Role): UserDB?
    fun findByUsernameOrEmail(email: String, username: String): UserDB?

}