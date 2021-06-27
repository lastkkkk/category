package com.example.category.Mapper;

import com.example.category.Bean.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleMenuMapper extends JpaRepository<RoleMenu,Integer> {
    List<RoleMenu> findAllByRid(int rid);
    List<RoleMenu> findAllByRidIn(List<Integer> rids);
    void deleteAllByRid(int rid);
}
