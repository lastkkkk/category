package com.example.category.Controller;


import com.example.category.Bean.Gpcate;
import com.example.category.Bean.page;
import com.example.category.Mapper.GpcMapper;
import com.example.category.Service.GpcService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/gpc")
public class GpcController {

    @Autowired
    private GpcMapper gpcMapper;
    @Autowired
    private GpcService gpcService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询所有gpc
     * @return
     */
    //查询得到所有数据
    @RequestMapping("/queryGpcList")
    public List<Gpcate> queryAllSon(){
        List<Gpcate> list = gpcService.queryAll();
        return list;
    }

    //分页查询
    @RequestMapping("/getGpcList")
    @ResponseBody
    public PageInfo<Gpcate> getGpc(page page) throws JsonProcessingException {
        PageHelper.startPage(page.getPagenum(),page.getPagesize());
        //查询数据
        if (page.getGpcnum() != null && !page.getGpcnum().equals("")){
            Page<Gpcate> gpclist1 = gpcService.selectGpc(page.getGpcnum());
            System.out.println(gpclist1);
            PageInfo<Gpcate> pageInfo = new PageInfo<Gpcate>(gpclist1);
            pageInfo.setList(gpclist1);
            return pageInfo;
        }
        //获取所有数据
        Page<Gpcate> gpclist = gpcMapper.getAllGpcTree();
        PageInfo<Gpcate> pageInfo = new PageInfo<Gpcate>(gpclist);
        pageInfo.setList(gpclist);
        return pageInfo;
    }

    //通过id找数据
    @RequestMapping(value = "/findgpc/{id}", method = RequestMethod.GET)
    public Gpcate getGpcById(@PathVariable("id") int id){
        Gpcate gpc = gpcService.getGpcByid(id);
        return gpc;
    }

    //更新信息
    @ResponseBody
    @RequestMapping(value = "/updategpc", method = RequestMethod.POST)
    public boolean updateGpc(@RequestBody Gpcate g){
       // System.out.println("进入");
        return gpcService.update(g);
    }

    //删除信息
    @RequestMapping(value = "/deletegpc/{id}", method = RequestMethod.GET)
    public boolean deleteGpc(@PathVariable("id") int id){
       return gpcService.deleteGpcByid(id);
    }

    //添加分类
    @ResponseBody
    @RequestMapping(value = "/addgpc", method = RequestMethod.POST)
    public boolean addGpc(@RequestBody Gpcate g){
        return gpcService.addGpc(g);
    }

//    //查询数据
//    @RequestMapping(value = "/getgpctree/{gpcnum}", method = RequestMethod.GET)
//    public Gpcate deleteGpc(@PathVariable("gpcnum") String gpcnum){
//        return gpcService.getGpcTree(gpcnum);
//    }
}
