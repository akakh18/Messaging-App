package ge.akakhishvili.messangerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.view.UserProfile

class UserSearchAdapter(var profiles: List<UserProfile>): RecyclerView.Adapter<UserSearchItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchItemViewHolder {
        return UserSearchItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_search_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: UserSearchItemViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}

class UserSearchItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    fun bind(profile: UserProfile) {
        itemView.findViewById<TextView>(R.id.user_search_item_nickname).setText(profile.username)
        itemView.findViewById<TextView>(R.id.user_search_item_career).setText(profile.career)

    }

}