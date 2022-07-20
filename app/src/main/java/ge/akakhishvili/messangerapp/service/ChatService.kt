package ge.akakhishvili.messangerapp.service

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.adapter.MessagesAdapter

class ChatService(
    val messagesAdapter: MessagesAdapter,
    val messagesList: ArrayList<Message>,
    val noMessagesView: TextView,
    val messagesView: RecyclerView
) {

    private var database = Firebase.database

    fun sendMessage(activeUserId: String, receiverUserId: String, messageText: String) {
        val chatKey = makeChatKey(activeUserId, receiverUserId)
        val chatRef = database.getReference("messages")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chatRef.child(chatKey).push().setValue(
                Message(
                    messageText,
                    System.currentTimeMillis(),
                    receiverUserId,
                    activeUserId
                )
            )
        }
    }

    fun getMessagesFor(receiver: String, sender: String) {
        val result = arrayListOf<Message>()

        val searchKey = makeChatKey(receiver, sender)
        messagesList.clear()

        Log.i("searchKey", "$searchKey")

        val chatRef = database.getReference("messages")

        chatRef.get().addOnSuccessListener {
            val data = it.value as HashMap<String, HashMap<String, HashMap<String, String>>>
            for ((key, messagesItem) in data) {
                if (key == searchKey) {
                    for ((_, messageItem) in messagesItem) {
                        val message = Message(
                            messageItem["message"]!!,
                            messageItem["messageTime"]!! as Long?,
                            messageItem["receiverUserId"]!!,
                            messageItem["senderUserId"]!!
                        )
                        result.add(message)
                        messagesList.add(message)
                        messagesAdapter.notifyDataSetChanged()
                    }
                }
            }
            if (messagesList.isNotEmpty()) {
                noMessagesView.visibility = View.GONE
                messagesView.visibility = View.VISIBLE
            }
        }
    }

    private fun makeChatKey(firstKey: String, secondKey: String): String {
        if (firstKey < secondKey) {
            return firstKey + "_____" + secondKey
        }
        return secondKey + "_____" + firstKey
    }


}

data class Message(
    val message: String? = null,
    val messageTime: Long? = null,
    val receiverUserId: String? = null,
    val senderUserId: String? = null
)