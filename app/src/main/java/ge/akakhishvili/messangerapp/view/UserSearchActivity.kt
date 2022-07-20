package ge.akakhishvili.messangerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.adapter.UserSearchAdapter
import ge.akakhishvili.messangerapp.service.UserProfileWithId
import ge.akakhishvili.messangerapp.service.UserSearchService
import ge.akakhishvili.messangerapp.view.`interface`.IUserSearchView

class UserSearchActivity : AppCompatActivity(), IUserSearchView {

    private lateinit var userSearchEditText: EditText
    private lateinit var usersRecyclerView: RecyclerView

    private lateinit var userProfilesList: ArrayList<UserProfileWithId>
    private lateinit var userSearchAdapter: UserSearchAdapter

    private lateinit var userSearchService: UserSearchService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        initViews()
        userSearchService = UserSearchService(this)
        userProfilesList = arrayListOf()
        userSearchAdapter = UserSearchAdapter(userProfilesList, this)
        usersRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        usersRecyclerView.adapter = userSearchAdapter

        addListeners()
    }

    private fun addListeners() {
        userSearchEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.length >= 3){
                    fetchUsers(s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })
    }

    private fun fetchUsers(nicknameSearchString: String) {
        userSearchService.findUsersLike(nicknameSearchString)
    }

    private fun initViews() {
        userSearchEditText = findViewById(R.id.user_search_search_edit_text)
        usersRecyclerView = findViewById(R.id.user_search_users_recycler_view)
    }

    override fun loadUsers(profiles: List<UserProfileWithId>) {
        userProfilesList.clear()
        userProfilesList.addAll(profiles)
        userSearchAdapter.notifyDataSetChanged()
    }
}