package br.com.githubrepos.screens.home

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.githubrepos.R
import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.library.extension.hideKeyboard
import br.com.githubrepos.library.extension.toast
import br.com.githubrepos.screens.BaseActivity
import br.com.githubrepos.screens.BaseUi
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.queryTextChanges
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_home.defaultError
import kotlinx.android.synthetic.main.activity_home.offlineState
import kotlinx.android.synthetic.main.activity_home.progress
import kotlinx.android.synthetic.main.activity_home.recyclerView
import kotlinx.android.synthetic.main.activity_home.searchNotFound
import kotlinx.android.synthetic.main.activity_home.searchView
import kotlinx.android.synthetic.main.offline_state.retryButton
import kotlinx.android.synthetic.main.search_not_found_state.notFoundDescription

interface HomeUi : BaseUi {
    fun showDefaultQueryString(queryString: String)
    fun showProgress()
    fun hideProgress()
    fun search(): Observable<String>
    fun loadNextPage(): Observable<Boolean>
    fun retryButton(): Observable<Unit>
    fun showSearchResult(repositories: List<Repository>)
    fun showNextPage(repositories: List<Repository>)
    fun repositorySelected(): Observable<Repository>
    fun showSearchError(queryString: String)
    fun showAllItemsLoaded()
    fun showOfflineState()
    fun showDefaultError()
    fun hideAllErrorState()
    fun openRepositoryWebPage(url: String)
}

class HomeActivity : BaseActivity<HomePresenter>(), HomeUi {

    override val layoutRes: Int? = R.layout.activity_home
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }
    private val searchAdapter by lazy { SearchAdapter(this) }
    private var lastRecyclerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastRecyclerPosition = savedInstanceState.getInt(LAST_RECYCLER_POSITION)
        }
    }

    override fun setupViews() {
        super.setupViews()

        with(recyclerView) {
            layoutManager = linearLayoutManager
            adapter = searchAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                    searchView.clearFocus()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val linearManager = recyclerView.layoutManager as LinearLayoutManager
        val scrollPosition = linearManager.findFirstVisibleItemPosition()
        outState.putInt(LAST_RECYCLER_POSITION, scrollPosition)
        super.onSaveInstanceState(outState)
    }

    override fun showDefaultQueryString(queryString: String) {
        searchView.setQuery(queryString, false)
    }

    override fun showProgress() {
        progress.isVisible = true
    }

    override fun hideProgress() {
        progress.isVisible = false
    }

    override fun search(): Observable<String> {
        return searchView.queryTextChanges()
            .map { it.toString() }
    }

    override fun loadNextPage(): Observable<Boolean> {
        return recyclerView.scrollStateChanges()
            .filter { event -> event == RecyclerView.SCROLL_STATE_IDLE }
            .filter {
                linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchAdapter.itemCount - 1
            }.map { true }
    }

    override fun retryButton(): Observable<Unit> {
        return retryButton.clicks()
    }

    override fun showSearchResult(repositories: List<Repository>) {
        showRecyclerIfNeeded()
        searchAdapter.setItems(repositories)

        if (lastRecyclerPosition > 0) {
            val linearManager = recyclerView.layoutManager as LinearLayoutManager
            linearManager.scrollToPositionWithOffset(lastRecyclerPosition, 0)
        }
    }

    override fun showNextPage(repositories: List<Repository>) {
        showRecyclerIfNeeded()
        searchAdapter.addItems(repositories)
    }

    private fun showRecyclerIfNeeded() {
        if (!recyclerView.isVisible) recyclerView.isVisible = true
    }

    override fun repositorySelected(): Observable<Repository> {
        return searchAdapter.onItemSelected()
    }

    override fun showSearchError(queryString: String) {
        hideRecycler()

        val descriptionFormatted = notFoundDescription.text.toString().format(queryString)

        // This will set the queryString to bold
        val spannableString = SpannableString(descriptionFormatted).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                descriptionFormatted.length - queryString.length,
                descriptionFormatted.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }

        notFoundDescription.text = spannableString
        searchNotFound.isVisible = true
    }

    override fun showAllItemsLoaded() {
        toast(R.string.all_items_loaded)
        hideProgress()
    }

    override fun showOfflineState() {
        hideRecycler()

        offlineState.isVisible = true
    }

    override fun showDefaultError() {
        hideRecycler()
        defaultError.isVisible = true
    }

    override fun hideAllErrorState() {
        searchNotFound.isVisible = false
        defaultError.isVisible = false
        hideOfflineState()
    }

    override fun openRepositoryWebPage(url: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
            startActivity(it)
        }
    }

    private fun hideRecycler() {
        recyclerView.isVisible = false
        searchAdapter.clearAll()
    }

    private fun hideOfflineState() {
        offlineState.isVisible = false
    }
}

private const val LAST_RECYCLER_POSITION = "last_recycler_position"
