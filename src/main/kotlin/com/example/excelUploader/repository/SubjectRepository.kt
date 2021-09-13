package com.example.excelUploader.repository

import com.example.excelUploader.model.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository: JpaRepository<Subject, Int>{
    fun findBySubjectNameIgnoreCase(subjectName:String): Subject?
}