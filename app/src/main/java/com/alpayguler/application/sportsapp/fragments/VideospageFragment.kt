package com.alpayguler.application.sportsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alpayguler.application.sportsapp.model.TrainingVideos
import com.alpayguler.application.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.inside_fragment_videospage.*


class VideospageFragment(val data: TrainingVideos, val x: Int) : Fragment() {

    private var theVideoHasCome =data
    private var thelastfragmentid = x

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.inside_fragment_videospage,
            container,
            false
        )


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_Videopage_back_button.setOnClickListener {
                if (thelastfragmentid == 0)
                {
                    loadFragment(HomepageFragment())
                }
                else{
                    loadFragment(VideosFragment())
                }
        }




        val youTubePlayerView: YouTubePlayerView = view.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        tv_video_name.text = theVideoHasCome.trName
        tv_Videospage_training_level_buttom.text = theVideoHasCome.trLevel
        tv_Videospage_training_period_buttom.text = theVideoHasCome.trPeriod
        tv_Videospage_training_time_buttom.text = theVideoHasCome.trTime

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = theVideoHasCome.trVideoId
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

}