package `fun`.inaction.network.service

import `fun`.inaction.network.bean.GetParkResult
import `fun`.inaction.network.bean.Park
import `fun`.inaction.network.bean.ParkSearchResult
import `fun`.inaction.network.bean.Result
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ParkService {

    @Multipart
    @POST("/park/add")
    fun addPark(@Part("parkData") park: Park, @Part file: MultipartBody.Part): Call<Result>

    @FormUrlEncoded
    @POST("/park/search")
    fun searchParks(
        @Field("city") city: String,
        @Field("longitude") longitude: Double,
        @Field("latitude") latitude: Double,
        @Field("searchRadius") searchRadius: Float
    ): Call<ParkSearchResult>

    @GET("/park/get")
    fun getPark(@Query("parkID") parkId: String): Call<GetParkResult>

    @FormUrlEncoded
    @POST("/park/comment/add")
    fun commentPark(
        @Field("userID") userID: String,
        @Field("userName") userName: String,
        @Field("parkID") parkID: String,
        @Field("rate") rate: Float,
        @Field("comment") comment: String
    ): Call<Result>

}