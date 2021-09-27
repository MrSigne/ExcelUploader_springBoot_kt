package com.example.excelUploader.model

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
        @Column
        var passwordChangeCode: String? = null,
        @Enumerated(EnumType.STRING)
        var role: Role = Role.Student
        )