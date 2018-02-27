package cinemax.indo.aliagus.com.indocinemax.maincontent

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cinemax.indo.aliagus.com.indocinemax.R
import cinemax.indo.aliagus.com.indocinemax.about.About
import cinemax.indo.aliagus.com.indocinemax.fragmentcontentview.ContentMovieFragment
import cinemax.indo.aliagus.com.indocinemax.lib.StringSource
import cinemax.indo.aliagus.com.indocinemax.toastfragment.ToastFragment
import kotlinx.android.synthetic.main.activity_main_content.*

class MainContentActivity : AppCompatActivity(), MainContentActivityContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener {

    val mainContentActivityPresenter: MainContentActivityPresenter by lazy {
        MainContentActivityPresenter(this)
    }

    var menuNavBottom: Menu? = null
    var isFirstOpen = false
    var isMainContent = true
    private lateinit var contentMovie: ContentMovieFragment
    var toastFragment: ToastFragment? = null
    var about: About? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_content)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        menuNavBottom = bottom_navigation_view.menu
        bottomNavigationViowGetTreeObserver(menuNavBottom!!)
        bottom_navigation_view.setOnNavigationItemSelectedListener(this)
    }

    private fun bottomNavigationViowGetTreeObserver(menuNavBottom: Menu) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean =
            item?.itemId.let {
                return when (it) {
                    R.id.bottom_navigation_now_playing -> {
                        attachFragmentToActivity(StringSource.GET_NOW_PLAYING_MOVIE, "now")
                        setTitle(R.string.now_playing)
                        setCondition()
                        true
                    }

                    R.id.botton_navigation_popular -> {
                        attachFragmentToActivity(StringSource.GET_POPULAR_MOVIE, "popular")
                        setTitle(R.string.popular)
                        setCondition()
                        true
                    }

                    R.id.bottom_navigation_favorite -> {
                        attachFragmentToActivity(StringSource.GET_TRAILLER, "favorite")
                        setTitle(R.string.favorite)
                        setCondition()
                        true
                    }

                    R.id.botton_navigation_comming_soon -> {
                        attachFragmentToActivity(StringSource.GET_COMING_SOON_MOVIE, "soon")
                        setTitle(R.string.coming_soon)
                        setCondition()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

    private fun setCondition() {
        isFirstOpen = false
        isMainContent = true
    }

    private fun attachFragmentToActivity(urlData: String, filter: String) {
        if (!isFirstOpen) {
            contentMovie.onStop()
            val handler = Handler()
            handler!!.postDelayed({
                attachFragment(urlData, filter)!!
            }, 500)
        } else {
            attachFragment(urlData, filter)
        }
    }

    private fun attachFragment(urlData: String, filter: String) {
        contentMovie = ContentMovieFragment()
        contentMovie.setUrlData(urlData)
        contentMovie.setFilter(filter)
        contentMovie.setIsFirstOpen(isFirstOpen)
        supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_content_activity, contentMovie)
                .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    fun notifyPosition(position: Int?) {
        contentMovie.refreshAdapterPosition(position)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                about = About()
                about!!.show(
                        supportFragmentManager,
                        about!!.tag
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setTitle(R.string.now_playing)
        isFirstOpen = true
        bottom_navigation_view.selectedItemId = R.id.bottom_navigation_now_playing
    }

    fun resumeActivity() {
        checkToastFragment()
        onResume()
    }

    private fun checkToastFragment() {
        toastFragment?.dismissAllowingStateLoss()
    }

    fun showToastFragment(message: String) {
        if (toastFragment == null) {
            toastFragment = ToastFragment().getInstance(applicationContext)
        }
        toastFragment?.setMessage(message)
        toastFragment?.show(
                supportFragmentManager,
                toastFragment?.tag
        )
    }

    override fun onPause() {
        super.onPause()
        checkToastFragment()
    }
}
