package cinemax.indo.aliagus.com.indocinemax.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import cinemax.indo.aliagus.com.indocinemax.adapter.AdapterContentMovie

/**
 * Created by ali on 26/02/18.
 */
object Animated {

    private val VISIBLE = 1

    fun animatedView(view: View, visibility: Int) {
        val alpa: Float
        val y: Float
        if (visibility == VISIBLE) {
            alpa = 1.0f
            y = 0f
        } else {
            alpa = 0.0f
            y = view.height.toFloat()
        }
        view.animate()
                .translationY(y)
                .alpha(alpa)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (visibility == VISIBLE) {
                            view.visibility = View.VISIBLE
                        } else {
                            view.visibility = View.GONE
                        }
                    }
                })
    }

    fun animatedView(view: View,
                     adapterContentMovie: AdapterContentMovie
    ) {
        view.animate()
                .translationY(view.height.toFloat())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        view.visibility = View.GONE
                        adapterContentMovie.notifyDataSetChanged()
                    }
                })
    }
}
