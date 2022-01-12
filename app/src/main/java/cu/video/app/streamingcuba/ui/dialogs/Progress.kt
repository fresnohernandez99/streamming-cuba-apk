package cu.video.app.streamingcuba.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import cu.video.app.streamingcuba.R

class Progress constructor(
    context: Context?,
    private val titleRes: String,
    cancelable: Boolean = false
) {

    private var view: View? = null
    private var builder: AlertDialog.Builder
    private var dialog: Dialog

    init {
        view = LayoutInflater.from(context).inflate(R.layout.progress, null)
        view?.findViewById<TextView>(R.id.text)?.text = titleRes
        builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCancelable(cancelable)
    }

    fun setProgressMessage(@StringRes titleRes: Int) {
        view?.findViewById<TextView>(R.id.text)?.setText(titleRes)
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun setProgressDialogVisibility(isVisible: Boolean) {

    }
}