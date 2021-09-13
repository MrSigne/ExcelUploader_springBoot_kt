package com.example.excelUploader.repository

import com.example.excelUploader.model.MarksDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MarksRepository: JpaRepository<MarksDB, Long>{

    fun findBySubjectAndUserId(subId: Long, userId: Long): MarksDB?
}