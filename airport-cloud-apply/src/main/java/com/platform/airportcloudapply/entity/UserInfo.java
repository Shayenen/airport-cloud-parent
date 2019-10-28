package com.platform.airportcloudapply.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private String id;

    private String userOpenid;

    private String userHead;

    private String userName;

    private String userGender;

    private String userCountry;

    private String userCity;

    private String userProvince;

    private Date userNewLogin;

    private Double amount;

    private Double giveAmount;

    private Double todayIncome;

    private Double totalIncome;

    private Double incomeBalance;

    private Integer isBoss;

    private String sourceUserId;

    private Integer firstPay;

    private Integer firstCardPay;
}