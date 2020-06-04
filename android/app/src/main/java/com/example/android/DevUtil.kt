package com.example.android

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun errorMessage(ctx : Context, msg : String)
{
    val toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}
