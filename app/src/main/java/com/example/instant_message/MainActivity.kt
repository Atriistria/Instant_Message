package com.example.instant_message

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.instant_message.data.adapter.MainViewPagerAdapter
import com.example.instant_message.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Instant
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: MainViewPagerAdapter

    //申请通知权限
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        run {
            if(!isGranted){
                Toast.makeText(this, "权限申请失败,您可以在设置中手动开启", Toast.LENGTH_SHORT).show()
                showAppSettings()
            }
        }
    }

    private fun showAppSettings() {
        val intent = Intent().apply {
            action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //申请通知权限
        requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)

        // 设置 Toolbar
        setSupportActionBar(binding.navToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 配置 ViewPager2 和 Adapter
        val viewPager: ViewPager2 = binding.mainViewPager
        pagerAdapter = MainViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter


        val navView: BottomNavigationView = binding.bottomNavigation
        navView.itemBackground = null

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0, true)
                R.id.navigation_contacts -> viewPager.setCurrentItem(1, true)
                R.id.navigation_profile -> viewPager.setCurrentItem(2, true)
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> navView.selectedItemId = R.id.navigation_home
                    1 -> navView.selectedItemId = R.id.navigation_contacts
                    2 -> navView.selectedItemId = R.id.navigation_profile
                }
            }
        })
    }

}