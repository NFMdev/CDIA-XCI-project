package com.github.NFMdev.cdia.ingestion_service.repository;

import com.github.NFMdev.cdia.ingestion_service.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
