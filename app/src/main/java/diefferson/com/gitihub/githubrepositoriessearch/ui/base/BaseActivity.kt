package diefferson.com.gitihub.githubrepositoriessearch.ui.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewGroup
import diefferson.com.gitihub.githubrepositoriessearch.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext


abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    abstract val viewModel: BaseViewModel
    abstract val activityLayout: Int
    private val executionJob: Job by lazy { Job() }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + executionJob
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayout)
        setupViewState()
    }

    override fun onDestroy() {
        super.onDestroy()
        executionJob.cancel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewState(){
        viewModel.getViewStateObservable().observe(this, Observer {viewState->
            when(viewState){
                is ViewState.Error ->  errorHandler(getString(viewState.message))
                is ViewState.Loading ->   loadingHandler(viewState.isLoading)
            }
        })
    }

    open fun errorHandler(message:String?){
        Snackbar.make(this.findViewById<ViewGroup>(android.R.id.content),
                message?:getString(R.string.generic_error), Snackbar.LENGTH_LONG).show()
    }

    open fun loadingHandler(isLoading:Boolean){

    }
}
