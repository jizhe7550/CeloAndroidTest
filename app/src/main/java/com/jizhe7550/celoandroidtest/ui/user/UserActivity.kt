package com.jizhe7550.celoandroidtest.ui.user

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.jizhe7550.celoandroidtest.R
import com.jizhe7550.celoandroidtest.ui.BaseActivity
import com.jizhe7550.celoandroidtest.viewmodelutil.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_user.*
import javax.inject.Inject

class UserActivity : BaseActivity(),
    UserDependencyProvider {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var requestManager: RequestManager

    override fun getGlideRequestManager(): RequestManager {
        return requestManager
    }

    override fun getVMProviderFactory(): ViewModelProviderFactory {
        return providerFactory
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        setupActionBar()
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    private fun setupActionBar() {
        setSupportActionBar(tool_bar)
    }

    override fun displayProgressBar(bool: Boolean) {
        if (bool) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}