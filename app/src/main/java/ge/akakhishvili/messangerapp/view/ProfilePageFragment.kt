package ge.akakhishvili.messangerapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import ge.akakhishvili.messangerapp.R


class ProfilePageFragment(private val activity: MainPageActivity) : Fragment() {

    private var auth = Firebase.auth
    private var utils = Utils()

    private var firebaseStorage = FirebaseStorage.getInstance()
    private var storageReference = firebaseStorage.reference

    private lateinit var nicknameEditText: EditText
    private lateinit var careerEditText: EditText

    private lateinit var signOutButton: Button
    private lateinit var updateButton: Button

    private lateinit var changeImageView: ImageView

    private var changedPhotoFilepath: Uri? = null



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

        val profilesReferences = Firebase.database.getReference(DatabaseConstants.PROFILES)

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
        updateButton = activity.findViewById(R.id.profile_fragment_update_button)

        changeImageView = activity.findViewById(R.id.profile_fragment_change_photo)
    }

    private fun addListeners() {
        signOutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        updateButton.setOnClickListener {
            updateData(nicknameEditText.text.toString(), careerEditText.text.toString())
        }

        changeImageView.setOnClickListener{
            selectImage()
        }

    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE){
            changedPhotoFilepath = data?.data
            changeImageView.setImageURI(data?.data) // handle chosen image
        }
    }

    private fun uploadImage(){
        if(changedPhotoFilepath != null){
            var ref = storageReference.child("images/profile_image_" + auth.currentUser!!.uid)

            ref.putFile(changedPhotoFilepath!!).addOnCompleteListener{
                if(it.isSuccessful){
                    println("success")
                }else{
                    println("fail")
                }
            }
        }
    }

    private fun updateData(nickname: String, career: String) {
        val user = Firebase.auth.currentUser
        user!!.updateEmail(utils.nicknameToEmailFormat(nickname))
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(activity, "Sign in again to make updates", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        val profilesReference = Firebase.database.getReference(DatabaseConstants.PROFILES)
        profilesReference.child(auth.currentUser!!.uid)
            .setValue(UserProfile(username = nickname, career = career))

        uploadImage()

    }

    companion object{
        const val PICK_IMAGE_REQUEST_CODE = 1010
    }
}