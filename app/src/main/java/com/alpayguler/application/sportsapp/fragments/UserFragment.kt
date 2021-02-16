package com.alpayguler.application.sportsapp.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import coil.load
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.activities.LoginActivity
import com.alpayguler.application.sportsapp.fragments.toolbarmenupage.ConnectionFragment
import com.alpayguler.application.sportsapp.fragments.toolbarmenupage.EditProfileFragment
import com.alpayguler.application.sportsapp.fragments.toolbarmenupage.SettingsFragment
import com.alpayguler.application.sportsapp.model.UserValues
import com.alpayguler.application.sportsapp.util.Gone
import com.alpayguler.application.sportsapp.util.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_user.*
import java.util.*


class UserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userPageTextView: TextView
    var selectedPicture: Uri? = null
    var storaged = FirebaseStorage.getInstance()
    private lateinit var db: FirebaseDatabase
    private var gymCapacity: Int = 0
    private var gymCurrentUser: Int = 0
    private lateinit var circularProgressXML: View
    private var userValues: UserValues? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


        auth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("CDA", "onBackPressed Called")
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_cl.Gone()
        userPage_progressbar.Visible()
        (activity as AppCompatActivity?)!!.setSupportActionBar(editprofile_toolbar)
        setClicks()
        circularProgressBar()
        userInfoLoad()


    }

    private fun userInfoLoad(){

        userPageTextView = requireView().findViewById(R.id.tv_userPage_username)
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    setUser(
                        userValues?.userName?.capitalize(Locale.getDefault()),
                        userValues?.userPhotoUrl
                    )


                }
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })

    }



    private fun signOut() {
        auth.signOut()
        val intent = Intent(getActivity(), LoginActivity::class.java)
        activity?.startActivity(intent)
        val manager: FragmentManager = requireActivity().supportFragmentManager
        val trans: FragmentTransaction = manager.beginTransaction()
        trans.remove(this)
        trans.commit()
    }

    private fun setClicks() {
        iv_profile_photo3.setOnClickListener() {
            openGallery()
        }
    }

    private fun openGallery() {
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent =
                Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data

            try {
                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {
                        val source =
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                selectedPicture!!
                            )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        iv_profile_photo3.setImageBitmap(bitmap)
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPicture
                        )
                        iv_profile_photo3.setImageBitmap(bitmap)
                    }

                    val uuid = UUID.randomUUID()
                    val ppname = "$uuid.jpg"

                    val reference = storaged.reference
                    val ppreference = reference.child("profilephotos").child(ppname)



                    ppreference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->

                        val uploadPPReferance =
                            FirebaseStorage.getInstance().reference.child("profilephotos")
                                .child(ppname)
                        uploadPPReferance.downloadUrl.addOnSuccessListener { uri ->

                            val downloadUrl = uri.toString()

                            db.reference.child("users")
                                .child(auth.currentUser?.uid ?: "").child("userPhotoUrl")
                                .setValue(downloadUrl)


                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setUser(name: String?, photoUrl: String?) {
        userPageTextView.text = name ?: ""
        if (photoUrl != "") {
            iv_profile_photo3.load(photoUrl)
        }
        userPage_progressbar.Gone()
        user_cl.Visible()

    }

    private fun circularProgressBar() {

        gymCapacity = 34
        gymCurrentUser = 12

        circularProgressXML = v_userpage_CircularProgressBar_1
        circularProgressXML = requireView().findViewById(R.id.v_userpage_CircularProgressBar_1)

        v_userpage_CircularProgressBar_1.apply {
            progress = gymCurrentUser.toFloat()
            setProgressWithAnimation(85f, 1900)
            progressMax = 150f
            progressBarColorStart = Color.parseColor("#84AC28")
            progressBarColorEnd = Color.parseColor("#84AC28")
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 7f // in DP
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        v_userpage_CircularProgressBar_2.apply {
            progress = gymCurrentUser.toFloat()
            setProgressWithAnimation(183.toFloat(), 1900)
            progressMax = 200f
            progressBarColorStart = Color.parseColor("#84AC28")
            progressBarColorEnd = Color.parseColor("#84AC28")
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 7f // in DP
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        v_userpage_CircularProgressBar_3.apply {
            progress = gymCurrentUser.toFloat()
            setProgressWithAnimation(19.4f, 1900)
            progressMax = 25f
            progressBarColorStart = Color.parseColor("#84AC28")
            progressBarColorEnd = Color.parseColor("#84AC28")
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 7f // in DP
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.userpage_top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_editprofile -> {
                loadFragment(EditProfileFragment())
            }
            R.id.menu_connection -> {
                loadFragment(ConnectionFragment())
            }
            R.id.menu_settings -> {
                loadFragment(SettingsFragment())
            }
            R.id.menu_signout -> {
                signOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}



