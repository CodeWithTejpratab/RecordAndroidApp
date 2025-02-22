package com.example.recordkeeper

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.recordkeeper.databinding.FragmentCyclingBinding
import com.example.recordkeeper.roomDataBase.RecordDao
import com.example.recordkeeper.roomDataBase.RecordLogDatabase
import com.example.recordkeeper.roomDataBase.RecordType
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CyclingFragment : Fragment() {

    private lateinit var binding: FragmentCyclingBinding
    private lateinit var recordDao: RecordDao
    private val nA: String by lazy { getString(R.string.na) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCyclingBinding.inflate(inflater, container, false)
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
            val longestRide = async { recordDao.getRecordByType(RecordType.LongestRide) }
            val biggestClimb = async { recordDao.getRecordByType(RecordType.BiggestClimb) }
            val bestAverageSpeed = async { recordDao.getRecordByType(RecordType.BestAverageSpeed) }

            with(binding) {
                longestRide.await()?.let {
                    textViewLongestRideTimeValue.text = it.time
                    textViewLongestRideDate.text = it.date
                } ?: run {
                    textViewLongestRideTimeValue.text = nA
                    textViewLongestRideDate.text = nA
                }

                biggestClimb.await()?.let {
                    textViewBiggestClimbTimeValue.text = it.time
                    textViewBiggestClimbDate.text = it.date
                } ?: run {
                    textViewBiggestClimbTimeValue.text = nA
                    textViewBiggestClimbDate.text = nA
                }

                bestAverageSpeed.await()?.let {
                    textViewBestAverageSpeedTimeValue.text = it.time
                    textViewBestAverageSpeedDate.text = it.date
                } ?: run {
                    textViewBestAverageSpeedTimeValue.text = nA
                    textViewBestAverageSpeedDate.text = nA
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            containerLongestRide.setOnClickListener { launchCyclingRecordScreen("Longest Ride") }
            containerBiggestClimb.setOnClickListener { launchCyclingRecordScreen("Biggest Climb") }
            containerBestAverageSpeed.setOnClickListener { launchCyclingRecordScreen("Best Average Speed") }
        }
    }

    private fun launchCyclingRecordScreen(distance: String) {
        val intent = Intent(context, EditCyclingRecordActivity::class.java)
        intent.putExtra("Distance", distance)
        startActivity(intent)
    }
}