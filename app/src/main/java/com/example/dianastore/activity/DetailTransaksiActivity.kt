package com.example.dianastore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dianastore.R
import com.example.dianastore.adapter.AdapterProdukTransaksi
import com.example.dianastore.app.ApiConfig
import com.example.dianastore.helper.Helper
import com.example.dianastore.model.DetailTransaksi
import com.example.dianastore.model.ResponModel
import com.example.dianastore.model.Transaksi
import com.inyongtisto.myhelper.extension.setToolbar
import com.inyongtisto.myhelper.extension.toGone
import com.inyongtisto.myhelper.extension.toRupiah
import com.inyongtisto.myhelper.extension.toVisible
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_detail_transaksi.*
import kotlinx.android.synthetic.main.layout_btn_delete.*
import kotlinx.android.synthetic.main.layout_btn_simpan.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailTransaksiActivity : AppCompatActivity() {

    var transaksi = Transaksi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi)
        setToolbar(toolbar, "Detail Transaksi")
        Helper().blackStatusBar(this)

        val json = intent.getStringExtra("transaksi")
        transaksi = Transaksi().toModel(json!!)

        setData(transaksi)
        displayProduk(transaksi.details)
        mainButton()
    }

    private fun mainButton() {
        btn_delete.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Apakah anda yakin?")
                    .setContentText("Transaksi akan di batalkan dan tidak bisa di kembalikan!")
                    .setConfirmText("Yes, Batalkan")
                    .setConfirmClickListener {
                        it.dismissWithAnimation()
                        batalTransaksi()
                    }
                    .setCancelText("Tutup")
                    .setCancelClickListener {
                        it.dismissWithAnimation()
                    }.show()
        }

        btn_simpan.setOnClickListener {
            val intent = Intent(this, CaraPembayaranActivity::class.java)
            intent.putExtra("transaksi", transaksi.toJson())
            startActivity(intent)
        }
    }

    private fun batalTransaksi() {
        val loading = SweetAlertDialog(this@DetailTransaksiActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()
        ApiConfig.instanceRetrofit.batalChekout(transaksi.id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1) {
                    SweetAlertDialog(this@DetailTransaksiActivity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success...")
                            .setContentText("Transaksi berhasil dibatalakan")
                            .setConfirmClickListener {
                                it.dismissWithAnimation()
                                onBackPressed()
                            }
                            .show()

//                    Toast.makeText(this@DetailTransaksiActivity, "Transaksi berhasil di batalkan", Toast.LENGTH_SHORT).show()
//                    onBackPressed()
//                    displayRiwayat(res.transaksis)
                } else {
                    error(res.message)
                }
            }
        })
    }

    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .show()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(t: Transaksi) {
        val status = if (t.status == "MENUNGGU") "Menunggu Pembayaran" else t.status
        tv_status.text = status

        val formatBaru = "dd MMMM yyyy, kk:mm:ss"
        tv_tgl.text = Helper().convertTanggal(t.created_at, formatBaru)

        tv_penerima.text = t.name + "\n" + t.phone + "\n" + t.detail_lokasi
        tv_alamat.text = t.detail_lokasi
        tv_catatan.text = "" + if (t.deskripsi.isEmpty()) "-" else t.deskripsi
        tv_kodeUnik.text = Helper().convertRupiah(t.kode_unik)
        tv_totalBelanja.text = Helper().convertRupiah(t.total_harga)
        tv_ongkir.text = Helper().convertRupiah(t.ongkir)
        tv_total.text = Helper().convertRupiah(t.total_transfer.toInt() + t.kode_unik)
        tv_kurir.text = t.kurir + "-" + t.jasa_pengiriaman
        tv_invoice.text = t.kode_trx
        tv_catatan.text = t.catatan
        if (t.pajak > 0) div_pajak.toVisible() else div_pajak.toGone()
        tv_pajak.text = t.pajak.toRupiah()
        if (t.discount > 0) div_diskon.toVisible() else div_diskon.toGone()
        tv_diskon.text = t.discount.toRupiah()

        btn_delete.text = "Batalkan Transaksi"
        btn_simpan.text = "Cara Pembayaran"

        if (t.status != "MENUNGGU") div_footer.visibility = View.GONE

        var color = getColor(R.color.menungu)
        if (t.status == "SELESAI") color = getColor(R.color.selesai)
        else if (t.status == "BATAL") color = getColor(R.color.batal)

        tv_status.setTextColor(color)
    }

    private fun displayProduk(transaksis: ArrayList<DetailTransaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_produk.adapter = AdapterProdukTransaksi(transaksis)
        rv_produk.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
