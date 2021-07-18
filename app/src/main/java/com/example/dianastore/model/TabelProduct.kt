package com.example.dianastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "tabel_produk")
open class TabelProduct  {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id = ""
    var name: String = ""
    var harga: String = ""
    var deskripsi: String = ""
    var status: String = ""
    var image: String = ""

    open fun toJson(): String = Gson().toJson(this)

    open fun toModel(json: String): TabelProduct = Gson().fromJson(json, TabelProduct::class.java)
}