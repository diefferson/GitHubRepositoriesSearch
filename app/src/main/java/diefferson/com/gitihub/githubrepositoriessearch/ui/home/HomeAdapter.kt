package diefferson.com.gitihub.githubrepositoriessearch.ui.home


import diefferson.com.gitihub.githubrepositoriessearch.util.loadUrlImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import diefferson.com.gitihub.githubrepositoriessearch.R
import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel

class HomeAdapter(data: List<GitHubRepositoryModel>) : BaseQuickAdapter<GitHubRepositoryModel, BaseViewHolder>(R.layout.repo_item,data) {

    override fun convert(helper: BaseViewHolder, item: GitHubRepositoryModel) {
        helper.setText(R.id.name, "${item.name} - ${item.language}")
        helper.setText(R.id.fullName, item.fullName)
        helper.setText(R.id.owner, item.owner.login )
        helper.loadUrlImage(R.id.image, item.owner.avataUrl)
    }
}
