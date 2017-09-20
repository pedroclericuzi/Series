package pedroclericuzi.seriesclient.Activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pedroclericuzi.seriesclient.R;
import pedroclericuzi.seriesclient.Util.DownloadFile;
import pedroclericuzi.seriesclient.Util.Info;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AsyncFunc syncFunc = new AsyncFunc();
        syncFunc.execute("http://10.0.2.2:8964/getImages");
    }

    class AsyncFunc extends AsyncTask<String, Void, Void>{
        private final HttpClient Client = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                HttpGet httpget = new HttpGet(strings[0]);
                httpResponse = Client.execute(httpget);
                httpEntity = httpResponse.getEntity();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String myResponse = null;
            try {
                myResponse = EntityUtils.toString( httpEntity );
                Log.d("Meu Json", myResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //public String

}
