package ge.akakhishvili.messangerapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R

class ProfilePageFragment(private val activity: MainPageActivity) : Fragment() {

    private var auth = Firebase.auth

    private lateinit var nicknameEditText: EditText
    private lateinit var careerEditText: EditText

    private lateinit var signOutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        addListeners()

        val profilesReferences = Firebase.database.getReference(SignUpActivity.PROFILES)

        profilesReferences.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            val currentUser = it.value as HashMap<String, String>
            val name = currentUser["username"]
            val career = currentUser["career"]
            nicknameEditText.setText(name)
            careerEditText.setText(career)


        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Can't fetch user data", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initViews() {
        nicknameEditText = activity.findViewById(R.id.profile_fragment_nickname)
        careerEditText = activity.findViewById(R.id.profile_fragment_career)
        signOutButton = activity.findViewById(R.id.profile_fragment_sign_out_button)
    }

    private fun addListeners() {
        signOutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }

}