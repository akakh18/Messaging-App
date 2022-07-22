package ge.akakhishvili.messangerapp.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.view.`interface`.IMessageListView
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GeneralChatService(val view: IMessageListView) {
    fun fetchChatsForUser(currentUserId: String) {
        var messagesRef = Firebase.database.getReference("messages")
        messagesRef.get().addOnSuccessListener {
            var messages = it.value as HashMap<String, Object>
            var userChats = getUserChats(currentUserId, messages)
            var userChatsWithNicknames = arrayListOf<UserKeyWithLastMessageAndNickname>()
            var profilesRef = Firebase.database.getReference("profiles")
            profilesRef.get().addOnSuccessListener {
                var profilesData = it.value as HashMap<String, HashMap<String, String>>
                for(user in userChats){
                    var nextUserData = profilesData[user.secondUserKey]
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
                view.updateChats(userChatsWithNicknames)

            }
        }
    }

    private fun getUserChats(currentUserId: String, messages: HashMap<String, Object>): ArrayList<UserKeyWithLastMessage>{
        var userChats = arrayListOf<UserKeyWithLastMessage>()
        for((k, v) in messages){
            var tokens = k.split("_____")
            var firstKey = tokens[0]
            var secondKey = tokens[1]
            var userKeyWithLastMessage: UserKeyWithLastMessage
            if(firstKey.equals(currentUserId)){
                userKeyWithLastMessage = makeUserKeyWithLastMessage(secondKey, v)
                userChats.add(userKeyWithLastMessage)
            }else if(secondKey.equals(currentUserId)){
                userKeyWithLastMessage = makeUserKeyWithLastMessage(firstKey, v)
                userChats.add(userKeyWithLastMessage)
            }
        }
        return userChats
    }

    private fun makeUserKeyWithLastMessage(key: String, v: Object): UserKeyWithLastMessage {
        var messageMap = v as HashMap<String, HashMap<String, String>>
        var messagesList = messageMap.values.toList()

        var mappedMessages = arrayListOf<Message>()
        for(m in messagesList) {
            var message = Message(m["message"]!!,
                m["messageTime"] as Long,
                m["receiverUserId"],
                m["senderUserId"])
            mappedMessages.add(message)
        }

        mappedMessages.sortByDescending{
            it.messageTime
        }
        if(mappedMessages.size > 0){
            return UserKeyWithLastMessage(key, mappedMessages[0].message!!, mappedMessages[0].messageTime!!)
        }
        return UserKeyWithLastMessage(key, "", 0L)
    }

    fun addChatsListener(currentUserId: String) {
        var messagesRef = Firebase.database.getReference("messages")
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fetchChatsForUser(currentUserId)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

data class UserKeyWithLastMessageAndNickname(val secondUserKey: String,
                                             val secondUserNickname: String,
                                             val lastMessage: String,
                                             val messageTime: Long,
                                             val secondUserCareer: String)

data class UserKeyWithLastMessage(val secondUserKey: String,
                                  val lastMessage: String,
                                  val messageTime: Long, )