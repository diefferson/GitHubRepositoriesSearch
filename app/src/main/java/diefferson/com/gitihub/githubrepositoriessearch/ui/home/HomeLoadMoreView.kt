package diefferson.com.gitihub.githubrepositoriessearch.ui.home

import com.chad.library.adapter.base.loadmore.LoadMoreView
import diefferson.com.gitihub.githubrepositoriessearch.R

class HomeLoadMoreView : LoadMoreView() {

    override fun getLayoutId() = R.layout.home_load_more

    override fun getLoadingViewId() = R.id.load_more_loading_view

    override fun getLoadFailViewId() = R.id.load_more_load_fail_view

    override fun getLoadEndViewId() = R.id.load_more_load_end_view
}
