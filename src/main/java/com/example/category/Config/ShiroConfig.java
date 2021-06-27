//package com.example.category.Config;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.shiro.mgt.DefaultSecurityManager;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.CrossOrigin;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.LinkedHashMap;
//import java.util.Map;
//@CrossOrigin
//@Configuration
//public class ShiroConfig {
//    /**
//     * shiro请求拦截器(解决登录校验跨域问题)
//     */
//    public class AuthenticationFilter extends FormAuthenticationFilter {
//
//        @Override
//        protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//            // 错误异常提示
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            HttpServletRequest httpRequest = (HttpServletRequest) request;
//            setHeader(httpRequest,httpResponse);
//            httpResponse.setCharacterEncoding("UTF-8");
//            httpResponse.setContentType("application/json");
////            httpResponse.getWriter().write(JSONObject.toJSONString(new RestResponceBody(ResponceStatus.USER_NOT_LOGIN, "请先登录!")));
//            return false;
//        }
//
//        /**
//         * 为response设置header，实现跨域
//         */
//        private void setHeader(HttpServletRequest request,HttpServletResponse response){
//            //跨域的header设置
//            response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
//            response.setHeader("Access-Control-Allow-Methods", request.getMethod());
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//            //防止乱码，适用于传输JSON数据
//            //Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild
//            response.setHeader("Content-Type","application/json;charset=UTF-8");
//            response.setStatus(HttpStatus.OK.value());
//        }
//
//        @Override
//        protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//            if (request instanceof HttpServletRequest) {
//                if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
//                    return true;
//                }
//            }
//            return super.isAccessAllowed(request, response, mappedValue);
//        }
//    }
//    @Bean
//    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        // 必须设置 SecurityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
//        shiroFilterFactoryBean.setLoginUrl("/notLogin");
//        // 设置无权限时跳转的 url;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
//
//        // 设置拦截器
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        //游客，开发权限
//        filterChainDefinitionMap.put("/guest/**", "anon");
//        //用户，需要角色权限 “user”
//        filterChainDefinitionMap.put("/user/**", "roles[user]");
//        //管理员，需要角色权限 “admin”
//        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
//        //开放登陆接口
//        filterChainDefinitionMap.put("/login", "anon");
//        //其余接口一律拦截
//        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
//        filterChainDefinitionMap.put("/**", "authc");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        System.out.println("Shiro拦截器工厂类注入成功");
//        return shiroFilterFactoryBean;
//    }
//    /**
//     * 注入 securityManager
//     */
//    @Bean
//    public SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        // 设置realm.
//        securityManager.setRealm(customRealm());
//        return securityManager;
//    }
//
//    /**
//     * 自定义身份认证 realm;
//     * <p>
//     * 必须写这个类，并加上 @Bean 注解，目的是注入 CustomRealm，
//     * 否则会影响 CustomRealm类 中其他类的依赖注入
//     */
//    @Bean
//    public CustomRealm customRealm() {
//        return new CustomRealm();
//    }
//
//}
