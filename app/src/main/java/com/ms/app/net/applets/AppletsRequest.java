package com.ms.app.net.applets;

import android.util.Log;

import com.bdlbsc.common.Result;
import com.bdlbsc.common.api.APPLETSAPI;
import com.bdlbsc.common.api.USERAPI;
import com.bdlbsc.common.applets.AppletssRequest;
import com.bdlbsc.common.applets.AppletssResponse;
import com.bdlbsc.common.user.UserTokenCheckResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.app.net.user.UserRequest;

import org.ms.module.supper.client.Modules;
import org.ms.module.supper.inter.net.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class AppletsRequest implements IAppletsRequestServer {


    private static final String TAG = "AppletsRequest";

    private AppletsRequest() {
    }

    private static AppletsRequest instance;

    public static AppletsRequest getInstance() {
        if (instance == null) {
            synchronized (AppletsRequest.class) {
                instance = new AppletsRequest();
            }
        }
        return instance;
    }


    @Override
    public Observable<AppletssResponse> applets(AppletssRequest request) {
        return Observable.create(emitter -> {


            Map<String,String> headers = new HashMap<>();
            headers.put("Content-Type","application/json");

            String requestBody = Modules.getUtilsModule().getGsonUtils().toJson(request);
            Response response = Modules.getRequestModule().requestBody(headers, Modules.getApiModule().getBaseUrl().get(0) + APPLETSAPI.APPLETS_GET_APPS, requestBody);


            Log.e(TAG, "applets: "+Modules.getUtilsModule().getGsonUtils().toJson(response));

            int code = response.code;

            if (code == 200) {
                String body = response.body;
                Type type = new TypeToken<Result<AppletssResponse>>() {
                }.getType();
                Object o = Modules.getUtilsModule().getGsonUtils().fromJson(body, type);
                Result<AppletssResponse> result = (Result<AppletssResponse>) o;
                AppletssResponse data = result.getData();
                emitter.onNext(data);
            } else {
                emitter.onError(new Exception("" + code));
            }
        });
    }
}
