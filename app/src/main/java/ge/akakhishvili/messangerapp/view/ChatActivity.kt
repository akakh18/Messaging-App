package ge.akakhishvili.messangerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.service.ChatService

class ChatActivity : AppCompatActivity() {

    private var auth = Firebase.auth

    private lateinit var messageInputText: EditText
    private lateinit var sendMessageButton: ImageView
    private lateinit var activeUserId: String
    private lateinit var receiverUserId: String
    private lateinit var receiverUsername: String
    private lateinit var receiverCareer: String

    private lateinit var chatService: ChatService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        activeUserId = auth.currentUser!!.uid
        receiverUserId = intent.getStringExtra("receiverUserId").toString()
        receiverUsername = intent.getStringExtra("receiverUsername").toString()
        receiverCareer = intent.getStringExtra("receiverCareer").toString()

        chatService = ChatService()

        initViews()
        addListeners()
    }

    private fun addListeners() {
        sendMessageButton.setOnClickListener{
            var messageText = messageInputText.text.toString()
            chatService.sendMessage(activeUserId, receiverUserId, messageText)
        }
    }

    private fun initViews() {
        messageInputText = findViewById(R.id.chat_page_message_input_text)
        sendMessageButton = findViewById(R.id.chat_page_send_message_button)

        findViewById<TextView>(R.id.chat_page_reicever_name).setText(receiverUsername)
        findViewById<TextView>(R.id.chat_page_receiver_career).setText(receiverCareer)

    }
}