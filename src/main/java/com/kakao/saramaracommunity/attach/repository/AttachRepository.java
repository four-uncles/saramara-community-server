package com.kakao.saramaracommunity.attach.repository;

import com.kakao.saramaracommunity.attach.entity.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    List<Attach> findAllByIds(Long id);
}
