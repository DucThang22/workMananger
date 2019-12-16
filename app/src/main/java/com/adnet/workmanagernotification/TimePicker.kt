package com.adnet.workmanagernotification

import android.app.TimePickerDialog
import android.content.Context
import java.util.*

class TimePicker(private val timePickerDialogCallback: TimePickerDialogCallback) {
    fun clickTimePicker(context: Context) {
        val c = Calendar.getInstance()
        val hh = c.get(Calendar.HOUR_OF_DAY)
        val mm = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            context, TimePickerDialog.OnTimeSetListener { _,
                                                          hourOfDay,
                                                          minute ->
            timePickerDialogCallback.onChangeTimePicker(hourOfDay, minute)
        }, hh, mm, true)
        timePickerDialog.show()
    }
    
    interface TimePickerDialogCallback {
        fun onChangeTimePicker(hourOfDay: Int, minute: Int)
    }
}
