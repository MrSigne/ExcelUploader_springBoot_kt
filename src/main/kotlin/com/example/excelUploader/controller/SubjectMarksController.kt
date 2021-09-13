package com.example.excelUploader.controller

import com.example.excelUploader.dtos.MessageDTO
import com.example.excelUploader.dtos.ScoreDTO
import com.example.excelUploader.dtos.SubjectDTO
import com.example.excelUploader.model.MarksDB
import com.example.excelUploader.model.Role
import com.example.excelUploader.model.Subject
import com.example.excelUploader.service.SubjectService
import com.example.excelUploader.service.UserSevice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subject")
class SubjectMarksController(@Autowired val subjectService: SubjectService, val userSevice: UserSevice) {

    @PostMapping
    fun addSubject(@RequestBody sub:SubjectDTO, @CookieValue("jwt") jwt: String?):ResponseEntity<Any>{

        val body = userSevice.verifyToken(jwt) ?: return ResponseEntity.status(401).body(MessageDTO("Invalid token!"))
        if(!userSevice.isTeacher(body)) return ResponseEntity.status(403).body(MessageDTO("Permission denied!"))
        val subject = Subject()
        subject.subjectName = sub.subject

        subjectService.addSubject(subject)
            ?: return  ResponseEntity.badRequest().body(MessageDTO("Subject already exist!"))
        return ResponseEntity.ok().body(MessageDTO("Subject Successfully saved"))
    }

    @PostMapping("/register")
    fun register(@RequestBody sub: SubjectDTO, @CookieValue("jwt") jwt: String?):ResponseEntity<Any>{
        val body = userSevice.verifyToken(jwt) ?: return ResponseEntity.status(401).body(MessageDTO("Invalid token!"))
        if(!userSevice.isStudent(body)) return ResponseEntity.status(403).body(MessageDTO("Only student can register subject"))
        val subject = subjectService.findBySubjectName(sub.subject)
            ?: return  ResponseEntity.badRequest().body(MessageDTO("No subject with name "+sub.subject))

        val reg = MarksDB()
        reg.userId = body.issuer.toLong()
        reg.subject = subject.id!!

        return if(subjectService.register(reg)) {
            ResponseEntity.ok().body(MessageDTO(subject.subjectName+" successfully registered!"))
        }else {
            ResponseEntity.badRequest().body(MessageDTO("Subject already registered!"))
        }
    }

    @PostMapping("/score")
    fun addScore(@RequestBody score: ScoreDTO, @CookieValue("jwt") jwt: String?):ResponseEntity<Any>{
        val body = userSevice.verifyToken(jwt) ?: return ResponseEntity.status(401).body(MessageDTO("Invalid token!"))
        if(!userSevice.isTeacher(body)) return ResponseEntity.status(403).body(MessageDTO("Permission denied!"))

        if(!subjectService.isValidScore(score.score)) return ResponseEntity.badRequest().body(MessageDTO("Invalid score"))
        val subject = subjectService.findBySubjectName(score.subject)
            ?: return ResponseEntity.badRequest().body(MessageDTO("No subject with name "+score.subject))
        val user = userSevice.findByNameAndRole(score.student, Role.Student)
            ?: return ResponseEntity.badRequest().body(MessageDTO("No student with name "+score.student))
        val mark = MarksDB()
        mark.userId = user.id!!
        mark.subject = subject.id!!
        mark.score = score.score
        return if(subjectService.addMark(mark)){
            ResponseEntity.ok().body(MessageDTO("Score successfully recorded!"))
        }else{
            ResponseEntity.badRequest().body(MessageDTO(user.username+" has not registered subject: "+subject.subjectName))
        }

    }

    @GetMapping
    fun getSubject(@CookieValue("jwt") jwt: String?): ResponseEntity<Any>{
        userSevice.verifyToken(jwt) ?: return ResponseEntity.status(401).body(MessageDTO("Unauthorized!"))
        return ResponseEntity.ok(subjectService.getAllSubjects())
    }

}