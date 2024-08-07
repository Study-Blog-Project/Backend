package com.study.studyproject.global.config.redis;

import jakarta.transaction.Transactional;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisUtilsTest {

    final String KEY = "key";
    final String VALUE = "value";
    final Duration DURATION = Duration.ofMillis(5000);

    @Autowired
    private RedisUtils redisUtils;

    @BeforeEach
    void shutDown() {
        redisUtils.setValues(KEY, VALUE, DURATION);
    }

    @AfterEach
    void tearDown() {
        redisUtils.deleteValues(KEY);
    }


    @Test
    @DisplayName("Redis에 데이터를 저장하면 조회된다")
    void saveAndFindTest() throws Exception {
        //given
        String findValue = redisUtils.getValues(KEY);

        //then
        assertThat(VALUE).isEqualTo(findValue);


    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 수정한다.")
    void updateTest() throws Exception {
        //given
        String updateValue = "updateValue";
        redisUtils.setValues(KEY, updateValue, DURATION);

        //when
        String findValue = redisUtils.getValues(KEY);

        //then
        assertThat(updateValue).isEqualTo(findValue);
        assertThat(VALUE).isNotEqualTo(findValue);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 삭제한다.")
    void deleteTest() throws Exception {
        //given
        redisUtils.deleteValues(KEY);
        String findValue = redisUtils.getValues(KEY);

        //then
        assertThat(findValue).isEqualTo("false");
    }

    @Test
    @DisplayName("Redis에 저장된 데이터는 만료시간이 지나면 삭제된다.")
    void expiredTest() throws Exception {
        String findValue = redisUtils.getValues(KEY);
        Awaitility.await().pollDelay(Duration.ofMillis(6000)).untilAsserted(
                () -> {
                    String expiredValue = redisUtils.getValues(KEY);
                    assertThat(expiredValue).isNotEqualTo(findValue);
                    assertThat(expiredValue).isEqualTo("false");
                }
        );

    }






}