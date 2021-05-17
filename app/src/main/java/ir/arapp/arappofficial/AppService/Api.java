package ir.arapp.arappofficial.AppService;

import ir.arapp.arappofficial.Data.UserData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("registerPhoneApp")
    Call<ResponseBody> registerPhoneApp
            (
                    @Field("phone") String phone
            );

    @FormUrlEncoded
    @POST("verifyPhoneApp")
    Call<ResponseBody> verifyPhoneApp
            (
                    @Field("phone") String phone,
                    @Field("verify_code") String verifyCode
            );

    @FormUrlEncoded
    @POST("registerUserApp")
    Call<ResponseBody> registerUserApp
            (
                    @Field("phone") String phone,
                    @Field("name") String name,
                    @Field("email") String email,
                    @Field("password") String password,
                    @Field("service") String service
            );

    @FormUrlEncoded
    @POST("loginApp")
    Call<ResponseBody> loginApp
            (
                    @Field("phone") String phone,
                    @Field("password") String password
            );

    @FormUrlEncoded
    @POST("resetPasswordPhoneApp")
    Call<ResponseBody> resetPasswordPhoneApp
            (
                    @Field("phone") String phone
            );

    @FormUrlEncoded
    @POST("resetPasswordCodeApp")
    Call<ResponseBody> resetPasswordCodeApp
            (
                    @Field("phone") String phone,
                    @Field("verify_code") String verifyCode
            );

    @FormUrlEncoded
    @POST("resetPasswordApp")
    Call<ResponseBody> resetPasswordApp
            (
                    @Field("phone") String phone,
                    @Field("password") String password,
                    @Field("confirmPassword") String cnfPassword
            );

    @FormUrlEncoded
    @POST("getUserDataApp")
    Call<UserData> getUserData1(
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("getUserDataApp")
    Call<ResponseBody> getUserData
            (
                    @Field("phone") String phone
            );

    @FormUrlEncoded
    @POST("setNotificationReceiverApp")
    Call<ResponseBody> setNotificationReceiver
            (
                    @Field("phone") String phone,
                    @Field("notify") int notify
            );

    @FormUrlEncoded
    @POST("editUserProfileApp")
    Call<ResponseBody> editUserProfile
            (
                    @Field("phone") String phone,
                    @Field("name") String name,
                    @Field("email") String email,
                    @Field("bio") String bio,
                    @Field("education") String education,
                    @Field("province") int province,
                    @Field("city") String city
            );

    @FormUrlEncoded
    @POST("uploadProfilePictureApp")
    Call<ResponseBody> uploadProfilePicture
            (
                    @Field("phone") String phone,
                    @Field("picture") String picture
            );

    @FormUrlEncoded
    @POST("changePasswordApp")
    Call<ResponseBody> changePassword
            (
                    @Field("phone") String phone,
                    @Field("password") String password
            );

    @GET("getNews")
    Call<ResponseBody> getNews();

    @GET("getCategory")
    Call<ResponseBody> getCategory();

    @GET("getNotification")
    Call<ResponseBody> getNotification();
}
