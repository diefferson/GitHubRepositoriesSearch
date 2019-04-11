package diefferson.com.gitihub.githubrepositoriessearch.app

import diefferson.com.gitihub.githubrepositoriessearch.data.api.RestClient
import diefferson.com.gitihub.githubrepositoriessearch.data.repository.GitHubRepository
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.BaseViewModel
import diefferson.com.gitihub.githubrepositoriessearch.ui.details.DetailsViewModel
import diefferson.com.gitihub.githubrepositoriessearch.ui.home.HomeViewModel
import io.coroutines.cache.core.CoroutinesCache
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object AppInject {

    fun modules() : List<Module> = listOf(
            applicationModule,
            viewModelModule,
            repositoriesModule
    )

    private val applicationModule: Module = module {
        single { RestClient().api }
        single { CoroutinesCache(get())}
    }

    private val viewModelModule = module {
        viewModel{ BaseViewModel() }
        viewModel{ DetailsViewModel(get()) }
        viewModel{ HomeViewModel(get()) }
    }

    private val repositoriesModule: Module = module {
        single{ GitHubRepository(get(), get()) }
    }
}