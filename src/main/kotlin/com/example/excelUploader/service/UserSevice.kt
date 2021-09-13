package com.example.excelUploader.service

import com.example.excelUploader.dtos.LoginDTO
import com.example.excelUploader.model.Role
import com.example.excelUploader.model.UserDB
import com.example.excelUploader.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Key
import java.time.Duration
import java.time.Instant
import java.util.*


@Service
class UserSevice(@Autowired val userRepository: UserRepository) {

    val key : Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    @Transactional
    fun userSignIn (user: UserDB): UserDB? {
        val userExist = userRepository.findByUsernameIgnoreCase(user.username)
        println(userExist)
        if(userExist != null){
            return null
        }
        user.password = BCryptPasswordEncoder().encode(user.password)
        return userRepository.save(user)
    }

    @Transactional
    fun findByUsername (username: String): UserDB?{
        return userRepository.findByUsernameIgnoreCase(username)
    }

    @Transactional
    fun findByNameAndRole(name: String, role: Role): UserDB? {
        return userRepository.findByUsernameIgnoreCaseAndRole(name, role)
    }

    @Transactional
    fun loginVerif(cred: LoginDTO): String?{

        val user = userRepository.findByUsernameIgnoreCase(cred.username) ?: return null
        if(!BCryptPasswordEncoder().matches(cred.password, user.password)) return null
        return  Jwts.builder()
            .setIssuer(user.id.toString())
            .setAudience(user.role.toString())
            .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(24))))
            .setIssuedAt(Date.from(Instant.now()))
            .signWith(key)
            .compact()

    }

    fun verifyToken(jwt: String?): Claims? {
        if(jwt == null) return null
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .body
        }catch (e:Exception){
            null
        }
    }

    fun getRole(body:Claims): String?{
        return body.audience
    }

    fun isTeacher(body: Claims): Boolean{
        return body.audience == "Teacher"
    }

    fun isStudent(body: Claims): Boolean{
        return body.audience == "Student"
    }

}