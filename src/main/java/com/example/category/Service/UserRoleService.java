package com.example.category.Service;

import com.example.category.Bean.Role;
import com.example.category.Bean.UserRole;
import com.example.category.Mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    UserRoleMapper userRoleMapper;

    public List<UserRole> listAllByUid(int uid){
//        System.out.println(uid);
        return userRoleMapper.findAllByUid(uid);
    }

    @Transactional
    public void saveRoleChanges(int uid,List<Role> roles){
        userRoleMapper.deleteAllByUid(uid);
        List<UserRole> urs = new ArrayList<>();
        roles.forEach(r ->{
            UserRole ur = new UserRole();
            ur.setUid(uid);
            ur.setRid(r.getId());
            urs.add(ur);
        });
        userRoleMapper.saveAll(urs);
    }


}
