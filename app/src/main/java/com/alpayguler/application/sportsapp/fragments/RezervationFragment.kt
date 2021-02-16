package com.alpayguler.application.sportsapp.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.alpayguler.application.sportsapp.adapters.Reservation_RVAdapter
import com.alpayguler.application.sportsapp.model.ReservationInfo
import com.alpayguler.application.sportsapp.model.UserValues
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.util.Gone
import com.alpayguler.application.sportsapp.util.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_rezervation.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class RezervationFragment : Fragment() {

    private lateinit var date: Date
    private var reservationList = ArrayList<ReservationInfo>()
    private var userValues: UserValues? = null
    private lateinit var datePickerDate : LocalDate

    private var pickedDateTime = Calendar.getInstance()
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

        return inflater.inflate(R.layout.fragment_rezervation, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setClicks()
        rezpage_cl.Gone()
        rezPage_progressbar.Visible()
        userInfoLoad()

        iv_rezervation_profile_photo.setOnClickListener() {
            loadFragment(UserFragment())
        }
    }


    private fun createdefaultResList(x: String): ArrayList<ReservationInfo> {

        val strDate = x
        var res: ReservationInfo ?= null

        var reservationList1 = ArrayList<ReservationInfo>()
        var formatter = DateTimeFormatter.ofPattern("HH:mm")
        var now = LocalTime.now()
        var nowDate = LocalDate.now()


        var t1 = ReservationInfo(
            "09:00 - 10:30",
            reservationDate = strDate,
            reservationId = "${strDate} - 09:00 - 10:30")

        var t2 = ReservationInfo(
            "10:30 - 12:00",
            reservationDate = strDate,
            reservationId = "${strDate} - 10:30 - 12:00")

        var t3 = ReservationInfo(
            "12:00 - 13:30",
            reservationDate = strDate,
            reservationId = "${strDate} - 12:00 - 13:30")

        var t4 = ReservationInfo(
            "13:30 - 15:00",
            reservationDate = strDate,
            reservationId = "${strDate} - 13:30 - 15:00")

        var t5 = ReservationInfo(
            "15:00 - 16:30",
            reservationDate = strDate,
            reservationId = "${strDate} - 15:00 - 16:30")

        var t6 = ReservationInfo(
            "16:30 - 18:00",
            reservationDate = strDate,
            reservationId = "${strDate} - 16:30 - 18:00")

        var t7 = ReservationInfo(
            "18:00 - 19:30",
            reservationDate = strDate,
            reservationId = "${strDate} - 18:00 - 19:30")

        var t8 = ReservationInfo(
            "19:30 - 21:00",
            reservationDate = strDate,
            reservationId = "${strDate} - 19:30 - 21:00")

        var t9 = ReservationInfo(
            "21:00 - 22:30",
            reservationDate = strDate,
            reservationId = "${strDate} - 21:00 - 22:30")


        when {

            now.isBefore(LocalTime.parse("09:00", formatter))  || !nowDate.isEqual(datePickerDate)->  {
                reservationList1.add(t1)
                reservationList1.add(t2)
                reservationList1.add(t3)
                reservationList1.add(t4)
                reservationList1.add(t5)
                reservationList1.add(t6)
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }


            now.isBefore(LocalTime.parse("10:30",formatter)) -> {
                reservationList1.add(t2)
                reservationList1.add(t3)
                reservationList1.add(t4)
                reservationList1.add(t5)
                reservationList1.add(t6)
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("12:00",formatter))-> {
                reservationList1.add(t3)
                reservationList1.add(t4)
                reservationList1.add(t5)
                reservationList1.add(t6)
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("13:30",formatter))-> {
                reservationList1.add(t4)
                reservationList1.add(t5)
                reservationList1.add(t6)
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("15:00",formatter)) -> {
                reservationList1.add(t5)
                reservationList1.add(t6)
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("16:30",formatter)) -> {
                reservationList1.add(t6)
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("18:00",formatter))-> {
                reservationList1.add(t7)
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("19:30",formatter)) -> {
                reservationList1.add(t8)
                reservationList1.add(t9)
            }

            now.isBefore(LocalTime.parse("21:00",formatter)) -> {
                reservationList1.add(t9)
            }

        }


        for (i in 0 until reservationList1.size) {
            val reference = FirebaseDatabase.getInstance().reference
            val query = reference.child("reservations").child(strDate)
                .child(reservationList1[i].reservationHour)

            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    res = snapshot.getValue(ReservationInfo::class.java)
                    if  (res?.reservationCurrent != null){
                        var changeRes = ReservationInfo(
                            res!!.reservationHour,
                            res!!.reservationCurrent,
                            res!!.reservationQuota,
                            res!!.reservationDate,
                            res!!.reservationId
                        )

                        reservationList1[i] = changeRes
                    }
                    query.setValue(reservationList1[i])
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
        return reservationList1
    }


    private fun setClicks() {
        bt_select_date.setOnClickListener {
            pickDateTime()
        }
    }

    override fun onResume() {
        super.onResume()

        doSomethingWith(pickedDateTime)

    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        var datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day)

                doSomethingWith(pickedDateTime)
            },
            startYear,
            startMonth,
            startDay
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()


    }

    private fun takenewList(strDate: String) {
        var strDate = strDate
        reservationList = createdefaultResList(strDate)
    }

    private fun doSomethingWith(pickedDateTime: Calendar) {
        date = pickedDateTime.time

        var dFormatCal = SimpleDateFormat("dd/MM/yyyy")
        var dFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var datePickerDateStr = dFormatCal.format(pickedDateTime.time)
        datePickerDate = LocalDate.parse(datePickerDateStr, dFormatDate)

        val dateFormat: DateFormat = SimpleDateFormat("dd  MMMM yyyy EEEE")
        val dateFormat2: DateFormat = SimpleDateFormat("ddMMyyyy")
        val strDate: String = dateFormat.format(date)
        val strDate2: String = dateFormat2.format(date)
        editTextDate.setText(strDate)


        takenewList(strDate2)
        reservation_rv.layoutManager = LinearLayoutManager(activity)
        reservation_rv.adapter =
            Reservation_RVAdapter(requireContext(), reservationList, strDate2) {
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
            iv_rezervation_profile_photo.load(photoUrl)
        }
        rezPage_progressbar.Gone()
        rezpage_cl.Visible()
    }
}