package com.example.category.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.category.Bean.GoodsListInfo;

import com.example.category.Bean.Result;
import com.example.category.Bean.page;
import com.example.category.Service.GoodService;
import com.example.category.Service.GoodsRService;
import com.example.category.utils.MyPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.List;

@Controller
@CrossOrigin
public class GoodsController {
    @Autowired
    GoodService goodService;
    @Autowired
    GoodsRService goodsRService;

    //获取商品信息
    @RequestMapping(value = "/goods",method = RequestMethod.GET)
    @ResponseBody
    public MyPage<GoodsListInfo> list(page page){
        if (page.getGoodname() != null && !page.getGoodname().equals("")) {
            System.out.println("商品名称");
            Specification<GoodsListInfo> specification = new Specification<GoodsListInfo>() {
                @Override
                public Predicate toPredicate(Root<GoodsListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path userName = root.get("goodname");
                    Predicate predicate = criteriaBuilder.like(userName,"%"+page.getGoodname()+"%");
                    return predicate;
                }
            };
            PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
            Page<GoodsListInfo> page1 = goodService.findUser(specification,pageRequest1);
            MyPage<GoodsListInfo> page2 = new MyPage<>(page1);
            return page2;
        }else if (page.getGpcnum() != null && !page.getGpcnum().equals("")) {
            System.out.println("gpcnum");
            Specification<GoodsListInfo> specification = new Specification<GoodsListInfo>() {
                @Override
                public Predicate toPredicate(Root<GoodsListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path userName = root.get("gpcnum");
                    Predicate predicate = criteriaBuilder.like(userName,"%"+page.getGpcnum()+"%");
                    return predicate;
                }
            };
            PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
            Page<GoodsListInfo> page1 = goodService.findUser(specification,pageRequest1);
            MyPage<GoodsListInfo> page2 = new MyPage<>(page1);
            return page2;
        }else if (page.getUnnum() != null && !page.getUnnum().equals("")) {
            System.out.println("unspsc");
            Specification<GoodsListInfo> specification = new Specification<GoodsListInfo>() {
                @Override
                public Predicate toPredicate(Root<GoodsListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path userName = root.get("unnum");
                    Predicate predicate = criteriaBuilder.like(userName,"%"+page.getUnnum()+"%");
                    return predicate;
                }
            };
            PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
            Page<GoodsListInfo> page1 = goodService.findUser(specification,pageRequest1);
            MyPage<GoodsListInfo> page2 = new MyPage<>(page1);
//            MyPage<GoodsListInfo> page2 = new MyPage<GoodsListInfo>();
//            BeanUtils.copyProperties(page1,page2);
            return page2;
        }else if (page.getId() != null && !page.getId().equals("")) {
            System.out.println("id");
            Specification<GoodsListInfo> specification = new Specification<GoodsListInfo>() {
                @Override
                public Predicate toPredicate(Root<GoodsListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path userName = root.get("id");
                    Predicate predicate = criteriaBuilder.like(userName,"%"+page.getId()+"%");
                    return predicate;
                }
            };
            PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
            Page<GoodsListInfo> page1 = goodService.findUser(specification,pageRequest1);
            MyPage<GoodsListInfo> page2 = new MyPage<>(page1);
            return page2;
        }
        //获取所有数据
        return goodsRService.list(page.getPagenum()-1,page.getPagesize());
    }


    //查询商品信息
    @RequestMapping(value = "/goods1/{goodname}",method = RequestMethod.GET)
    @ResponseBody
    public GoodsListInfo getLists1(@PathVariable("goodname") String goodname){
        return goodService.FindGoodByName(goodname);
    }

    //手动保存商品信息
    @RequestMapping(value = "/savegoods",method = RequestMethod.POST)
    @ResponseBody
    public Result saveGoods(@RequestBody GoodsListInfo g){
        System.out.println(g.getGoodname());
        Result result = new Result();
//        if(goodService.FindGoods(g)!= null && !goodService.FindGoods(g).equals("")){
//            result.setSuccess(false);
//            result.setMsg("商品已存在!");
//            GoodsListInfo g1 = goodService.FindGoods(g);
//            result.setData(g1);
//        }else {
//            goodService.saveGoods(g);
//            result.setSuccess(true);
//            result.setMsg("保存成功!");
//        }
        if (goodsRService.findById(g)!=null && !goodsRService.findById(g).equals("")){
            result.setSuccess(false);
            result.setMsg("商品已存在!");
            GoodsListInfo g1 = goodsRService.findById(g);
            result.setData(g1);
        }else {
            goodsRService.addOrUpdate(g);
            result.setSuccess(true);
            result.setMsg("保存成功!");
        }
        return result;
    }

