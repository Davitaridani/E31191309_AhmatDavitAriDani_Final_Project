package com.example.dianastore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.example.dianastore.R
import com.example.dianastore.adapter.AdapterRiwayat
import com.example.dianastore.app.ApiConfig
import com.example.dianastore.helper.Helper
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.ResponModel
import com.example.dianastore.model.Transaksi
import com.inyongtisto.myhelper.extension.setToolbar
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        setToolbar( toolbar, "Riwayat Belanja")
        Helper().blackStatusBar(this)
    }

    fun getRiwayat() {
        pd.visibility = View.VISIBLE
        val id = Prefs(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pd.visibility = View.GONE
                tv_error.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pd.visibility = View.GONE
                val res = response.body()!!
                if (res.success == 1) {
                    displayRiwayat(res.transaksis)
                    tv_error.visibility = View.GONE
                } else {
                    tv_error.visibility = View.VISIBLE
                }
            }
        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat.adapter = AdapterRiwayat(transaksis, object : AdapterRiwayat.Listeners {
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data, Transaksi::class.java)
                val intent = Intent(this@RiwayatActivity, DetailTransaksiActivity::class.java)
                intent.putExtra("transaksi", json)
                startActivity(intent)
            }
        })
        rv_riwayat.layoutManager = layoutManager
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
