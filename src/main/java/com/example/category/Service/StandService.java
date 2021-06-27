package com.example.category.Service;

import com.example.category.Bean.standListInfo;
import com.example.category.Mapper.StandMapper;
import com.example.category.utils.MyPage;
import com.example.category.utils.updateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;


@Service
public class StandService {
    @Autowired
    StandMapper standMapper;
    @Autowired
    RedisService redisService;

    //模糊查询
    public Page<standListInfo> findData(Specification specification, Pageable pageable){
        return standMapper.findAll(specification,pageable);
    }

    //获取所有数据
    public Page<standListInfo> GetList(Pageable pageable){
        return standMapper.findAll(pageable);
    }

    //删除数据
    public void delete(standListInfo g) {
        standMapper.deleteById(g.getId());
        // 删除当前选中的文章和所有文章页面的缓存
        redisService.delete("stand:" + g.getId());
        Set<String> keys = redisService.getKeysByPattern("standpage*");
        redisService.delete(keys);
    }
    //保存数据
    public void saveList(standListInfo g){
        standMapper.save(g);
    }

    //查找特定数据
    public standListInfo findStand(String unnum){
        return standMapper.findByunnum(unnum);
    }

    //批量保存数据
    public void saveManyList(List<standListInfo> s){
        standMapper.saveAll(s);
    }

    //更新数据
    public void updateUser(standListInfo g){
        standListInfo s = standMapper.findByunnum(g.getUnnum());
        if(StringUtils.isEmpty(s)){
            standMapper.save(g);
        }else{
            updateUtil.copyNullProperties(g,s);
            standMapper.save(s);
        }
    }

    //Redis取值
    public MyPage list(int page, int size){
        MyPage<standListInfo> stands;
        // 用户访问列表页面时按页缓存文章
        String key = "standpage:" + page;
        Object standPageCache = redisService.get(key);
        if (standPageCache == null) {
//            System.out.println("空的");
            //去数据库中取
            Page<standListInfo> standsInDb = standMapper.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
            stands = new MyPage<>(standsInDb);
            redisService.set(key, stands);
        } else {
//            System.out.println("有的");
            stands = (MyPage<standListInfo>) standPageCache;
        }
        return stands;
    }

    public standListInfo findByid(standListInfo g) {
        standListInfo stands;
        // 用户访问具体商品时缓存单个商品，通过 id 区分
        String key = "stand:" + g.getId();
        Object standCache = redisService.get(key);
        if (standCache == null) {
//            System.out.println("从mapper中");
            stands = standMapper.findById(g.getId());
            redisService.set(key, stands);
        } else {
//            System.out.println("从cache中");
            stands = (standListInfo) standCache;
        }
        return stands;
    }

    //添加数据
    public void addOrUpdate(standListInfo g) {
        standMapper.save(g);
        // 删除当前选中的商品和所有商品的缓存
        redisService.delete("stand:" + g.getId());
        Set<String> keys = redisService.getKeysByPattern("standpage*");
        redisService.delete(keys);
    }


}
