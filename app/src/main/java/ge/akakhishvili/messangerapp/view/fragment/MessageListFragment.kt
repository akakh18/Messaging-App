package ge.akakhishvili.messangerapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var noChatsTextView: TextView


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
        addChatsListener()
        addSearchListener()
    }

    private fun addSearchListener() {
        requireActivity()
            .findViewById<TextView>(R.id.messages_list_fragment_search_text_view)
                .addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {
                        if (s.length >= 0) {
                            chatService.searchUsersByName(Firebase.auth.currentUser!!.uid, s.toString())
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                    }
                })
    }

    private fun addChatsListener() {
        chatService.addChatsListener(Firebase.auth.currentUser!!.uid!!)
    }

    private fun fetchUserChats() {
        var currentUserId = Firebase.auth.currentUser!!.uid
        chatService.fetchChatsForUser(currentUserId, null)
    }

    private fun initViews() {
        chatList = arrayListOf()
        chatAdapter = ChatAdapter(chatList, requireActivity())

        chatsRecyclerView = requireActivity().findViewById(R.id.messages_recycler_view)
        chatsRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chatsRecyclerView.adapter = chatAdapter

        chatService = GeneralChatService(this)
        noChatsTextView = requireActivity().findViewById(R.id.no_message_text_view)
        noChatsTextView.visibility = View.VISIBLE
    }

    override fun updateChats(userMessageData: List<UserKeyWithLastMessageAndNickname>) {
        if(userMessageData.size != 0){
            chatsRecyclerView.visibility = View.VISIBLE
            noChatsTextView.visibility = View.GONE
            chatList.clear()
            chatList.addAll(userMessageData)
            chatAdapter.notifyDataSetChanged()
        }else {
            chatsRecyclerView.visibility = View.GONE
            noChatsTextView.visibility = View.VISIBLE
        }
    }
}