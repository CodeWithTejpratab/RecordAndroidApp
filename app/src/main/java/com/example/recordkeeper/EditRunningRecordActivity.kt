package com.example.recordkeeper

import android.os.Bundle
import android.widget.Toast
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

    private val binding: ActivityEditRunningRecordBinding by lazy { ActivityEditRunningRecordBinding.inflate(layoutInflater) }
    private val recordDao: RecordDao by lazy { RecordLogDatabase.getDatabase(this).recordDao() }
    private val distance: String by lazy { intent.getStringExtra("Distance") ?: "Running" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setActionBar()

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
            Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$distance record"
    }
}
