package ge.akakhishvili.messangerapp.view.`interface`

import ge.akakhishvili.messangerapp.service.UserProfileWithId
import ge.akakhishvili.messangerapp.view.UserProfile

interface IUserSearchView {
    fun loadUsers(profiles: List<UserProfileWithId>)
}