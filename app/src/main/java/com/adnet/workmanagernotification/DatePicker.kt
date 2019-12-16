package com.adnet.workmanagernotification

import android.app.DatePickerDialog
import android.content.Context
import java.util.*

class DatePicker(private val datePickerDialogCallback: DatePickerDialogCallback) {
    fun clickDatePicker(context: Context) {

        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog: DatePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                datePickerDialogCallback.onChangeDatePicker(dayOfMonth, monthOfYear, year)
            }, y, m, d
        )
        datePickerDialog.show()
    }

    interface DatePickerDialogCallback {
        fun onChangeDatePicker(dayOfMonth: Int, monthOfYear: Int, year: Int)
    }
}