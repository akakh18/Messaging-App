package ge.akakhishvili.messangerapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R


class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpButton: Button
    private lateinit var nicknameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var careerEditText: EditText
    private lateinit var auth: FirebaseAuth
    private var utils: Utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth
        initViews()
        initSignUpListeners()
    }

    private fun initViews() {
        signUpButton = findViewById<Button>(R.id.sign_up_page_sign_up_button)
        nicknameEditText = findViewById<EditText>(R.id.sign_up_nickname_edit_text)
        passwordEditText = findViewById<EditText>(R.id.sign_up_password_edit_text)
        careerEditText = findViewById<EditText>(R.id.sign_up_career_edit_text)
    }

    private fun initSignUpListeners() {
        signUpButton.setOnClickListener {
            val nickname = nicknameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val career = careerEditText.text.toString()
            validateAndLogin(nickname, password, career)
        }
    }

    private fun validateAndLogin(nickname: String, password: String, career: String) {
        if (nickname.length == 0) {
            shortToast("Nickname can't be empty")
            return
        }
        if (password.length == 0) {
            shortToast("Password can't be empty")
            return
        }
        if (career.length == 0) {
            shortToast("What I do - can't be empty")
            return
        }
        val nicknameEmailFormat = utils.nicknameToEmailFormat(nickname)
        val hashedPassword = utils.hashedPassword(password)
        auth.createUserWithEmailAndPassword(nicknameEmailFormat, hashedPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    shortToast("registered")
                    mapCareer(nickname, career)
                    openHomePage()
                } else {
                    if (task.exception!!.message!! == USERNAME_ALREADY_IN_USE) {
                        shortToast("Username already exists!")
                    } else {
                        shortToast(task.exception!!.message!!)
                    }
                }
            }

    }

    private fun openHomePage() {
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }

    private fun mapCareer(nickname: String, career: String) {
        val database = Firebase.database
        val profileReference = database.getReference(PROFILES)
        profileReference.child(nickname).setValue(UserProfile(nickname, career))
    }

    private fun shortToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val USERNAME_ALREADY_IN_USE: String =
            "The email address is already in use by another account."
        const val PROFILES = "profiles"
    }
}

data class UserProfile(
    val username: String? = null,
    val career: String? = null,
    val picture: String? = null
)
