package com.study.sns.fixture;

import com.study.sns.controller.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setUserName(userName);
        entity.setPassword(password);
        return entity;
    }
}
