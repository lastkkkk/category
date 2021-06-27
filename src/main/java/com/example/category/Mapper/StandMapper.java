package com.example.category.Mapper;

import com.example.category.Bean.standListInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface StandMapper extends JpaRepository<standListInfo,String>,JpaSpecificationExecutor<standListInfo> {
    standListInfo findByunnum(String unnum);


    @Query(value = "select * from standlist where id =?1",nativeQuery = true)
    standListInfo findById(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from standlist  where id =?1",nativeQuery = true)
    void deleteById(int id);
}
