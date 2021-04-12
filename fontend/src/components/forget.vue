<template>
        <div>
            <div class="outer_label">
      <!-- <img class="inner_label login_logo" src="../../assets/logo.png"> -->
           </div>
             <div class="forget-box">
             <el-form label-width="100px" class="forget-form"  ref="forgetpwdform" :model="forgetpwdform" :rules="rules">
                 <el-row :gutter="20">
                    <el-col :span="16">
                        <el-form-item prop="username" label="用户名：">
                        <el-input v-model="forgetpwdform.username" ></el-input>
                    </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-form-item prop="phone" label="手机号：">
                        <el-input v-model="forgetpwdform.phone"  maxlength="11"></el-input>
                    </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-button size="small" @click="sendcodeMsg" :disabled="disabled">{{sendcode}}</el-button>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-form-item prop="code" label="验证码：">
                        <el-input v-model="forgetpwdform.code" ></el-input>
                    </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-form-item prop="newpassword" label="新密码">
                           <el-input type="password" v-model="forgetpwdform.newpassword"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-form-item prop="repeatpassword" label="确认密码">
                           <el-input type="password" v-model="forgetpwdform.repeatpassword"></el-input>
                       </el-form-item>
                    </el-col>
                </el-row>
        </el-form>
        <div class="footer">
            <el-button class="submit" type="primary" @click="handleSubmit">提交</el-button>
            <el-button style="width:25%" type="default" @click="handleClose">取消</el-button>
        </div>
        </div>
        </div>
</template>
 
<script>
    import qs from "qs"
 
    export default {
        name: "forgetpwd",
        props: ["centerDialogVisibleSecond"],
        data() {
            return {
                getCode: "",
                sendcode: "发送验证码",
                disabled: false,
                forgetpwdform: {
                    phone: "",
                    code: "",
                    newpassword: "",
                    repeatpassword: "",
                    username:""
                },
                rules: {
                    username: [
                        {required: true, message: "用户名不能为空", trigger: "blur"}
                    ],
                    phone: [
                        {required: true, message: "手机号不能为空", trigger: "blur"}
                    ],
                    code: [
                        {
                            required: true, message: "验证码不能为空", trigger: "blur"
                        },
                    ],
                    newpassword: [
                        {
                            required: true, message: "新密码不能为空", trigger: "blur"
                        },
                    ],
                    repeatpassword: [
                        {
                            required: true, message: "重置密码不能为空", trigger: "blur"
                        }
                    ]
                }
            }
        },
        methods: {
            //手机号正则判断
            judgePhone() {
                const reg = /^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$/;
                //var url="/nptOfficialWebsite/apply/sendSms?mobile="+this.ruleForm.phone;
                if (this.forgetpwdform.phone == '') {
                    this.$message("请输入手机号码")
                    return false;
                } else if (!reg.test(this.forgetpwdform.phone)) {
                    this.$message("手机号格式不正确")
                    return false;
                } else {
                    return true;
                }
            },
            //发送验证码
            sendcodeMsg() {
                let _this = this;
                if (_this.judgePhone()) {
                    var num = 60;
                    var timer = setInterval(function () {
                        num--;
                        _this.disabled = true;
                        _this.sendcode = num + '秒后重新获取';
                        if (num === 0) {
                            _this.sendcode = '获取验证码';
                            _this.disabled = false;
                            clearInterval(timer)
                        }
                    }, 1000)
                    _this.$http.post("你的接口", qs.stringify(
                        {phoneNumber: _this.forgetpwdform.phone}
                    )).then((res) => {
                        _this.getCode = res;
                    })
                }
 
            },
            //关闭页面
            handleClose() {
                this.centerDialogVisibleSecond = false;
                this.$emit("tyonke", this.centerDialogVisibleSecond);
            },
 
            //表单提交
            handleSubmit() {
                let _this = this;
                _this.$refs['forgetpwdform'].validate(valid => {
                    if (valid) {
                        if (_this.forgetpwdform.code != _this.getCode) {
                            this.$message({
                                type: "error",
                                message: "修改失败",
                                duration: 1000
                            })
                            return false;
                        }
                        return this.$message({
                            type: "success",
                            message: "修改成功",
                            duration: 1000
                        })
                    } else {
                        return
                    }
                })
            }
        },
        watch: {
            centerDialogVisibleSecond(newValue, oldValue) {
                this.centerDialogVisibleSecond = newValue;
            }
        }
    }
</script>
 
<style scoped>
 .forget-box{
     width: 500px;
     height: 500px;
     margin: 50px calc(50% - 250px) 0;
     /* border: 1px solid #2761FF;
     border-radius: 5%; */
 }
 .forget-form{
     padding: 10% 5% 5% 5%;
 }
 .footer{
     width: 90%;
 }
 .submit{
     width: 25%;
     background: -webkit-linear-gradient(
    left,
    #000099,
    #2154fa
  );
 }
</style>