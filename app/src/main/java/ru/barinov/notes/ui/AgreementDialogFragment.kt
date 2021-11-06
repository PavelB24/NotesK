package ru.barinov.notes.ui
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.barinov.R

class AgreementDialogFragment: DialogFragment() {
    var AGREEMENT_KEY = "OK"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        return builder.setTitle(R.string.clear_data_base_dialog_title_text)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(R.string.clear_data_base_dialog_message_text)
            .setPositiveButton(R.string.clear_data_base_dialog_positive_text, DialogInterface.OnClickListener { dialog, which ->
               val result= Bundle()
               result.putBoolean(AGREEMENT_KEY, true)
                parentFragmentManager.setFragmentResult(AgreementDialogFragment::class.simpleName!!, result)
            }).setNegativeButton(R.string.clear_data_base_dialog_negative_text, null).create()
    }
}