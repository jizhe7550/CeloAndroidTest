package com.jizhe7550.celoandroidtest.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.jizhe7550.celoandroidtest.R
import com.jizhe7550.celoandroidtest.ui.user.viewmodel.UserViewModel
import com.jizhe7550.celoandroidtest.di.Injectable
import com.jizhe7550.celoandroidtest.ui.DataStateChangeListener
import com.jizhe7550.celoandroidtest.ui.UICommunicationListener
import com.jizhe7550.celoandroidtest.ui.user.state.USER_VIEW_STATE_BUNDLE_KEY
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState

abstract class BaseUserFragment : Fragment(), Injectable
{

    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: UserDependencyProvider

    private lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var stateChangeListener: DataStateChangeListener

    lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.userFragment, activity as AppCompatActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(
                this,
                dependencyProvider.getVMProviderFactory()
            ).get(UserViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        cancelActiveJobs()

        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[USER_VIEW_STATE_BUNDLE_KEY] as UserViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    fun cancelActiveJobs(){
        viewModel.cancelActiveJobs()
    }

    fun isViewModelInitialized() = ::viewModel.isInitialized

    /**
     * !IMPORTANT!
     * Must save ViewState b/c in event of process death the LiveData in ViewModel will be lost
     */
    override fun onSaveInstanceState(outState: Bundle) {
        if(isViewModelInitialized()){
            val viewState = viewModel.viewState.value

            //clear the list. Don't want to save a large list to bundle.
            viewState?.userFields?.userList = ArrayList()

            outState.putParcelable(
                USER_VIEW_STATE_BUNDLE_KEY,
                viewState
            )
        }
        super.onSaveInstanceState(outState)
    }

    /*
          @fragmentId is id of fragment from graph to be EXCLUDED from action back bar nav
        */
    private fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity){
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }

        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }

        try{
            dependencyProvider = context as UserDependencyProvider
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DependencyProvider" )
        }
    }
}