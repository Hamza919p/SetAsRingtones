package com.wmt.setasringtones.`interface`

import com.wmt.setasringtones.models.TonesItem
import com.wmt.setasringtones.api.ApiUrls
import com.wmt.setasringtones.models.CategoriesItem
import com.wmt.setasringtones.models.ChannelsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {

    @GET(ApiUrls.getFeatureToneURL+ "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getFeaturedTonesResponse(pagerNumber: String): Response<List<TonesItem>>

    @GET(ApiUrls.getBestToneURL+ "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getBestTonesResponse(@Path("currentPageNumber") pagerNumber: String): Response<List<TonesItem>>

    @GET(ApiUrls.getLatestToneURL+ "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getLatestTonesResponse(@Path("currentPageNumber") pageNumber: String): Response<List<TonesItem>>

    @GET(ApiUrls.getCategoriesURL+ "/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getCategoriesResponse() : Response<ArrayList<CategoriesItem>>

    @GET(ApiUrls.getSearchResultsURL + "{searchQuery}/{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getSearchResults(@Path("searchQuery") searchQuery: String?, @Path("currentPageNumber") pageNumber: String) : Response<ArrayList<TonesItem>>

    @GET(ApiUrls.getChannelsURL +"{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getChannelsResponse(@Path("currentPageNumber") pageNumber: String): Response<List<ChannelsItem>>

    @GET(ApiUrls.getChannelURL +"{userId}/" + ApiUrls.getLatestURL + "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getChannelLatestTonesResponse(@Path("userId") userId: String, @Path("currentPageNumber") pageNumber: String): Response<ArrayList<TonesItem>>

    @GET(ApiUrls.getChannelURL +"{userId}/" + ApiUrls.getBestURL + "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getChannelBestTonesResponse(@Path("userId") userId: String, @Path("currentPageNumber") pageNumber: String): Response<ArrayList<TonesItem>>

    @GET(ApiUrls.getRelatedTonesURL +"{id}/" + "995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getRelatedTonesResponse(@Path("id") id: String): Response<ArrayList<TonesItem>>

    @GET(ApiUrls.getCategoryURL +"{ringtoneId}/" + ApiUrls.getLatestURL + "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getCategoryLatestTonesResponse(@Path("ringtoneId") userId: String, @Path("currentPageNumber") pageNumber: String): Response<ArrayList<TonesItem>>

    @GET(ApiUrls.getCategoryURL +"{ringtoneId}/" + ApiUrls.getBestURL + "{currentPageNumber}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getCategoryBestTonesResponse(@Path("ringtoneId") userId: String, @Path("currentPageNumber") pageNumber: String): Response<ArrayList<TonesItem>>

    @GET(ApiUrls.getDeepLinkUrl +"{ringtoneId}/995c07928b9b0bd5ab683c59e93263ba")
    suspend fun getDeepLinkResponse(@Path("ringtoneId") ringtoneId: String): Response<TonesItem>


}