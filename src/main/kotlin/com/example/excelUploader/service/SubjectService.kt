package com.example.excelUploader.service


import com.example.excelUploader.dtos.SubjectDTO
import com.example.excelUploader.model.MarksDB
import com.example.excelUploader.model.Subject
import com.example.excelUploader.repository.MarksRepository
import com.example.excelUploader.repository.SubjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubjectService(@Autowired val subjectRepository: SubjectRepository, @Autowired val marksRepository: MarksRepository) {

    @Transactional
    fun addSubject (subject: Subject): Subject?{
        val sub = subjectRepository.findBySubjectNameIgnoreCase(subject.subjectName)
        if(sub != null){
            return null
        }
        return subjectRepository.save(subject)
    }

    @Transactional
    fun findBySubjectName(name: String?): Subject?{
        return if(name==null) null else subjectRepository.findBySubjectNameIgnoreCase(name)
    }

    @Transactional
    fun register (marksDB: MarksDB): Boolean{
        val reg = marksRepository.findBySubjectAndUserId(marksDB.subject, marksDB.userId)
        if(reg != null) return false
        marksRepository.save(marksDB)
        return true
    }

    @Transactional
    fun addMark (marksDB: MarksDB): Boolean{
        var reg = marksRepository.findBySubjectAndUserId(marksDB.subject, marksDB.userId) ?: return false
        reg.score = marksDB.score
        marksRepository.save(reg)
        return true
    }

    @Transactional
    fun getAllSubjects(): List<SubjectDTO?>{
        val listSubDTO: MutableList<SubjectDTO> = mutableListOf()
        subjectRepository.findAll().map {
            val subDTO = SubjectDTO()
            subDTO.subject = it.subjectName
            listSubDTO.add(subDTO)
        }
        return listSubDTO
    }

    @Transactional
    fun getAllScore(id: Long): List<Any>{
        val res = marksRepository.findByUserId(id)
        println(res)
        return res
    }

}


