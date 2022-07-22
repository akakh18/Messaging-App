package ge.akakhishvili.messangerapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.adapter.ChatAdapter
import ge.akakhishvili.messangerapp.service.GeneralChatService
import ge.akakhishvili.messangerapp.service.UserKeyWithLastMessageAndNickname
import ge.akakhishvili.messangerapp.view.`interface`.IMessageListView

class MessageListFragment : Fragment(), IMessageListView {

    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<UserKeyWithLastMessageAndNickname>
    private lateinit var chatService: GeneralChatService
    private lateinit var fetchingBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        fetchUserChats()
    }

    private fun fetchUserChats() {
        var currentUserId = Firebase.auth.currentUser!!.uid
        chatService.fetchChatsForUser(currentUserId)
    }

    private fun initViews() {
        chatList = arrayListOf()
        chatAdapter = ChatAdapter(chatList, requireActivity())

        chatsRecyclerView = requireActivity().findViewById(R.id.messages_recycler_view)
        chatsRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chatsRecyclerView.adapter = chatAdapter

        chatService = GeneralChatService(this)
        fetchingBar = requireActivity().findViewById(R.id.no_message_loading_progress_bar)
    }

    override fun updateChats(userMessageData: List<UserKeyWithLastMessageAndNickname>) {
        if(userMessageData.size != 0){
            chatsRecyclerView.visibility = View.VISIBLE
            fetchingBar.visibility = View.GONE
            chatList.clear()
            chatList.addAll(userMessageData)
            chatAdapter.notifyDataSetChanged()
        }else {
            chatsRecyclerView.visibility = View.GONE
            fetchingBar.visibility = View.VISIBLE
        }
    }
}