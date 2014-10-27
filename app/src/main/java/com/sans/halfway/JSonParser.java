package com.sans.halfway;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class JSonParser  {

    public JSonParser() throws JSONException {
    }

    protected void getJSONFromUrl(final String url, final ResponseListener target) {
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());

                    int read;
                    char[] buff = new char[1024];
                    while ((read = inputStreamReader.read(buff)) != -1) {
                        stringBuilder.append(buff, 0, read);
                    }
                    return stringBuilder.toString();
                } catch (MalformedURLException localMalformedURLException) {
                    Log.e("ex", localMalformedURLException.toString());
                    return "";
                } catch (IOException localIOException) {
                    Log.e("ex", localIOException.toString());
                    return "";
                } finally {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }

            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //Log.d("onPostExecute ", result);
                target.onResponseComplete(result);
            }
        }.execute();
    }

}
