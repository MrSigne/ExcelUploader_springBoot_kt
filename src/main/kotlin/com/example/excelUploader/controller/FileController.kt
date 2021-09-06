package com.example.excelUploader.controller

import com.example.excelUploader.model.File
import com.example.excelUploader.model.FileDB
import com.example.excelUploader.service.FileServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/file")
class FileController(val fileServiceImpl: FileServiceImpl) {
    @PostMapping
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Void> {
        fileServiceImpl.uploadFile(file)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @GetMapping
    fun getPeople(): List<FileDB> =
        fileServiceImpl.getPeople()

    @DeleteMapping
    fun delPeople (): ResponseEntity<Void> {
        fileServiceImpl.delPeople()
        return ResponseEntity(HttpStatus.OK)
    }

}