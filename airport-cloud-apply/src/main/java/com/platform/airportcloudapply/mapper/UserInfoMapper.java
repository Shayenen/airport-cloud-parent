package com.platform.airportcloudapply.mapper;

import com.platform.airportcloudapply.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    /**
     * 根据openID获取用户信息
     * @param openId
     * @return
     */
    UserInfo selectByOpenId(@Param("openId") String openId);




}