package diefferson.com.gitihub.githubrepositoriessearch.ui.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem

import diefferson.com.gitihub.githubrepositoriessearch.ui.details.DetailsActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import diefferson.com.gitihub.githubrepositoriessearch.R
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.BaseActivity
import diefferson.com.gitihub.githubrepositoriessearch.ui.custom.SearchAnimationToolbar
import diefferson.com.gitihub.githubrepositoriessearch.util.hideKeyboard
import diefferson.com.gitihub.githubrepositoriessearch.util.inflateView
import kotlinx.android.synthetic.main.error_view.view.*
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity(){

    override val viewModel by viewModel<HomeViewModel>()
    override val activityLayout = R.layout.home_activity
    private lateinit var adapter: HomeAdapter
    private var jobSearch: Job?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
        setupToolbar()
        observeViewModel()
        viewModel.loadRepos()
    }

    override fun onBackPressed() {
        if(!hideKeyboard()){
            if(!vToolbar.onBackPressed()){
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_search ->{
                vToolbar.onSearchIconClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar(){
        vToolbar.setSupportActionBar(this)
        vToolbar.setOnSearchQueryChangedListener(onSearchQueryChangedListener)
    }

    private fun setupAdapter(){

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener)

        adapter = HomeAdapter(viewModel.repos).apply {
            setOnLoadMoreListener(requestLoadMoreListener, recyclerView)
            openLoadAnimation(BaseQuickAdapter.ALPHAIN)
            setLoadMoreView(HomeLoadMoreView())
            setEnableLoadMore(true)

            setOnItemClickListener { adapter, view, position ->
                val repo = data[position]
                DetailsActivity.launch(this@HomeActivity, repo.name, repo.owner.login)
            }
        }

        recyclerView.adapter = adapter
        setLoadingView()
    }

    private fun observeViewModel(){

        viewModel.updatedRepos.observe(this, Observer {
            if(it!= null && it){
                swipeRefreshLayout.isRefreshing = false

                if(viewModel.searching){
                    setLoadingView()
                }else{
                    setEmptyView()
                }

                if(viewModel.currentPage == 1){
                    adapter.notifyDataSetChanged()
                }else{
                    adapter.loadMoreComplete()
                }
            }
        })

        viewModel.enableLoadMore.observe(this, Observer {
            it?.let { enableLoadMore ->
                adapter.setEnableLoadMore(enableLoadMore)
            }
        })
    }


    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        setLoadingView()
        viewModel.refreshRepos()
    }

    private val requestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        viewModel.loadMoreRepos()
    }

    private val onSearchQueryChangedListener = object : SearchAnimationToolbar.OnSearchQueryChangedListener{

        override fun onSearchExpanded() {
            swipeRefreshLayout.isEnabled = false
        }

        override fun onSearchCollapsed() {
            swipeRefreshLayout.isEnabled = true
            viewModel.currentQuery = ""
            viewModel.refreshRepos()
        }

        override fun onSearchQueryChanged(query: String){
            jobSearch?.cancel()
            jobSearch = launch {
                delay(500)
                setLoadingView()
                viewModel.searchRepos(query,1)
            }
        }

        override fun onSearchSubmitted(query: String) {
            setLoadingView()
            jobSearch?.cancel()
            viewModel.searchRepos(query,1)
        }
    }

    private fun setLoadingView(){
        adapter.emptyView = inflateView(R.layout.home_activity_loading, recyclerView)
    }
    private fun setEmptyView() {
        val emptyView = inflateView(R.layout.error_view, recyclerView)
        emptyView.messageTextView.text = getString(R.string.results_not_found)
        adapter.emptyView = emptyView
    }

    override fun errorHandler(message: String?) {
        super.errorHandler(message)
        adapter.loadMoreFail()
        adapter.emptyView = inflateView(R.layout.error_view, recyclerView)
        swipeRefreshLayout.isRefreshing = false
    }
}
