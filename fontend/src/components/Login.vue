<template>
  <div>
    <div class="outer_label">
    </div>
    <div class="login-box">
      <el-form class="login_form" label-width="0px" >
        <el-form-item >
          <i class="iconfont icon-user icon-sel"></i>
          <el-input type="text" class="qxs-icon"  placeholder="用户名"v-model="loginForm.userName"  />
        </el-form-item>
        <el-form-item>
          <i class="iconfont icon-anquanyinsi icon-sel"></i>
          <el-input type="password" class="qxs-icon" placeholder="密码"v-model="loginForm.password"/>
        </el-form-item>
        <!--<button class="login_btn el-button el-button&#45;&#45;primary is-round" type="primary" round>登录</button>-->
        <el-form-item >
          <el-button class="login_btn" @click.native="login" type="primary" round :loading="isBtnLoading" >登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        loginForm:{
          userName: "",
          password: "",
        },
        isBtnLoading: false,
      };
    },
    computed: {
      btnText() {
        if (this.isBtnLoading) return "登录中...";
        return "登录";
      },
    },
    methods: {
      login() {
        var _this = this
        console.log(this.$store.state)
        if (!this.loginForm.userName) {
          this.$message.error("请输入用户名");
          return;
        }
        if (!this.loginForm.password) {
          this.$message.error("请输入密码");
          return;
        }
        this.$axios.post('/login',{username:this.loginForm.userName,
                                   password:this.loginForm.password})
          .then(res => {
          //console.log("ok");
            console.log(res)
            if(res.data.success==true){
              console.log(res.data)
              // console.log("ok")
              _this.$store.commit('login',_this.loginForm)
              // this.$router.push("/users");
              this.$message.success(res.data.msg);
              var path = this.$route.query.redirect
              this.$router.replace({path: path === '/' || path === undefined ? '/index' : path})
              console.log('path='+path)
            }else{
              console.log('用户名或者密码错误');
              this.$message.error(res.data.msg);
            }
        }).catch(err =>{
          console.log(err);
        })
      }
    },
  };
</script>

<style scoped>
  .login-box{
    width: 360px;
    height: 320px;
    margin: 50px calc(50% - 180px) 0;
  }
  .login_form {
    padding: 10% 5% 5% 5%;
    border: 1px solid #4D87E0;
    border-radius: 5%;
  }
  .login_logo {
    height: 100%;
  }
  .login_btn {
    width: 100%;
    font-size: 16px;
    /*background-color: #3d4344;*/
    /*background-color: #4D87E0;*/
    background-color: #0090FD;
    /*background: -webkit-linear-gradient(*/
      /*left,*/
      /*#000099,*/
      /*#2154fa*/
    /*); !* Safari 5.1 - 6.0 *!*/
    /*background: -o-linear-gradient(*/
      /*right,*/
      /*#000099,*/
      /*#2154fa*/
    /*); !* Opera 11.1 - 12.0 *!*/
    /*background: -moz-linear-gradient(*/
      /*right,*/
      /*#000099,*/
      /*#2154fa*/
    /*); !* Firefox 3.6 - 15 *!*/
    /*background: linear-gradient(to right, #000099, #2154fa); !* 标准的语法 *!*/
    filter: brightness(1.4);
  }
  .icon {
    width: 1em;
    height: 1em;
    vertical-align: -0.15em;
    fill: currentColor;
    overflow: hidden;
  }
  .icon-sel {
    font-size: 25px;
  }
  .bottom {
    margin-top: 10px;
    cursor: pointer;
  }
  span{
    cursor: pointer;
  }
</style>
