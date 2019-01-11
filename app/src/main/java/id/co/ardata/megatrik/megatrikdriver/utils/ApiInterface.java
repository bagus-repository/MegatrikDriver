package id.co.ardata.megatrik.megatrikdriver.utils;

import java.util.HashMap;
import java.util.List;

import id.co.ardata.megatrik.megatrikdriver.model.Content;
import id.co.ardata.megatrik.megatrikdriver.model.MaterialCategory;
import id.co.ardata.megatrik.megatrikdriver.model.MaterialsItem;
import id.co.ardata.megatrik.megatrikdriver.model.OAuthSecret;
import id.co.ardata.megatrik.megatrikdriver.model.OAuthToken;
import id.co.ardata.megatrik.megatrikdriver.model.Order;
import id.co.ardata.megatrik.megatrikdriver.model.OrderMaterial;
import id.co.ardata.megatrik.megatrikdriver.model.OrderStatus;
import id.co.ardata.megatrik.megatrikdriver.model.User;
import id.co.ardata.megatrik.megatrikdriver.model.UserOrder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    /**
     * List api user dan authnya
     * @return
     */
    @GET(ApiConfig.API_URL+"oauthclients/2")
    Call<OAuthSecret> getOAuthSecret();

    @FormUrlEncoded
    @POST(ApiConfig.BASE_URL+"oauth/token")
    Call<OAuthToken> getAccessToken(
            @FieldMap HashMap<String, String> params
            );

    @GET(ApiConfig.API_URL+"user")
    Call<User> getUser();

    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"register")
    Call<User> storeUser(
            @FieldMap HashMap<String, String> params
    );

    @FormUrlEncoded
    @PATCH(ApiConfig.API_URL+"users/{id}")
    Call<User> updateUser(
            @Path("id") String id,
            @FieldMap HashMap<String, String> params
    );

    /**
     * Home Slider
     * @return
     */
    @GET(ApiConfig.API_URL+"contentlists/1")
    Call<Content> getContent();

    /**
     * Locations
     * @param id
     * @param params
     * @return
     */
    @FormUrlEncoded
    @PATCH(ApiConfig.API_URL+"locations/{id}")
    Call<ResponseBody> updateLocation(
            @Path("id") String id,
            @FieldMap HashMap<String, Double> params
    );

    /**
     * Order
     * @param id
     * @return
     */
    @GET(ApiConfig.API_URL+"orders/{id}")
    Call<Order> getOrder(
            @Path("id") String id
    );

    @FormUrlEncoded
    @PATCH(ApiConfig.API_URL+"order/kerjakan/{id}")
    Call<ResponseBody> updateOrderKerjakan(
            @Path("id") String id,
            @FieldMap HashMap<String, String> params
    );

    @FormUrlEncoded
    @PATCH(ApiConfig.API_URL+"order/selesai/{id}")
    Call<ResponseBody> updateOrderSelesai(
            @Path("id") String id,
            @FieldMap HashMap<String, String> params
    );

    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"order/accept/{id}")
    Call<Order> updateOrder(
            @Path("id") String id,
            @FieldMap HashMap<String, String> params
    );

    @GET(ApiConfig.API_URL+"drivers/{id}/ordernotcompleted")
    Call<UserOrder> getOrderNotCompleted(
            @Path("id") String id
    );

    @GET(ApiConfig.API_URL+"drivers/{id}/ordercompleted")
    Call<UserOrder> getOrderCompleted(
            @Path("id") String id
    );

    @GET(ApiConfig.API_URL+"order/materials/{id}")
    Call<OrderMaterial> getOrderMaterial(
            @Path("id") String order_id
    );

    @GET(ApiConfig.API_URL+"order/notaccepted")
    Call<List<Order>> getOrderNotAccpeted();

    /**
     * Material
     * @return
     */
    @GET(ApiConfig.API_URL+"materialcategories")
    Call<List<MaterialCategory>> getMaterialCategories();

    @GET(ApiConfig.API_URL+"materialcategories/{id}")
    Call<MaterialCategory> getMaterialCategoryList(
            @Path("id") String material_category_id
    );

    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"materials")
    Call<MaterialsItem> storeMaterial(
            @FieldMap HashMap<String, String> params
    );

    @DELETE(ApiConfig.API_URL+"materials/{id}")
    Call<ResponseBody> deleteMaterial(
            @Path("id") String material_id
    );

}
