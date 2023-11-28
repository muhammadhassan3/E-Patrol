package com.muhammhassan.epatrol

import android.app.Application
import com.muhammhassan.epatrol.di.Module.viewModelModule
import com.muhammhassan.epatrol.domain.di.Module.dataStoreModule
import com.muhammhassan.epatrol.domain.di.Module.datasourceModule
import com.muhammhassan.epatrol.domain.di.Module.repModule
import com.muhammhassan.epatrol.domain.di.Module.retrofit
import com.muhammhassan.epatrol.domain.di.Module.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@BaseApplication)
            modules(
                viewModelModule,
                datasourceModule,
                retrofit(BuildConfig.VERSION_CODE),
                repModule,
                useCaseModule,
                dataStoreModule
            )
        }
    }
}