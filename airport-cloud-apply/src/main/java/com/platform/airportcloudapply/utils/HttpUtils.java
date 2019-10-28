package com.platform.airportcloudapply.utils;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class HttpUtils {
    public static String doGet(String httpurl,String sessionKey) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            if(StringUtils.isNotBlank(sessionKey)) {
                connection.addRequestProperty("Cookie", "sessionkey=" + sessionKey);
            }
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();

                System.out.println("------Get:"+result);
            }else{
                System.out.println(connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static String  doPost(String urlStr,String xmlInfo) {
        String result="";
        try {

            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            //con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(con
                    .getOutputStream());
            System.out.println("urlStr=" + urlStr);
            System.out.println("xmlInfo=" + xmlInfo);
            out.write(new String(xmlInfo.getBytes("ISO-8859-1")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream()));
            StringBuffer sbf = new StringBuffer();
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                sbf.append(line);
                sbf.append("\r\n");
            }
            result=sbf.toString();
            System.out.println("------POST:"+result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public interface CLibrary extends StdCallLibrary {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("D://log/IPNBSSDK.dll", CLibrary.class);
       // int Add(int plus1, int plus2);
        //初始化
        boolean IPNBSSDKInit();
        //设置广播服务器接收控制的网络端口, 默认端口为 2048
        boolean IPNBSSDK_SetServerPort(int wPort);
        //设置调用程序接收状态的网络端口, 默认端口为 2046.
        boolean IPNBSSDK_SetStatePort(int wPort);
        //设置广播服务器所在的 IP 地址, 默认 IP 为本机 IP
       boolean IPNBSSDK_SetServerIP(String strServerIP);
       //控制消防报警
       boolean IPNBSSDK_CtrlFireAlarm(int wAlarmArea, boolean bStart);
        //查看IP状态
        boolean IPNBSSDK_CtrlQueryIP(int wID);
        //查询终端状态
        boolean IPNBSSDK_CtrlQueryState(int wID);
        //查询终端的 ID 号.
        int IPNBSSDK_CtrlQueryID(String strTermIP);
        //查询服务器终端的数量
        int IPNBSSDK_CtrlQueryTermCount();
        //设置终端名称.
        boolean IPNBSSDK_CtrlSetName(int wID, byte[] strName);

        //设置终端音量
        boolean IPNBSSDK_CtrlSetVolume(int wID, int ucVolume);



    }
    public static void main(String[] args) {
        try {
            boolean result = false;
            //result = CLibrary.INSTANCE.IPNBSSDK_CtrlQueryID("255.255.255.255");


/*
        result = CLibrary.INSTANCE.IPNBSSDK_SetServerPort(2048);
        System.out.println("设置服务器的端口结果为:" + result);

        result = CLibrary.INSTANCE.IPNBSSDK_SetStatePort(2046);
        System.out.println("设置服务器的网络端口结果为:" + result);*/
            result = CLibrary.INSTANCE.IPNBSSDK_SetServerIP("192.168.50.121");

            System.out.println("设置服务器的IP结果为:" + result);
            result = CLibrary.INSTANCE.IPNBSSDK_CtrlFireAlarm(1, true);
            System.out.println("结果为:" + result+";终端数量："+CLibrary.INSTANCE.IPNBSSDK_CtrlQueryTermCount());
            System.out.println("设置服务器的IP结果为:" + CLibrary.INSTANCE.IPNBSSDK_CtrlQueryID("192.168.50.121"));
            System.out.println("查询终端状态结果为:" + CLibrary.INSTANCE.IPNBSSDK_CtrlQueryState(3));
            //System.out.println("设置服务器终端名称：" + CLibrary.INSTANCE.IPNBSSDK_CtrlSetName(2, "服务终端2".getBytes()));
            System.out.println("设置设备终端音量：" + CLibrary.INSTANCE.IPNBSSDK_CtrlSetVolume(2, 1));
            Thread.sleep(5000);

            result = CLibrary.INSTANCE.IPNBSSDK_CtrlFireAlarm(1, false);
            System.out.println("结果为:" + result);
        }catch (Exception e){
            e.printStackTrace();
        }

       /* try{
        String result=HttpUtils.doGet("http://192.168.50.121/command?command=setRelativePosition"
                        ,"2147030873");
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        //String url = "http://192.168.50.121/command?command=getDeviceList&noCache=1571710621649";//"http://192.168.50.121/webservice?noCache=";
        //new HttpUtils().doGet(url);//testPost(url);

        /* 第二种方法，使用json-lib提供的方法 */
        //创建 XMLSerializer对象
      /*  XMLSerializer xmlSerializer = new XMLSerializer();
        //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
        String result = xmlSerializer.read(xml).toString();
        //输出json内容
        System.out.println(result);
*/
    }
}
