package com.example.category.Mapper;

import com.example.category.Bean.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuMapper extends JpaRepository<Menu,Integer> {
    Menu findById(int id);
    List<Menu> findAllByParentId(int parentId);
}
