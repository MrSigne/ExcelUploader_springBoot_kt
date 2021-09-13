package com.example.excelUploader.model

import javax.persistence.*

@Entity
@Table(name="subject")
data class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(unique = true)
    var subjectName: String = ""
)