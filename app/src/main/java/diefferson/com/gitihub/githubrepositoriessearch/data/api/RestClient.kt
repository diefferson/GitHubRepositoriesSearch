package diefferson.com.gitihub.githubrepositoriessearch.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import diefferson.com.gitihub.githubrepositoriessearch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by diefferson.santos
 */
class RestClient {

    val api: GitHubApi

    init {

        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.HTTP_LOG_ENABLED) {
            httpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        val httpClient = httpClientBuilder.build()

        val retrofitClient: Retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

        api = retrofitClient.create(GitHubApi::class.java)
    }

}
