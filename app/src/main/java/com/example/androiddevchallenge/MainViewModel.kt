/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val mInterval by lazy { TIMER_INTERVAL }
    var mHandler: Handler? = null
    var timeInSeconds = 0L
    var startButtonClicked = MutableLiveData(false)
    var pauseClicked = MutableLiveData(false)
    private var initialTime = mutableListOf<Int>()

    var minProgressTime = MutableLiveData(0.0f)
    var secProgressTime = MutableLiveData(0.0f)
    var hourProgressTime = MutableLiveData(0.0f)

    var hour = MutableLiveData(TextFieldValue("00"))
    var minutes = MutableLiveData(TextFieldValue("00"))
    var seconds = MutableLiveData(TextFieldValue("00"))

    var hourFocus = MutableLiveData(false)
    var minFocus = MutableLiveData(false)
    var secFocus = MutableLiveData(false)

    val delta = mutableStateOf(1f)

    fun initStopWatch() {
        getFormattedStopWatch(timeInSeconds * 1000)
    }

    companion object {
        const val TIMER_INTERVAL = 1000
    }

    fun resetTimerView() {

        timeInSeconds = 0L
        startButtonClicked.value = false
        pauseClicked.value = false

        initStopWatch()
    }

    fun reloadTimer() {

        if (initialTime.isNotEmpty()) {
            timeInSeconds = (initialTime[0] * 60 * 60L) +
                (initialTime[1] * 60L) +
                (initialTime[2])
            startButtonClicked.value = false
            pauseClicked.value = false

            initStopWatch()
        }
    }

    fun setTimerData() {
        timeInSeconds =
            (if (hour.value!!.text.isEmpty()) 0 else hour.value!!.text.toInt() * 60 * 60L) +
            (if (minutes.value!!.text.isEmpty()) 0 else minutes.value!!.text.toInt() * 60L) +
            (if (seconds.value!!.text.isEmpty()) 0 else seconds.value!!.text.toInt())
    }

    fun startTimer() {
        stopTimer()
        mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }

    fun stopTimer() {
        mHandler?.removeCallbacksAndMessages(null)
    }

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                if (timeInSeconds == 0L) {
                    stopTimer()
                    startButtonClicked.value = false
                    pauseClicked.value = false
//                    resetTimerView()
                } else {
                    timeInSeconds -= 1
                    Log.e("timeInSeconds", timeInSeconds.toString())

                    updateStopWatchView(timeInSeconds)
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler!!.postDelayed(this, mInterval.toLong())
            }
        }
    }

    fun updateStopWatchView(timeInSeconds: Long) {
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        Log.e("formattedTime", formattedTime)
    }

    private fun getFormattedStopWatch(ms: Long): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        this.hour = MutableLiveData(TextFieldValue("${if (hours < 10) "0" else ""}$hours"))
        this.minutes = MutableLiveData(TextFieldValue("${if (minutes < 10) "0" else ""}$minutes"))
        this.seconds = MutableLiveData(TextFieldValue("${if (seconds < 10) "0" else ""}$seconds"))

        minProgressTime.value = (minutes.toFloat() / 60f)
        secProgressTime.value = (seconds.toFloat() / 60f)
        hourProgressTime.value = (hours.toFloat() / 12f)
        return "${if (hours < 10) "0" else ""}$hours:" +
            "${if (minutes < 10) "0" else ""}$minutes:" +
            "${if (seconds < 10) "0" else ""}$seconds"
    }

    fun setInitialTime() {
        initialTime = mutableListOf()
        initialTime.add(0, if (hour.value!!.text.isEmpty()) 0 else hour.value!!.text.toInt())
        initialTime.add(1, if (minutes.value!!.text.isEmpty()) 0 else minutes.value!!.text.toInt())
        initialTime.add(2, if (seconds.value!!.text.isEmpty()) 0 else seconds.value!!.text.toInt())
    }
}
