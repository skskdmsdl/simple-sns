package com.study.sns.service;

import com.study.sns.controller.model.User;
import com.study.sns.controller.model.entity.UserEntity;
import com.study.sns.exception.SnsApplicationException;
import com.study.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    // TODO : implement
    public User join(String userName, String password) {
        // 회원가입하려는 userName으로 회원가입한 user가 있는지
        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);

        // 회원가입 진행 = user를 등록
        userEntityRepository.save(new UserEntity());

        return new User();
    }

    //TODO : implement
    public String login(String userName, String password) {
        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException());

        // 비밀번호 체크
        if(userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }

        // 토큰 생성

        return "";
    }
}
