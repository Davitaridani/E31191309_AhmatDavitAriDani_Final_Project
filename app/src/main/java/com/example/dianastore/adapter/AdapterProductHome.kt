package com.example.dianastore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dianastore.R
import com.example.dianastore.activity.DetailProdukActivity
import com.example.dianastore.model.TabelProduct
import com.example.dianastore.util.Config
import com.inyongtisto.myhelper.extension.toRupiah
import com.inyongtisto.myhelper.extension.toSquare
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterProductHome(var data: ArrayList<TabelProduct>) : RecyclerView.Adapter<AdapterProductHome.Holder>() {

    lateinit var context: Context

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val layout = view.findViewById<CardView>(R.id.layout)
        val image = view.findViewById<ImageView>(R.id.btn_image)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_product_home, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.tvName.text = a.name
        holder.tvHarga.text = a.harga.toRupiah()
        val listImage = a.image.split('|')
        var image = a.image
        if (listImage.isNotEmpty()) image = listImage[0]
        Picasso.get()
                .load(Config.urlProduct(image))
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.product)
                .into(holder.image)

        holder.image.toSquare()

//        holder.image.setImageResource(a.image)
        holder.layout.setOnClickListener {
            val activiti = Intent(context, DetailProdukActivity::class.java)
            activiti.putExtra("extra", data[position].toJson())
            context.startActivity(activiti)
        }
    }
}