package com.muhammhassan.epatrol.utils

object Utils {
    fun getStatus(statusCode: String): String =
        when(statusCode){
            "belum-dikerjakan" -> "Belum dikerjakan"
            "sedang-dikerjakan" -> "Sedang berjalan"
            "sudah-dikerjakan" -> "Sudah dikerjakan"
            else -> throw IllegalArgumentException("Invalid status code : $statusCode")
        }

}