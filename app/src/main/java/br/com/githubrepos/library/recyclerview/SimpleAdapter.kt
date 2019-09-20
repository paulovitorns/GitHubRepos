package br.com.githubrepos.library.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.githubrepos.data.model.Repository

abstract class SimpleAdapter<I, VH : ViewHolder>(
    private val itemList: MutableList<I> = mutableListOf()
) : Adapter<ViewHolder>() {

    var onItemClicked: ((item: I) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    abstract fun onCreateItemViewHolder(parent: ViewGroup): VH

    abstract fun onBindViewHolder(holder: VH, position: Int, item: I)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return onCreateItemViewHolder(parent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val childItem = itemList[position]

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(childItem)
        }

        onBindViewHolder(holder as VH, position, childItem)
    }

    protected fun inflateView(
        @LayoutRes layoutResource: Int,
        rootView: ViewGroup,
        attachToRoot: Boolean = false
    ): View {
        val inflater = LayoutInflater.from(rootView.context)
        return inflater.inflate(layoutResource, rootView, attachToRoot)
    }

    open fun setItems(newItems: List<I>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    open fun addItems(newItems: List<I>) {
        val oldItems = itemList
        if (oldItems.isEmpty()) {
            itemList.addAll(newItems)
            notifyDataSetChanged()
        } else {
            itemList.clear()
            itemList.addAll(newItems)

            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = oldItems.size

                override fun getNewListSize(): Int = newItems.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldItems.getOrNull(oldItemPosition)
                    val newItem = newItems[newItemPosition]

                    if (oldItem == null) return false

                    if ((oldItem is Repository && newItem is Repository) && oldItem.id == newItem.id) {
                        return true
                    }

                    return false
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldItem = oldItems.getOrNull(oldItemPosition)
                    val newItem = newItems[newItemPosition]

                    return (oldItem is Repository && newItem is Repository) && oldItem.id == newItem.id
                }
            }, false).dispatchUpdatesTo(this)
        }
    }

    open fun clearAll() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean = itemList.isEmpty()
}
