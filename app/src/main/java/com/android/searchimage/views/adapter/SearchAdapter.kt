package com.android.searchimage.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.searchimage.R
import com.android.searchimage.databinding.ItemSearchBinding
import com.android.searchimage.model.Hit
import com.android.searchimage.views.callbacks.OnItemClickListener

class SearchAdapter(
    private val activity: Context,
    private val arrData: List<Hit>,
    private val onItemClick: OnItemClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater: LayoutInflater? = null
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val inProgressCourseBind = DataBindingUtil.inflate<ItemSearchBinding>(
            layoutInflater!!,
            R.layout.item_search,
            parent,
            false
        )
        return ViewHolder(inProgressCourseBind!!, activity)

    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder) {
            bindTo(model)
            setListener(this, model)
        }
    }


    private fun setListener(holder: ViewHolder, model: Hit) {

        holder.run {
            /*inProgressCourseBind?.layoutCourse?.setOnClickListener { view ->

                onItemClick.onItemClick(
                    adapterPosition, model, view,
                    SearchAdapter::class.java.simpleName
                )
            }*/
        }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    inner class ViewHolder(val inProgressCourseBind: ItemSearchBinding?, activity: Context) :
        RecyclerView.ViewHolder(inProgressCourseBind!!.getRoot()) {
        var model: Hit? = null
        fun bindTo(model: Hit) {
            inProgressCourseBind!!.model = model
            inProgressCourseBind.executePendingBindings()
            this.model = model
        }


    }

}