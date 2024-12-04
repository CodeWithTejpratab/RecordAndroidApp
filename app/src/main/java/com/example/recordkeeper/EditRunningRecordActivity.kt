package com.example.recordkeeper

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.recordkeeper.databinding.ActivityEditRunningRecordBinding
import com.example.recordkeeper.roomDataBase.RecordDao
import com.example.recordkeeper.roomDataBase.RecordLogDatabase
import com.example.recordkeeper.roomDataBase.RecordType
import com.example.recordkeeper.roomDataBase.getCurrentFormattedDate
import kotlinx.coroutines.launch

class EditRunningRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRunningRecordBinding
    private lateinit var recordDao: RecordDao
    private lateinit var distance: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditRunningRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        distance = intent.getStringExtra("Distance") ?: "Running"

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setActionBar()

        recordDao = RecordLogDatabase.getDatabase(this).recordDao()

        binding.saveButton.setOnClickListener {
            setupSaveButton(distance)
        }
    }

    private fun setupSaveButton(distance: String) {
        val recordType = when (distance) {
            "5km" -> RecordType.FiveKm
            "10km" -> RecordType.TenKm
            "Half-Marathon" -> RecordType.HalfMarathon
            "Marathon" -> RecordType.Marathon
            else -> null
        }

        recordType?.let {
            lifecycleScope.launch {
                recordDao.updateRecord(
                    it,
                    binding.timeTextField.text.toString(),
                    getCurrentFormattedDate()
                )
            }
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$distance record"
    }
}
