package br.com.githubrepos.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.githubrepos.R
import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.library.recyclerview.SimpleAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.search_item.view.forks
import kotlinx.android.synthetic.main.search_item.view.ownerImage
import kotlinx.android.synthetic.main.search_item.view.ownerName
import kotlinx.android.synthetic.main.search_item.view.repository
import kotlinx.android.synthetic.main.search_item.view.stars

class SearchAdapter(
    private val context: Context,
    repositories: MutableList<Repository> = mutableListOf()
) : SimpleAdapter<Repository, SearchAdapter.ViewHolder>(repositories) {

    override fun onCreateItemViewHolder(parent: ViewGroup): ViewHolder {
        val searchItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return ViewHolder(searchItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Repository) {
        with(holder) {
            repository.text = item.fullName
            stars.text = item.stargazersCount.toString()
            forks.text = item.forksCount.toString()

            if (item.owner.avatarUrl.isNotBlank()) {
                Glide.with(context)
                    .load(item.owner.avatarUrl)
                    .placeholder(R.drawable.github_mark)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(ownerImage)
            }

            ownerName.text = item.owner.login
        }
    }

    inner class ViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        val ownerImage: ImageView = itemVIew.ownerImage
        val repository: TextView = itemVIew.repository
        val stars: TextView = itemVIew.stars
        val forks: TextView = itemVIew.forks
        val ownerName: TextView = itemVIew.ownerName
    }
}
