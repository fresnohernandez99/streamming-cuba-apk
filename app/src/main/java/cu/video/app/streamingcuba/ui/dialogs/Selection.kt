package cu.video.app.streamingcuba.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import cu.video.app.streamingcuba.R

class Selection constructor(
    context: Context?,
    private val title: String,
    val listItems: Array<String>,
    cancelable: Boolean = false,
    val dialogSelectionClickListener: DialogSelectionClickListener
) {

    private var builder: AlertDialog.Builder = AlertDialog.Builder(context)
    private var dialog: Dialog

    init {
        builder.setTitle(title)
        builder.setSingleChoiceItems(listItems, -1
        ) { _, which ->
            dialogSelectionClickListener.dialogSelectionClicked(which, null)
            dismiss()
        }

        dialog = builder.create()
        dialog.setCancelable(cancelable)
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}