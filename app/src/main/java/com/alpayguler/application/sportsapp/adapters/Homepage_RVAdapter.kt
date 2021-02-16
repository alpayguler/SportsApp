package com.alpayguler.application.sportsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.model.TrainingVideos

class Homepage_RVAdapter(private val mContext: Context,
                         private val trainingList: List<TrainingVideos>,
                         private val urlListener:(trainingVideos: TrainingVideos)-> Unit) :
    RecyclerView.Adapter<Homepage_RVAdapter.CardViewHolderOfDesignObjects>() {

    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var homepageCardView: CardView
        var tv_training_type: TextView
        var tv_training_repetition: TextView
        var iv_training_playIcon: ImageView

        init {
            homepageCardView = view.findViewById(R.id.cv_reservation_cardview)
            tv_training_type = view.findViewById(R.id.tv_training_type)
            tv_training_repetition = view.findViewById(R.id.tv_training_repetition)
            iv_training_playIcon = view.findViewById(R.id.iv_training_playIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderOfDesignObjects {

        val view = LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_homepage, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        val training = trainingList[position]

        holder.tv_training_type.text = training.trName

        holder.tv_training_repetition.text = training.trPeriod

        holder.homepageCardView.setOnClickListener {
            urlListener.invoke(trainingList[position])

        }
    }

    override fun getItemCount(): Int {

        return trainingList.size
    }
}