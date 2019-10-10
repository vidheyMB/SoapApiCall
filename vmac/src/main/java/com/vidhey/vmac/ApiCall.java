package com.vidhey.vmac;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCall {

    private String soapRequest;
    private String soapMethodBase;
    private String url;

    public ApiCall(String soapRequest, String soapMethodBase, String url) {
        this.soapRequest = soapRequest.replaceAll("&", "and");
        this.soapMethodBase = soapMethodBase;
        this.url = url;
    }

    /**
     * Single is an Observable which gives response at once.
     **/

    public Single call() {

        return new Single() {
            @Override
            protected void subscribeActual(final SingleObserver observer) {
                RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"), soapRequest);
//                    Timber.d(soapRequest);

                final Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "text/xml; charset=utf-8")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Accept", "text/xml")
                        .addHeader("soapaction", soapMethodBase)
                        .addHeader("Pragma", "no-cache")
                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String mMessage = response.body().string();
//                            Timber.w("SUCCESS Response: "+ mMessage);
                        observer.onSuccess(mMessage);
                    }
                });
            }
        };
    }
}
