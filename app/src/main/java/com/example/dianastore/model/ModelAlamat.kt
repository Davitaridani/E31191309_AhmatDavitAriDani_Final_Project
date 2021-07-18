package com.example.dianastore.model

/**
 * Created by Tisto on 1/20/2021.
 */
class ModelAlamat {
    val id = 0
    val nama = ""

    val status = Status()
    val results = ArrayList<Provinsi>()

    class Status {
        val code = 0
        val description = ""
    }

    class Provinsi {
        var province_id = "0"
        var province = ""
        var city_id = "0"
        var city_name = ""
        var postal_code = ""
        var type = ""
    }

}