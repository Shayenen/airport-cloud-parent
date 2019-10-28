package com.platform.airportcloudapply.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.airportcloudapply.utils.DateUtils;
import com.platform.airportcloudapply.utils.HttpUtils;
import com.platform.airportcloudapply.utils.MD5Util;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

/**
 * VDG控制入口
 */
@RestController
@RequestMapping("/vdgApi")
public class VDGController {
    private final static Logger logger = LoggerFactory.getLogger(VDGController.class);

    /**
     * 获取sessionKey
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSessionKey")
    public String getSessionKey(@RequestBody JSONObject jsonParam){
        logger.info("调用VDG获取sessionKey入参：{}",jsonParam.toJSONString());
        System.out.println(jsonParam.toJSONString());
        String result="";
        try {
            String userName = jsonParam.getString("userName");
            String nonce = jsonParam.getString("nonce");
            String timestamp = DateUtils.convert(new Date(),DateUtils.DATE_TIME_FORMAT);
            String password="!DVadmin";
            String sha1=MD5Util.getMD5String(timestamp)+userName+MD5Util.getSha1(MD5Util.getSha1(password));
            System.out.println(sha1);

            StringBuilder sb = new StringBuilder();
           /* sb.append("<!--?xml version='1.0'?-->" +
                    "<AuthenticateUserDigest>" +
                        "<username>"+userName+"</username>" +
                        "<nonce>"+nonce+"</nonce>" +
                        "<timestamp>"+timestamp+"</timestamp>" +
                        "<digest>"+ HMACSHA1.byte2hex(HMACSHA1.HmacSHA1Encrypt(nonce,sha1)) +"</digest>" +
                    "</AuthenticateUserDigest>");
*/
           sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                   "<AuthenticateUser>\n" +
                   " <username>"+userName+"</username>\n" +
                   " <password>"+password+"</password>\n" +
                   "</AuthenticateUser>");

            result=HttpUtils.doPost("http://192.168.50.121/webservice?noCache=" + DateUtils.dateToStamp(new Date()), sb.toString());
            XMLSerializer xmlSerializer = new XMLSerializer();
            result = xmlSerializer.read(result).toString();
        }catch (ParseException pe){
            logger.error("调用VDG获取sessionKey出现异常（ParseException）：{}",pe.getMessage());
            pe.printStackTrace();
        }catch (Exception e){
            logger.error("调用VDG获取sessionKey出现异常（Exception）：{}",e.getMessage());
            e.printStackTrace();
        }
        logger.info("调用VDG获取sessionKey响应数据：{}",result.replace("@",""));
        return result.replace("@","");
    }

    /**
     * 获取相机列表
     * @param jsonParam
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCameraList")
    public String getCameraList(@RequestBody JSONObject jsonParam){
        logger.info("调用VDG获取相机列表入参：{}",jsonParam.toJSONString());
        System.out.println(jsonParam.toJSONString());
        String result="";
        try {
            result=HttpUtils.doGet("http://192.168.50.121/command?command=getDeviceList&noCache="+DateUtils.dateToStamp(new Date()),jsonParam.getString("sessionKey"));
            XMLSerializer xmlSerializer = new XMLSerializer();
            result = xmlSerializer.read(result).toString();
        }catch (ParseException pe){
            logger.error("调用VDG获取getCameraList出现异常（ParseException）：{}",pe.getMessage());
            pe.printStackTrace();
        }catch (Exception e){
            logger.error("调用VDG获取getCameraList出现异常（Exception）：{}",e.getMessage());
            e.printStackTrace();
        }
        logger.info("调用VDG获取相机列表响应数据：{}",result.replace("@",""));
        return result.replace("@","");
    }

    /**
     * 获取事件列表
     * @param jsonParam
     * @return
     */
    @ResponseBody
    @RequestMapping("/getEventsList")
    public String getEventsList(@RequestBody JSONObject jsonParam){
        logger.info("调用VDG获取getEventsList入参：{}",jsonParam.toJSONString());
        System.out.println(jsonParam.toJSONString());
        String startid=jsonParam.getString("startid");
        String starttime=jsonParam.getString("starttime");
        String endtime=jsonParam.getString("endtime");
        String types=jsonParam.getString("types");
        String limit=jsonParam.getString("limit");
        String deviceid=jsonParam.getString("deviceid");
        String serverid=jsonParam.getString("serverid");

        StringBuffer stringBuffer = new StringBuffer();
        if(StringUtils.isNotBlank(startid))
            stringBuffer.append("&startid="+startid);
        if(StringUtils.isNotBlank(starttime))
            stringBuffer.append("&starttime="+starttime);
        if(StringUtils.isNotBlank(endtime))
            stringBuffer.append("&endtime="+endtime);
        if(StringUtils.isNotBlank(types))
            stringBuffer.append("&types="+types);
        if(StringUtils.isNotBlank(limit))
            stringBuffer.append("&limit="+limit);
        if(StringUtils.isNotBlank(deviceid))
            stringBuffer.append("&deviceid="+deviceid);
        if(StringUtils.isNotBlank(serverid))
            stringBuffer.append("&serverid="+serverid);

        String result="";
        try {
            result=HttpUtils.doGet("http://192.168.50.121/command?command=getEvents&noCache="
                    +DateUtils.dateToStamp(new Date())+stringBuffer.toString(),jsonParam.getString("sessionKey"));
            XMLSerializer xmlSerializer = new XMLSerializer();
            result = xmlSerializer.read(result).toString();
        }catch (ParseException pe){
            logger.error("调用VDG获取getEventsList出现异常ParseException：{}",pe.getMessage());
            pe.printStackTrace();
        }catch (Exception e){
            logger.error("调用VDG获取getEventsList出现异常Exception：{}",e.getMessage());
            e.printStackTrace();
        }
        logger.info("调用VDG获取getEventsList响应：{}",result);
        return result.replace("@","");
    }
    @ResponseBody
    @RequestMapping("/setRelativePosition")
    public String setRelativePosition(@RequestBody JSONObject jsonParam){
        logger.info("调用VDG获取setRelativePosition入参：{}",jsonParam.toJSONString());
        System.out.println(jsonParam.toJSONString());
        String pan=jsonParam.getString("pan");
        String tilt=jsonParam.getString("tilt");
        String zoom=jsonParam.getString("zoom");
        String focus=jsonParam.getString("focus");
        String iris=jsonParam.getString("iris");
        String deviceid=jsonParam.getString("deviceid");
        String serverid=jsonParam.getString("serverid");

        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isNotBlank(pan))
            stringBuffer.append("&pan="+pan);
        if (StringUtils.isNotBlank(tilt))
            stringBuffer.append("&tilt="+tilt);
        if (StringUtils.isNotBlank(zoom))
            stringBuffer.append("&zoom="+zoom);
        if (StringUtils.isNotBlank(focus))
            stringBuffer.append("&focus="+focus);
        if (StringUtils.isNotBlank(iris))
            stringBuffer.append("&iris="+iris);
        if (StringUtils.isNotBlank(deviceid))
            stringBuffer.append("&cameraid="+deviceid);
        if (StringUtils.isNotBlank(serverid))
            stringBuffer.append("&serverid="+serverid);

        String result="";
        try {
            result=HttpUtils.doGet("http://192.168.50.121/command?command=setRelativePosition&noCache="
                    +DateUtils.dateToStamp(new Date())+stringBuffer.toString(),jsonParam.getString("sessionKey"));
            XMLSerializer xmlSerializer = new XMLSerializer();
            result = xmlSerializer.read(result).toString();
        }catch (ParseException pe){
            logger.error("调用VDG获取setRelativePosition出现异常ParseException：{}",pe.getMessage());
            pe.printStackTrace();
        }catch (Exception e){
            logger.error("调用VDG获取setRelativePosition出现异常Exception：{}",e.getMessage());
            e.printStackTrace();
        }
        logger.info("调用VDG获取setRelativePosition响应：{}",result.replace("@",""));
        return result.replace("@","");
    }
}
