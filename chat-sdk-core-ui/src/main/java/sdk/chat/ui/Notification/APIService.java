package sdk.chat.ui.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAADit3424:APA91bFjYgjz0sQvodnfCbAKBWJGD_2gkUTrdJYjh17y08Aqq1dDUScncIUnR9wgeWRRMojV5qiQYsVAJpJz-dkPUlLnlSOjx2qJ3AF8AnN4a1fIEC0w6pd7Vt_WU5VKM8rqQnyWg9UH"
    })
    @POST("fcm/send")
    Call<MyResponse>sendNotification(@Body Sender body);
}
