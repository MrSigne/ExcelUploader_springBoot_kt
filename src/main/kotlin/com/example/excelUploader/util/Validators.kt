package com.example.excelUploader.util

import org.springframework.stereotype.Service

@Service
class Validators {
    private val regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"

    fun isEmail(email: String?): Boolean{
        if(email.isNullOrBlank()){
            return false
        }
        val reg = regex.toRegex(setOf(RegexOption.IGNORE_CASE))
        return reg.matches(email)
    }

    fun isValidScore (score:Any): Boolean{
        return if(score is Number){
            score.toInt() in (0..100)
        }else{
            false
        }
    }
}