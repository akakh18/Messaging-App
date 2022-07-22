package ge.akakhishvili.messangerapp.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.view.`interface`.IMessageListView

class GeneralChatService(val view: IMessageListView) {
    fun fetchChatsForUser(currentUserId: String, searchKeyword: String?) {
        val messagesRef = Firebase.database.getReference("messages")
        messagesRef.get().addOnSuccessListener {
            val messages = it.value as HashMap<String, Object>
            val userChats = getUserChats(currentUserId, messages)
            val userChatsWithNicknames = arrayListOf<UserKeyWithLastMessageAndNickname>()
            val profilesRef = Firebase.database.getReference("profiles")
            profilesRef.get().addOnSuccessListener {
                val profilesData = it.value as HashMap<String, HashMap<String, String>>
                for (user in userChats) {
                    val nextUserData = profilesData[user.secondUserKey]
                    userChatsWithNicknames.add(
                        UserKeyWithLastMessageAndNickname(
                            user.secondUserKey,
                            nextUserData?.get("username")!!,
                            user.lastMessage,
                            user.messageTime,
                            nextUserData["career"]!!
                        )
                    )
                }
                var searchedList = userChatsWithNicknames
                if (searchKeyword != null) {
                    searchedList = userChatsWithNicknames.filter { u ->
                        u.secondUserNickname.lowercase().contains(searchKeyword.lowercase())
                    } as ArrayList<UserKeyWithLastMessageAndNickname>
                }

                searchedList.sortBy { -1 * it.messageTime }
                view.updateChats(searchedList)

            }
        }
    }

    private fun getUserChats(
        currentUserId: String,
        messages: HashMap<String, Object>
    ): ArrayList<UserKeyWithLastMessage> {
        val userChats = arrayListOf<UserKeyWithLastMessage>()
        for ((k, v) in messages) {
            val tokens = k.split("_____")
            val firstKey = tokens[0]
            val secondKey = tokens[1]
            var userKeyWithLastMessage: UserKeyWithLastMessage
            if (firstKey.equals(currentUserId)) {
                userKeyWithLastMessage = makeUserKeyWithLastMessage(secondKey, v)
                userChats.add(userKeyWithLastMessage)
            } else if (secondKey.equals(currentUserId)) {
                userKeyWithLastMessage = makeUserKeyWithLastMessage(firstKey, v)
                userChats.add(userKeyWithLastMessage)
            }
        }
        return userChats
    }

    private fun makeUserKeyWithLastMessage(key: String, v: Object): UserKeyWithLastMessage {
        val messageMap = v as HashMap<String, HashMap<String, String>>
        val messagesList = messageMap.values.toList()

        val mappedMessages = arrayListOf<Message>()
        for (m in messagesList) {
            val message = Message(
                m["message"]!!,
                m["messageTime"] as Long,
                m["receiverUserId"],
                m["senderUserId"]
            )
            mappedMessages.add(message)
        }

        mappedMessages.sortByDescending {
            it.messageTime
        }
        if (mappedMessages.size > 0) {
            return UserKeyWithLastMessage(
                key,
                mappedMessages[0].message!!,
                mappedMessages[0].messageTime!!
            )
        }
        return UserKeyWithLastMessage(key, "", 0L)
    }

    fun addChatsListener(currentUserId: String) {
        val messagesRef = Firebase.database.getReference("messages")
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fetchChatsForUser(currentUserId, null)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun searchUsersByName(currentUserId: String, searchString: String) {
        fetchChatsForUser(currentUserId, searchString)
    }
}

data class UserKeyWithLastMessageAndNickname(
    val secondUserKey: String,
    val secondUserNickname: String,
    val lastMessage: String,
    val messageTime: Long,
    val secondUserCareer: String
)

data class UserKeyWithLastMessage(
    val secondUserKey: String,
    val lastMessage: String,
    val messageTime: Long,
)