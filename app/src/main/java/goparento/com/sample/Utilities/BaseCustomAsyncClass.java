package goparento.com.sample.Utilities;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by mohit on 14/8/16.
 */

public class BaseCustomAsyncClass extends AsyncTask<String, String, String> {

    BasicCallBack basicCallback;
    JSONObject postJson;
    String url;
    private String resp;

    // post
    public BaseCustomAsyncClass(JSONObject postJson, BasicCallBack basicCallback, String url) {
        this.basicCallback = basicCallback;
        this.postJson = postJson;
        this.url = url;
    }

    //get
    public BaseCustomAsyncClass(BasicCallBack basicCallback, String url) {
        this.basicCallback = basicCallback;
        this.url = url;
    }


    @Override
    protected String doInBackground(String... params) {
        String resp;
        if (postJson != null) {
            resp = API.makeServiceCall(url, postJson);

        } else {
            resp = API.makeServiceCall(url);
        }
        return resp;
    }

    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        if (result != null) {
            if (basicCallback != null) {
                JSONObject response = null;
                try {
                    response = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    basicCallback.callBack(0, response);

                } else if (response != null && "600".equals(response.optString("ssc"))) {

                    basicCallback.callBack(1, response);

                }


            }


        } else {
            if (basicCallback != null) {
                basicCallback.callBack(1, null);
            }
        }


    }
}


