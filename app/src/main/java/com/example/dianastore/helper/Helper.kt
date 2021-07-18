package com.example.dianastore.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {

    fun convertRupiah(x: Int?, showCurrency: Boolean = true): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        var value = format.format(x).replace(",00", "")
        if (!showCurrency) value = value.replace("Rp", "")
        return value
    }

    fun convertRupiah(x: Double?, showCurrency: Boolean = true): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        var value = format.format(x).replace(",00", "")
        if (!showCurrency) value = value.replace("Rp", "")
        return value
    }

    fun convertRupiah(x: String?, showCurrency: Boolean = true): String {
        if (x == null || x.isEmpty()) return ""
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        var value = format.format(x.toDouble()).replace(",00", "")
        if (!showCurrency) value = value.replace("Rp", "")
        return value
    }

//    fun setToolbar(activity: Activity, toolbar: Toolbar, title: String) {
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        activity.supportActionBar!!.title = title
//        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
//        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//    }


    fun blackStatusBar(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = context.window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    fun convertTanggal(tgl: String, formatBaru: String, fromatLama: String = "yyyy-MM-dd kk:mm:ss") :String{
        val dateFormat = SimpleDateFormat(fromatLama)
        val confert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        return dateFormat.format(confert)
    }

    fun squareImage(view: View) {
        val observer = view.viewTreeObserver
        observer.addOnGlobalLayoutListener {
            view.viewTreeObserver
//            val a = view.measuredHeight
            val b = view.measuredWidth
            view.layoutParams.height = b
            view.requestLayout()
        }
    }

    fun onTextChanged(editText: EditText, listener: TextListener) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener.onChanged(p0!!, p1, p2, p3)
            }
        })
    }

    interface TextListener {
        fun onChanged(s: CharSequence, start: Int, before: Int, count: Int)
    }

    @SuppressLint("SimpleDateFormat")
    class getSalam(){
        val dateNow = System.currentTimeMillis()
        val sTgl = SimpleDateFormat("dd MMMM yyyy")
        val sJam = SimpleDateFormat("kk")
        val tgl: String = sTgl.format(dateNow)
        val jam: String = sJam.format(dateNow)

        fun tgl():String {
            return "Hari Ini, $tgl"
        }

        fun salam():String {
            val iJam = jam.toInt()
            var salam = ""
            if (iJam <= 10) salam = "Selamat Pagi"
            if (iJam in 11..14) salam = "Selamat Siang"
            if (iJam in 13..18) salam = "Selamat Sore"
            if (iJam in 19..24) salam = "Selamat Malam"
            return salam
        }
    }
}