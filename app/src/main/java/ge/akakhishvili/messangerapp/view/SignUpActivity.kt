package ge.akakhishvili.messangerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R
import java.security.MessageDigest

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpButton: Button
    private lateinit var nicknameEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var careerEditText: EditText
    private lateinit var auth: FirebaseAuth

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
        signUpButton.setOnClickListener{
            var nickname = nicknameEditText.text.toString()
            var password = passwordEditText.text.toString()
            var career = careerEditText.text.toString()
            validateAndLogin(nickname, password, career)
        }
    }

    private fun validateAndLogin(nickname: String, password: String, career: String) {
        if(nickname.length == 0){
            shortToast("Nickname can't be empty")
            return
        }
        if(password.length == 0){
            shortToast("Password can't be empty")
            return
        }
        if(career.length == 0){
            shortToast("What I do - can't be empty")
            return
        }
        auth.createUserWithEmailAndPassword(nicknameToEmailFormat(nickname), hashedPassword(password))
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    shortToast("registered")
                } else {
                    if(task.exception!!.message!! == USERNAME_ALREADY_IN_USE){
                        shortToast("Username already exists!")
                    }else{
                        shortToast(task.exception!!.message!!)
                    }
                }
            }

    }


    private fun hashedPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun nicknameToEmailFormat(nickname: String): String {
        return nickname + "@mail.com"
    }



    private fun shortToast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val USERNAME_ALREADY_IN_USE: String = "The email address is already in use by another account."
    }

}