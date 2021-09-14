package com.example.excelUploader.model

import org.jetbrains.annotations.NotNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*


enum class Role{
        Student, Teacher
}

@Entity
@Table(name="users")
class UserDB (

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        var id: Long? = null,
        @Column(unique = true)
        var username: String ="",
        @Column(unique = true)
        var email: String ="",
        @Column
        var password: String ="",
        @Enumerated(EnumType.STRING)
        var role: Role = Role.Student
        )