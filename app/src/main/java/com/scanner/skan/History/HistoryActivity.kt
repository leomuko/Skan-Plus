package com.scanner.skan.History

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.scanner.skan.Helpers.ViewPagerAdapter
import com.scanner.skan.MainActivity
import com.scanner.skan.R
import com.scanner.skan.data.ScanViewModel
import com.google.android.material.tabs.TabLayout

class HistoryActivity : AppCompatActivity() {

    var createFragment : CreateFragment? = null
    var scanFragment : ScanFragment? = null
    var viewPager : ViewPager? = null
    var tabLayout : TabLayout? = null
    var viewPagerAdapter : ViewPagerAdapter? = null
    private lateinit var mScanViewModel : ScanViewModel


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

        mScanViewModel = ViewModelProvider(this).get(ScanViewModel::class.java)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else if(item.itemId == R.id.menu_delete){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Yes"){_, _ ->
            mScanViewModel.deleteAll()
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Warning")
        builder.setMessage("Are you sure you want to delete All Data?")
        builder.create().show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }



}