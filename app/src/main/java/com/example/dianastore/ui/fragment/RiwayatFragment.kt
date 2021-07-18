package com.example.dianastore.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import com.example.dianastore.R
import com.example.dianastore.activity.DetailTransaksiActivity
import com.example.dianastore.adapter.AdapterRiwayat
import com.example.dianastore.app.ApiConfig
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.ResponModel
import com.example.dianastore.model.Transaksi
import com.example.dianastore.room.MyDatabase
import com.inyongtisto.myhelper.extension.logs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class RiwayatFragment : Fragment() {

    lateinit var myDb: MyDatabase
    lateinit var s: Prefs

    // dipangil sekali ketika aktivity aktif
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_riwayat, container, false)
        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!
        s = Prefs(requireActivity())

        return view
    }

    private fun getRiwayat() {
        pd.visibility = View.VISIBLE
        var id = 0
        if (Prefs(requireActivity()).getUser() != null) {
            id = Prefs(requireActivity()).getUser()!!.id
        }

        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pd.visibility = View.GONE
                tvError.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pd.visibility = View.GONE
                val res = response.body()!!
                if (res.success == 1) {
                    logs("cek ini:" + res.transaksis.isEmpty())
                    tvError.visibility = if (res.transaksis.isEmpty()) View.VISIBLE else View.GONE
                    displayRiwayat(res.transaksis)
                } else {
                    tvError.visibility = View.VISIBLE
                }
            }
        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>) {
        tvError.visibility = if (transaksis.isEmpty()) View.VISIBLE else View.GONE
        val layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rvRiwayat.adapter = AdapterRiwayat(transaksis, object : AdapterRiwayat.Listeners {
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data, Transaksi::class.java)
                val intent = Intent(requireActivity(), DetailTransaksiActivity::class.java)
                intent.putExtra("transaksi", json)
                startActivity(intent)
            }
        })
        rvRiwayat.layoutManager = layoutManager
    }


    lateinit var pd: ProgressBar
    lateinit var rvRiwayat: RecyclerView
    lateinit var tvError: TextView
    private fun init(view: View) {
        pd = view.findViewById(R.id.pd)
        rvRiwayat = view.findViewById(R.id.rv_riwayat)
        tvError = view.findViewById(R.id.tv_error)
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }
}
