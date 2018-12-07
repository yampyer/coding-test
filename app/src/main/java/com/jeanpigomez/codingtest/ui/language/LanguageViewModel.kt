package com.jeanpigomez.codingtest.ui.language

import android.arch.lifecycle.MutableLiveData
import com.jeanpigomez.codingtest.base.BaseViewModel
import com.jeanpigomez.codingtest.model.Language
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.os.CountDownTimer
import java.util.concurrent.TimeUnit


class LanguageViewModel : BaseViewModel() {
    private val languageName = MutableLiveData<String>()
    private val languageScore = MutableLiveData<String>()
    private val languageRemainingTime = MutableLiveData<String>()

    fun bind(language: Language) {
        languageName.value = language.name
        languageScore.value = language.score.toString()
        languageRemainingTime.value = calculateDateDiff(language.endDate)
    }

    fun getLanguageName(): MutableLiveData<String> {
        return languageName
    }

    fun getLanguageScore(): MutableLiveData<String> {
        return languageScore
    }

    fun getLanguageRemainingTime(): MutableLiveData<String> {
        return languageRemainingTime
    }

    private fun parseDateToFormat(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        var convertedDate = Date()
        try {
            convertedDate = dateFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convertedDate
    }

    fun calculateDateDiff(dateString: String): String {
        val dateConverted = parseDateToFormat(dateString)
        val now = Date()

        var diff = dateConverted.time - now.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = diff / daysInMilli
        diff %= daysInMilli

        val elapsedHours = diff / hoursInMilli
        diff %= hoursInMilli

        val elapsedMinutes = diff / minutesInMilli
        diff %= minutesInMilli

        val elapsedSeconds = diff / secondsInMilli

        if (diff < 0) {
            return "Passed!"
        } else if (elapsedDays < 1) {
            object : CountDownTimer(dateConverted.time - now.time, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    languageRemainingTime.value = "The remaining time is: ${TimeUnit.MILLISECONDS.toHours(millisUntilFinished)} hours, " +
                            "${TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished))} minutes, " +
                            "${TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))} seconds"
                }

                override fun onFinish() {
                    languageRemainingTime.value = "Passed!"
                }
            }.start()
        } else {
            languageRemainingTime.value = "The remaining time is: $elapsedDays days, $elapsedHours hours, $elapsedMinutes minutes, $elapsedSeconds seconds"
        }
        return "The remaining time is: $elapsedDays days, $elapsedHours hours, $elapsedMinutes minutes, $elapsedSeconds seconds"
    }
}
