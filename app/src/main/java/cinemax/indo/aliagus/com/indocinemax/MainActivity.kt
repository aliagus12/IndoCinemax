package cinemax.indo.aliagus.com.indocinemax

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import cinemax.indo.aliagus.com.indocinemax.maincontent.MainContentActivity
import cinemax.indo.aliagus.com.indocinemax.toastfragment.ToastFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    val mainActivityPresenter: MainActivityPresenter by lazy {
        MainActivityPresenter(this, applicationContext)
    }

    val PERMISSION_REQUEST_CODE : Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideNavigationBar()
    }

    private fun hideNavigationBar() {
        var versionName: String = BuildConfig.VERSION_NAME
        version_name.text = versionName
        logo.setImageResource(R.drawable.logo_app)
        var flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        } else {
            (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
        window.decorView.systemUiVisibility = flags
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        checkAllPermission()
    }

    @SuppressLint("NewApi")
    private fun checkAllPermission() {
        val isPermissionStorageGranted = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val isPermissionAccessNetworkState = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
        val isPermissionInternet = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED

        val listPermission = arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.INTERNET
        )
        if (!isPermissionStorageGranted || !isPermissionAccessNetworkState || !isPermissionInternet) {
            requestPermissions(listPermission, PERMISSION_REQUEST_CODE)
            return
        } else {
            mainActivityPresenter.getAllGenresMovies()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            var isPermitted = true
            grantResults.forEach { granted ->
                if (granted == PackageManager.PERMISSION_GRANTED) {
                    isPermitted = false
                }
            }
            if (isPermitted) {
                mainActivityPresenter.getAllGenresMovies()
            } else {
                moveTaskToBack(true)
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun toMainContent() {
        val intent = Intent(this@MainActivity, MainContentActivity::class.java)
        startActivity(intent)
    }

    override fun jumpToMainContent() {
        toMainContent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    fun resumeActivity() {
        checkToastFragment()
        onResume()
    }

    var toastFragment: ToastFragment? = null
    override fun showToastFragment(message: String?) {
        checkToastFragment()
        toastFragment = ToastFragment().getInstance(applicationContext)
        toastFragment?.setMessage(message!!)
        toastFragment?.show(
                supportFragmentManager,
                toastFragment?.tag
        )
    }

    private fun checkToastFragment() {
        toastFragment?.dismissAllowingStateLoss()
    }
}
