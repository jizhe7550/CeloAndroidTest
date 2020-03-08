package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.jizhe7550.celoandroidtest.R
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.util.DateUtils
import com.jizhe7550.celoandroidtest.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_user_list_item.view.*

class UserListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val USER_ITEM = 0
    private val NO_MORE_RESULTS_USER = User(
      "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        1

    )

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            UserRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {

            NO_MORE_RESULTS -> {
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            USER_ITEM -> {
                return UserViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_user_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return UserViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_user_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class UserRecyclerChangeCallback(
        private val adapter: UserListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].email != "") {
            return USER_ITEM
        }
        return NO_MORE_RESULTS
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Prepare the images that will be displayed in the RecyclerView.
    // This also ensures if the network connection is lost, they will be in the cache
    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<User>
    ) {
        for (user in list) {
            requestManager
                .load(user.thumbnail)
                .preload()
        }
    }

    fun submitList(
        userList: List<User>?,
        isQueryExhausted: Boolean
    ) {
        val newList = userList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_USER)
        val commitCallback = Runnable {
            // if process died must restore list position
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class UserViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: User) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            requestManager
                .load(item.thumbnail)
                .transition(withCrossFade())
                .into(itemView.user_image)
            itemView.user_name.text = "This is ${item.title} ${item.first} ${item.last}"
            itemView.user_gender.text = item.gender
            itemView.user_date.text = DateUtils.convertLongToStringDate(item.date)
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: User)

        fun restoreListPosition()
    }
}
