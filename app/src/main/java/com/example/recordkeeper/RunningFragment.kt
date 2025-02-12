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
    private lateinit var recordDao: RecordDao
    private val nA: String by lazy { getString(R.string.na) }

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

            with(binding) {
                fiveKmRecord.await()?.let {
                    textView5kmTimeValue.text = it.time
                    textView5kmDate.text = it.date
                } ?: run {
                    textView5kmTimeValue.text = nA
                    textView5kmDate.text = nA
                }

                tenKmRecord.await()?.let {
                    textView10kmTimeValue.text = it.time
                    textView10kmDate.text = it.date
                } ?: run {
                    textView10kmTimeValue.text = nA
                    textView10kmDate.text = nA
                }

                halfMarathonRecord.await()?.let {
                    textViewHalfMarathonTimeValue.text = it.time
                    textViewHalfMarathonDate.text = it.date
                } ?: run {
                    textViewHalfMarathonTimeValue.text = nA
                    textViewHalfMarathonDate.text = nA
                }

                marathonRecord.await()?.let {
                    textViewMarathonTimeValue.text = it.time
                    textViewMarathonDate.text = it.date
                } ?: run {
                    textViewMarathonTimeValue.text = nA
                    textViewMarathonDate.text = nA
                }
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