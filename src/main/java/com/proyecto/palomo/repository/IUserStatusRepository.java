package com.proyecto.palomo.repository;

import com.proyecto.palomo.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserStatusRepository extends JpaRepository<UserStatus, Long> {
}
