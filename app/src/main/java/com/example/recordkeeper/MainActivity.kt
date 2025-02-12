package com.example.recordkeeper

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.lifecycle.lifecycleScope
import com.example.recordkeeper.databinding.ActivityMainBinding
import com.example.recordkeeper.roomDataBase.Record
import com.example.recordkeeper.roomDataBase.RecordDao
import com.example.recordkeeper.roomDataBase.RecordLogDatabase
import com.example.recordkeeper.roomDataBase.RecordType
import com.example.recordkeeper.roomDataBase.getCurrentFormattedDate
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recordDao: RecordDao

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
        setSupportActionBar(binding.toolbar)
        setupBottomBar()
        initializedDataBase()
    }

    private fun initializedDataBase() {
        recordDao = RecordLogDatabase.getDatabase(this).recordDao()

        lifecycleScope.launch {
            try {
                val allRecords = recordDao.getAllRecords()
                if (allRecords.isEmpty()) {
                    RecordType.entries.forEach { type ->
                        recordDao.insertOrUpdateRecord(
                            Record(type = type, time = "00:00", date = getCurrentFormattedDate())
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectOptionsItem(item.itemId)
        return true
    }

    private fun selectOptionsItem(itemId: Int) {
        when (itemId) {
            R.id.reset_running -> {
                showToast(getString(R.string.reset_running_records))
            }

            R.id.reset_cycling -> {
                showToast(getString(R.string.reset_cycling_records))
            }

            R.id.reset_all -> {
                lifecycleScope.launch {
                    RecordType.entries.forEach { type ->
                        recordDao.insertOrUpdateRecord(
                            Record(type = type, time = "00:00", date = getCurrentFormattedDate())
                        )
                    }
                    recreate()
                }
                showToast(getString(R.string.reset_all_records))
            }
        }
    }

    private fun showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

//    This is one way of doing a callback listener

    private val itemSelectedListener = object : NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            selectFragmentOnNavClick(item)
            return true
        }
    }

    private fun selectFragmentOnNavClick(item: MenuItem) {
        selectNavFragmentById(item.itemId)
    }

    private fun selectNavFragmentById(itemId: Int) {
        var frag: Fragment? = null

        when (itemId) {
            R.id.nav_running -> {
                frag = RunningFragment()
            }

            R.id.nav_cycling -> {
                frag = CyclingFragment()
            }
        }

        frag?.let { fragment ->
            launchNavFragment(fragment)
        }
    }

    private fun launchNavFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .replace(R.id.frame_content, frag)
            .commitAllowingStateLoss()
    }

    private fun setupBottomBar() = binding.bottomNav.setOnItemSelectedListener(itemSelectedListener)

//    This is the other way for the callback above
//    private fun setupBottomBar() = binding.bottomNav.setOnItemSelectedListener(object :
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
}
