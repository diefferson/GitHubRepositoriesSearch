package diefferson.com.gitihub.githubrepositoriessearch.ui.details

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import diefferson.com.gitihub.githubrepositoriessearch.R
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.BaseActivity
import diefferson.com.gitihub.githubrepositoriessearch.util.inflateView
import diefferson.com.gitihub.githubrepositoriessearch.util.loadUrlImage
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : BaseActivity(){

    override val viewModel by viewModel<DetailsViewModel>()
    override val activityLayout = R.layout.activity_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observeViewModel()
        viewModel.getRepository(intent?.getStringExtra(OWNER)?:"", intent?.getStringExtra(REPO)?:"")
    }

    private fun observeViewModel(){
        viewModel.repository.observe(this, Observer {
            it?.let { repo->
                image.loadUrlImage(repo.owner.avataUrl)
                name.text = "${repo.name} - ${repo.language}"
                fullName.text = repo.fullName
                owner.text = repo.owner.login
                overviewTextView.text = repo.description
                stars.text = repo.stargazersCount.toString()
                eyes.text = repo.watchersCount.toString()
                branch.text = repo.forksCount.toString()
            }
        })
    }

    override fun loadingHandler(isLoading: Boolean) {
        if(isLoading){
            if(findViewById<View>(R.id.loading_root)== null){
                val loadingView = inflateView(R.layout.activity_details_loading,
                        this.findViewById<ViewGroup>(android.R.id.content) )

                findViewById<ViewGroup>(android.R.id.content).addView(loadingView)
            }
        }else{
            val root = findViewById<ViewGroup>(android.R.id.content)
            root.removeView(findViewById(R.id.loading_root))
        }
    }

    companion object {
        private const val REPO = "REPO"
        private const val OWNER = "OWNER"
        fun launch(context: Context, repo:String, owner:String){
            context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                putExtra(REPO, repo)
                putExtra(OWNER, owner)
            })
        }
    }
}