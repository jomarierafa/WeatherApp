package com.jvrcoding.weatherapp.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

fun Context.toast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Double.kelvinToCelsius(): Double {
    val total = this - 273.15
    return (total * 100.0).roundToInt() / 100.0
}

fun Long.convertUtcToLocaleTime(): String {
    // Convert to ZonedDateTime
    val utcDateTime = Instant.ofEpochSecond(this).atZone(ZoneOffset.UTC)

    // Convert to localtime
    val phTimeZone = ZoneId.of(ZoneId.systemDefault().toString())
    val phDateTime = utcDateTime.withZoneSameInstant(phTimeZone)

    // Format the date time
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return phDateTime.format(formatter)
}

fun Long.epochToString(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date(this * 1000))
}

fun String.getCountryName(): String {
    val locale = Locale("", this)
    return locale.displayCountry ?: ""
}

fun bytesToHexString(bytes: ByteArray): String {
    val hexArray = "0123456789abcdef".toCharArray()
    val hexChars = CharArray(bytes.size * 2)
    for (i in bytes.indices) {
        val v = bytes[i].toInt() and 0xff
        hexChars[i * 2] = hexArray[v.ushr(4)]
        hexChars[i * 2 + 1] = hexArray[v and 0x0f]
    }
    return String(hexChars)
}

fun String.hashPassword(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(this.toByteArray(Charset.defaultCharset()))
    return bytesToHexString(hash)
}

fun isAutomaticTimeEnabled(context: Context): Boolean {
    return Settings.Global.getInt(context.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
}

fun buildAlertDialog(context: Context, message: String, buttonText: String, onPositiveButton: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(buttonText) { _, _ ->
            onPositiveButton()
        }
    val alert = builder.create()
    alert.show()
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun isAllPermissionGranted(
    context: Context,
    permissions: ArrayList<String>
): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}