package com.scanner.skan

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.scanner.skan.Helpers.GridSpacingItemDecoration
import com.scanner.skan.adapters.MenuRecyclerAdapter
import com.scanner.skan.models.MenuModel
import java.util.*

private const val CAMERA_REQUEST_CODE = 101
private var doubleBackToExitPressedOnce = false

private var recyclerView :RecyclerView? = null;
private var adapter : MenuRecyclerAdapter? = null
private lateinit var dashboardList : MutableList<MenuModel>

private lateinit var firebaseAnalytics: FirebaseAnalytics

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dashboardList  = ArrayList<MenuModel>()

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        setUpPermissions()
/*
        findViewById<CardView>(R.id.barcode_create_view).setOnClickListener {
            val intent = Intent(applicationContext, CreateBarcodeActivity::class.java )
            startActivity(intent)
        }

        findViewById<CardView>(R.id.barcode_scan_view).setOnClickListener{
            val intent = Intent(applicationContext, BarCodeActivity::class.java )
            startActivity(intent)
        }

        findViewById<CardView>(R.id.history_view).setOnClickListener{
            val intent = Intent(applicationContext, HistoryActivity::class.java )
            startActivity(intent)
        }

      findViewById<CardView>(R.id.barcode_settings_view).setOnClickListener{

            val intent = Intent(applicationContext, SettingsActivity::class.java )
            startActivity(intent)
        }*/

        /*  findViewById<Button>(R.id.create_barcode).setOnClickListener{
            val intent = Intent(applicationContext, CreateBarcodeActivity::class.java )
            startActivity(intent)
        }*/

        recyclerView = findViewById(R.id.recyclerView)
        adapter = MenuRecyclerAdapter(applicationContext, dashboardList)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView?.setLayoutManager(mLayoutManager)
        recyclerView?.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.setAdapter(adapter)

        populateRecyclerView()


    }

    private fun populateRecyclerView() {
        dashboardList.add(MenuModel(R.drawable.ic_barcode_scanner, "Scan"))
        dashboardList.add(MenuModel(R.drawable.ic_qr_code, "Create"))
        dashboardList.add(MenuModel(R.drawable.ic_history, "History"))
        dashboardList.add(MenuModel(R.drawable.ic_settings, "Settings"))
    }


    /**
     * Converting dp to pixel
     */
    fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }



    private fun setUpPermissions(){
        val permission  = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need a Camera Permission in order to scan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "Camera Permission granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}