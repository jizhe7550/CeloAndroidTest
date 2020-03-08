package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.jizhe7550.celoandroidtest.R
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.persistence.UserQueryUtils.Companion.USER_GENDER_All
import com.jizhe7550.celoandroidtest.persistence.UserQueryUtils.Companion.USER_GENDER_FEMALE
import com.jizhe7550.celoandroidtest.persistence.UserQueryUtils.Companion.USER_GENDER_MALE
import com.jizhe7550.celoandroidtest.session.SessionManager
import com.jizhe7550.celoandroidtest.ui.DataState
import com.jizhe7550.celoandroidtest.ui.user.BaseUserFragment
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState
import com.jizhe7550.celoandroidtest.util.ErrorHandling
import com.jizhe7550.celoandroidtest.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : BaseUserFragment(),
    UserListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()

    }

    override fun onResume() {
        super.onResume()
        if (SessionManager.isReopenApp) {
            viewModel.refreshFromCache()
        } else {
            onUserSearchOrFilter()
            SessionManager.isReopenApp = true
        }
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        users_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                // call before onDataStateChange to consume error if there is one
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "UserFragment, ViewState: $viewState")
            if (viewState != null) {
                recyclerAdapter.apply {
                    preloadGlideImages(
                        requestManager = dependencyProvider.getGlideRequestManager(),
                        list = viewState.userFields.userList
                    )
                    submitList(
                        userList = viewState.userFields.userList,
                        isQueryExhausted = viewState.userFields.isQueryExhausted
                    )
                }

            }
        })
    }

    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }

        // ENTER ON COMPUTER KEYBOARD OR ARROW ON VIRTUAL KEYBOARD
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val searchQuery = v.text.toString()
                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: $searchQuery")
                viewModel.setQuery(searchQuery)
                onUserSearchOrFilter()

            }
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: $searchQuery")
            viewModel.setQuery(searchQuery)
            onUserSearchOrFilter()


        }
    }

    private fun onUserSearchOrFilter() {
        viewModel.loadFirstPage()
        resetUI()

    }

    private fun resetUI() {
        users_recyclerview.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun handlePagination(dataState: DataState<UserViewState>) {

        // Handle incoming data from DataState
        dataState.data?.let { data ->
            data.data?.let { event ->
                event.getContentIfNotHandled()?.let { userViewState ->
                    viewModel.handleIncomingUserListData(userViewState)
                }
            }
        }

        // Check for pagination end (no more results)
        // must do this b/c server will return an ApiErrorResponse if page is not valid,
        // -> meaning there is no more data.
        dataState.error?.let { event ->
            event.peekContent().response.message?.let {
                if (ErrorHandling.isPaginationDone(it)) {

                    // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()

                    // set query exhausted to update RecyclerView with
                    // "No more results..." list item
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }

    private fun initRecyclerView() {

        users_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@UserFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = UserListAdapter(
                dependencyProvider.getGlideRequestManager(),
                this@UserFragment
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "UserFragment: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter_settings -> {
                showFilterDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(position: Int, item: User) {
        viewModel.setUser(item)
        findNavController().navigate(R.id.action_userFragment_to_viewUserFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.userFields?.layoutManagerState?.let { lmState ->
            users_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        users_recyclerview.adapter = null
    }

    override fun onRefresh() {
        onUserSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    private fun showFilterDialog() {
        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_user_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilter()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    USER_GENDER_MALE -> check(R.id.filter_male)
                    USER_GENDER_FEMALE -> check(R.id.filter_female)
                    USER_GENDER_All -> check(R.id.filter_all)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_male -> USER_GENDER_MALE
                        R.id.filter_female -> USER_GENDER_FEMALE
                        else -> USER_GENDER_All
                    }

                viewModel.apply {
                    saveFilterOptions(newFilter)
                    setUserFilter(newFilter)
                }

                onUserSearchOrFilter()

                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}

















