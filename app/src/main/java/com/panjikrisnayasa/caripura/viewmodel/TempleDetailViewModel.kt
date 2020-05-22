package com.panjikrisnayasa.caripura.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.panjikrisnayasa.caripura.BuildConfig
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class TempleDetailViewModel : ViewModel() {

    private val mDistanceDuration = MutableLiveData<ArrayList<String>>()

    fun getDistanceDuration(
        lastLat: String,
        lastLng: String,
        destinationLat: String,
        destinationLng: String
    ): LiveData<ArrayList<String>> {
        val client = AsyncHttpClient()
        val origin = "$lastLat,$lastLng"
        val destination = "$destinationLat,$destinationLng"
        val builtUri = Uri.parse(FindTempleViewModel.BASE_URL_ROUTE).buildUpon()
            .appendQueryParameter(FindTempleViewModel.ORIGIN_PARAM, origin)
            .appendQueryParameter(FindTempleViewModel.DESTINATION_PARAM, destination)
            .appendQueryParameter(
                FindTempleViewModel.GOOGLE_API_KEY_PARAM,
                BuildConfig.GOOGLE_API_KEY
            )
            .build()
        val url = builtUri.toString()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                var tResult = ""
                if (responseBody != null) {
                    tResult = String(responseBody)
                }
                val resultRoute = tResult
                val responseObjectRoute = JSONObject(resultRoute)
                val routes = responseObjectRoute.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val distance = legs.getJSONObject(0).getJSONObject("distance").getString("text")
                val duration = legs.getJSONObject(0).getJSONObject("duration").getString("text")
                val distanceDuration = arrayListOf<String>()
                distanceDuration.add(distance)
                distanceDuration.add(duration)
                mDistanceDuration.postValue(distanceDuration)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                error?.printStackTrace()
            }
        })
        return mDistanceDuration
    }
}