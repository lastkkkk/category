package com.example.category.Service;

import com.example.category.Bean.RoleMenu;
import com.example.category.Mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoleMenuService {
    @Autowired
    RoleMenuMapper roleMenuMapper;

    public List<RoleMenu> findAllByRid(int rid){
        return roleMenuMapper.findAllByRid(rid);
    }

    public List<RoleMenu> findAllByRidIn(List<Integer> rids){
        return roleMenuMapper.findAllByRidIn(rids);
    }

    public void save(RoleMenu rm){
        roleMenuMapper.save(rm);
    }

    @Modifying
    @Transactional
    public void updateRoleMenu(int rid, Map<String,List<Integer>> menusIds){
        roleMenuMapper.deleteAllByRid(rid);
        List<RoleMenu> rms = new ArrayList<>();
        for(Integer mid: menusIds.get("menusIds")){
            RoleMenu rm = new RoleMenu();
            rm.setMid(mid);
            rm.setRid(rid);
            rms.add(rm);
        }
        roleMenuMapper.saveAll(rms);
    }
}
