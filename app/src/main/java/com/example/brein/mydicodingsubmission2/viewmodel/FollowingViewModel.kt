package com.example.brein.mydicodingsubmission2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.brein.mydicodingsubmission2.model.ItemUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel(application: Application) : AndroidViewModel(application) {

    val listFollowingModel = MutableLiveData<ArrayList<ItemUser>>()

    fun setDataFollowing(Login : String){
        val client = AsyncHttpClient()
        client.addHeader("authorization" , "token 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")
        client.addHeader("user-agent" , "request")

        val url = "https://api.github.com/users/$Login/following"

        client.get(url , object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try{
                    val listFoll = ArrayList<ItemUser>()
                    JSONArray(String(responseBody!!)).run {
                        for(i in 0 until this.length()){
                            this.getJSONObject(i).run {
                                listFoll.add(
                                    ItemUser(
                                        this.getString("login"),
                                        this.getString("avatar_url")
                                    )
                                )
                            }
                        }
                    }

                    listFollowingModel.postValue(listFoll)
                }catch (ex : Exception){
                    Log.d("Exception : " , ex.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ){ val error = when(statusCode){
                401 -> "$statusCode : Bad Request"
                403 -> "$statusCode : Forbidden"
                404 -> "$statusCode : Not Found"
                else -> "$statusCode : ${error?.message}"
            }
                Log.d("error" , error)
            }
        })
    }

    fun getDataFollowing() : LiveData<ArrayList<ItemUser>>{
        return listFollowingModel
    }

}