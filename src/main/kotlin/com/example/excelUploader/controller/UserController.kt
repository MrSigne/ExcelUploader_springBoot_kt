package com.example.excelUploader.controller

import com.example.excelUploader.dtos.LoginDTO
import com.example.excelUploader.dtos.MessageDTO
import com.example.excelUploader.dtos.PassChangeDTO
import com.example.excelUploader.dtos.SigninDTO
import com.example.excelUploader.service.UserSevice
import com.example.excelUploader.util.Validators
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
class UserController(@Autowired val userSevice: UserSevice)  {
    val validators = Validators()

    @PostMapping("/signin")
    fun signin (@RequestBody body: SigninDTO): ResponseEntity<Any>{
        if(!validators.isEmail(body.email)) return ResponseEntity.badRequest().body(MessageDTO("Invalid email address!"))
        if(body.username.isNullOrEmpty()) return ResponseEntity.badRequest().body(MessageDTO("Username required!"))
        if(body.password.isNullOrEmpty()) return ResponseEntity.badRequest().body(MessageDTO("Password required!"))

        val res = userSevice.userSignIn(body)
        if(res !=null){
            return ResponseEntity.status(201).body(MessageDTO("User successfully registered!"))
        }
        return ResponseEntity.badRequest().body(MessageDTO("Could not register! Username already Exist"))
    }
    @PostMapping("/login")
    fun login (@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any>{
        val jwt = userSevice.loginVerif(body)
        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return if(jwt != null) ResponseEntity.ok(MessageDTO("Login successful!")) else ResponseEntity.badRequest().body(MessageDTO("Invalid login credentials"))
    }

    @PostMapping("/forgetPass")
    fun sendPasswordCode(@RequestBody body: PassChangeDTO): ResponseEntity<Any>{
        if(!validators.isEmail(body.email)) return ResponseEntity.badRequest().body(MessageDTO("Invalid email address!"))
        val res = userSevice.sendPasswordCode(body.email)
            ?: return ResponseEntity.status(404).body(MessageDTO("No user with this email"))
        return ResponseEntity.ok(MessageDTO("Password change code successfully sent to your email"))
    }

    @PostMapping("/changePass")
    fun changePassword(@RequestBody body: PassChangeDTO): ResponseEntity<Any>{
        if(!validators.isEmail(body.email)) return ResponseEntity.badRequest().body(MessageDTO("Invalid email address!"))
        if(body.code.isNullOrEmpty()) return ResponseEntity.badRequest().body(MessageDTO("Password change code required!"))
        if(body.password.isNullOrEmpty()) return ResponseEntity.badRequest().body(MessageDTO("Password required!"))

        val res = userSevice.changePassword(body) ?: return ResponseEntity.status(404).body(MessageDTO("No user with this email"))
        return  if(!res){
            ResponseEntity.badRequest().body(MessageDTO("Invalid password confirmation code!"))
        }else{
            ResponseEntity.ok(MessageDTO("Password successfully changed!!!"))
        }
    }

    @GetMapping("/login")
    fun verifToken(@CookieValue("jwt") jwt: String?): ResponseEntity<Any>{
        try{
            if (jwt == null) return ResponseEntity.status(401).body(MessageDTO("Unauthorized!"))
            val body = userSevice.verifyToken(jwt)
            return ResponseEntity.ok(body)
        }catch (e: Exception){
            return  ResponseEntity.status(401).body(MessageDTO("Could not parse jwt!"))
        }
    }

}