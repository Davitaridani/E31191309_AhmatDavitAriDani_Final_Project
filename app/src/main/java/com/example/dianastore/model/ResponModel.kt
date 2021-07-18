package com.example.dianastore.model

class ResponModel {
    var success = 0
    lateinit var message: String
    var user = User()
    var produks: ArrayList<TabelProduct> = ArrayList()
    var transaksis: ArrayList<Transaksi> = ArrayList()
    var sliders: ArrayList<Slider> = ArrayList()

    var rajaongkir = ModelAlamat()
    var transaksi = Transaksi()
    var voucher = ModelVoucher()

    var provinsi: ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten: ArrayList<ModelAlamat> = ArrayList()
    var kecamatan: ArrayList<ModelAlamat> = ArrayList()
}