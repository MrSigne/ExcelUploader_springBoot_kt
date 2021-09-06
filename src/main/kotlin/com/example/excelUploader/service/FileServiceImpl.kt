package com.example.excelUploader.service

import com.example.excelUploader.model.FileDB
import com.example.excelUploader.repository.FileRepository
import com.example.excelUploader.util.ExcelHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

//interface FileService{
//    fun uploadFile(file: MultipartFile)
//}

@Service
class FileServiceImpl(private  val fileRepository: FileRepository) {
    @Transactional
    fun uploadFile(file: MultipartFile){
        if (ExcelHelper.hasExcelFormat(file)) {
            val files = ExcelHelper.excelToFile(file)
            fileRepository.saveAll(files)
        } else {
            throw RuntimeException("Not excel format")
        }
    }
    @Transactional
    fun getPeople(): List<FileDB>{
       return fileRepository.findAll()
    }

    @Transactional
    fun delPeople(){
        fileRepository.deleteAll()
    }
}