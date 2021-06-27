<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户信息管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 搜索与添加区域 -->
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input
            v-model="queryInfo.username"
            placeholder="请输入用户名"
            clearable
            @clear="getUserList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getUserList"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogdVisible = true"
          >添加用户</el-button
          >
        </el-col>
      </el-row>
      <!-- 用户列表区域 -->
      <el-table :data="userlist" border stripe>
        <el-table-column type="index"> </el-table-column>
        <el-table-column prop="username" label="用户名"> </el-table-column>
        <el-table-column prop="email" label="邮箱"> </el-table-column>
        <el-table-column prop="role" label="角色"> </el-table-column>
        <el-table-column prop="mobile" label="电话"> </el-table-column>
        <el-table-column prop="role_name" label="操作" width="180px">
          <template slot-scope="scope">
            <!-- 修改 -->
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="showEditDialog(scope.row.username)"
            ></el-button>
            <!-- 删除 -->
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="removeUserById(scope.row.id)"
            ></el-button>
            <!-- 分配角色按钮 -->
            <el-tooltip
              effect="dark"
              content="分配角色"
              placement="top"
              :enterable="false"
            >
              <el-button
                type="warning"
                icon="el-icon-setting"
                size="mini"
                @click="setRole(scope.row)"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[1, 2, 5, 10]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
    <el-dialog
      title="添加用户"
      :visible.sync="addDialogdVisible"
      width="30%"
      @close="addDialogClosed"
    >
      <!-- 内容添加主体区域 -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="addForm.mobile"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="addForm.role" placeholder="请选择" style="width:360px">
            <el-option
              v-for="item in rolesList"
              :key="item.id"
              :label="item.roleName"
              :value="item.roleName"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改用户的对话框 -->
    <el-dialog
      title="修改用户信息"
      :visible.sync="editDialogdVisible"
      width="50%"
      @close="editDialogClosed"
    >
      <el-form
        :model="editForm"
        :rules="editFormRules"
        ref="editFormRef"
        label-width="80px"
      >
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="editForm.mobile"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="edituserInfo">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 分配角色的对话框 -->
    <el-dialog title="分配角色" :visible.sync="setRoleDialogVisible" width="50%" @close="rolesDialogClosed">
      <div>
        <p>当前的用户：{{ userInfo.username }}</p>
        <p>当前的角色：{{ userInfo.role }}</p>
        <p>
          分配新角色：
          <el-select v-model="selectedRole" placeholder="请选择">
            <el-option
              v-for="item in rolesList"
              :key="item.id"
              :label="item.roleName"
              :value="item.roleName"
            >
            </el-option>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="setRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      //验证邮箱的规则 cb为回调函数
      var checkEmail = (rule, value, cb) => {
        //验证邮箱的正则表达式
        const regEmail = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
        if (regEmail.test(value)) {
          //合法的邮箱
          return cb();
        }
        cb(new Error("请输入合法的邮箱"));
      };
      //验证手机号码的规则
      var checkMobile = (rule, value, cb) => {
        //验证邮箱的正则表达式
        const regMobile = /^[1][3,4,5,7,8][0-9]{9}$/;
        if (regMobile.test(value)) {
          //合法的邮箱
          return cb();
        }
        cb(new Error("请输入合法的手机号"));
      };
      return {
        //获取用户列表值
        queryInfo: {
          username: "",
          //当前的页数
          pagenum: 1,
          //当前每页显示多少条数据
          pagesize: 2,
        },
        user:{},
        userlist: [{}],
        total: 0,
        //控制添加用户对话框的显示与隐藏
        addDialogdVisible: false,
        //控制编辑用户框的显示与隐藏
        editDialogdVisible: false,
        //控制删除用户框的显示与隐藏
        DeletedialogVisible: false,
        //添加用户的表单数据
        addForm: {
          username: "",
          password: "",
          email: "",
          mobile: "",
        },
        addFormRules: {
          username: [
            {
              required: true,
              message: "请输入用户名",
              trigger: "blur",
            },
            {
              min: 3,
              max: 10,
              message: "用户名的长度在3-10个字符之间",
              trigger: "blur",
            },
          ],
          password: [
            {
              required: true,
              message: "请输入密码",
              trigger: "blur",
            },
            {
              min: 6,
              max: 15,
              message: "用户名的长度在6-15个字符之间",
              trigger: "blur",
            },
          ],
          email: [
            {
              required: true,
              message: "请输入邮箱",
              trigger: "blur",
            },
            { validator: checkEmail, trigger: "blur" },
          ],
          mobile: [
            {
              required: true,
              message: "请输入电话",
              trigger: "blur",
            },
            { validator: checkMobile, trigger: "blur" },
          ],
          role: [
            {
              required: true,
              message: "请输入角色",
              trigger: "blur",
            },
            {  trigger: "blur" },
          ],
        },
        //查询到的用户对象
        editForm: {
          email: "",
          mobile: "",
          username: "",
        },
        //编辑表单的规则
        editFormRules: {
          email: [
            {
              required: true,
              message: "请输入邮箱",
              trigger: "blur",
            },
            { validator: checkEmail, trigger: "blur" },
          ],
          mobile: [
            {
              required: true,
              message: "请输入电话",
              trigger: "blur",
            },
            { validator: checkMobile, trigger: "blur" },
          ],
        },
        //控制分配角色对话框的显示与隐藏
        setRoleDialogVisible: false,
        //需要被分配角色的用户信息
        userInfo: {},
        rolesList: [{roleName:"管理员",id:'1'},
                    {roleName:"操作员",id:"2"}],
        selectedRole:''
      };
    },
    created() {
       this.getUserList();
    },
    methods: {
      //查询得到数据
      getUserList() {
        // console.log(this.queryInfo)
        this.$axios.get('/pages', {params:this.queryInfo}).then(res => {
          console.log(res);
          // console.log(res.data.content)
          this.userlist = res.data.content;
          // console.log(this.userlist);
          this.total =res.data.totalElements;
        }).catch(err => {
          console.log(err);
        })
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.queryInfo.pagesize = val;
        this.getUserList();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.queryInfo.pagenum = val;
        this.getUserList();
      },
      //监听添加用户对话框的关闭事件
      addDialogClosed() {
        this.$refs.addFormRef.resetFields();
      },
      //点击按钮，添加新用户,进行预验证
      addUser() {
        this.$refs.addFormRef.validate(async (valid) => {
          if (!valid) return;
          //添加数据
          this.$axios.post('/saveuser', this.addForm).then(res => {
            console.log(res)
            if(res.data.success==true){
              this.$message.success("保存成功!")
              this.getUserList();
            }else{
              this.$message.error("用户已存在!")
              var userlist1 = []
              userlist1.push(res.data.data);
              this.userlist = userlist1;
              console.log(this.userlist)
              this.total = 1;
            }
          }).catch(err => {
            console.log(err);
          })
          this.addDialogdVisible = false;
        });
      },
      //展示用户编辑的对话框
       showEditDialog(username) {
        this.$axios.post('/edit',{username:username}).then(res => {
          this.editForm = res.data;
        }).catch(err => {
          console.log(err);
        })
        this.editDialogdVisible = true;
      },
      //监听编辑用户对话框的关闭事件
      editDialogClosed() {
        this.$refs.editFormRef.resetFields();
      },
      edituserInfo() {
        this.$refs.editFormRef.validate(async (valid) => {
          if (!valid) return;
          console.log(this.editForm)
          this.$axios.post('/editinfo',{username:this.editForm.username,
                                        email:this.editForm.email,
                                        mobile:this.editForm.mobile})
            .then(res => {
              if(res.status == 200){
                this.$message.success("修改成功!");
              }
              this.getUserList();
          }).catch(err => {
            console.log(err);
          })
          this.editDialogdVisible = false;
        });
      },
      async removeUserById(id) {
        console.log(id)
        const confirmResult = await this.$confirm(
          "此操作将永久删除该用户, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).catch((err) => err);
        //如果用户确认删除 则为confirm

        //如果用户取消了删除，则返回值为字符串 cancel
        //console.log(confirmResult)
        if (confirmResult !== "confirm") {
          return this.$message.info("已取消删除");
        }
        this.$axios.post('/delete',{id}).then(res => {
          // console.log("ok");
          //console.log(res);
          if(res.status == 200){
            this.$message.success("删除成功!");
          }
          this.getUserList();
        }).catch(err => {
          console.log(err);
        })
      },
      //展示分配角色的对话框
      async setRole(userInfo) {
        this.userInfo = userInfo;
        this.setRoleDialogVisible = true;
      },
      //监听角色对话框的关闭事件
      rolesDialogClosed(){
        this.selectedRole=''
        this.userInfo = ''
      },
      saveRoleInfo(){
        if(!this.selectedRole){
          return this.$message.error('未选中任何角色!')
        }
        this.userInfo.role=this.selectedRole;
        console.log(this.userInfo)
        this.$axios.post('/setrole',this.userInfo).then(res => {
          if(res.status == 200){
            this.$message.success("设置成功!")
          }else{
            this.$message.error("设置失败!")
          }
          //console.log('ok');
        }).catch(err => {
          console.log(err);
        })
        this.setRoleDialogVisible = false
        this.getUserList()
      },
    },
  };
</script>

<style scoped>
</style>
