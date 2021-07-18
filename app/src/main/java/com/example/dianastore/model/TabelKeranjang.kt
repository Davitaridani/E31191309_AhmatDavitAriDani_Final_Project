package com.example.dianastore.model

import androidx.room.Entity
import com.google.gson.Gson

@Entity(tableName = "tabel_keranjang")
class TabelKeranjang : TabelProduct() {

    var idTabel = 0
    var idProduct = ""
    var catatan: String = "" // catatan gigi
    var discount = 0
    var jumlah = 1
    var selected = true

    override fun toJson() = Gson().toJson(this)

    override fun toModel(json: String) = Gson().fromJson(json, TabelKeranjang::class.java)

}