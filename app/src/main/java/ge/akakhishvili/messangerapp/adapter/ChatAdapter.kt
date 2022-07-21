package ge.akakhishvili.messangerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.akakhishvili.messangerapp.R
import kotlin.collections.ArrayList

class ChatAdapter(var chatList: ArrayList<Object>): RecyclerView.Adapter<ChatItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}

class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(chat: Object){

    }
}