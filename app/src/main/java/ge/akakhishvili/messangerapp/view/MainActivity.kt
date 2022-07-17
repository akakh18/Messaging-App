package ge.akakhishvili.messangerapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R


class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var nicknameEditText: EditText
    private lateinit var passwordEditText: EditText

    private var utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (auth.currentUser != null) {
            openMainPage()
        }
        initViews()
        initButtonActions()

    }

    private fun initViews() {
        nicknameEditText = findViewById(R.id.nickname_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        signInButton = findViewById(R.id.sign_in_button)
        signUpButton = findViewById(R.id.sign_up_button)
    }

    private fun login() {
        auth.signInWithEmailAndPassword(
            utils.nicknameToEmailFormat(nicknameEditText.text.toString()),
            utils.hashedPassword(passwordEditText.text.toString())
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                openMainPage()
            } else {
                Toast.makeText(
                    this,
                    LOGIN_ERROR_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initButtonActions() {
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener {
            login()
        }
    }

    private fun openMainPage(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val LOGIN_ERROR_MESSAGE: String = "Username or password is incorrect"
    }
}
