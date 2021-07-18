package com.example.dianastore.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.example.dianastore.R
import com.example.dianastore.helper.Helper
import com.example.dianastore.model.Transaksi
import com.inyongtisto.myhelper.extension.setToolbar
import com.inyongtisto.myhelper.extension.toRupiah
import kotlinx.android.synthetic.main.activity_cara_pembayaran.*
import kotlinx.android.synthetic.main.toolbar.*

class CaraPembayaranActivity : AppCompatActivity() {

    var nominal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cara_pembayaran)
        setToolbar(toolbar, "Informasi Pembayaran")
        Helper().blackStatusBar(this)

        setValues()
        mainButton()
    }

    fun mainButton() {
        btn_copyNoRek.setOnClickListener {
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener {
            copyText(nominal.toString())
        }
    }

    private fun copyText(text: String) {
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)

        Toast.makeText(this, "Text berhasil di Kopi", Toast.LENGTH_LONG).show()
    }

    private fun setValues() {
        val jsTransaksi = intent.getStringExtra("transaksi")
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)

        tv_nomorRekening.text = getString(R.string.norek)
        tv_namaPenerima.text = getString(R.string.user)
//        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_transfer) + transaksi.kode_unik
        tv_nominal.text = nominal.toRupiah()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
