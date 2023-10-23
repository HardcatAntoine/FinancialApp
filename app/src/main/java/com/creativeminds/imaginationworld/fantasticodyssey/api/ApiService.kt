package com.creativeminds.imaginationworld.fantasticodyssey.api


import com.creativeminds.imaginationworld.fantasticodyssey.data.Backend
import com.creativeminds.imaginationworld.fantasticodyssey.data.LastModified
import com.creativeminds.imaginationworld.fantasticodyssey.data.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/auth")
    suspend fun getDbJson(
        @Query("p1") p1: String?,
        @Query("p2") p2: String?,
        @Query("p3") p3: String?,
        @Query("p4") p4: String?,
        @Query("p5") p5: String?,
        @Query("p6") p6: String?,
        @Query("p7") p7: String?,
        @Query("p8") p8: String?,
        @Query("p9") p9: String?,
        @Query("p10") p10: String?,
        @Query("p11") p11: String?,
        @Query("p12") p12: String?,
        @Query("p13") p13: String?,
        @Query("p14") p14: String?,
    ): Backend?

    @GET("/{string}/date.json")
    suspend fun getRefreshDate(@Path("string") string: String): LastModified

    @GET("/{string}/db.json")
    suspend fun getResponse(@Path("string") string: String): Response

    @POST("subs/aff_sub1")
    suspend fun getAffSub1(@Body body: HashMap<String, Any>): String

    @POST("subs/aff_sub2")
    suspend fun getAffSub2(@Body body: HashMap<String, Any>): String?

    @POST("subs/aff_sub3")
    suspend fun getAffSub3(@Body body: HashMap<String, Any>): String

    @POST("subs/aff_sub5")
    suspend fun getAffSub5(@Body body: HashMap<String, Any>): String
}



