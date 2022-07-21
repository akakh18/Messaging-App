package ge.akakhishvili.messangerapp.service

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GeneralChatService {
    fun fetchChatsForUser(currentUserId: String) {
        var messagesRef = Firebase.database.getReference("messages")

    }
}