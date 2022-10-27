package com.study.sns.repository;

import com.study.sns.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

}
