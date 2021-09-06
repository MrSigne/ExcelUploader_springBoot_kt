package com.example.excelUploader.util

import com.example.excelUploader.model.FileDB
import org.apache.poi.ss.usermodel.*
import org.jboss.logging.Logger
import org.springframework.web.multipart.MultipartFile
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.util.*


object ExcelHelper {
    private val logger = Logger.getLogger(ExcelHelper::class.java.name)

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
//            var cell: Cell = sheet.getRow(rowNumber).getCell(1)
//            println(cell.numericCellValue)
            var fileInfo = FileDB(UUID.randomUUID().toString(), "", "")
            var formatter = DataFormatter()
            currentRow.forEach { it ->
                when (it.columnIndex) {
                    //put your parameters
//                    0 -> fileInfo.id = fileInfo.id
                    0 -> fileInfo.name = it.stringCellValue
                    1 -> fileInfo.phones = formatter.formatCellValue(it)
//                        2 -> fileInfo.phones = it.arrayFormulaRange.map { it.formatAsString()
//                    }

                }
            }
            rowNumber++
//            println(fileInfo.id.toString()+" "+fileInfo.name+" "+fileInfo.phones)
            fileList.add(fileInfo)
        }
        workbook.close()
        return fileList
    }
}