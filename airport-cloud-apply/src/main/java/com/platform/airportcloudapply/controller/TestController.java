package com.platform.airportcloudapply.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.airportcloudapply.config.RedisUtils;
import com.platform.airportcloudapply.entity.UserInfo;
import com.platform.airportcloudapply.service.InitService;
import com.platform.airportcloudapply.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/homeApi")
public class TestController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private InitService initService;

    @RequestMapping("/bannerHomeList")
    public String getBannerList(){
        String result="";
        System.out.println(redisUtils.get("ttt"));
        redisUtils.set("ttt","1111111");
        System.out.println(redisUtils.get("ttt"));
        redisUtils.expire("ttt",100);
        return result;
    }

   @Autowired
   private UserInfoService userInfoService;
    @RequestMapping("/getUserInfo/{id}")
    public String getBannerList(@PathVariable(value = "id") String id){
        logger.info("用户传进的id：{}，确定ID是：{}",id,id);

        UserInfo userInfo = userInfoService.selectById(id);
        logger.debug("查询的数据：{}",JSONObject.toJSONString(userInfo));

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchange.name"));
        rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));
        rabbitTemplate.convertAndSend(userInfo, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties=message.getMessageProperties();
                messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,UserInfo.class);
                return message;
            }
        });
        test(userInfo);

        return JSONObject.toJSONString(userInfo);
    }

    public void test(UserInfo user){
        try{
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("simple.mq.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("simple.mq.routing.key.name"));

            Message message= MessageBuilder.withBody(objectMapper.writeValueAsBytes(user)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
            rabbitTemplate.convertAndSend(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @RequestMapping("/getUserInfo2/{id}")
    public String getBannerList2(@PathVariable(value = "id") String id) {
        logger.info("用户传进的id：{}，确定ID是：", id, id);

        UserInfo userInfo = userInfoService.selectById(id);
        initService.generateMultiThread();
        return JSONObject.toJSONString(userInfo);
    }
}
