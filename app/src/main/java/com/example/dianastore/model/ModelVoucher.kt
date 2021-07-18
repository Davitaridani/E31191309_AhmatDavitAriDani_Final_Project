package com.example.dianastore.model

import com.google.gson.Gson

/**
 * Created by Tisto on 1/20/2021.
 */

class ModelVoucher {
    var id = 0
    var name = ""
    var value = 0
    var status = 0

    fun toJson() = Gson().toJson(this)

    fun toModel(string: String) = Gson().fromJson(string, ModelVoucher::class.java)
}