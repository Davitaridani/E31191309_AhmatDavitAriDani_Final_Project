package com.example.dianastore.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.example.dianastore.R
import com.example.dianastore.activity.DetailProdukActivity
import com.example.dianastore.helper.Helper
import com.example.dianastore.model.TabelKeranjang
import com.example.dianastore.util.Config
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterProduk(var activity: Activity, var data: ArrayList<TabelKeranjang>) : RecyclerView.Adapter<AdapterProduk.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<RelativeLayout>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return if (data.size > 6) 6 else data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val hargaAsli = Integer.valueOf(a.harga)
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().convertRupiah(hargaAsli)
        val image = Config.productUrl + data[position].image
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, DetailProdukActivity::class.java)
            val str = Gson().toJson(data[position], TabelKeranjang::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }
    }

}