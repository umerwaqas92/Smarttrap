package com.smarttrap.Utills

import java.text.SimpleDateFormat
import java.util.*

public class Utilss{
    companion object{

        fun getDate(date:String): String {
            var milliSeconds: Long= date.split(".")[0].toLong()*1000
            var dateFormat: String="dd/MM/yyyy hh:mm:ss.SSS"
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.setTimeInMillis(milliSeconds)
            return formatter.format(calendar.getTime())
        }
    }
}