package ge.akakhishvili.messangerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.service.Message
import ge.akakhishvili.messangerapp.utils.Utils
import ge.akakhishvili.messangerapp.view.ChatActivity

class MessagesAdapter(
    private var messages: MutableList<Message>,
    private var activity: ChatActivity
) : RecyclerView.Adapter<ChatMessagesViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessagesViewHolder<*> {
        return when (viewType) {
            SENDER -> {
                val view = LayoutInflater.from(activity)
                    .inflate(R.layout.sent_message_layout, parent, false)
                SentMessagesViewHolder(view)
            }
            RECEIVER -> {
                val view = LayoutInflater.from(activity)
                    .inflate(R.layout.received_message_layout, parent, false)
                ReceivedMessagesViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Illegal viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: ChatMessagesViewHolder<*>, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (activity.getActiveUserId() == messages[position].senderUserId) SENDER else RECEIVER
    }

    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
    }
}

abstract class ChatMessagesViewHolder<MessageEntity>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Message)
}

class SentMessagesViewHolder(itemView: View) : ChatMessagesViewHolder<Message>(itemView) {
    private val utils = Utils()
    var text: TextView = itemView.findViewById(R.id.chat_message_sender_text)
    var time: TextView = itemView.findViewById(R.id.chat_message_sender_time)

    override fun bind(item: Message) {
        text.text = item.message
        time.text = utils.getMessageFormForMills(item.messageTime!!)
    }

}

class ReceivedMessagesViewHolder(itemView: View) : ChatMessagesViewHolder<Message>(itemView) {
    private val utils = Utils()
    var text: TextView = itemView.findViewById(R.id.chat_message_receiver_text)
    var time: TextView = itemView.findViewById(R.id.chat_message_receiver_time)

    override fun bind(item: Message) {
        text.text = item.message
        time.text = utils.getMessageFormForMills(item.messageTime!!)
    }

}
