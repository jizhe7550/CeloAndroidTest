package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.jizhe7550.celoandroidtest.R
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.ui.user.BaseUserFragment
import com.jizhe7550.celoandroidtest.util.DateUtils
import kotlinx.android.synthetic.main.fragment_view_user.*

class ViewUserFragment : BaseUserFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        stateChangeListener.expandAppBar()

    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)

            if (dataState != null) {
//                dataState.data?.let { data ->
//                    data.data?.getContentIfNotHandled()?.let { viewState ->
//                        viewModel.setIsAuthorOfBlogPost(
//                            viewState.viewBlogFields.isAuthorOfBlogPost
//                        )
//                    }
//                    data.response?.peekContent()?.let{ response ->
//                        if(response.message.equals(SUCCESS_BLOG_DELETED)){
//                            viewModel.removeDeletedBlogPost()
//                            findNavController().popBackStack()
//                        }
//                    }
//                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewUserFields.user?.let { user ->
                setUserProperties(user)
            }
        })
    }

    private fun setUserProperties(user: User) {
        dependencyProvider.getGlideRequestManager()
            .load(user.large)
            .into(user_image)
        page_title.text = "This is ${user.title} ${user.first} ${user.last}'s page."
        user_name.text = user.first + " " + user.last
        user_gender.text = user.gender
        user_date.text = DateUtils.convertLongToStringDate(user.date)
        user_introduction.text =
            "${user.title} ${user.first} ${user.last} 's phone number is ${user.phone}" +
                    " and the cellphone number is ${user.cell}."
    }
}













