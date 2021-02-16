package com.alpayguler.application.sportsapp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.adapters.Homepage_RVAdapter
import com.alpayguler.application.sportsapp.model.TrainingVideos
import com.alpayguler.application.sportsapp.model.UserValues
import com.alpayguler.application.sportsapp.util.Gone
import com.alpayguler.application.sportsapp.util.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_homepage.*
import kotlin.random.Random


class HomepageFragment() : Fragment() {

    private var listOfTrainingVideos = ArrayList<TrainingVideos>()
    private var listOfrandomTrVideos  = ArrayList<TrainingVideos>()
    private lateinit var circularProgressXML: View
    private var userValues: UserValues? = null
    private var trainingVideos : TrainingVideos? = null
    private var trNames = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


    private fun circularProgressBar() {


        val reference = FirebaseDatabase.getInstance().reference
        val queryCurrentUserVal = reference.child("gymCurrentUser").child("value")
        var gymCapacity = 30
        var gymCurrentUser = 5

        queryCurrentUserVal.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var curValue = snapshot.getValue(Int::class.java)!!
                gymCurrentUser = curValue
                tv_current_user_ratio.text = curValue.toString() + "/" + gymCapacity.toString()
                toProgressBar()
            }

            fun toProgressBar() {
                circularProgressXML = v_homepage_CircularProgressBar
                circularProgressXML = view!!.findViewById(R.id.v_homepage_CircularProgressBar)

                v_homepage_CircularProgressBar.apply {
                    progress = gymCurrentUser.toFloat()
                    setProgressWithAnimation(gymCurrentUser.toFloat(), 3000)
                    progressMax = gymCapacity.toFloat()
                    progressBarColorStart = Color.parseColor("#84AC28")
                    progressBarColorEnd = Color.parseColor("#84AC28")
                    progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorDirection =
                        CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    progressBarWidth = 24f // in DP
                    backgroundProgressBarWidth = 25f // in DP
                    roundBorder = true
                    startAngle = 0f
                    progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homepage_cl.Gone()
        homePage_progressbar.Visible()

        setUI()
        setClicks()
        userInfoLoad()
        circularProgressBar()



    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }


    private fun openUserPage() {
        loadFragment(UserFragment())
    }


    private fun setClicks() {
        iv_homepage_photo.setOnClickListener {
            openUserPage()
        }
    }


    private fun setUI() {
        initializeRv()
    }








    private fun takenewList(){


        trNames.add("onKol")
        trNames.add("Gogus")
        trNames.add("Sırt")
        trNames.add("arkaKol")
        trNames.add("Bacak")
        trNames.add("Karın")
        trNames.add("Omuz")

        for (video in trNames) {


            val reference = FirebaseDatabase.getInstance().reference
            val query = reference.child("videos").child(video)

            query.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (singleSnapshot in snapshot!!.children) {
                        trainingVideos = singleSnapshot.getValue(TrainingVideos::class.java)

                        createnewList(
                            trainingVideos!!.trLevel,
                            trainingVideos!!.trName,
                            trainingVideos!!.trPeriod,
                            trainingVideos!!.trTime,
                            trainingVideos!!.trVideoId
                        )

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
    private fun createnewList(
        level: String,
        name: String,
        period: String,
        time: String,
        videoId: String
        ){

        var x = true


        var newVideo = TrainingVideos(
            level,
            name,
            period,
            time,
            videoId,
            )

            for (i in listOfTrainingVideos){
                if(i.trName == name){
                    x = false
                }
            }

            if(x)
            {
                listOfTrainingVideos.add(newVideo)


                    val random = Random
                    val r =random.nextInt(0, 5)
                    if (r == 2 ) {
                        if (listOfrandomTrVideos.size < 6){
                        listOfrandomTrVideos.add(newVideo)
                        }
                        if (listOfrandomTrVideos.size>6){
                        }
                    }

            }

    }

    private fun initializeRv() {

        takenewList()


        recyclerview_homepage.layoutManager = LinearLayoutManager(activity)
        recyclerview_homepage.adapter = Homepage_RVAdapter(requireContext(), listOfrandomTrVideos)
        { trainingVideos ->
            val videospgFr: VideospageFragment = VideospageFragment(trainingVideos, 0)
            loadFragment(videospgFr)
        }
    }

    private fun userInfoLoad() {
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    minippLoad(userValues?.userPhotoUrl)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun minippLoad(photoUrl: String?) {

        if (photoUrl != "") {
            iv_homepage_photo.load(photoUrl)
        }
        homePage_progressbar.Gone()
        homepage_cl.Visible()
    }


}