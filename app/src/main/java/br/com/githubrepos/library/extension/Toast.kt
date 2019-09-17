package br.com.githubrepos.library.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Creates and shows a [Toast] with the given [text]
 *
 * @param duration Toast duration, defaults to [Toast.LENGTH_SHORT]
 */
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, text, duration).apply { show() }
}

/**
 * Creates and shows a [Toast] with text from a resource
 *
 * @param resId Resource id of the string resource to use
 * @param duration Toast duration, defaults to [Toast.LENGTH_SHORT]
 */
fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, resId, duration).apply { show() }
}
