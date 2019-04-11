package diefferson.com.gitihub.githubrepositoriessearch.app

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.log.EmptyLogger

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin(this, AppInject.modules(), logger = EmptyLogger())
    }
}