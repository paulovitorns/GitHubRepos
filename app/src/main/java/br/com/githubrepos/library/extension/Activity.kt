package br.com.githubrepos.library.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun Activity.hideKeyboard() {
    val focusedView = currentFocus
    if (focusedView is EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }
}
