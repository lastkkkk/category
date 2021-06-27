package com.example.category.Service;

import com.example.category.Bean.GoodsListInfo;
import com.example.category.Mapper.GoodsMapper;
import com.example.category.utils.MyPage;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GoodsRService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    RedisService redisService;

    public MyPage list(int page,int size){
        MyPage<GoodsListInfo> goods;
        // 用户访问列表页面时按页缓存文章
        String key = "goodpage:" + page;
        Object goodPageCache = redisService.get(key);
        if (goodPageCache == null) {
            System.out.println("空的");
            //去数据库中取
            Page<GoodsListInfo> goodsInDb = goodsMapper.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
            goods = new MyPage<>(goodsInDb);
            redisService.set(key, goods);
        } else {
            System.out.println("有的");
            goods = (MyPage<GoodsListInfo>) goodPageCache;
        }
        return goods;
    }

    public GoodsListInfo findById(GoodsListInfo g) {
        GoodsListInfo good;
        // 用户访问具体商品时缓存单个商品，通过 id 区分
        String key = "good:" + g.getId();
        Object goodCache = redisService.get(key);
        if (goodCache == null) {
            good = goodsMapper.findByid(g.getId());
            redisService.set(key, good);
        } else {
            good = (GoodsListInfo) goodCache;
        }
        return good;
    }

    public void addOrUpdate(GoodsListInfo g) {
        goodsMapper.save(g);
        // 删除当前选中的商品和所有商品的缓存
        redisService.delete("good:" + g.getId());
        Set<String> keys = redisService.getKeysByPattern("goodpage*");
        redisService.delete(keys);
    }

    public void delete(GoodsListInfo g) {
         goodsMapper.deleteById(g.getId());
        // 删除当前选中的文章和所有文章页面的缓存
       redisService.delete("good:" + g.getId());
        Set<String> keys = redisService.getKeysByPattern("goodpage*");
        redisService.delete(keys);
    }
}
