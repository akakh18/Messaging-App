package ge.akakhishvili.messangerapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.adapter.ChatAdapter
import ge.akakhishvili.messangerapp.service.ChatService
import ge.akakhishvili.messangerapp.service.GeneralChatService

class MessageListFragment : Fragment() {

    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<Object>
    private lateinit var chatService: GeneralChatService


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
        chatAdapter = ChatAdapter(chatList)

        chatsRecyclerView = requireActivity().findViewById(R.id.messages_recycler_view)
        chatsRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chatsRecyclerView.adapter = chatAdapter

        chatService = GeneralChatService()
    }

}