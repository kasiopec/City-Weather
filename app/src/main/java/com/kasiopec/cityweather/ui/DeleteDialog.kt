package com.kasiopec.cityweather.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder



class DeleteDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete this item?")
            .setMessage("You up to delete the picked city. " +
                    "You will have to add the city again if you want to see the weather")
            .setPositiveButton("Delete"){dialog, which ->
                targetFragment?.onActivityResult(targetRequestCode, 1, activity?.intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog, which ->
                dialog.dismiss()
            }
            .create()
    }


}