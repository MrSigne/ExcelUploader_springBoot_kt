package com.example.excelUploader.model

import javax.persistence.*

@Entity
@Table(name = "marks")
class MarksDB (
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var userId: Long=0,
    var subject: Long=0,
    var score: Int? = null
        )