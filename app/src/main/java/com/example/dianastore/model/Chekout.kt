package com.example.dianastore.model

import com.google.gson.Gson

class Chekout {
    lateinit var user_id: String
    lateinit var total_item: String
    lateinit var total_harga: String
    lateinit var name: String
    lateinit var phone: String
    var pajak: String = "0"
    lateinit var detail_lokasi: String
    var catatan: String = "catatan"
    lateinit var kurir: String
    var kodeVoucher: String = "0"
    lateinit var jasa_pengiriaman: String
    lateinit var ongkir: String
    lateinit var total_transfer: String
    lateinit var bank: String
    var produks = ArrayList<Item>()

    class Item {
        lateinit var id: String
        lateinit var total_item: String
        lateinit var total_harga: String
        lateinit var catatan: String
    }

    fun toJson(): String = Gson().toJson(this)

    fun toModel(string: String): Chekout = Gson().fromJson(string, Chekout::class.java)
}