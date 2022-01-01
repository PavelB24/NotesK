package ru.barinov.notes.ui.dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast

import androidx.fragment.app.DialogFragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ru.barinov.R

import ru.barinov.databinding.ReminderDialogFragmentLayoutBinding
import ru.barinov.notes.domain.ReminderWorker
import ru.barinov.notes.domain.ReminderWorker.Companion.NOTIFICATION_WORK
import ru.barinov.notes.ui.application
import ru.ifr0z.timepickercompact.TimePickerCompact
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderDialogFragment(): DialogFragment() {
    private lateinit var binding: ReminderDialogFragmentLayoutBinding
    private lateinit var datePicker: DatePicker
    private lateinit var timePickerCompact: TimePickerCompact
    private lateinit var negativeButton: Button
    private lateinit var positiveButton: Button


//todo Дописать результат для сброса ИД в роутере
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReminderDialogFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setListeners() {
        positiveButton.setOnClickListener {
            val customCalendar = Calendar.getInstance()
            customCalendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, timePickerCompact.hour, timePickerCompact.minute)
            val customTime = customCalendar.timeInMillis
            val currentTime = currentTimeMillis()
            if (customTime > currentTime) {
                val data = Data.Builder().putInt(ReminderWorker.NOTIFICATION_ID, 0).build()
                val delay = customTime - currentTime
                scheduleNotification(delay, data)}
            Toast.makeText(requireContext(), getString(R.string.on_reminder_activate_text), Toast.LENGTH_LONG).show()

            requireContext().application().router.resetId()

            dismiss()
        }
        negativeButton.setOnClickListener { dismiss() }
    }

    private fun initViews() {
        datePicker= binding.datePicker
        timePickerCompact= binding.timePicker
        negativeButton = binding.reminderDialogNegativeButton
        positiveButton = binding.reminderDialogPositiveButton
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(ReminderWorker::class.java).setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data).build()
        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
    }

}
