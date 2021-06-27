package com.example.category.Mapper;

import com.example.category.Bean.GoodsListInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Mapper
public interface GoodsMapper extends JpaRepository<GoodsListInfo,String>,JpaSpecificationExecutor<GoodsListInfo> {

    GoodsListInfo findByid(String id);

    GoodsListInfo findBygoodname(String goodname);
}
