package com.example.dianastore.helper

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by Tisto on 1/27/2021.
 */
object LayoutManager {
    fun vertical(context: Context): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        return layoutManager
    }

    fun horizontal(context: Context): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        return layoutManager
    }
}