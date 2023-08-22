package com.faithckorir.mytraveltales.apputils

import android.app.Activity
import android.graphics.Color
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog


object GlobalFunctions{
fun isEmailValid(rawEmailAddress: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return rawEmailAddress.matches(emailPattern)
}

    fun Activity.sweetProgressBar(): SweetAlertDialog {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).apply {
            titleText = "Sending Request"
            setCancelable(false)
            progressHelper.apply {
                barColor = Color.parseColor(
                    "#232692"
                )
                rimColor = Color.parseColor(
                    "#232692"
                )
                contentText = "Please Wait ..."

            }
        }
        pDialog.`dismiss`()
        return pDialog
}
}
fun Activity.toastMessage(message: String?) {
    if (!message.isNullOrBlank())
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}