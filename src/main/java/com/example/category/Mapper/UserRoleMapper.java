package com.example.category.Mapper;

import com.example.category.Bean.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleMapper extends JpaRepository<UserRole,Integer> {
    List<UserRole> findAllByUid(int uid);
    void deleteAllByUid(int uid);
}
