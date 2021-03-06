package com.example.excelUploader.util

import com.example.excelUploader.model.FileDB
import com.example.excelUploader.model.Role
import com.example.excelUploader.model.Subject
import com.example.excelUploader.model.UserDB
import org.apache.poi.ss.usermodel.*
import org.springframework.web.multipart.MultipartFile
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate
import java.util.*


object ExcelHelper {

    private const val EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    fun hasExcelFormat(file:MultipartFile):Boolean = EXCEL_TYPE == file.contentType

    fun excelToFile(file: MultipartFile): List<FileDB> {
        val workbook: Workbook = XSSFWorkbook(file.inputStream)
        val sheet: Sheet = workbook.getSheet(workbook.getSheetName(0))
        val rows: Iterator<Row> = sheet.iterator()
        val fileList: MutableList<FileDB> = mutableListOf()
        val phone: Array<String> = arrayOf()

        var rowNumber = 0
        while (rows.hasNext()) {
            val currentRow = rows.next()
            // skip header
            if (rowNumber == 0) {
                rowNumber++
                continue
            }

            val fileInfo = FileDB(UUID.randomUUID(), "",phone , "", Date(), 0)
            val formatter = DataFormatter()
            currentRow.forEach { it ->
                when (it.columnIndex) {

                    0 -> fileInfo.name = formatter.formatCellValue(it)
                    1 -> fileInfo.phones = formatter.formatCellValue(it).split(",").map{it.trim()}.toTypedArray()
                    2 -> fileInfo.address = formatter.formatCellValue(it)
                    3 -> {
                        fileInfo.dob = it.dateCellValue
                        val year = it.dateCellValue.toString().split(" ")
                        (LocalDate.now().year - year[year.lastIndex].toInt()).also { fileInfo.age = it }

                    }
                    4 -> fileInfo.location = formatter.formatCellValue(it).split(",").map {it.trim().toFloat()}.toTypedArray()

                }
            }

            rowNumber++
            fileList.add(fileInfo)
        }
        workbook.close()
        return fileList
    }

    fun excelToUser(file: MultipartFile): List<UserDB> {
        val workbook: Workbook = XSSFWorkbook(file.inputStream)
        val sheet: Sheet = workbook.getSheet(workbook.getSheetName(0))
        val rows: Iterator<Row> = sheet.iterator()
        val userList: MutableList<UserDB> = mutableListOf()

        var rowNumber = 0
        while (rows.hasNext()) {
            val currentRow = rows.next()
            // skip header
            if (rowNumber == 0) {
                rowNumber++
                continue
            }

            val userInfo = UserDB()
            val formatter = DataFormatter()
            currentRow.forEach { it ->
                when (it.columnIndex) {
                    //The Excel file should have the username, email password and role in that order per row
                    0 -> userInfo.username = formatter.formatCellValue(it)
                    1 -> userInfo.email = formatter.formatCellValue(it)
                    2 -> userInfo.password = BCryptPasswordEncoder().encode(formatter.formatCellValue(it))
                    3 -> userInfo.role = if (formatter.formatCellValue(it).equals("teacher", true)) Role.Teacher else Role.Student

                }
            }

            rowNumber++
            userList.add(userInfo)
        }
        workbook.close()
        return userList
    }

    fun excelToSubject(file: MultipartFile): List<Subject> {
        val workbook: Workbook = XSSFWorkbook(file.inputStream)
        val sheet: Sheet = workbook.getSheet(workbook.getSheetName(0))
        val rows: Iterator<Row> = sheet.iterator()
        val subjectList: MutableList<Subject> = mutableListOf()

        var rowNumber = 0
        while (rows.hasNext()) {
            val currentRow = rows.next()
            if (rowNumber == 0) {
                rowNumber++
                continue
            }

            val subjectInfo = Subject()
            val formatter = DataFormatter()
            currentRow.forEach { it ->
                when (it.columnIndex) {
                    0 -> subjectInfo.subjectName = formatter.formatCellValue(it)
                }
            }

            rowNumber++
            subjectList.add(subjectInfo)
        }
        workbook.close()
        return subjectList
    }
}