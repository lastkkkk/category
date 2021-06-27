package com.example.category.Controller;

import com.example.category.Bean.Result;
import com.example.category.Bean.UserInfo;
import com.example.category.Service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result loginSuccess(@RequestBody UserInfo user, HttpSession session) {
        Result result = new Result();
        String username = user.getUsername();
        String password = user.getPassword();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            result.setMsg("登录成功");
            session.setAttribute("user",user);
            result.setSuccess(true);
            return result;
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            result.setMsg("登录失败");
            return result;
        }
    }

    //登出
    @ResponseBody
    @GetMapping("/logout")
    public Result logout(){
        System.out.println("登出");
        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        result.setMsg("成功登出");
        result.setSuccess(true);
        return result;
    }

    @ResponseBody
    @GetMapping(value = "/authentication")
    public String authentication(){
        return "身份认证成功";
    }

}
