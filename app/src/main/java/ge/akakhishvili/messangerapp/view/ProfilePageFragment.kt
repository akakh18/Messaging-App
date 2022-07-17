package ge.akakhishvili.messangerapp.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.akakhishvili.messangerapp.R

class ProfilePageFragment(val activity: MainPageActivity) : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val utils = Utils()
    private lateinit var nicknameEditText: EditText
    private lateinit var careerEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nicknameEditText = activity.findViewById(R.id.profile_fragment_nickname)
        careerEditText = activity.findViewById(R.id.profile_fragment_career)

        auth = Firebase.auth
        var profilesReferences = Firebase.database.getReference(SignUpActivity.PROFILES)

        profilesReferences.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            var currentUser = it.value as HashMap<String, String>
            var name = currentUser.get("username")
            var career = currentUser.get("career")
            nicknameEditText.setText(name)
            careerEditText.setText(career)


        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Can't fetch user data", Toast.LENGTH_SHORT).show()
        }

    }




}