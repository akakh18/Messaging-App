package ge.akakhishvili.messangerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.service.UserProfileWithId
import ge.akakhishvili.messangerapp.view.ChatActivity
import ge.akakhishvili.messangerapp.view.UserProfile
import ge.akakhishvili.messangerapp.view.UserSearchActivity

class UserSearchAdapter(var profiles: List<UserProfileWithId>, var activity: UserSearchActivity): RecyclerView.Adapter<UserSearchItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchItemViewHolder {
        return UserSearchItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_search_item, parent,false)
        )
    }

    override fun onBindViewHolder(holder: UserSearchItemViewHolder, position: Int) {
        holder.bind(profiles[position], activity)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}

class UserSearchItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    fun bind(profile: UserProfileWithId, activity: UserSearchActivity) {
        itemView.findViewById<TextView>(R.id.user_search_item_nickname).setText(profile.username)
        itemView.findViewById<TextView>(R.id.user_search_item_career).setText(profile.career)
        itemView.setOnClickListener{
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra("receiverUserId", profile.userId)
            intent.putExtra("receiverCareer", profile.career)
            intent.putExtra("receiverUsername", profile.username)
            activity.startActivity(intent)
        }
    }

}