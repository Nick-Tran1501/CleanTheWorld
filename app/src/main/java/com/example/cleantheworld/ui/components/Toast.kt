package com.example.cleantheworld.ui.components

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast


fun showToast(context: Context, message: String, backgroundColor: Int, textColor: Int) {
//    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    val toast = Toast(context)
    val view = TextView(context).apply {
        text = message
        textSize = 16f
        setTextColor(textColor)
        setBackgroundColor(backgroundColor)
        setPadding(20, 20, 20, 20)

    }

    toast.view = view
    toast.duration = Toast.LENGTH_SHORT
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
    toast.show()
}
