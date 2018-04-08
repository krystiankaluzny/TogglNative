package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.internal.entity.User
import retrofit2.Call
import retrofit2.http.GET

internal interface TogglApi {

    @GET("me")
    fun me() : Call<Response<User>>
}