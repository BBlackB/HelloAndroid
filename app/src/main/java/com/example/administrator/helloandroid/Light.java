package com.example.administrator.helloandroid;

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

/**
 * Created by Administrator on 2016/9/22.
 */
public class Light {
    private boolean state;
    private Color color;

    public final static int OFF = 0;
    public final static int STATIC = 1;
    public final static int FLOW = 2;
    final int ERROR = -1;

    public Color getColor() {
        return color;
    }

    public boolean isState() {
        return state;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setLight()
    {
        try {
            final URL SERVER_URL =  new URL("http://192.168.222.223/light");
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
     //   String JsonString = prepareJsonString(state, color);
    }

    private static JSONArray prepareJsonArray(String val){
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

    private static String prepareJsonString(JSONArray jsonArray, int mode){
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

    private static String prepareJsonString(boolean state, Color color)
    {
        JSONArray  JsonColor = new JSONArray();
        JSONObject JsonObj = new JSONObject();
        JSONStringer JsonStringerColor = new JSONStringer();
        JSONStringer JsonStringer = new JSONStringer();

        try {
            JsonStringerColor.array();
            JsonStringerColor.key("r");
            JsonStringerColor.value(color.getR());
            JsonStringerColor.key("g");
            JsonStringerColor.value(color.getG());
            JsonStringerColor.key("b");
            JsonStringerColor.value(color.getB());
            JsonStringerColor.endArray();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String stateStr = state ? "ON" : "OFF";

        try {
            JsonStringer.object();
            JsonStringer.key("status");
            JsonStringer.value(stateStr);
            JsonStringer.key("color");
            JsonStringer.value(JsonStringerColor);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return JsonStringer.toString();
    }

    private static String postJson(URL url, String jsonString){
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

    private static  String inputStream2String(InputStream is){
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

    public static String postRequest(URL url, String strColor, int mode){
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
        String res = postJson(url, strJson);
       // System.out.println("response is " + res);
        return res;
    }

}

