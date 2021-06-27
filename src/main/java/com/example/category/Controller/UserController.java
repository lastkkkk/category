package com.example.category.Controller;

import com.example.category.Bean.Menu;
import com.example.category.Bean.page;
import com.example.category.Bean.Result;
import com.example.category.Bean.UserInfo;
import com.example.category.Mapper.UserMapper;
import com.example.category.Service.MenuService;
import com.example.category.Service.UserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.criteria.*;
import java.util.List;

@Controller
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    MenuService menuService;


    @GetMapping(value = "/pages")
    @ResponseBody
    public Page<UserInfo> showUsers(page page){
        //模糊查询数据
        if (page.getUsername() != null && !page.getUsername().equals("")) {
            Specification<UserInfo> specification = new Specification<UserInfo>() {
                @Override
                public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path userName = root.get("username");
                    Predicate predicate = criteriaBuilder.like(userName,"%"+page.getUsername()+"%");
                    return predicate;
                }
            };
            PageRequest pageRequest1 = PageRequest.of(page.getPagenum()-1,page.getPagesize());

            Page<UserInfo> page1 = userService.findUser(specification,pageRequest1);
            return page1;
        }
        //直接获取所有数据
        PageRequest pageRequest = PageRequest.of(page.getPagenum()-1,page.getPagesize());
        Page<UserInfo> userPage = userService.findUsers(pageRequest);
        return userPage;
    }


    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    //准确查询信息进行编辑
    public UserInfo Find(@RequestBody UserInfo userInfo){
        String username;
        username = userInfo.getUsername();
        UserInfo user = new UserInfo();
        user = userService.editUser(username);
        return user;
    }

    //保存用户信息
    @RequestMapping(value = "/saveuser",method = RequestMethod.POST)
    @ResponseBody
    public Result saveUser(@RequestBody UserInfo adduser){
        Result result = new Result();
        String username = adduser.getUsername();
        String password = adduser.getPassword();
        username = HtmlUtils.htmlEscape(username);
        adduser.setUsername(username);
        //生成盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //设置hash 算法迭代次数
        int time = 2;
        //得到hash后的密码
        String encodedPassword = new SimpleHash("md5",password,salt,time).toString();
        //存储用户信息，包括salt与hash后的密码
        adduser.setSalt(salt);
        adduser.setPassword(encodedPassword);
//        System.out.println(userService.getByUserName(username));
        if (userService.getByUserName(username)!=null && !userService.getByUserName(username).equals("")){
            result.setMsg("用户已存在");
            System.out.println("cunzai");
            result.setSuccess(false);
            result.setYes(0);
            UserInfo user = userService.getByUserName(username);
            result.setData(user);
            System.out.println(result.isSuccess());
            return result;
        }else{
            userService.saveUser(adduser);
            result.setMsg("用户保存成功");
            result.setSuccess(true);
            result.setYes(0);
            System.out.println("保存成功");
            return result;
        }
    }


    //进行用户信息的更新
    @RequestMapping(value = "/editinfo",method = RequestMethod.POST)
    @ResponseBody
    public void editInfo(@RequestBody UserInfo userInfo){
        userService.updateUser(userInfo);
    }

    //用户信息删除
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteUser(@RequestBody UserInfo u){
        userService.deleteUser(u);
        //System.out.println("删除");
    }

    //用户信息赋予权限
    @RequestMapping(value = "/setrole",method = RequestMethod.POST)
    @ResponseBody
    public void setRole(@RequestBody UserInfo u){
        userService.updateUser(u);
//        System.out.println("设置");
    }


}