package com.example.category.Mapper;

import com.example.category.Bean.UserInfo;

import org.apache.ibatis.annotations.Mapper;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


@Mapper
public interface UserMapper extends JpaRepository<UserInfo,String>,JpaSpecificationExecutor<UserInfo> {

    UserInfo findByUsernameAndPassword(String username,String password);

    UserInfo findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "delete from userinfo  where id =?1",nativeQuery = true)
    void deleteById(int id);

    UserInfo getByUsername(String username);

}
