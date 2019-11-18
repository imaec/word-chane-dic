package com.imaec.wordchandic.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wordchandic.R
import com.imaec.wordchandic.TYPE_ITEM
import com.imaec.wordchandic.TYPE_PROGRESS
import com.imaec.wordchandic.model.Item
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.item_progress.view.*

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listItem = ArrayList<Item>()
    private lateinit var context: Context
    private var isLoadMore = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        val view: View
        val vh: RecyclerView.ViewHolder
        when (viewType) {
            TYPE_ITEM -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
                vh = ItemViewHolder(view)
            }
            TYPE_PROGRESS -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false)
                vh = ProgressViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
                vh = ItemViewHolder(view)
            }
        }
        return vh
    }

    override fun getItemCount(): Int = if (listItem.size > 0) listItem.size + 1 else 0

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            listItem.size -> {
                TYPE_PROGRESS
            }
            else -> TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position])
        } else if (holder is ProgressViewHolder) {
            holder.onBind()
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textWord by lazy { itemView.textItemWord }
        private val textPos by lazy { itemView.textItemPos }

        fun onBind(item: Item) {
            textWord.text = item.word

//            var pos = ""
//            for (sense in item.listSense) {
//                pos += if (pos == "") sense.pos
//                else "/${sense.pos}"
//            }
            textPos.text = item.listSense[0].pos

            itemView.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle(item.word)
                    setMessage(item.listSense[0].definition)
                    setPositiveButton("확인") { dialog, which ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        }
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val progress by lazy { itemView.progressItem }

        fun onBind() {
            progress.visibility = if (isLoadMore) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

        }
    }

    fun setIsLoadMore(isLoadMore: Boolean) {
        this.isLoadMore = isLoadMore
    }

    fun addItem(item: Item) {
        listItem.add(item)
    }

    fun clearItem() {
        listItem.clear()
    }
}