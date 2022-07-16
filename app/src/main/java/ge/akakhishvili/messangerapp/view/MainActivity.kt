package ge.akakhishvili.messangerapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R


class MainActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login()
        initButtonActions()

    }

    private fun login(){
        auth = Firebase.auth
        if(auth.currentUser == null){
            shortToast("not logined")
        }else{

        }
    }

    private fun shortToast(s: String){
        Toast.makeText(this, "not logined", Toast.LENGTH_SHORT).show()
    }

    private fun initButtonActions(){
        findViewById<Button>(R.id.sign_up_button).setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}