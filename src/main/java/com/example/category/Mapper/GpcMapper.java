package com.example.category.Mapper;

import com.example.category.Bean.Gpcate;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GpcMapper {
    public Gpcate getGpcById(@Param("id") int id);

    boolean updateGpc(Gpcate g);

    public Page<Gpcate> getAllGpcTree();

    boolean deleteGpc(@Param("id") int id);

    boolean addGpc(Gpcate g);

//    public Gpcate getGpcTre(@Param("gpcnum") String gpcnum);

    Page<Gpcate> selectGpc(@Param("gpcnum") String gpcnum);


}
