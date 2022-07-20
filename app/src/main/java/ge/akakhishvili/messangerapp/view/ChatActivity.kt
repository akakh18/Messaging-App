package ge.akakhishvili.messangerapp.view

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.adapter.MessagesAdapter
import ge.akakhishvili.messangerapp.service.ChatService
import ge.akakhishvili.messangerapp.service.Message

class ChatActivity : AppCompatActivity() {

    private var auth = Firebase.auth

    private lateinit var messageInputText: EditText
    private lateinit var sendMessageButton: ImageView
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var noMessagesView: TextView
    private lateinit var activeUserId: String
    private lateinit var receiverUserId: String
    private lateinit var receiverUsername: String
    private lateinit var receiverCareer: String
    private lateinit var messagesAdapter: MessagesAdapter

    private lateinit var messagesList: ArrayList<Message>

    private lateinit var chatService: ChatService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        activeUserId = auth.currentUser!!.uid
        receiverUserId = intent.getStringExtra("receiverUserId").toString()
        receiverUsername = intent.getStringExtra("receiverUsername").toString()
        receiverCareer = intent.getStringExtra("receiverCareer").toString()

        messagesList = arrayListOf()

        initViews()

        chatService =
            ChatService(messagesAdapter, messagesList, noMessagesView, messagesRecyclerView)
        chatService.getMessagesFor(receiverUserId, activeUserId)

        addListeners()
    }

    private fun addListeners() {
        sendMessageButton.setOnClickListener {
            val messageText = messageInputText.text.toString()
            chatService.sendMessage(activeUserId, receiverUserId, messageText)
            messageInputText.setText("")
        }
    }

    private fun initViews() {
        messageInputText = findViewById(R.id.chat_page_message_input_text)
        sendMessageButton = findViewById(R.id.chat_page_send_message_button)
        noMessagesView = findViewById(R.id.chat_no_messages_textview)
        messagesRecyclerView = findViewById(R.id.chat_page_messages_recycler_view)

        messagesAdapter = MessagesAdapter(messagesList, this)
        messagesRecyclerView.adapter = messagesAdapter
        messagesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        findViewById<TextView>(R.id.chat_page_reicever_name).setText(receiverUsername)
        findViewById<TextView>(R.id.chat_page_receiver_career).setText(receiverCareer)


    }

    fun getActiveUserId(): String {
        return activeUserId
    }
}