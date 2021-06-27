package com.example.category.Mapper;

import com.example.category.Bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMapper extends JpaRepository<Role,Integer> {
    Role findById(int id);
}
