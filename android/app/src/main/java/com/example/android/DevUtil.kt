package com.example.android

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ShareCompat

fun errorMessage(ctx: Context, msg: String)
{
    val toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}


fun share(activity : Activity, link : String) {
    ShareCompat.IntentBuilder.from(activity)
        .setType("text/plain")
        .setChooserTitle("Share URL")
        .setText(link)
        .startChooser();
}