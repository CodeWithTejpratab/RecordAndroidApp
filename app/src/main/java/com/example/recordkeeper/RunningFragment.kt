package com.example.recordkeeper

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.recordkeeper.databinding.FragmentRunningBinding
import com.example.recordkeeper.roomDataBase.RecordDao
import com.example.recordkeeper.roomDataBase.RecordLogDatabase
import com.example.recordkeeper.roomDataBase.RecordType
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RunningFragment : Fragment() {

    private lateinit var binding: FragmentRunningBinding
    private lateinit  var recordDao: RecordDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        recordDao = RecordLogDatabase.getDatabase(requireContext()).recordDao()
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun setupView() {
        lifecycleScope.launch {
            val fiveKmRecord = async { recordDao.getRecordByType(RecordType.FiveKm) }
            val tenKmRecord = async { recordDao.getRecordByType(RecordType.TenKm) }
            val halfMarathonRecord = async { recordDao.getRecordByType(RecordType.HalfMarathon) }
            val marathonRecord = async { recordDao.getRecordByType(RecordType.Marathon) }

            fiveKmRecord.await()?.let {
                binding.textView5kmTimeValue.text = it.time
                binding.textView5kmDate.text = it.date
            } ?: run {
                binding.textView5kmTimeValue.text = "N/A"
                binding.textView5kmDate.text = "N/A"
            }

            tenKmRecord.await()?.let {
                binding.textView10kmTimeValue.text = it.time
                binding.textView10kmDate.text = it.date
            } ?: run {
                binding.textView10kmTimeValue.text = "N/A"
                binding.textView10kmDate.text = "N/A"
            }

            halfMarathonRecord.await()?.let {
                binding.textViewHalfMarathonTimeValue.text = it.time
                binding.textViewHalfMarathonDate.text = it.date
            } ?: run {
                binding.textViewHalfMarathonTimeValue.text = "N/A"
                binding.textViewHalfMarathonDate.text = "N/A"
            }

            marathonRecord.await()?.let {
                binding.textViewMarathonTimeValue.text = it.time
                binding.textViewMarathonDate.text = it.date
            } ?: run {
                binding.textViewMarathonTimeValue.text = "N/A"
                binding.textViewMarathonDate.text = "N/A"
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            container5km.setOnClickListener { launchRunningRecordScreen("5km") }
            container10km.setOnClickListener { launchRunningRecordScreen("10km") }
            containerHalfMarathon.setOnClickListener { launchRunningRecordScreen("Half-Marathon") }
            containerMarathon.setOnClickListener { launchRunningRecordScreen("Marathon") }
        }
    }

    private fun launchRunningRecordScreen(distance: String) {
        val intent = Intent(context, EditRunningRecordActivity::class.java)
        intent.putExtra("Distance", distance)
        startActivity(intent)
    }

}