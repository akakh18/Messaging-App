package ge.akakhishvili.messangerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ge.akakhishvili.messangerapp.R
import ge.akakhishvili.messangerapp.adapter.UserSearchAdapter

class UserSearchActivity : AppCompatActivity() {

    private lateinit var userSearchEditText: EditText
    private lateinit var usersRecyclerView: RecyclerView

    private lateinit var userSearchAdapter: UserSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        initViews()
        addListeners()
    }

    private fun addListeners() {
        userSearchEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.length >= 3){
                    Toast.makeText(this@UserSearchActivity, "time to fetch users", Toast.LENGTH_SHORT).show()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })
    }

    private fun initViews() {
        userSearchEditText = findViewById(R.id.user_search_search_edit_text)
        usersRecyclerView = findViewById(R.id.user_search_users_recycler_view)

        userSearchAdapter = UserSearchAdapter()
        usersRecyclerView.adapter = userSearchAdapter
    }
}