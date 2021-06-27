package com.example.category.Service;

import com.example.category.Bean.Uncate;
import com.example.category.Mapper.UnMapper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnService {
    @Autowired
    private UnMapper unMapper;
    public boolean update(Uncate g){
        return unMapper.updateUn(g);
    }

    public List<Uncate> queryAll(){
        return unMapper.getAllUnTree();
    }

    //根据id查找gpc数据
    public Uncate getUnByid(int id){
        return unMapper.getUnById(id);
    }

    //根据id删除gpc数据
    public boolean deleteUnByid(int id){
        return unMapper.deleteUn(id);
    }

    //添加数据
    public boolean addUn(Uncate g){
        return unMapper.addUn(g);
    }

//    //查询gpc数据
//    public Gpcate getGpcTree(String gpcnum){
//        return gpcMapper.getGpcTre(gpcnum);
//    }

    //模糊查询
    public Page<Uncate> selectUn(String unnum){
        return unMapper.selectUn(unnum);
    }
}
