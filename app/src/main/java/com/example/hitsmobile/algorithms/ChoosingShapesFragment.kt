package com.example.hitsmobile.algorithms

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.hitsmobile.activity.SplineActivity

class ChoosingShapesFragment : DialogFragment() {
    private val shapes = arrayOf("Треугольник", "Квадрат", "Шестиугольник",
        "Восьмиугольник", "Интерполяционный сплайн", "Сглаживающий сплайн" )
    private var currShape = 4

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите режим")
                .setSingleChoiceItems(shapes, -1
                ) { dialog, item ->
                    Toast.makeText(activity, "Выбран режим :  ${shapes[item]}",
                        Toast.LENGTH_SHORT).show()
                    when(item){
                        0 -> {currShape = 0}
                        1 -> {currShape = 1}
                        2 -> {currShape = 2}
                        3 -> {currShape = 3}
                        4 -> {currShape = 4}
                        5 -> {currShape = 5}
                    }
                }
                .setPositiveButton("OK"
                ) { dialog, id ->
                    SplineActivity.MyFun.checkView(currShape)
                }
                .setNegativeButton("Отмена") {
                        dialog, id ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
