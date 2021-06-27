package com.example.category.Service;

import com.example.category.Bean.UserInfo;
import com.example.category.Mapper.UserMapper;
import com.example.category.utils.updateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



import java.util.List;

@Service
public class UserService{

    @Autowired
    UserMapper userMapper;

    //登录
    public boolean Find(String username,String password){
        boolean b = false;
        //调用mapper
        List<UserInfo> All = userMapper.findAll();
        //System.out.println("======="+All);
        //对表进行循环判断
        for(int i=0;i<All.size();i++){
            UserInfo one = All.get(i);
//            System.out.println(one.getRole());
            if(username.equals(one.getUsername())&&password.equals(one.getPassword())){
                b = true;
                break;
            }
        }
        return b;
    }

    //保存用户
    public void saveUser(UserInfo u){
        userMapper.save(u);
    }

    //模糊查询用户
    public Page<UserInfo> findUser(Specification specification, Pageable pageable){
        return userMapper.findAll(specification,pageable);
    }

    //编辑时获得特定用户的信息
    public UserInfo editUser(String username){
        return userMapper.findByUsername(username);
    }

    //保存编辑信息
    public void updateUser(UserInfo u){
        UserInfo user = userMapper.findByUsername(u.getUsername());
        if(StringUtils.isEmpty(user)){
            userMapper.save(u);
        }else{
            updateUtil.copyNullProperties(u,user);
            userMapper.save(user);
        }
    }

    //删除用户
    public void deleteUser(UserInfo u){
        userMapper.deleteById(u.getId());
    }

    //查询分页返回
    public Page<UserInfo> findUsers(Pageable pageable){
        return userMapper.findAll(pageable);
    }

    //由用户名得到该用户信息
    public UserInfo getByUserName(String username){
        return userMapper.getByUsername(username);
    }


}
