<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <span>商品分类管理系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ?'64px' : '200px'">
        <div class="toggle-button" @click="toggleCollaps">|||</div>
        <!-- 侧边栏菜单区域 -->
        <el-menu
          background-color="#383d48"
          text-color="#fff"
          active-text-color="#409eff"
          :unique-opened="true"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          :default-active="activePath"
        >
          <!-- 一级菜单 -->
          <el-submenu :index="item.id + ''" :key="item.id" v-for="item in menulist">
            <!-- 一级菜单的模板区域 -->
            <template slot="title">
              <!-- 图标 -->
              <i style="font-size: 16px" :class="iconsObj[item.id]"></i>
              <!-- 文本 -->
              <span>{{item.name}}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item :index="'/'+subItem.path" :key="subItem.id" v-for="subItem in item.children" @click="saveNavState('/'+subItem.path)">
              <template slot="title">
                <!-- 图标 -->
                <i class="el-icon-menu"></i>
                <!-- 文本 -->
                <span>{{subItem.name}}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 右侧内容主体 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
  export default {
    data() {
      return {
        //左侧菜单数据
        menulist:[
          {name:'用户信息管理',
           id:'125',
           children:[
             {name:'用户列表',
               id:'110',
               path:'users'}
           ]
          },
          {name:'商品分类管理',
            id:'101',
            children:[
              {name:'商品列表',
                id:'110',
                path:'goodslist'},
              {name:'商品分类',
                id:'110',
                path:'goodscate'}
            ]
          },
          {name:'分类标准信息管理',
            id:'103',
            children:[
              {name:'信息列表',
                id:'110',
                path:'gpclist'}
            ]
          }
        ],
        iconsObj:{
          "125":'iconfont icon-user',
          "103":'iconfont icon-qukuai',
          "101":'iconfont icon-cangpeitubiao_shangpin'
        },
        isCollapse:false,
        //被激活的链接地址
        activePath:''
      }
    },
    // created(){
    //   this.getMenuList()
    //   //在创建时就要取值
    //   this.activePath = window.sessionStorage.getItem('activePath')
    // },
    methods: {
      logout() {
        window.sessionStorage.clear();
        this.$router.push("/login");
      },
      //获取所有菜单
      // async getMenuList(){
      //   const {data:res} = await this.$http.get('menus')
      //   if(res.meta.status !== 200) return this.$message.error(res.meta.msg)
      //   this.menulist = res.data
      //   console.log(res)
      // },
      //点击按钮，切换菜单的折叠与展开
      toggleCollaps(){
        this.isCollapse = !this.isCollapse
      },
      //保存链接的激活状态
      saveNavState(activePath){
        window.sessionStorage.setItem('activePath',activePath)
        this.activePath = activePath
      }
    },
  };
</script>
<style >
  .el-header {
    background-color: #3d4344;
    display: flex;
    height: 100px !important;
    justify-content: space-between;
    padding-left: 0;
    align-items: center;
    color: #fff;
    font-size: 20px;
  }

  .el-header > div {
    display: flex;
    height: 200px;
    align-items: center;
  }
  .el-header > div span {
    margin-left: 15px;
  }
  .home-container {
    height: 100%;
    width: 100%;
  }
  .el-aside {
    background-color: #383d48;

  }
  .el-aside .el-menu {
    border-right: none;
  }
  .el-main {
    background-color: #d7dbde;
  }
  body > .el-container {
    margin-bottom: 40px;
  }
  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
  .iconfont {
    margin-right: 10px;
  }
  .toggle-button {
    background-color: #4A5064;
    color: white;
    text-align: center;
    font-size: 10px;
    line-height: 26px;
    letter-spacing: 0.2em;
    cursor: pointer;
  }

</style>
