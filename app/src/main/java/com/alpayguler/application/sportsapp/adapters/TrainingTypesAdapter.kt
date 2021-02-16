package com.alpayguler.application.sportsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.model.TrainingVideos

class TrainingTypesAdapter(private val mContext: Context,
                           private val trainingVideosList: List<TrainingVideos>,
                           private val urlListener:(trainingVideos: TrainingVideos)-> Unit) :
    RecyclerView.Adapter<TrainingTypesAdapter.CardViewHolderOfDesignObjects>() {

    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var trainingCardView: CardView
        var trainingLevel: TextView
        var trainingName: TextView
        var trainingPeriod: TextView
        var trainingTime: TextView
        var trainingPhoto: ImageView
        var trainingButton: Button

        init {
            trainingCardView = view.findViewById(R.id.cv_trainingtypes_cardview)
            trainingLevel = view.findViewById(R.id.tv_training_level)
            trainingName = view.findViewById(R.id.tv_training_name)
            trainingPeriod = view.findViewById(R.id.tv_training_period)
            trainingTime = view.findViewById(R.id.tv_training_time)
            trainingPhoto = view.findViewById(R.id.iv_training_photo)
            trainingButton = view.findViewById(R.id.bt_training_watch)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderOfDesignObjects {

        val view = LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_trainintypes, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        val trainingVideos = trainingVideosList[position]

        holder.trainingLevel.text = trainingVideos.trLevel
        holder.trainingName.text = trainingVideos.trName
        holder.trainingPeriod.text = trainingVideos.trPeriod
        holder.trainingTime.text = trainingVideos.trTime
        holder.trainingPhoto.setImageResource(trainingVideos.trPhotoUrl)
        holder.trainingPhoto.setOnClickListener {
            urlListener.invoke(trainingVideosList[position]) }
        holder.trainingButton.setOnClickListener {
            urlListener.invoke(trainingVideosList[position]) }

        }

    override fun getItemCount(): Int {
        return trainingVideosList.size
    }
}

