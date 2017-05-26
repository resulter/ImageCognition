package com.ccut.imagecognition.utils;

import android.util.Log;

import com.ccut.imagecognition.config.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;


public class WebUtil {
    public static JSONArray getJSONArrayByWeb(String methodName,
                                              JSONArray params) {

        String returnValue = "";
        JSONArray result = null;
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("charset", "UTF-8");
        HttpClient hc = new DefaultHttpClient(httpParams);
        HttpPost hp = new HttpPost(Config.SERVER_IP + "/PictureCognitionServer/servlet/"
                + methodName);
        try {
            hp.setEntity(new StringEntity(params.toString(), "UTF-8"));
            HttpResponse hr = hc.execute(hp);
//            Log.e("wang",hr.getStatusLine().getStatusCode() + "****************" + hr.getEntity().getContent());
            if (hr.getStatusLine().getStatusCode() == 200) {
                returnValue = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("wang",returnValue + "return value");
                result = new JSONArray(returnValue);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (hc != null) {
            hc.getConnectionManager().shutdown();
        }
        return result;
    }
}
