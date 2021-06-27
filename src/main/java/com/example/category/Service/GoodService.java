package com.example.category.Service;

import com.example.category.Bean.GoodsListInfo;
import com.example.category.Mapper.GoodsMapper;
import com.example.category.utils.CastUtils;
import com.example.category.utils.updateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GoodService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    RedisService redisService;
    //获取全部商品信息
    public Page<GoodsListInfo> GetGoodsList(Pageable pageable){
        return goodsMapper.findAll(pageable);
    }

    //模糊查询用户
    public Page<GoodsListInfo> findUser(Specification specification, Pageable pageable){
        return goodsMapper.findAll(specification,pageable);
    }

    //保存商品信息
    public void saveGoods(GoodsListInfo g){
        goodsMapper.save(g);
    }

    //批量保存商品信息
    public void saveManyGoods(List<GoodsListInfo> g){
        goodsMapper.saveAll(g);
    }

    //查找特定的商品信息
    public GoodsListInfo FindGoods(GoodsListInfo g){
        return goodsMapper.findByid(g.getId());
    }

    //删除特定商品信息
    public void DeleteGoods(GoodsListInfo g){
        goodsMapper.deleteById(g.getId());
    }


    //更新商品信息
    public void updateUser(GoodsListInfo g){
        System.out.println(g.getId());
        GoodsListInfo s = goodsMapper.findByid(g.getId());
        System.out.println(s.getGpcnum());
        if(StringUtils.isEmpty(s)){
            goodsMapper.save(g);
        }else{
            updateUtil.copyNullProperties(g,s);
            goodsMapper.save(s);
        }
    }

    //根据商品名称查询商品信息
    public GoodsListInfo FindGoodByName(String goodname){
        return goodsMapper.findBygoodname(goodname);
    }

    //redis查询
    public List<GoodsListInfo> list() {
        List<GoodsListInfo> goods;
        String key = "goodslist";
        Object goodsCache = redisService.get(key);
        if (goodsCache == null) {
            goods = goodsMapper.findAll(Sort.by(Sort.Direction.DESC, "id"));
            redisService.set(key, goods);
        } else {
            goods = CastUtils.objectConvertToList(goodsCache, GoodsListInfo.class);
        }
        return goods;
    }

    public void addOrUpdate(GoodsListInfo goods) {
        redisService.delete("goodslist");
        goodsMapper.save(goods);
    }

    public void deleteById(GoodsListInfo g) {
        redisService.delete("goodslist");
        goodsMapper.deleteById(g.getId());
    }

}
