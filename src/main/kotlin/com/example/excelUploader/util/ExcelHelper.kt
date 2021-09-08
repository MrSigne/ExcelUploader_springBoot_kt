package com.example.excelUploader.util

import com.example.excelUploader.model.FileDB
import org.apache.poi.ss.usermodel.*
import org.springframework.web.multipart.MultipartFile
import org.apache.poi.xssf.usermodel.XSSFWorkbook
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
                    //put your parameters
                    0 -> fileInfo.name = formatter.formatCellValue(it)
                    1 -> fileInfo.phones = formatter.formatCellValue(it).split(",").map{it.trim()}.toTypedArray()
                    2 -> fileInfo.address = formatter.formatCellValue(it)
                    3 -> {
                        fileInfo.dob = it.dateCellValue
                        val year = it.dateCellValue.toString().split(" ")
                        (LocalDate.now().year - year[year.lastIndex].toInt()).also { fileInfo.age = it }

                    }

                }
            }

            rowNumber++
            fileList.add(fileInfo)
        }
        workbook.close()
        return fileList
    }
}