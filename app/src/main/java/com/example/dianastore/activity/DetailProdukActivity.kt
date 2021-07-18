package com.example.dianastore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.example.dianastore.R
import com.example.dianastore.helper.Helper
import com.example.dianastore.model.TabelKeranjang
import com.example.dianastore.room.MyDatabase
import com.example.dianastore.util.Config
import com.inyongtisto.myhelper.extension.setToolbar
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_custom.*

class DetailProdukActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var produk: TabelKeranjang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        myDb = MyDatabase.getInstance(this)!! // call database
        Helper().blackStatusBar(this)

        getInfo()
        mainButton()
        checkKeranjang()
    }

    private fun mainButton() {
        btn_keranjang.setOnClickListener {
            tambahKeranjang()
        }

        btn_toKeranjang.setOnClickListener {
            goToKeranjang()
        }

        btn_beli.setOnClickListener {
            beli()
        }
    }

    fun tambahKeranjang(iskeranjang: Boolean = true) {
        val data = myDb.daoKeranjang().getProduk(produk.id)
        if (data == null) {
            insert(iskeranjang)
        } else {
            data.jumlah += 1
            update(data, iskeranjang)
        }
    }

    fun goToKeranjang() {
        val intent = Intent("event:keranjang")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        onBackPressed()
    }

    private fun insert(iskeranjang: Boolean = true) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkKeranjang()
                    if (!iskeranjang) return@subscribe
                    else Toast.makeText(this, "Berhasil menambah kekeranjang", Toast.LENGTH_SHORT).show()
                })
    }

    private fun update(data: TabelKeranjang, iskeranjang: Boolean) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkKeranjang()
                    if (!iskeranjang) goToKeranjang()
                    else Toast.makeText(this, "Berhasil menambah kekeranjang", Toast.LENGTH_SHORT).show()
                })
    }

    private fun beli() {
        CompositeDisposable().add(Observable.fromCallable {
            myDb.daoKeranjang().update()
            produk.selected = true
            myDb.daoKeranjang().insert(produk)
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val intent = Intent(this, PengirimanActivity::class.java)
                    intent.putExtra("fromDetail", "true")
                    intent.putExtra("totalHarga", "" + produk.harga)
                    startActivity(intent)
                })
    }

    private fun checkKeranjang() {
        val dataKranjang = myDb.daoKeranjang().getAll()
        div_angka.visibility = if (dataKranjang.isNotEmpty()) View.VISIBLE else View.GONE
        val size = dataKranjang.size.toString()
        tv_angka.text = size
    }

    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson<TabelKeranjang>(data, TabelKeranjang::class.java)

        // set Value
        tv_nama.text = produk.name
        tv_harga.text = Helper().convertRupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi

        val img = Config.productUrl + produk.image
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.product)
                .resize(400, 400)
                .into(image)

        // setToolbar
        setToolbar(toolbar, produk.name)
    }

    override fun onResume() {
        checkKeranjang()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
