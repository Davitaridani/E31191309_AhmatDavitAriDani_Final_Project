package com.example.dianastore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.example.dianastore.R
import com.example.dianastore.activity.alamatActivity.ListAlamatActivity
import com.example.dianastore.adapter.AdapterKurir
import com.example.dianastore.app.ApiConfigAlamat
import com.example.dianastore.helper.Helper
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.*
import com.example.dianastore.model.rajaongkir.Costs
import com.example.dianastore.model.rajaongkir.ResponOngkir
import com.example.dianastore.room.MyDatabase
import com.example.dianastore.util.ApiKey
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.*
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class PengirimanActivity : BaseActivity() {

    lateinit var myDb: MyDatabase
    private var totalHarga = 0
    private lateinit var listProduk: ArrayList<TabelKeranjang>
    private lateinit var s: Prefs
    private var user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        setToolbar(toolbar, "Pengiriman")
        myDb = MyDatabase.getInstance(this)!!
        s = Prefs(this)
        user = s.getUser()!!

        totalHarga = Integer.valueOf(intent.getStringExtra("totalHarga")!!)
        tv_totalBelanja.text = Helper().convertRupiah(totalHarga)

        mainButton()
        setSepiner()
        getDataProduct()
        getIntentExtra()
    }

    private fun getDataProduct() {
        listProduk = myDb.daoKeranjang().getAll() as ArrayList
    }

    private fun getIntentExtra() {
        if (intent.getStringExtra("fromDetail") != null) deleteLastItem()
    }

    val gratisOngkir = "GRATIS ONGKIR"

    private fun setSepiner() {

        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")


        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spn_kurir.selectedItem.toString() != gratisOngkir) {
                    getOngkir(spn_kurir.selectedItem.toString())
                    div_kurir.visibility = View.VISIBLE
                } else {
                    ongkir = "0"
                    kurir = gratisOngkir
                    jasaKirim = "Reguler"
                    div_kurir.visibility = View.GONE
                    setTotal()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun chekAlamat() {
        if (myDb.daoAlamat().getByStatus(true) != null) {
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = a.type + " - " + a.alamat + ", " + a.kota + ", " + a.kodepos
            btn_tambahAlamat.text = "Ubah Alamat"

            getOngkir("JNE")
        } else {
            pd.visibility = View.GONE
            tv_error.visibility = View.VISIBLE
            tv_error.text = "Atur alamat terlebih dahulu"
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_tambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun mainButton() {
        btn_tambahAlamat.setOnClickListener {
            startActivity(Intent(this, ListAlamatActivity::class.java))
        }

        btn_bayar.setOnClickListener {
            bayar()
        }
    }

    var diskon = 0

    private fun bayar() {

        val a = myDb.daoAlamat().getByStatus(true)
        if (a == null) {
            Toast.makeText(this, "Atur alamat untuk melanjutkan", Toast.LENGTH_SHORT).show()
            return
        }

        var totalItem = 0
        var totalHarga = 0
        val produks = ArrayList<Chekout.Item>()
        for (p in listProduk) {
            if (p.selected) {
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                val produk = Chekout.Item()
                produk.id = "" + p.id
                produk.total_item = "" + p.jumlah
                produk.total_harga = "" + (p.jumlah * Integer.valueOf(p.harga))
                produk.catatan = "-"
                produks.add(produk)
            }
        }

        val chekout = Chekout()
        chekout.user_id = "" + user.id
        chekout.total_item = "" + totalItem
        chekout.total_harga = "" + totalHarga
        chekout.total_transfer = "" + (totalHarga + Integer.valueOf(ongkir))
        chekout.name = a.name
        chekout.phone = a.phone
        chekout.jasa_pengiriaman = jasaKirim
        chekout.ongkir = ongkir
        chekout.kurir = kurir
        chekout.detail_lokasi = tv_alamat.text.toString()
        chekout.produks = produks
        chekout.catatan = if (edt_catatan.isEmpty(false)) "-" else edt_catatan.text.toString()

        val json = Gson().toJson(chekout, Chekout::class.java)
        val intent = Intent(this, PembayaranActivity::class.java)
        intent.putExtra("extra", json)
        startActivity(intent)
    }

    private fun getOngkir(kurir: String) {
        val alamat = myDb.daoAlamat().getByStatus(true) ?: return
        val origin = "501"
        val destination = "" + alamat.id_kota.toString()
        val berat = 1000

        pd.visibility = View.VISIBLE
        rv_metode.visibility = View.GONE
        tv_error.toGone()
        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object : Callback<ResponOngkir> {
            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {
                pd.visibility = View.GONE
                rv_metode.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    Log.d("Success", "berhasil memuat data")
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()) {
                        tv_error.visibility = View.GONE
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    } else tv_error.visibility = View.VISIBLE
                } else {
                    tv_error.visibility = View.VISIBLE
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                pd.visibility = View.GONE
                tv_error.visibility = View.VISIBLE
                Log.d("Error", "gagal memuat data:" + t.message)
            }
        })
    }

    var ongkir = ""
    var kurir = ""
    var jasaKirim = ""
    private fun displayOngkir(_kurir: String, arrayList: ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices) {
            val ongkir = arrayList[i]
            if (i == 0) {
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }
        ongkir = arrayOngkir[0].cost[0].value
        setTotal()
        kurir = _kurir
        jasaKirim = arrayOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir, _kurir, object : AdapterKurir.Listeners {
            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for (ongkir in arrayOngkir) {
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()

                ongkir = data.cost[0].value
                setTotal()
                kurir = _kurir
                jasaKirim = data.service
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    fun setTotal() {
        tv_ongkir.text = ongkir.toRupiah()
        tv_total.text = (Integer.valueOf(ongkir) + totalHarga).toRupiah()
    }


    private fun deleteLastItem() {
        val produk = myDb.daoKeranjang().getLastItem()
        if (produk != null) myDb.daoKeranjang().delete(produk)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        chekAlamat()
        super.onResume()
    }
}
