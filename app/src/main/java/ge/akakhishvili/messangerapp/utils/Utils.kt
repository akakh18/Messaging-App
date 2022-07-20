package ge.akakhishvili.messangerapp.utils

import android.annotation.SuppressLint
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun hashedPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    fun nicknameToEmailFormat(nickname: String): String {
        return nickname + "@mail.com"
    }

    fun getNicknameFromEmailFormat(email: String): String {
        return email.split('@')[0]
    }

    @SuppressLint("SimpleDateFormat")
    fun getMessageFormForMills(mills: Long): String {
        val current = System.currentTimeMillis()

        val differenceInSeconds = (current - mills) / 1000

        if (differenceInSeconds < 60) {
            return "$differenceInSeconds sec"
        } else if (differenceInSeconds < 60 * 60) {
            val result = differenceInSeconds / 60
            return "$result min"
        } else if (differenceInSeconds < 25 * 60 * 60) {
            val result = differenceInSeconds / 60 / 60
            return "$result hour"
        }

        return SimpleDateFormat("dd/MM").format(Date(mills))
    }

}