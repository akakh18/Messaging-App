package ge.akakhishvili.messangerapp.service

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class GeneralChatService {
    fun fetchChatsForUser(currentUserId: String) {
        var messagesRef = Firebase.database.getReference("messages")
        messagesRef.get().addOnSuccessListener {
            var data = it
            var messages = it.value
            println(messages)
        }
    }
}