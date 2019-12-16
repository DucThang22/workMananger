@file:Suppress("ConvertToStringTemplate")

package com.adnet.workmanagernotification

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.adnet.workmanagernotification.NotifyWork.Companion.NOTIFICATION_ID
import com.adnet.workmanagernotification.NotifyWork.Companion.NOTIFICATION_WORK
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), View.OnClickListener,
    TimePicker.TimePickerDialogCallback, DatePicker.DatePickerDialogCallback {
    private var year: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var month: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayOfMonth: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private var minute: Int = Calendar.getInstance().get(Calendar.MINUTE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llTime.setOnClickListener(this)
        llDate.setOnClickListener(this)

        textViewTime.text = hour.toString() + " : " + minute.toString()
        textViewDate.text = dayOfMonth.toString() + "/" + month.toString() + "/" + year.toString()

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.llTime -> {
                TimePicker(this).clickTimePicker(this)
            }
            R.id.llDate -> {
                DatePicker(this).clickDatePicker(this)
            }

        }
    }

    @Subscribe
    fun onEvent(event: MyEvent) {
        progress_horizontal.progress= event.per
    }

    private fun createNoti() {
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            year, month, dayOfMonth, hour, minute, 0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = currentTimeMillis()
        if (customTime > currentTime) {
            val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
            val delay = customTime - currentTime
            scheduleNotification(delay, data)
        }
    }


    override fun onChangeTimePicker(hourOfDay: Int, minute: Int) {
        textViewTime.text = hourOfDay.toString() + " : " + minute.toString()
        hour = hourOfDay
        this.minute = minute

        createNoti()
    }

    override fun onChangeDatePicker(dayOfMonth: Int, monthOfYear: Int, year: Int) {
        textViewDate.text =
            dayOfMonth.toString() + "/" + monthOfYear.toString() + "/" + year.toString()
        this.dayOfMonth = dayOfMonth
        month = monthOfYear
        this.year = year

        createNoti()
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()
        
//
//        OneTimeWorkRequest
//
//        Dành cho công việc không được lặp lại
//                Có thể có thiết lập độ trễ
//        Có thể là 1 phần của 1 chuỗi các công việc
//        PeriodicWorkRequest
//
//        Được sử dụng cho các công việc cần thực hiện định kỳ cho đến khi bị hủy.
//            Khoảng thời gian lặp lại tối thiểu có thể được xác định là 15 phút (giống như API của JobScheduler) và không có độ trễ ban đầu
//        Không thể là 1 phần của 1 chuỗi các công việc
//        Trước v2.1-alpha02, bạn không thể tạo một periodicWorkRequest với độ trễ ban đầu.
//        Việc thực thi có thể bị trì hoãn vì WorkManager tuân theo tối ưu hóa pin của hệ điều hành, chẳng hạn như chế độ ngủ gật (doze mode)

    }
}
