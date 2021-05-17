package com.dicoding.estomihi.githubsearchuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.estomihi.githubsearchuser.Data.followingdata
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingModel : ViewModel() {

    val listFollowingUser = MutableLiveData<ArrayList<followingdata>>()

    fun setFollowingUser(username: String) {
        val listData = ArrayList<followingdata>()

        val url = "https://api.github.com/users/$username/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-agent", "request")
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val following = responseArray.getJSONObject(i)
                        val dataUser = followingdata()
                        dataUser.apply {
                            id = following.getInt("id")
                            login = following.getString("login")
                            avatars = following.getString("avatar_url")
                        }
                        listData.add(dataUser)
                    }
                    listFollowingUser.postValue(listData)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getFollowingUser(): LiveData<ArrayList<followingdata>> {
        return listFollowingUser
    }

}