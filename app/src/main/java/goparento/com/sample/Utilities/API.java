package goparento.com.sample.Utilities;

/**
 * Created by mohit on 27/6/16.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class API {
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int DELETE = 3;
    public static final int connectionTIMEOUT = 180000;
    public static final int readTTIMEOUT = 180000;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static OkHttpClient okHttpClient;

    public static OkHttpClient getClient() {
        OkHttpClient localInstance = okHttpClient;
        if (null == localInstance) {
            synchronized (API.class) {
                localInstance = okHttpClient;
                if (null == localInstance) {
                    okHttpClient = localInstance = new OkHttpClient.Builder()
                            .connectTimeout(connectionTIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(readTTIMEOUT, TimeUnit.MILLISECONDS)
                            .build();
                }
            }
        }
        return localInstance;
    }


    public static String makeServiceCall(String url, int requestType, JSONObject postData) {
        String httpResponse = null;
        URL urlNew = null;


        try {
            urlNew = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        if (urlNew == null) return null;


        Log.d("URL", urlNew.toString());


        OkHttpClient client = getClient();

        if (requestType == GET) {
            try {
                Request request = new Request.Builder()
                        .url(urlNew).get()
                        .build();
                // response is object that contain body , headers, body etc etc
                Response response = client.newCall(request).execute();
                if (response != null) {
                    httpResponse = response.body().string();
                    response.body().close();
                    response.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        } else if (requestType == POST) {

            if (postData == null) {
                postData = new JSONObject();
            }

            try {

                RequestBody body = RequestBody.create(JSON, postData.toString());
                Request request = new Request.Builder()
                        .url(urlNew)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                httpResponse = response.body().string();
                response.body().close();
                response.close();
                //httpResponse is the json object

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return httpResponse;

    }


    public static String makeServiceCall(String url) {
        return makeServiceCall(url, GET, null);
    }

    public static String makeServiceCall(String url, JSONObject postJson) {
        return makeServiceCall(url, POST, postJson);
    }


}