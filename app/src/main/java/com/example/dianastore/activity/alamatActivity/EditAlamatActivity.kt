package com.example.dianastore.activity.alamatActivity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.example.dianastore.R
import com.example.dianastore.app.ApiConfigAlamat
import com.example.dianastore.helper.Helper
import com.example.dianastore.model.Alamat
import com.example.dianastore.model.ModelAlamat
import com.example.dianastore.model.ResponModel
import com.example.dianastore.room.MyDatabase
import com.example.dianastore.util.ApiKey
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.setToolbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAlamatActivity : BaseActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()

    var isFirstLoad = true

    private lateinit var alamat: Alamat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)
        setToolbar(toolbar, "Tambah Alamat")
        Helper().blackStatusBar(this)
        getStringExtra()

        mainButton()
        getProvinsi()
    }

    private fun getStringExtra() {
        val json = intent.getStringExtra("alamat")!!
        Log.d("TAG", "getStringExtra: $json")
        alamat = Alamat().toModel(json)
        setupData()
    }

    private fun setupData() {
        pb.visibility = View.GONE
        edt_provinsi.visibility = View.VISIBLE
        edt_kota.visibility = View.VISIBLE

        provinsi.province_id = alamat.id_provinsi.toString()
        provinsi.province = alamat.provinsi

        kota.city_id = alamat.id_kota.toString()
        kota.city_name = alamat.kota

        edt_nama.setText(alamat.name)
        edt_phone.setText(alamat.phone)
        edt_alamat.setText(alamat.alamat)
        edt_type.setText(alamat.type)
        edt_provinsi.setText(alamat.provinsi)
        edt_kota.setText(alamat.kota)
        edt_kodePos.setText(alamat.kodepos)

        getKota(alamat.id_provinsi.toString())
    }

    private fun mainButton() {
        btn_simpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan() {
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.province_id == "0") {
            toast("Silahkan pilih provinsi")
            return
        }

        if (kota.city_id == "0") {
            toast("Silahkan pilih Kota")
            return
        }

//        if (kecamatan.id == 0) {
//            toast("Silahkan pilih Kecamatan")
//            return
//        }

        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province
        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name
//        alamat.id_kecamatan = kecamatan.id
//        alamat.kecamatan = kecamatan.nama

        update(alamat)
    }

    fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText) {
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    private fun getProvinsi() {
        progress.show()
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                progress.dismiss()
                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    edt_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()

                    val listProvinsi = res.rajaongkir.results
                    for (prov in listProvinsi) {
                        arryString.add(prov.province)
                    }

                    setupProvinsi(arryString, listProvinsi)
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun setupProvinsi(arrayString: ArrayList<String>, listProvinsi: ArrayList<ModelAlamat.Provinsi>) {
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayString)
        edt_provinsi.threshold = 1
        edt_provinsi.setAdapter(adapter)
        edt_provinsi.setOnItemClickListener { _, _, position, _ ->
            val name = adapter.getItem(position)!!
            var index = 0
            for (i in listProvinsi.indices) {
                if (name == listProvinsi[i].province) {
                    index = i
                    break
                }
            }

            provinsi = listProvinsi[index]
            val idProv = provinsi.province_id
            getKota(idProv)
            hideKeyboard()
        }
    }

    private fun getKota(id: String) {
        if (!isFirstLoad) {
            pb.visibility = View.VISIBLE
            edt_kota.setText("")
            edt_kota.visibility = View.GONE
        }
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key, id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                isFirstLoad = false
                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    edt_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.rajaongkir.results

                    val arryString = ArrayList<String>()
                    for (kota in listArray) {
                        arryString.add(kota.type + " " + kota.city_name)
                    }

                    progress.dismiss()
                    setupKota(arryString, listArray)
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun setupKota(arrayString: ArrayList<String>, listArray: ArrayList<ModelAlamat.Provinsi>) {
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayString)
        edt_kota.threshold = 1
        edt_kota.setAdapter(adapter)
        edt_kota.setOnItemClickListener { _, _, position, _ ->
            val name = adapter.getItem(position)!!
            var index = 0
            for (i in listArray.indices) {
                if (name == (listArray[i].type + " " + listArray[i].city_name)) {
                    Log.d("TAG", "setupKota: " + name + " - " + listArray[i].city_name + " - " + i)
                    index = i
                    break
                }
            }

            kota = listArray[index]
            Log.d("cek", "Kota:" + Gson().toJson(kota))
            val kodePos = kota.postal_code
            edt_kodePos.setText(kodePos)

            hideKeyboard()
        }
    }

    private fun getKecamatan(id: Int) {
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE
                    val res = response.body()!!
                    val listArray = res.kecamatan

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kecamatan")
                    for (data in listArray) {
                        arryString.add(data.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@EditAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                kecamatan = listArray[position - 1]
                            }
                        }
                    }
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) view = View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun update(data: Alamat) {
        val myDb = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toast("Insert data success")
                    onBackPressed()
                })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
