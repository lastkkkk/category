package com.example.category.Service;

import com.example.category.Bean.Gpcate;
import com.example.category.Mapper.GpcMapper;
import com.example.category.utils.MyPage;
import com.example.category.utils.MyPageInfo;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GpcService {
    @Autowired
    private GpcMapper gpcMapper;
    @Autowired
    private RedisService redisService;

    //Redis取值
//    public MyPageInfo list(int page, int size){
//        MyPageInfo<Gpcate> gpcates;
//        // 用户访问列表页面时按页缓存文章
//        String key = "gpcatepage:" + page;
//        Object gpcatePageCache = redisService.get(key);
//        if (gpcatePageCache == null) {
////            System.out.println("空的");
//            //去数据库中取
//            Page<Gpcate> gpcateInDb = gpcMapper.getAllGpcTree();
//            gpcates = new MyPageInfo<>(gpcateInDb);
//            redisService.set(key, gpcateInDb);
//        } else {
////            System.out.println("有的");
//            gpcates = (MyPageInfo<Gpcate>) gpcatePageCache;
//        }
//        return gpcates;
//    }
    public boolean update(Gpcate g){
        return gpcMapper.updateGpc(g);
    }

    public List<Gpcate> queryAll(){
        return gpcMapper.getAllGpcTree();
    }

    //根据id查找gpc数据
    public Gpcate getGpcByid(int id){
        return gpcMapper.getGpcById(id);
    }

    //根据id删除gpc数据
    public boolean deleteGpcByid(int id){
        return gpcMapper.deleteGpc(id);
    }

    //添加数据
    public boolean addGpc(Gpcate g){
        return gpcMapper.addGpc(g);
    }

//    //查询gpc数据
//    public Gpcate getGpcTree(String gpcnum){
//        return gpcMapper.getGpcTre(gpcnum);
//    }

    //模糊查询
    public Page<Gpcate> selectGpc(String gpcnum){
        return gpcMapper.selectGpc(gpcnum);
    }

}
