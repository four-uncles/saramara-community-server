package com.kakao.saramaracommunity.common.dto;

public interface ConvertDtoByBusinessLayer<T> {
    T toServiceRequest();
}
