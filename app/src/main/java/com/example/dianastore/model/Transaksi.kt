package com.example.dianastore.model

import com.google.gson.Gson

class Transaksi {
    var id = 0
    var bank = ""
    var jasa_pengiriaman = ""
    var kurir = ""
    var name = ""
    var ongkir = ""
    var phone = ""
    var pajak = 0
    var discount = 0
    var total_harga = ""
    var total_item = ""
    var total_transfer = ""
    var detail_lokasi = ""
    var deskripsi = ""
    var user_id = ""
    var kode_payment = ""
    var kode_trx = ""
    var kode_unik = 0
    var status = ""
    var catatan = ""
    var expired_at = ""
    var updated_at = ""
    var created_at = ""
    val details = ArrayList<DetailTransaksi>()

    fun toJson() = Gson().toJson(this)

    fun toModel(string: String) = Gson().fromJson(string, Transaksi::class.java)
}