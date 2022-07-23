package ge.akakhishvili.messangerapp.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.service.UserKeyWithLastMessage
import ge.akakhishvili.messangerapp.service.UserKeyWithLastMessageAndNickname
import ge.akakhishvili.messangerapp.utils.Utils
import ge.akakhishvili.messangerapp.view.ChatActivity
import java.io.File
import kotlin.collections.ArrayList

class ChatAdapter(var chatList: ArrayList<UserKeyWithLastMessageAndNickname>, var activity: Activity): RecyclerView.Adapter<ChatItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(chatList[position], activity)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}

class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(chat: UserKeyWithLastMessageAndNickname, activity: Activity){
        itemView.findViewById<TextView>(R.id.message_name).setText(chat.secondUserNickname)
        itemView.findViewById<TextView>(R.id.message_text).setText(chat.lastMessage)
        itemView.findViewById<TextView>(R.id.message_sent_time).setText(Utils().getMessageFormForMills(chat.messageTime))
        if(chat.hasProfileImage) {
            var ref = FirebaseStorage.getInstance().reference.child("images/profile_image_" + chat.secondUserKey)
            var localfile = File.createTempFile("tempimage", "jpg")
            ref.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                itemView.findViewById<ImageView>(R.id.chat_message_image).setImageBitmap(bitmap)
            }
        }
        itemView.setOnClickListener{
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra("receiverUserId", chat.secondUserKey)
            intent.putExtra("receiverCareer", chat.secondUserCareer)
            intent.putExtra("receiverUsername", chat.secondUserNickname)
            intent.putExtra("hasProfilePicture", chat.hasProfileImage)
            activity.startActivity(intent)
        }
    }
}