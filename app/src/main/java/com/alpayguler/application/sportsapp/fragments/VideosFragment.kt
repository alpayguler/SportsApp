package com.alpayguler.application.sportsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.alpayguler.application.sportsapp.util.Gone
import com.alpayguler.application.sportsapp.adapters.TrainingTypesAdapter
import com.alpayguler.application.sportsapp.model.TrainingVideos
import com.alpayguler.application.sportsapp.model.UserValues
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.util.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_training_types.*
import kotlin.collections.ArrayList


class VideosFragment() : Fragment() {


    private var listOfOnKol = ArrayList<TrainingVideos>()
    private var listOfGogus = ArrayList<TrainingVideos>()
    private var listOfSirt = ArrayList<TrainingVideos>()
    private var listOfArkaKol = ArrayList<TrainingVideos>()
    private var listOfBacak = ArrayList<TrainingVideos>()
    private var listOfKarın = ArrayList<TrainingVideos>()
    private var listOfOmuz = ArrayList<TrainingVideos>()
    private var trNames = ArrayList<String>()


    private var userValues: UserValues? = null
    private var trainingVideos : TrainingVideos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
        return inflater.inflate(R.layout.fragment_training_types, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        videospage_cl.Gone()
        videosPage_progressbar.Visible()
        userInfoLoad()


        setTexts()

    }

    private fun setUI() {
        initializeRv1()
        initializeRv2()
        initializeRv3()
        initializeRv4()
        initializeRv5()
        initializeRv6()
        initializeRv7()
    }

    private fun initializeRv1() {

        takenewList()


        recycleview_training_types_1.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_1.adapter =
            TrainingTypesAdapter(requireContext(), listOfOnKol) {trainingVideos ->


                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
    }
    private fun initializeRv2() {


        takenewList()

        recycleview_training_types_2.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_2.adapter =
            TrainingTypesAdapter(requireContext(), listOfGogus)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
    }
    private fun initializeRv3() {

        takenewList()

        recycleview_training_types_3.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_3.adapter =
            TrainingTypesAdapter(requireContext(), listOfSirt)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
    }
    private fun initializeRv4() {

        takenewList()

        recycleview_training_types_4.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_4.adapter =
            TrainingTypesAdapter(requireContext(), listOfArkaKol)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
    }
    private fun initializeRv5() {

        takenewList()

        recycleview_training_types_5.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_5.adapter =
            TrainingTypesAdapter(requireContext(), listOfBacak)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
    }
    private fun initializeRv6() {

        takenewList()

        recycleview_training_types_6.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_6.adapter =
            TrainingTypesAdapter(requireContext(), listOfKarın)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
    }
    private fun initializeRv7() {

        takenewList()

        recycleview_training_types_7.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_7.adapter =
            TrainingTypesAdapter(requireContext(), listOfOmuz)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos,1)
                loadFragment(videospgFr)
            }
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


            var reference = FirebaseDatabase.getInstance().reference
            var query = reference.child("videos").child(video)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot!!.children) {
                        trainingVideos = singleSnapshot.getValue(TrainingVideos::class.java)

                        createnewList(
                            trainingVideos!!.trLevel,
                            trainingVideos!!.trName,
                            trainingVideos!!.trPeriod,
                            trainingVideos!!.trTime,
                            trainingVideos!!.trVideoId,
                            video
                        )
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun createnewList(level: String, name: String, period: String, time: String, videoId: String, type: String){

        var x = true
        var photo: Int = R.drawable.iv_dumbell_lift


        when(type){
            "onKol" ->photo = R.drawable.iv_dumbell_lift
            "Gogus" -> photo = R.drawable.iv_benc_press
            "Sırt" ->photo = R.drawable.iv_dead_lift
            "arkaKol" ->photo = R.drawable.iv_dumbell_lift
            "Bacak" -> photo = R.drawable.iv_dead_lift
            "Karın" ->  photo = R.drawable.iv_dead_lift
            "Omuz" ->  photo = R.drawable.iv_benc_press
        }

        var newVideo = TrainingVideos(
            level,
            name,
            period,
            time,
            videoId,
            photo
        )

        when (type){
            "onKol" ->{
                for (i in listOfOnKol){
                    if(i.trName == name){
                        x = false
                    }
                }

                if(x)
                {
                    listOfOnKol.add(newVideo)
                }
            }
            "Gogus" -> {
                for (i in listOfGogus){
                    if(i.trName == name){
                        x = false
                    }
                }
                if(x)
                {
                    listOfGogus.add(newVideo)
                }
            }
            "Sırt" ->{
                for (i in listOfSirt){
                    if(i.trName == name){
                        x = false
                    }
                }
                if(x)
                {
                    listOfSirt.add(newVideo)
                }
            }
            "arkaKol" ->{
                for (i in listOfArkaKol){
                    if(i.trName == name){
                        x = false
                    }
                }
                if(x)
                {
                    listOfArkaKol.add(newVideo)
                }
            }
            "Bacak" -> {
                for (i in listOfBacak){
                    if(i.trName == name){
                        x = false
                    }
                }
                if(x)
                {
                    listOfBacak.add(newVideo)
                }
            }
            "Karın" -> {
                for (i in listOfKarın){
                    if(i.trName == name){
                        x = false
                    }
                }
                if(x)
                {
                    listOfKarın.add(newVideo)
                }
            }
            "Omuz" -> {

                for (i in listOfOmuz){
                    if(i.trName == name){
                        x = false
                    }
                }
                if(x)
                {
                    listOfOmuz.add(newVideo)
                }
            }
        }


    }



    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
    private fun userInfoLoad() {
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    minippLoad(userValues?.userPhotoUrl)
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        iv_videos_profile_photo.setOnClickListener {
            loadFragment(UserFragment())
        }

    }
    private fun minippLoad(photoUrl: String?) {
        if (photoUrl != "") {
            iv_videos_profile_photo.load(photoUrl)
        }
        videosPage_progressbar.Gone()
        videospage_cl.Visible()
    }

    private fun setTexts(){
        tv_training_type1.text = "Ön Kol Hareketleri"
        tv_training_type2.text = "Göğüs Hareketleri"
        tv_training_type3.text = "Sırt Hareketleri"
        tv_training_type4.text = "Arka Kol Hareketleri"
        tv_training_type5.text = "Bacak Hareketleri"
        tv_training_type6.text = "Karın Hareketleri"
        tv_training_type7.text = "Omuz Hareketleri"
    }


}