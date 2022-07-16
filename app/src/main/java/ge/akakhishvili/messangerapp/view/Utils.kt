package ge.akakhishvili.messangerapp.view

import java.security.MessageDigest

class Utils {
    public fun hashedPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    public fun nicknameToEmailFormat(nickname: String): String {
        return nickname + "@mail.com"
    }
}