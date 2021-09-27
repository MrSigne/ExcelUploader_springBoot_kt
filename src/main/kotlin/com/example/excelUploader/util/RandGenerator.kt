package com.example.excelUploader.util

class RandGenerator {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun RandGenerator(length: Int): String {
        return (1..length)
            .map{charPool.random()}
            .joinToString("")
    }
}