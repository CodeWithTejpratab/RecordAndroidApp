package com.example.recordkeeper

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.recordkeeper.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setActionBar()
        onRunningClicked()
        setupBottomBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.reset_running -> {
            Toast.makeText(this, "Reset Running Records", Toast.LENGTH_SHORT).show()
            true
        }

        R.id.reset_cycling -> {
            Toast.makeText(this, "Reset Cycling Records", Toast.LENGTH_SHORT).show()
            true
        }

        R.id.reset_all -> {
            Toast.makeText(this, "Reset All Records", Toast.LENGTH_SHORT).show()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    /*
    This is one way of doing a callback listener
    */
    private val navigationItemSelectedListener = object : NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_running -> {
                    onRunningClicked()
                    return true
                }

                R.id.nav_cycling -> {
                    onCyclingClicked()
                    return true
                }

                else -> return false

            }
        }
    }

    private fun setupBottomBar() {
        binding.bottomNav.setOnItemSelectedListener(navigationItemSelectedListener)
    }

    /*
    This is the other way for the callback above
    */
//    private fun setupBottomBar() {
//        binding.bottomNav.setOnItemSelectedListener(object :
//            NavigationBarView.OnItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                when (item.itemId) {
//                    R.id.nav_running -> {
//                        onRunningClicked()
//                        return true
//                    }
//
//                    R.id.nav_cycling -> {
//                        onCyclingClicked()
//                        return true
//                    }
//
//                    else -> return false
//
//                }
//            }
//        })
//    }

    private fun onRunningClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, RunningFragment())
        }
    }

    private fun onCyclingClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, CyclingFragment())
        }
    }

    private fun setActionBar() = setSupportActionBar(binding.toolbar)

}
