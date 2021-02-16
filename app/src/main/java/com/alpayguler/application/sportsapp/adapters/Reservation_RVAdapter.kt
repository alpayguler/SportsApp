package com.alpayguler.application.sportsapp.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.model.ReservationInfo
import com.alpayguler.application.sportsapp.util.Gone
import com.alpayguler.application.sportsapp.util.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Reservation_RVAdapter(
    private val mContext: Context,
    private var reservationList: ArrayList<ReservationInfo>,
    private val takingdate: String,
    private val urlListener: (reservationList: ReservationInfo) -> Unit
) :
    RecyclerView.Adapter<Reservation_RVAdapter.CardViewHolderOfDesignObjects>() {


    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var reservationCardView: CardView
        var reservationHour: TextView
        var strDate2: String

        var reservationProgressBar: ProgressBar
        var reservationRatio: TextView
        var reservationIcon: ImageView
        var reservationCancelIcon: ImageView


        init {
            reservationCardView = view.findViewById(R.id.cv_reservation_cardview)
            reservationHour = view.findViewById(R.id.tv_Reservation_hours)
            strDate2 = takingdate
            reservationProgressBar = view.findViewById(R.id.pb_Reservation_progressBar)
            reservationRatio = view.findViewById(R.id.tv_Reservation_ratio)
            reservationIcon = view.findViewById(R.id.iv_Reservation_reserve)
            reservationCancelIcon = view.findViewById((R.id.iv_Reservation_canceled))

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolderOfDesignObjects {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_reservation, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        var auth = FirebaseAuth.getInstance()
        val reservation = reservationList[position]
        var newReservation: ReservationInfo? = null
        var res: ReservationInfo? = null
        val strDate2 = takingdate
        var referenceF = FirebaseDatabase.getInstance().reference

        var queryF = referenceF.child("users").child(auth.currentUser!!.uid).child("UserResId")
        queryF.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                var checkid = singleSnapshot.getValue(String::class.java)
                if (checkid == reservationList[position].reservationId){

                    holder.reservationIcon.Gone()
                    holder.reservationCancelIcon.Visible()
                    break
                } else{
                    holder.reservationIcon.Visible()
                    holder.reservationCancelIcon.Gone()
                    }
                }
                var queryQ = referenceF.child("reservations").child(strDate2)
                    .child(reservationList[position].reservationHour)
                queryQ.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        res = snapshot.getValue(ReservationInfo::class.java)
                        if (res?.reservationCurrent != 0) {

                            holder.reservationProgressBar.progress = res!!.reservationCurrent
                            holder.reservationProgressBar.max = res!!.reservationQuota
                            holder.reservationRatio.text =
                                "${res!!.reservationCurrent}/${res!!.reservationQuota}"
                        }
                        else{
                            holder.reservationProgressBar.progress = reservationList[position].reservationCurrent
                            holder.reservationProgressBar.max = reservationList[position].reservationQuota
                            holder.reservationRatio.text =
                                "${reservationList[position].reservationCurrent}/${reservationList[position].reservationQuota}"
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        holder.reservationHour.text = reservation.reservationHour




        holder.reservationIcon.setOnClickListener {
            urlListener.invoke(reservation)

            val dialog = Dialog(mContext)
            dialog.setContentView(R.layout.custom_dialog_reservation)

            val btSave = dialog.findViewById(R.id.bt_Dialog_save) as Button
            val btCancel = dialog.findViewById(R.id.bt_Dialog_cancel) as Button
            val tvText = dialog.findViewById(R.id.tv_Dialog_text) as TextView

            tvText.text = "${reservation.reservationHour} saatleri arasına rezervasyon yapmak istiyor musunuz?"

            btSave.setOnClickListener {
                val reference = FirebaseDatabase.getInstance().reference
                val query = reference.child("reservations").child(strDate2)
                    .child(reservationList[position].reservationHour)
                var x: Boolean = true
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        newReservation = snapshot.getValue(ReservationInfo::class.java)

                        val newRes = createnewRes(
                            newReservation!!.reservationHour,
                            newReservation!!.reservationCurrent,
                            newReservation!!.reservationQuota,
                            newReservation!!.reservationDate,
                            newReservation!!.reservationId
                        )
                        if (newRes.reservationCurrent > newRes.reservationQuota) {
                            Toast.makeText(mContext, "Kota Doldu!", Toast.LENGTH_SHORT).show()
                        }

                        else {

                            var query2 = reference.child("users").child(auth.currentUser!!.uid).child("UserResId")
                            query2.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (singleSnapshot in snapshot!!.children) {
                                        var checkid = singleSnapshot.getValue(String::class.java)
                                        if (checkid == newRes.reservationId ){
                                            x = false
                                            Toast.makeText(
                                                mContext,
                                                "Aynı saat aralığına rezervasyon yapılamaz!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        else if(checkid?.substring(0, 8) == newRes.reservationDate){
                                            x = false
                                            Toast.makeText(
                                                mContext,
                                                "Aynı tarih için yalnızca 1 rezervasyon yapılabilir!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }


                                    }
                                    if (x){
                                        query2.child(newRes.reservationId).
                                        setValue(newRes.reservationId)
                                        query.setValue(newRes)
                                        Toast.makeText(
                                            mContext,
                                            "${reservation.reservationHour} saatleri arasına rezervasyon yapılmıştır.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        holder.reservationRatio.setText("${(newReservation!!.reservationCurrent)+1}/${(newReservation!!.reservationQuota)}")
                                        holder.reservationIcon.Gone()
                                        holder.reservationCancelIcon.Visible()
                                        holder.reservationProgressBar.progress = (newReservation!!.reservationCurrent)+1
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                dialog.dismiss()
            }
            btCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }


        holder.reservationCancelIcon.setOnClickListener {
            urlListener.invoke(reservation)

            val dialog = Dialog(mContext)
            dialog.setContentView(R.layout.custom_dialog_reservation)

            var btSave = dialog.findViewById(R.id.bt_Dialog_save) as Button
            btSave.text = "Evet"
            var btCancel = dialog.findViewById(R.id.bt_Dialog_cancel) as Button
            btCancel.text = "Hayır"
            val tvText = dialog.findViewById(R.id.tv_Dialog_text) as TextView

            tvText.text = "${reservation.reservationHour} saatleri arasındaki rezervasyon kaydını silmek istiyor musunuz?"


            btSave.setOnClickListener {
                val reference = FirebaseDatabase.getInstance().reference
                val query = reference.child("reservations").child(strDate2)
                    .child(reservationList[position].reservationHour)

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        newReservation = snapshot.getValue(ReservationInfo::class.java)

                        val newRes = cancelRes(
                            newReservation!!.reservationHour,
                            newReservation!!.reservationCurrent,
                            newReservation!!.reservationQuota,
                            newReservation!!.reservationDate,
                            newReservation!!.reservationId
                        )


                            var query2 = reference.child("users").child(auth.currentUser!!.uid).child("UserResId")
                            query2.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (singleSnapshot in snapshot!!.children) {
                                        var checkid = singleSnapshot.getValue(String::class.java)

                                        if (checkid == newRes.reservationId) {
                                            query2.child(newRes.reservationId).removeValue()
                                            query.setValue(newRes)
                                            Toast.makeText(
                                                mContext,
                                                "${reservation.reservationHour} saatleri arasındaki rezervasyon iptal edilmiştir.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            holder.reservationRatio.text =
                                                "${(newReservation!!.reservationCurrent)-1}/${(newReservation!!.reservationQuota)}"
                                            holder.reservationCancelIcon.Gone()
                                            holder.reservationIcon.Visible()
                                            holder.reservationProgressBar.progress =
                                                (newReservation!!.reservationCurrent)-1
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })

                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                dialog.dismiss()
            }
            btCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()


        }
    }


    private fun createnewRes(hour: String, resCurrent: Int, resQuota: Int, date: String, id: String): ReservationInfo {

        val newRes = ReservationInfo(
            hour,
            resCurrent + 1,
            resQuota,
            date,
            id
        )
        return newRes
    }

    private fun cancelRes(hour: String, resCurrent: Int, resQuota: Int, date: String, id: String): ReservationInfo {
        val newRes = ReservationInfo(
            hour,
            resCurrent - 1,
            resQuota,
            date,
            id
        )
        return newRes
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }


}






