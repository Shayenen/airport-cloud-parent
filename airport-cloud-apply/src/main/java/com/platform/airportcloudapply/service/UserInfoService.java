package com.platform.airportcloudapply.service;


import com.platform.airportcloudapply.entity.UserInfo;

public interface UserInfoService {
    /**
     * 根据openID获取用户信息
     * @param openId
     * @return
     */
    UserInfo selectByOpenId(String openId);

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    UserInfo selectById(String id);


    /**
     * 添加用户信息
     * @param user
     * @return
     */
    int insert(UserInfo user);

    public void sendRobbingMsgV2(String mobile);
}
