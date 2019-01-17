package com.rwz.network.net;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by HaohaoChang on 2017/2/11.
 */
public interface DouBanMovieService {

    String BASE_URL = "https://api.douban.com/v2/movie/";
///video/rank/lists

    /**
     * 热播榜单
     * @param start
     * @param count
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("game/lists")
    Call<ResponseBody> getMovies2(@QueryMap Map<String, String> map, @Field("limit") int start, @Field("page") int count, @Field("typeId") int id);


}
