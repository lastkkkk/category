package com.example.category.Mapper;

import com.example.category.Bean.Uncate;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UnMapper {
    public Uncate getUnById(@Param("id") int id);

    boolean updateUn(Uncate g);

    public Page<Uncate> getAllUnTree();

    boolean deleteUn(@Param("id") int id);

    boolean addUn(Uncate g);

//    public Gpcate getGpcTre(@Param("gpcnum") String gpcnum);

    Page<Uncate> selectUn(@Param("unnum") String unnum);


}
