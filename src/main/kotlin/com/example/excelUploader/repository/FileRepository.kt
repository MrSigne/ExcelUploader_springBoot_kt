package com.example.excelUploader.repository

import com.example.excelUploader.model.FileDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FileRepository: JpaRepository<FileDB, UUID>