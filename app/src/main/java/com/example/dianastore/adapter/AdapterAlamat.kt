package com.example.dianastore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dianastore.R
import com.example.dianastore.activity.alamatActivity.EditAlamatActivity
import com.example.dianastore.model.Alamat
import kotlin.collections.ArrayList

class AdapterAlamat(var data: ArrayList<Alamat>, var listener: Listeners) : RecyclerView.Adapter<AdapterAlamat.Holder>() {


    lateinit var context: Context

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvAlamat = view.findViewById<TextView>(R.id.tv_alamat)
        val layout = view.findViewById<CardView>(R.id.layout)
        val btnEdit = view.findViewById<ImageView>(R.id.btn_edit)
        val rd = view.findViewById<RadioButton>(R.id.rd_alamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alamat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.rd.isChecked = a.isSelected
        holder.tvNama.text = a.name
        holder.tvPhone.text = a.phone
        holder.tvAlamat.text = a.alamat + ", " + a.kota + ", " + a.kodepos + ", (" + a.type + ")"

        holder.rd.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }

        holder.layout.setOnClickListener {
            a.isSelected = true
            listener.onClicked(a)
        }

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, EditAlamatActivity::class.java)
            intent.putExtra("alamat", a.toJson())
            context.startActivity(intent)
        }
    }

    interface Listeners {
        fun onClicked(data: Alamat)
    }

}