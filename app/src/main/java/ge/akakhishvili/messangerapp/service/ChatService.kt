package ge.akakhishvili.messangerapp.service

import android.os.Build
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.LocalDateTime.*

class ChatService {

    private var database = Firebase.database

    fun sendMessage(activeUserId: String, receiverUserId: String, messageText: String) {
        var chatKey = makeChatKey(activeUserId, receiverUserId)
        var chatRef = database.getReference("messages")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chatRef.child(chatKey).push().setValue(
                Message(messageText,
                        now(),
                        receiverUserId,
                        activeUserId
                    )
            )
        }

    }

    fun makeChatKey(firstKey: String, secondKey: String): String{
        if(firstKey.compareTo(secondKey) < 0){
            return firstKey + "_____" + secondKey
        }
        return secondKey + "_____" + firstKey
    }

}

data class Message(val message: String? = null,
                   val messageTime: LocalDateTime? = null,
                   val receiverUserId: String? = null,
                   val senderUserId: String? = null)