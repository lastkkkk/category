package com.example.category.Controller;

import com.example.category.Bean.Result;
import com.example.category.Bean.page;
import com.example.category.Bean.standListInfo;
import com.example.category.Service.StandService;
import com.example.category.utils.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import java.util.List;

@Controller
@CrossOrigin
public class StandController {

        @Autowired
        private StandService standService;

        //获取数据
        @RequestMapping(value = "/stlist",method = RequestMethod.GET)
        @ResponseBody
        public MyPage<standListInfo> showUsers(page page){
            //根据unspsc分类号模糊查询数据
            if (page.getUnnum() != null && !page.getUnnum().equals("")) {
                Specification<standListInfo> specification = new Specification<standListInfo>() {
                    @Override
                    public Predicate toPredicate(Root<standListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        Path userName = root.get("unnum");
                        Predicate predicate = criteriaBuilder.like(userName,"%"+page.getUnnum()+"%");
                        return predicate;
                    }
                };
                PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
                Page<standListInfo> page1 = standService.findData(specification,pageRequest1);
                MyPage<standListInfo> page2 = new MyPage<>(page1);
                return  page2;
//                return page1;
            }
            //根据gpc分类号查询
            else if (page.getGpcnum() != null && !page.getGpcnum().equals("")) {
                Specification<standListInfo> specification = new Specification<standListInfo>() {
                    @Override
                    public Predicate toPredicate(Root<standListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        Path userName = root.get("gpcnum");
                        Predicate predicate = criteriaBuilder.like(userName,"%"+page.getGpcnum()+"%");
                        return predicate;
                    }
                };
                PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
                Page<standListInfo> page1 = standService.findData(specification,pageRequest1);
                MyPage<standListInfo> page2 = new MyPage<>(page1);
                return  page2;
//                return page1;
            }else if(page.getActiveName().equals("0")){
                //获取全部层数据
//                PageRequest pageRequest = PageRequest.of(page.getPagenum()-1,page.getPagesize());
//                Page<standListInfo> goodsPage = standService.GetList(pageRequest);
//                return goodsPage;
                return standService.list(page.getPagenum()-1,page.getPagesize());
            }
            //获取对应层的数据
            Specification<standListInfo> specification = new Specification<standListInfo>() {
                @Override
                public Predicate toPredicate(Root<standListInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path userName = root.get("gpclevel");
                    Predicate predicate = criteriaBuilder.equal(userName,page.getActiveName());
                    return predicate;
                }
            };
            PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());
            Page<standListInfo> page1 = standService.findData(specification,pageRequest1);
//            return page1;
            MyPage<standListInfo> page2 = new MyPage<>(page1);
            return page2;

        }


        //删除分类信息
        @RequestMapping(value = "/deletelist",method = RequestMethod.POST)
        @ResponseBody
        public void deleteList(@RequestBody standListInfo g){
//            System.out.println(g.getId());
            standService.delete(g);
        }

        //保存分类信息
        @RequestMapping(value = "/savelist",method = RequestMethod.POST)
        @ResponseBody
        public Result saveUser(@RequestBody standListInfo g){
            Result result = new Result();
            standService.addOrUpdate(g);
            result.setMsg("保存成功");
            return result;
        }

        //批量保存分类信息
        @RequestMapping(value = "/savemanylist",method = RequestMethod.POST)
        @ResponseBody
        public Result saveManyList(@RequestBody List<standListInfo> s){
            Result result = new Result();
            try {
                standService.saveManyList(s);
                result.setSuccess(true);
                result.setMsg("保存成功!");
            }catch (Exception e){
                result.setSuccess(false);
                result.setMsg("保存失败");
            }
            return result;
        }

        //编辑
        @RequestMapping(value = "/editlist",method = RequestMethod.POST)
        @ResponseBody
        public standListInfo Find(@RequestBody standListInfo g){
            return standService.findByid(g);
        }

        //更新
        @RequestMapping(value = "/updatelist",method = RequestMethod.POST)
        @ResponseBody
        public void edit(@RequestBody standListInfo g){
//            standService.updateUser(g);
            standService.addOrUpdate(g);
        }



}
