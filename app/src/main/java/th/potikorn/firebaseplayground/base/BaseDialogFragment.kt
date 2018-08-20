package th.potikorn.firebaseplayground.base

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

abstract class BaseDialogFragment : DialogFragment() {

    @LayoutRes
    protected abstract fun layoutToInflate(): Int

    protected abstract fun startView()
    protected abstract fun stopView()
    protected abstract fun initData(argument: Bundle?)
    protected abstract fun setupView()
    protected abstract fun saveInstanceState(outState: Bundle)
    protected abstract fun restoreView(savedInstanceState: Bundle?)

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.apply {
            restoreView(savedInstanceState)
        } ?: kotlin.run { initData(arguments) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layoutToInflate(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceState(outState)
    }
}