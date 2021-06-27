package com.example.category.Service;

import com.example.category.Bean.Menu;
import com.example.category.Bean.RoleMenu;
import com.example.category.Bean.UserInfo;
import com.example.category.Bean.UserRole;
import com.example.category.Mapper.MenuMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;

    public List<Menu> getAllByParentId(int parentId){
        return menuMapper.findAllByParentId(parentId);
    }

    public List<Menu> getMenusByCurrentUser(){
        String username = SecurityUtils.getSubject().getPrincipal().toString();
//        String username = "ahsh";
        System.out.println(username);
        UserInfo user = userService.getByUserName(username);

        //得到当前用户的角色id
        List<Integer> rids = userRoleService.listAllByUid(user.getId()).stream().map(UserRole::getRid).collect(Collectors.toList());


        //查询出这些角色对应的所有菜单项
        List<Integer> menuIds = roleMenuService.findAllByRidIn(rids).stream().map(RoleMenu::getMid).collect(Collectors.toList());
        List<Menu> menus = menuMapper.findAllById(menuIds).stream().distinct().collect(Collectors.toList());

        //处理菜单项的结构
        handleMenus(menus);
        return menus;

    }

    public void handleMenus(List<Menu> menus){
        menus.forEach(m -> {
            List<Menu> children = getAllByParentId(m.getId());
            m.setChildren(children);
        });
        menus.removeIf(m -> m.getParentId() != 0);
    }
}
