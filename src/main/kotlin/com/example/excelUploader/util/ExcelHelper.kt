package com.example.excelUploader.util

import com.example.excelUploader.model.FileDB
import org.apache.poi.ss.usermodel.*
import org.springframework.web.multipart.MultipartFile
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.util.*


object ExcelHelper {

    private const val EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    fun hasExcelFormat(file:MultipartFile):Boolean = EXCEL_TYPE == file.contentType

    fun excelToFile(file: MultipartFile): List<FileDB> {
        val workbook: Workbook = XSSFWorkbook(file.inputStream)
        val sheet: Sheet = workbook.getSheet(workbook.getSheetName(0))
        val rows: Iterator<Row> = sheet.iterator()
        val fileList: MutableList<FileDB> = mutableListOf()
        var rowNumber = 0
        while (rows.hasNext()) {
            val currentRow = rows.next()
            // skip header
            if (rowNumber == 0) {
                rowNumber++
                continue
            }

            val fileInfo = FileDB(UUID.randomUUID(), "", "")
            val formatter = DataFormatter()
            currentRow.forEach {
                when (it.columnIndex) {
                    //put your parameters
                    0 -> fileInfo.name = it.stringCellValue
                    1 -> fileInfo.phones = formatter.formatCellValue(it)
//                        2 -> fileInfo.phones = it.arrayFormulaRange.map { it.formatAsString()
//                    }

                }
            }
            rowNumber++
            fileList.add(fileInfo)
        }
        workbook.close()
        return fileList
    }
}