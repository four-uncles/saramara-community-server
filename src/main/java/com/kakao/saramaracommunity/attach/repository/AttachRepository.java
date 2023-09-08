package com.kakao.saramaracommunity.attach.repository;

import com.kakao.saramaracommunity.attach.entity.Attach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachRepository extends JpaRepository<Attach, Long> {

    /**
     * select * from Attach
     * where ids = id;
     */
    List<Attach> findAllByIds(Long id);
}
