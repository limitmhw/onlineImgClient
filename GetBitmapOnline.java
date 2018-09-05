package com.example.mi.getimgbyweb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 //example
 new Thread(new Runnable() {
@Override
public void run() {
    for(int i=0;i<1000;i++){
        GetBitmapOnline cc=new GetBitmapOnline("http://10.231.56.111:8090","111","555");
        cc.getBmp();
        cc.submitResult("lihe");
    }

    }
}).start();
 */
public class GetBitmapOnline {
    public GetBitmapOnline(String host,String dataset,String task){
        if(host.charAt(host.length()-1)!='/'){
            host+="/";
        }
        this.host=host;
        this.dataset=dataset;
        this.task=task;
    }

    private  String host;
    private  String dataset;
    private  String task;
    private  String index;

    public Bitmap getBmp() {

        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = this.host+"getImg/?dataset="+this.dataset+"&task="+this.task;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = reader.readLine()) != null) {
                // 一行一行追加
                result += line;//+"\r\n");

            }
            is.close();
            JSONObject json;
            Map<String, String> itemMap = new HashMap<String, String>();
            try{
                json = JSON.parseObject(result);//这里对于非json数据可能会爆异常
                itemMap= JSONObject.toJavaObject(json, Map.class);
            }catch(Exception e){

            }
            this.index=itemMap.get("index");
            return this.getImg(itemMap.get("img"));


        } catch (Exception e) {
            Log.d("mhw","Path阶段,发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;

    }


    private Bitmap getImg(String url) {

        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段

            // 定义 BufferedReader输入流来读取URL的响应
            InputStream is = connection.getInputStream();

            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            int count=0;
            //开辟空间
            byte[] buffer=new byte[1024];
            while ((count=is.read(buffer))!=-1) {
                bos.write(buffer, 0, count);
            }
            byte[] buff=bos.toByteArray();
            Bitmap bitmap= BitmapFactory.decodeByteArray(buff, 0, buff.length);
            return bitmap;

        } catch (Exception e) {
            Log.d("mhw","Img阶段,发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;

    }
    public void submitResult(String result) {

        BufferedReader in = null;
        try {
            String urlNameString = this.host+"submit/?index="+this.index+"&result="+result;
            //Log.d("mhw",urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            InputStream is = connection.getInputStream();
            is.close();

        } catch (Exception e) {
            Log.d("mhw","提交阶段,发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


    }
}
