package com.example.administrator.helloandroid;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2016/9/22.
 */
public class Light {
    private URL _url;
    private int _port;
    private boolean WIFIMode;

    public final boolean AP = true;
    public final boolean STA = false;

    public final static int OFF = 0;
    public final static int STATIC = 1;
    public final static int FLOW = 2;
    final int ERROR = -1;


    public void setLight()
    {
        try {
            final URL SERVER_URL =  new URL("http://192.168.222.223/light");
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

    public Light(String host, int port){
        String tmpurl = "http://" + host + ":" + port;
        try {
            this._url = new URL(tmpurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this._port = port;
        this.WIFIMode = AP;
    }

    public Light(String host, int port, boolean wifimode){
        String tmpurl = "http://" + host + ":" + port;
        try {
            this._url = new URL(tmpurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this._port = port;
        this.WIFIMode = wifimode;
    }

    public void setLight(String host, int port) throws MalformedURLException {
        String tmpUrl = "http://" + host + ":" + port;
        this._url = new URL(tmpUrl);
        _port = port;
    }

    public void setWIFIMode(boolean wifiMode){
        this.WIFIMode = wifiMode;
    }

    public boolean getWIFIMode(){
        return this.WIFIMode;
    }

    private JSONArray prepareJsonArray(String val){
        String[] strArr = null;
        strArr  = val.split(" ");
        JSONArray jsonArray = new JSONArray();

        int[] intArr = null;
        intArr = new int[strArr.length];

        for (int i = 0; i < strArr.length; i++){
            intArr[i] = Integer.parseInt(strArr[i].replace("0x", ""), 16);
        }

        for (int i = 0; i < strArr.length; i++){
            try {
                jsonArray.put(i, intArr[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    private JSONArray prepareJsonArray(int[] color) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(0,0);
        for (int i = 0; i < color.length; i++){
                jsonArray.put(i+1, color[i]);
        }
        return jsonArray;
    }

    private String prepareJsonString(JSONArray jsonArray, int mode){
        JSONStringer jsonStringer = new JSONStringer();
        String strMode;
        switch (mode)
        {
            case STATIC: strMode = "static"; break;
            case FLOW: strMode = "flow"; break;
            default: strMode = "error";
        }

        try {
            jsonStringer.object();
            jsonStringer.key("params");
            jsonStringer.value(jsonArray);
            jsonStringer.key("mode");
            jsonStringer.value(strMode);
            jsonStringer.endObject();
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return jsonStringer.toString();
    }
    //发送json字符串
    private String postJson(URL url, String jsonString){
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("charset","utf-8");

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.writeBytes(jsonString);
            dataOutputStream.flush();
            dataOutputStream.close();
            int statusCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == statusCode)
            {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = inputStream2String(inputStream);
                return response;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private  String inputStream2String(InputStream is){
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null){
                stringBuffer.append(line);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public String postRequest(String strColor, int mode){
        if (mode == OFF) {
            strColor = "0 0";
            mode = STATIC;
        }
        else{
            strColor = "0 " + strColor;
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray = prepareJsonArray(strColor);
        //prepare json string
        String strJson = null;
        strJson = prepareJsonString(jsonArray, mode);
        System.out.println(strJson);
        //post http request
        String res = postJson(_url, strJson);
        // System.out.println("response is " + res);
        return res;
    }

    public String setStatic(int[] color) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        jsonArray = prepareJsonArray(color);
        //prepare json string
        String strJson = null;
        strJson = prepareJsonString(jsonArray, STATIC);
        System.out.println(strJson);
        //post http request
        String res = postJson(_url, strJson);
        // System.out.println("response is " + res);
        return res;
    }

    public String setFlow(int[] color) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        jsonArray = prepareJsonArray(color);
        //prepare json string
        String strJson = null;
        strJson = prepareJsonString(jsonArray, FLOW);
        System.out.println(strJson);
        //post http request
        String res = postJson(_url, strJson);
        // System.out.println("response is " + res);
        return res;
    }

    public void changeWIFIModeToAP(){
        String tmp = _url.toString() + "/setting";
        URL sendurl = null;
        try {
            sendurl = new URL(tmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object();
            jsonStringer.key("mode");
            jsonStringer.value("ap");
            jsonStringer.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("change to AP mode: " + jsonStringer.toString());

        postJson(sendurl, jsonStringer.toString());
    }

    public void changeWIFIModeToSTA(String ssid, String passwd){
        String tmp = _url.toString() + "/setting";
        URL url = null;
        try {
            url = new URL(tmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object();
            jsonStringer.key("mode");
            jsonStringer.key("sta");
            jsonStringer.key("wifi_ssid");
            jsonStringer.value(ssid);
            jsonStringer.key("wifi_passwd");
            jsonStringer.value(passwd);
            jsonStringer.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("change to STA send: " + jsonStringer.toString());

        postJson(url, jsonStringer.toString());
    }

    public void updateURL(URL url){
        this._url = url;
    }

    public void updateURL(String host){
        String tmpurl = "http://" + host + ":" + _port;
        URL  newurl = null;
        try {
            newurl = new URL(tmpurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this._url = newurl;
    }

}

