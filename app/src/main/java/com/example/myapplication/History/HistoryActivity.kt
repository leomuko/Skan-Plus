package com.example.myapplication.History

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.Helpers.ViewPagerAdapter
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout

class HistoryActivity : AppCompatActivity() {

    var createFragment : CreateFragment? = null
    var scanFragment : ScanFragment? = null
    var viewPager : ViewPager? = null
    var tabLayout : TabLayout? = null
    var viewPagerAdapter : ViewPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        viewPager = findViewById<ViewPager>(R.id.viewPager)
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout?.setupWithViewPager(viewPager)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = "History"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        createFragment = CreateFragment()
        scanFragment = ScanFragment()

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter?.addFragment(createFragment, "Create")
        viewPagerAdapter?.addFragment(scanFragment, "Scan")

        viewPager?.adapter = viewPagerAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }



}