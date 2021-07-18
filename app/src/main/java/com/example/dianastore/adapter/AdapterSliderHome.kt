package com.example.dianastore.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.example.dianastore.R
import com.example.dianastore.model.Slider
import com.example.dianastore.util.Config
import com.squareup.picasso.Picasso
import java.util.ArrayList

class AdapterSliderHome(var data: ArrayList<Slider>, var context: Activity?) : PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_slider, container, false)
        val a = data[position]
        //Init
        val imageView: ImageView = view.findViewById(R.id.image)
        val layout: CardView = view.findViewById(R.id.layout)

        layout.setOnClickListener {
//            val intnt = Intent(context, ListResepActivity::class.java)
//            context!!.startActivity(intnt)
        }

        Picasso.get()
                .load(Config.urlSlider(a.image))
                .error(R.drawable.img_kosong)
                .resize(400, 200)
                .noFade()
                .into(imageView)

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as View)
    }
}