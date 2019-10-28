package com.platform.airportcloudapply.service.impl;


import com.platform.airportcloudapply.entity.UserInfo;
import com.platform.airportcloudapply.mapper.UserInfoMapper;
import com.platform.airportcloudapply.service.UserInfoService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;




    /**
     * 根据openID获取用户信息
     *
     * @param openId
     * @return
     */
    @Override
    public UserInfo selectByOpenId(String openId) {
        return userInfoMapper.selectByOpenId(openId);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public UserInfo selectById(String id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }



    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public int insert(UserInfo user) {
        return userInfoMapper.insert(user);
    }

    public void sendRobbingMsgV2(String mobile){
        try {
            rabbitTemplate.setExchange(env.getProperty("user.order.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("user.order.routing.key.name"));
            Message message= MessageBuilder.withBody(mobile.getBytes("UTF-8")).setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .build();
            rabbitTemplate.send(message);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("枪弹失败!");
        }
    }


}
