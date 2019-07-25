package com.example.adriancloud.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

fun show(editText: EditText, activity: Activity, milis: Int) {
    Thread(Runnable {
        try {
            Thread.sleep(milis.toLong())
            activity.runOnUiThread { showKeyboard(editText, activity) }

        } catch (e: InterruptedException) {
            Toast.makeText(activity, "error launcheando teclado", Toast.LENGTH_SHORT).show()
        }
    }).start()
}

private fun showKeyboard(mEtSearch: EditText, context: Context) {
    mEtSearch.requestFocus()
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

