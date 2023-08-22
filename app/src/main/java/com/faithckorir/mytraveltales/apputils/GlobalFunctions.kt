package com.faithckorir.mytraveltales.apputils

fun isEmailValid(rawEmailAddress: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return rawEmailAddress.matches(emailPattern)
}