    //批量保存商品信息
    @RequestMapping(value = "/savemanygoods",method = RequestMethod.POST)
    @ResponseBody
    public Result saveManyGoods(@RequestBody List<GoodsListInfo> g){
        Result result = new Result();
//        System.out.println(g.get(0).getGoodname());
        try {
            goodService.saveManyGoods(g);
            result.setSuccess(true);
            result.setMsg("保存成功!");
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg("保存失败");
        }
        return result;
    }

    @Resource
    private RestTemplate restTemplate;

    public static final String PREDICT_URL = "http://localhost:5000";

    //自动保存商品信息
    @RequestMapping(value = "/saveauto",method = RequestMethod.POST)
    @ResponseBody
    public Result saveGoodsAuto(@RequestBody List<GoodsListInfo> g){
        Result result1 = new Result();
            List<String> list = new ArrayList<String>();
            for (int i=0;i<g.size();i++){
                list.add(g.get(i).getGoodname());
            }
            Map<String, List<String>> sentenceMap = new HashMap<>();
            sentenceMap.put("sentence", list);
            String result = restTemplate.postForEntity(PREDICT_URL+"/predict",sentenceMap,String.class).getBody();
            JSONObject jsonObject = JSONObject.parseObject(result);
            System.out.println(result);
            // 取出GPC
            JSONObject gpcJson = (JSONObject) jsonObject.get("gpc");
            // 取出UNSPSC
            JSONObject unJson = (JSONObject) jsonObject.get("unspsc");
            //单个查询时只返回一个对象
            if(g.size() == 1){
//                System.out.println("单个");
                // 获取编号（gpc编码和描述）
                List<String> gpcList = (List)gpcJson.get("0");
                // 获取编号（gpc编码和描述）
                List<String> unList = (List)unJson.get("0");
                GoodsListInfo g1 = new GoodsListInfo();
                g1.setGpcnum(gpcList.get(0));
                g1.setGpcname(gpcList.get(1));
                g1.setUnnum(unList.get(0));
                g1.setUnname(unList.get(1));
                result1.setData(g1);
                result1.setSuccess(true);
                return result1;
            }
            //定义一个商品集合
            List<GoodsListInfo> goodslist = new ArrayList<GoodsListInfo>();
            // 获取编号（gpc编码和描述）
            for(int j=0;j<g.size();j++){
                String s = String.valueOf(j);
                List<String> gpcList = (List)gpcJson.get(s);
                List<String> unList = (List)unJson.get(s);
                GoodsListInfo g2 = new GoodsListInfo();
                g2.setGoodname(g.get(j).getGoodname());
                g2.setId(g.get(j).getId());
                g2.setBrand(g.get(j).getBrand());
                g2.setGpcnum(gpcList.get(0));
                g2.setGpcname(gpcList.get(1));
                g2.setUnnum(unList.get(0));
                g2.setUnname(unList.get(1));
                goodslist.add(g2);
            }
            result1.setData(goodslist);
            result1.setSuccess(true);
//        }
        return result1;
    }


    //查找到商品信息
    @RequestMapping(value = "/findgoods",method = RequestMethod.POST)
    @ResponseBody
    public GoodsListInfo FindGoods(@RequestBody GoodsListInfo g){
        GoodsListInfo good;
        good = goodsRService.findById(g);
        return good;
    }

    //更新商品信息
    @RequestMapping(value = "/updategood",method = RequestMethod.POST)
    @ResponseBody
    public void edit(@RequestBody GoodsListInfo g){
        //System.out.println(g.getGoodname());
        //System.out.println(g.getGpcnum());
        goodService.updateUser(g);
    }

    //删除商品
    @RequestMapping(value = "/deletegood",method = RequestMethod.POST)
    @ResponseBody
    public void deleteGood(@RequestBody GoodsListInfo g){
//        System.out.println(g);
//        goodService.DeleteGoods(g);
        goodsRService.delete(g);
    }
}
