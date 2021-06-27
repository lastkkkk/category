package com.example.category.Controller;


import com.example.category.Bean.Uncate;
import com.example.category.Bean.page;
import com.example.category.Mapper.UnMapper;
import com.example.category.Service.UnService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/un")
public class UnController {

    @Autowired
    private UnMapper unMapper;
    @Autowired
    private UnService unService;

    /**
     * 查询所有gpc
     * @return
     */
    //查询得到所有数据
    @RequestMapping("/queryUnList")
    public List<Uncate> queryAllSon(){
        List<Uncate> list = unService.queryAll();
        return list;
    }

    //分页查询
    @RequestMapping("/getUnList")
    @ResponseBody
    public PageInfo<Uncate> getGpc(page page){
        PageHelper.startPage(page.getPagenum(),page.getPagesize());
        //查询数据
        if (page.getUnnum() != null && !page.getUnnum().equals("")){
            Page<Uncate> gpclist1 = unService.selectUn(page.getUnnum());
            System.out.println(gpclist1);
            PageInfo<Uncate> pageInfo = new PageInfo<Uncate>(gpclist1);
            pageInfo.setList(gpclist1);
            return pageInfo;
        }
        //获取所有数据
        Page<Uncate> gpclist = unMapper.getAllUnTree();
        PageInfo<Uncate> pageInfo = new PageInfo<Uncate>(gpclist);
        pageInfo.setList(gpclist);
        return pageInfo;
    }

    //通过id找数据
    @RequestMapping(value = "/findun/{id}", method = RequestMethod.GET)
    public Uncate getGpcById(@PathVariable("id") int id){
        Uncate gpc = unService.getUnByid(id);
        return gpc;
    }

    //更新信息
    @ResponseBody
    @RequestMapping(value = "/updateun", method = RequestMethod.POST)
    public boolean updateGpc(@RequestBody Uncate g){
       // System.out.println("进入");
        return unService.update(g);
    }

    //删除信息
    @RequestMapping(value = "/deleteun/{id}", method = RequestMethod.GET)
    public boolean deleteGpc(@PathVariable("id") int id){
       return unService.deleteUnByid(id);
    }

    //添加分类
    @ResponseBody
    @RequestMapping(value = "/addun", method = RequestMethod.POST)
    public boolean addGpc(@RequestBody Uncate g){
        return unService.addUn(g);
    }

//    //查询数据
//    @RequestMapping(value = "/getgpctree/{gpcnum}", method = RequestMethod.GET)
//    public Gpcate deleteGpc(@PathVariable("gpcnum") String gpcnum){
//        return gpcService.getGpcTree(gpcnum);
//    }
}
