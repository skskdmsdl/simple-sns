package com.study.sns.repository;

import com.study.sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.util.Optional;

// redis의 유저를 캐싱하고 그 캐시에서 유저를 가져오는 클래스
// redis 같은 경우 일정 시간이 지나면 expired 되도록 하는 것이 좋음(TTL)
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}:{}", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String userName) {
        String key = getKey(userName);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("Get data from Redis {}:{}", key, user);
        return Optional.ofNullable(user);
    }

    // 가장 많이 사용하는 필터부분(유저 확인을 위해)을 캐시로 대체하려함
    // JwtTokenFilter에 유저 가져오는 부분 userName으로 가져오기에 여기서도 userName으로 설정
    private String getKey(String userName) {

        // redis에 key값을 구성할때는 항상 prefix를 붙여서 사용
        return "USER:" + userName;  // USER:admin admin이라는 userName에 USER정보를 저장한다는걸 표현하기 위함
    }


}
