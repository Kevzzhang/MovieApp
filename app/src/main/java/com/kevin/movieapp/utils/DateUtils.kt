package com.kevin.movieapp.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun getParseFormattedDate(date: String?): String {
            val locale = Locale("in", "ID")
            val parser = SimpleDateFormat("yyyy-MM-dd", locale)
            val formatter = SimpleDateFormat("dd MMMM yyyy", locale)
            val dateStr: String = formatter.format(parser.parse(date))
            return dateStr
        }

    }
}