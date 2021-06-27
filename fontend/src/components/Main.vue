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
        <!--<el-menu-->
          <!--background-color="#0090FD"-->
          <!--text-color="#fff"-->
          <!--active-text-color="#0C3571"-->
          <!--:unique-opened="true"-->
          <!--:collapse="isCollapse"-->
          <!--:collapse-transition="false"-->
          <!--router-->
          <!--:default-active="activePath"-->
        <!--&gt;-->
          <!--&lt;!&ndash; 一级菜单 &ndash;&gt;-->
          <!--<el-submenu :index="item.id + ''" :key="item.id" v-for="item in menulist">-->
            <!--&lt;!&ndash; 一级菜单的模板区域 &ndash;&gt;-->
            <!--<template slot="title">-->
              <!--&lt;!&ndash; 图标 &ndash;&gt;-->
              <!--<i style="font-size: 16px; color: #fff" :class="iconsObj[item.id]"></i>-->
              <!--&lt;!&ndash; 文本 &ndash;&gt;-->
              <!--<span>{{item.name}}</span>-->
            <!--</template>-->
            <!--&lt;!&ndash; 二级菜单 &ndash;&gt;-->
            <!--<el-menu-item :index="'/'+subItem.path" :key="subItem.id" v-for="subItem in item.children" @click="saveNavState('/'+subItem.path)">-->
              <!--<template slot="title">-->
                <!--&lt;!&ndash; 图标 &ndash;&gt;-->
                <!--<i class="el-icon-menu" style="color: #fff"></i>-->
                <!--&lt;!&ndash; 文本 &ndash;&gt;-->
                <!--<span>{{subItem.name}}</span>-->
              <!--</template>-->
            <!--</el-menu-item>-->
          <!--</el-submenu>-->
        <!--</el-menu>-->
        <el-menu
          :default-active="activePath"
          class="el-menu-admin"
          router
          mode="vertical"
          background-color="#0090FD"
          text-color="#fff"
          :unique-opened="true"
          :collapse="isCollapse"
          :collapse-transition="false"
          active-text-color="#0C3571">
          <template v-for="(item,i) in adminMenus">
            <!--index 没有用但是必需字段且为 string -->
            <el-submenu :key="i" :index="i + ''" style="text-align: left">
          <span slot="title" >
            <i :class="item.iconCls" style="font-size: 16px; color: #fff"></i>
            {{item.nameZh}}
          </span>
              <el-menu-item v-for="child in item.children" :key="child.path" :index="child.path">
                <i :class="child.icon" style="font-size: 16px; color: #fff"></i>
                {{ child.nameZh }}
              </el-menu-item>
            </el-submenu>
          </template>
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
        // menulist:[
        //   {name:'用户信息管理',
        //    id:'125',
        //    children:[
        //      {name:'用户列表',
        //        id:'110',
        //        path:'users'}
        //    ]
        //   },
        //   {name:'商品分类管理',
        //     id:'101',
        //     children:[
        //       {name:'商品列表',
        //         id:'111',
        //         path:'goodslist'},
        //       // {name:'商品分类',
        //       //   id:'112',
        //       //   path:'goodscate'}
        //     ]
        //   },
        //   {name:'分类标准信息管理',
        //     id:'103',
        //     children:[
        //       {name:'分类映射查询表',
        //         id:'113',
        //         path:'gpclist'},
        //       {
        //         name:'GPC 分类 ',
        //         id:'114',
        //         path:'gpcate'
        //       },
        //       {
        //         name:'UNSPSC 分类 ',
        //         id:'115',
        //         path:'uncate'
        //       }
        //     ]
        //   }
        // ],
        // iconsObj:{
        //   "125":'iconfont icon-user',
        //   "103":'iconfont icon-qukuai',
        //   "101":'iconfont icon-cangpeitubiao_shangpin'
        // },
        isCollapse:false,
        //被激活的链接地址
        activePath:''
      }
    },
    computed:{
      adminMenus(){
        return this.$store.state.adminMenus
      }
    },
    created(){
      // this.getMenuList()
      //在创建时就要取值
      this.activePath = window.sessionStorage.getItem('activePath')
    },
    methods: {
      logout() {
        var _this = this
        // window.sessionStorage.clear();
        this.$axios.get('/logout').then(res => {
          if(res.data.success == true){
            _this.$store.commit('logout')
            _this.$router.push("/");
            _this.$store.state.adminMenus =[];
          }
          console.log(res)
        }).catch(err =>{
          console.log(err)
        })
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
    display: flex;
    background-color: #0090FD;
    height: 100px !important;
    /*background-color: #5882FA;*/
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
    /*background-color: #383d48;*/
    /*background-color: #66B1FF;*/
    /*background-color: #5882FA;*/
    /*background-color: #2C85E0;*/
    /*background-color: #6298EA;*/
    background-color: #0090FD;
  }
  .el-aside .el-menu {
    border-right: none;
  }
  .el-main {
    background-color: #F5F7FA;
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
    /*background-color: #4A5064;*/
    /*background-color: #095CB1;*/
    color: white;
    background-color: #3C75C9;
    text-align: center;
    font-size: 10px;
    line-height: 26px;
    letter-spacing: 0.2em;
    cursor: pointer;
  }

</style>
