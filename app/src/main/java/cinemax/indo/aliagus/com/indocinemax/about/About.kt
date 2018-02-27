package cinemax.indo.aliagus.com.indocinemax.about

import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import cinemax.indo.aliagus.com.indocinemax.BuildConfig
import cinemax.indo.aliagus.com.indocinemax.R

/**
 * Created by ali on 26/02/18.
 */
class About : BottomSheetDialogFragment() {

    lateinit var viewRoot: View
    lateinit var textViewVersionName: TextView

    override fun setupDialog(dialog: Dialog, style: Int) {
        viewRoot = View.inflate(context, R.layout.fragment_about, null)
        textViewVersionName = viewRoot.findViewById(R.id.version_name_about)
        dialog.setContentView(viewRoot!!)
        setLayout()
    }

    private fun setLayout() {
        val layoutParams = (viewRoot!!.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        val behavior = layoutParams.behavior!!
        (behavior as BottomSheetBehavior<*>).setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //nothing
            }
        })
        val parent = viewRoot!!.parent as View
        parent.fitsSystemWindows = true
        val bottomSheetBehavior = BottomSheetBehavior.from(parent)
        viewRoot!!.measure(0, 0)
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val screenHeight = displaymetrics.heightPixels
        bottomSheetBehavior.peekHeight = screenHeight
        layoutParams.height = screenHeight
        parent.layoutParams = layoutParams
        textViewVersionName!!.text = BuildConfig.VERSION_NAME
    }

}