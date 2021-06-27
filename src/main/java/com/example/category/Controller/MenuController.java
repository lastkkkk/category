package com.example.category.Controller;

import com.example.category.Bean.Menu;
import com.example.category.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("/menu")
    @ResponseBody
    public List<Menu> menu(){
        System.out.println("menu");
        System.out.println(menuService.getMenusByCurrentUser());
        return menuService.getMenusByCurrentUser();
    }

}
