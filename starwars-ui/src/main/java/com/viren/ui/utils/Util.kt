package com.viren.ui.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.viren.starwars_ui.R


/**
 * Class function example
 */
fun longToast(
        context: Context?,
        msg: String
) {
    context?.let {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

fun shortToast(
        context: Context?,
        msg: String
) {
    context?.let {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

fun showError(view: View, errorMessage: String, handleErrors: () -> Unit) {
    Snackbar.make(view, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction(view.context.resources.getString(R.string.retry)) { handleErrors() }.show()
}


fun Boolean.toVisibility() = if (this) View.VISIBLE else View.GONE


fun EditText.dismissKeyboard() {
    val imm: InputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}