package com.muhammhassan.epatrol.core.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.muhammhassan.epatrol.core.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferences(private val datastore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return datastore.data.map {
            UserModel(it[NAME], it[EMAIL], it[NRP])
        }
    }

    suspend fun setUser(email: String, nrp: String, name: String){
        datastore.edit {
            it[EMAIL] = email
            it[NAME] = name
            it[NRP] = nrp
        }
    }

    suspend fun setToken(token: String){
        datastore.edit {
            it[TOKEN] = token
        }
    }

    fun getToken(): Flow<String?>{
        return datastore.data.map {
            it[TOKEN]
        }
    }

    fun getEmail(): Flow<String?>{
        return datastore.data.map {
            it[EMAIL]
        }
    }

    companion object{
        val EMAIL = stringPreferencesKey("email")
        val NAME = stringPreferencesKey("name")
        val NRP = stringPreferencesKey("nrp")
        val TOKEN = stringPreferencesKey("token")
    }
}