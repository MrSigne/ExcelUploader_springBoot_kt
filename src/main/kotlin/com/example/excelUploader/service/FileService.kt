package com.example.excelUploader.service

import com.example.excelUploader.model.File
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun uploadFile(file: MultipartFile): File
}