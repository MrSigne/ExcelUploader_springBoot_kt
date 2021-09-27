package com.example.excelUploader.service

import com.example.excelUploader.dtos.LoginDTO
import com.example.excelUploader.dtos.PassChangeDTO
import com.example.excelUploader.dtos.SigninDTO
import com.example.excelUploader.model.Role
import com.example.excelUploader.model.UserDB
import com.example.excelUploader.repository.UserRepository
import com.example.excelUploader.util.ExcelHelper
import com.example.excelUploader.util.RandGenerator
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.security.Key
import java.time.Duration
import java.time.Instant
import java.util.*


@Service
class UserSevice(@Autowired val userRepository: UserRepository, @Autowired val emailService: EmailService) {

    val key : Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    @Transactional
    fun userSignIn (body: SigninDTO): UserDB? {
        val userExist = userRepository.findByUsernameIgnoreCase(body.username)
        if(userExist != null){
            return null
        }
        val user = UserDB()
        user.email = body.email
        user.username = body.username
        user.role = if(body.role.equals("teacher", true)) Role.Teacher else Role.Student
        user.password = BCryptPasswordEncoder().encode(body.password)
        return userRepository.save(user)
    }

    @Transactional
    fun uploadManyUsers (file: MultipartFile){
        try {
            if(ExcelHelper.hasExcelFormat(file)){
                val users = ExcelHelper.excelToUser(file)
                userRepository.saveAll(users)
            }else{
                throw RuntimeException("Not excel format")
            }
        }catch (e: Exception){
            throw Exception("Could not parse file!")
        }
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

    @Transactional
    fun sendPasswordCode(email: String): Boolean?{
        val user = userRepository.findByEmailIgnoreCase(email) ?: return null
        val randGen = RandGenerator()
        val code = randGen.RandGenerator(5)
        return try {
            emailService.sendConfirmationCode(code, user.email)
            user.passwordChangeCode = code
            userRepository.save(user)
            true
        }catch (e: Exception){
            println(e)
            false
        }

    }

    @Transactional
    fun changePassword(data: PassChangeDTO): Boolean?{
        val user = userRepository.findByEmailIgnoreCase(data.email) ?: return null
        if(user.passwordChangeCode == null) return false
        return if(user.passwordChangeCode.equals(data.code)){
            user.password = BCryptPasswordEncoder().encode(data.password)
            user.passwordChangeCode = null
            userRepository.save(user)
            true
        }else{
            false
        }
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