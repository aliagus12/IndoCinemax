package cinemax.indo.aliagus.com.indocinemax.toastfragment

import android.app.Dialog
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.TextView
import cinemax.indo.aliagus.com.indocinemax.MainActivity
import cinemax.indo.aliagus.com.indocinemax.R
import cinemax.indo.aliagus.com.indocinemax.maincontent.MainContentActivity

/**
 * Created by ali on 24/02/18.
 */
class ToastFragment : BottomSheetDialogFragment(), View.OnClickListener {

    var viewRoot: View? = null
    var textViewMessage: TextView? = null

    fun getInstance(context: Context): ToastFragment {
        return ToastFragment()
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        viewRoot = View.inflate(context, R.layout.fragment_toast, null)
        viewRoot?.findViewById<TextView>(R.id.txt_cancel)!!.setOnClickListener(this)
        viewRoot?.findViewById<TextView>(R.id.txt_refresh)!!.setOnClickListener(this)
        textViewMessage = viewRoot?.findViewById(R.id.text_message)
        dialog?.setContentView(viewRoot)
        setLayout()
    }

    private fun setLayout() {
        var container: ConstraintLayout = viewRoot!!.findViewById(R.id.container_toast)
        var layoutParams = (viewRoot?.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        var behavior = layoutParams.behavior!!
        (behavior as BottomSheetBehavior).setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
        container.post({
            val heightCoodinatorLayoutCountainer = container
                    .getHeight()
            behavior.peekHeight = heightCoodinatorLayoutCountainer
        })
    }

    override fun onClick(view: View?) {
        view?.id.let {
            when (it) {
                R.id.txt_cancel -> {
                    dismissAllowingStateLoss()
                    if (activity?.javaClass == MainActivity::class.java) {
                        activity?.onBackPressed()
                    }
                }
                R.id.txt_refresh -> {
                    if (activity?.javaClass == MainActivity::class.java) {
                        (activity as MainActivity).resumeActivity()
                    } else if (activity?.javaClass == MainContentActivity::class.java) {
                        (activity as MainContentActivity).resumeActivity()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (message != "") {
            textViewMessage?.setText(message)
        }
    }

    private lateinit var message: String
    fun setMessage(message: String) {
        this.message = message
    }
}