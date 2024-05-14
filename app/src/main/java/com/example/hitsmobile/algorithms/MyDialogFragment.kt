package com.example.hitsmobile.algorithms

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.hitsmobile.R

class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Руководство по использованию")
                .setMessage("1) Нажмите на кнопку 'FIRST POINTS' и расставьте 3 исходные точки\n" +
                        "\n2) Нажмите на кнопку 'SECOND POINTS' и расставьте 3  результирующие точки\n" +
                        "\n3) Нажмите на кнопку 'START' и дождитесь применения фильтра")
                .setIcon(R.drawable.affine_transformations)
                .setPositiveButton("ОК, понял") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}