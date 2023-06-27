package com.kakao.saramaracommunity.config;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    /**
     * Object Converter (객체 변환)을 위한 ModelMapper 설정 Bean 메서드입니다.
     * Mapper 객체에 설정 정보가 필요하므로 new 연산을 이용해 mapper 객체 생성 후, 설정 정보를 담는 식으로 진행했습니다.
     *
     * setFieldMatchingEnabled() : 필드 매칭 활성화 메서드입니다. 필드 네이밍을 통해 converter 여부를 판단합니다.
     * setFieldAccessLevel() : 필드 접근 수준 설정 메서드입니다. 현 프로젝트 개발 기준 private 필드 변수 접근을 위해 메서드 파라미터를 PRVIATE도 접근 가능하게 설정했습니다.
     * setMethodAccessLevel() : 메서드 접근 수준 설정 메서드입니다. 동작 원리는 위와 동일합니다.
     * setMatchingStrategy() : 필드 이름을 확인하고 매핑을 해주는 설정 메서드입니다. 현재는 LOOSE이기에 유사하다면 매핑을 해줍니다.
     */
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMethodAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }
}