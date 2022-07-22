package ge.akakhishvili.messangerapp.view.`interface`

import ge.akakhishvili.messangerapp.service.UserKeyWithLastMessageAndNickname

interface IMessageListView {
    fun updateChats(userMessageData: List<UserKeyWithLastMessageAndNickname>)
}