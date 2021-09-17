package com.example.excelUploader.repository

import com.example.excelUploader.model.MarksDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MarksRepository: JpaRepository<MarksDB, Long>{

    fun findBySubjectAndUserId(subId: Long, userId: Long): MarksDB?

    @Query(value = "SELECT subject_name, score FROM marks INNER JOIN" +
            " subject ON subject.id=marks.subject WHERE user_id = ?1", nativeQuery = true)
    fun findByUserId(id: Long): List<Any>


    @Query(value = "SELECT username, score FROM marks INNER JOIN"+
            " users ON users.id=marks.user_id WHERE subject = ?1", nativeQuery = true)
    fun findResultBySubject(id: Long): List<Any>
}