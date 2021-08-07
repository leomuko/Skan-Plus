package com.scanner.skan.Settings

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.scanner.skan.MainActivity
import com.scanner.skan.R
import kotlinx.android.synthetic.main.activity_settings.*


private val SettingsActivity.reviewManager: ReviewManager
    get() {
        val manager = ReviewManagerFactory.create(this)
        return manager
    }

var reviewInfo : ReviewInfo? = null;

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = Html.fromHtml("<font color='#ffffff'>Settings</font>")

        activateReviewInfo()

        ratingCard.setOnClickListener {
            activateFlow()
        }

        shareCard.setOnClickListener {
            shareApp()
        }


    }

    private fun shareApp() {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Share App")
            var sAux = "\nI recommend Skan++ \n\n" + "http://play.google.com/store/apps/details?id=" + "com.scanner.skan"
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(Intent.createChooser(i, "Share Via"))
        } catch (e: Exception) {
            Log.e(">>>", "Error: $e")
        }
    }

    private fun activateFlow() {
        val flow = reviewInfo?.let { reviewManager.launchReviewFlow(this, it) }
        flow?.addOnCompleteListener { _ ->

        }
    }

    private fun activateReviewInfo(){
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener {
            if (it.isSuccessful){
                reviewInfo = it.result
            }else{

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}