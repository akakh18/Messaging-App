package ge.akakhishvili.messangerapp.service

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.view.UserProfile
import ge.akakhishvili.messangerapp.view.`interface`.IUserSearchView


class UserSearchService(var userSearchView: IUserSearchView) {
    private var database = Firebase.database

    fun findUsersLike(nicknameSearchString: String) {

        var searchedProfiles = arrayListOf<UserProfileWithId>()

        var profilesRef = database.getReference("profiles")
        profilesRef.get().addOnSuccessListener {
            var data = it.value as HashMap<String, HashMap<String, String>>
            for((key, userProfile) in data){
                if(userProfile["username"]!!.contains(nicknameSearchString)){
                    var newProfile =
                        UserProfileWithId(userProfile["username"]!!,
                                    userProfile["career"]!!,
                                    userProfile["hasProfilePicture"]!! as Boolean?,
                                    key)
                    searchedProfiles.add(newProfile)
                }
            }
            userSearchView.loadUsers(searchedProfiles)
        }

    }
}

data class UserProfileWithId(val username: String? = null,
                             val career: String? = null,
                             val hasProfilePicture: Boolean? = null,
                             val userId: String? = null)