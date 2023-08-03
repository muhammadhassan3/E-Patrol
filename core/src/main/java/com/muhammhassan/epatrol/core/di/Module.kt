package com.muhammhassan.epatrol.core.di

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.muhammhassan.epatrol.core.BuildConfig
import com.muhammhassan.epatrol.core.datasource.local.LocalDataSource
import com.muhammhassan.epatrol.core.datasource.local.LocalDataSourceImpl
import com.muhammhassan.epatrol.core.datasource.local.datastore.DataStorePreferences
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSource
import com.muhammhassan.epatrol.core.datasource.remote.RemoteDataSourceImpl
import com.muhammhassan.epatrol.core.datasource.remote.api.ApiInterface
import com.muhammhassan.epatrol.core.repository.TaskRepository
import com.muhammhassan.epatrol.core.repository.TaskRepositoryImpl
import com.muhammhassan.epatrol.core.repository.UserRepository
import com.muhammhassan.epatrol.core.repository.UserRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Module {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("patuhi_com")

    val dataSourceModule = module {
        single<RemoteDataSource> { RemoteDataSourceImpl(get()) }
        single<LocalDataSource> { LocalDataSourceImpl(get()) }
    }

    val datastoreModule = module {
        single { DataStorePreferences(androidContext().datastore) }
    }

    fun provideApi(version: Int) = module {
        val client = OkHttpClient.Builder().addInterceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.addHeader("platform", "Android")
            requestBuilder.addHeader("level", Build.VERSION.SDK_INT.toString())
            requestBuilder.addHeader("version", version.toString())
            val request = requestBuilder.build()
            it.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        val retrofit = Retrofit.Builder().client(client.build()).baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        single { retrofit.create(ApiInterface::class.java) }
    }

    val repositoryModule = module {
        single<UserRepository> { UserRepositoryImpl(get(), get()) }
        single<TaskRepository> { TaskRepositoryImpl(get()) }
    }
}