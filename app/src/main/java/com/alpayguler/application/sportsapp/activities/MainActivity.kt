package com.alpayguler.application.sportsapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alpayguler.application.R
import com.alpayguler.application.sportsapp.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.rbddevs.splashy.Splashy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        setBottomNavigation()
        loadFragment(HomepageFragment())
        setSplashy()

    }
    private fun setSplashy() { Splashy(this)
            .setLogo(R.drawable.app_logo)
            .setLogoScaleType(ImageView.ScaleType.FIT_XY)
            .setLogoWHinDp(150,150)
            .setTitle("Sports App")
            .setTitleColor("#FFFFFF")
            .setTitleSize(25F)
            .setSubTitle("Her yolculuk tek bir adımla başlar.")
            .setSubTitleSize(15F)
            .setBackgroundColor(R.color.black)
            .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER, 1500)
            .setProgressColor(R.color.colorPrimaryGreen)
            .setFullScreen(true)
            .setClickToHide(true)
            .setDuration(2000)
            .setFullScreen(true)
            .showProgress(true)
            .show()
    }
    private fun setBottomNavigation() {

        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.homepage ->
                { loadFragment(HomepageFragment()) }

                R.id.rezervationpage ->
                { loadFragment(RezervationFragment()) }

                R.id.qrcodepage ->
                { loadFragment(QrcodeFragment()) }

                R.id.videospage ->
                { loadFragment(VideosFragment()) }

                R.id.bt_logOut ->
                { loadFragment(UserFragment()) }

            }

            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
    override fun onBackPressed() {
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }
}