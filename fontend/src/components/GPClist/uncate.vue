<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>分类标准信息管理</el-breadcrumb-item>
      <el-breadcrumb-item>UNSPSC分类</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片区域 -->
    <el-card>
      <!-- 搜索框头部 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入分类号"
            v-model="queryInfo.unnum"
            class="input-with-select"
            clearable
            @clear="getUncate"
          >
            <el-button slot="append"  @click=" getUncate">查询</el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="showAddCateDialog">添加分类</el-button>
        </el-col>
      </el-row>
      <div class="blank"></div>
      <!-- 表格区域 -->
      <tree-table
        :data="uncate"
        :columns="columns"
        :show-row-hove="false"
        :selection-type="false"
        :expand-type="false"
        show-index
        index-text="#"
        border
      >
        <template slot="isok" slot-scope="scope">
          <i
            v-if="scope.row.deleted === 0"
            style="color: lightgreen"
            class="el-icon-success"
          ></i>
          <i
            v-else-if="scope.row.deleted === 1"
            style="color: red"
            class="el-icon-error"
          ></i>
        </template>
        <template slot="order" slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.unlevel === 1">一层</el-tag>
          <el-tag
            size="mini"
            type="success"
            v-else-if="scope.row.unlevel === 2"
          >二层</el-tag
          >
          <el-tag size="mini" type="warning" v-else-if="scope.row.unlevel === 3">三层</el-tag>
          <el-tag size="mini" type="info" v-else-if="scope.row.unlevel === 4">四层</el-tag>
        </template>
        <template slot="opt" slot-scope="scope">
          <!-- 修改 -->
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditCateDialog(scope.row.id)"
          >编辑</el-button
          >
          <!-- 删除 -->
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeCateById(scope.row.id)"
          >删除</el-button
          >
        </template>
      </tree-table>
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
    <!-- 添加分类的对话框 -->
    <el-dialog
      title="添加UNSPSC分类"
      :visible.sync="addCateDialogVisible"
      width="40%"
      @close="addCateDialogClosed"
    >
      <!-- 添加分类的表单 -->
      <el-form
        :model="addCateForm"
        :rules="addCateFormRules"
        ref="addCateFormRef"
        label-width="100px"
      >
        <el-form-item label="分类名称：" prop="unname">
          <el-input v-model="addCateForm.unname"></el-input>
        </el-form-item>
        <el-form-item label="分类号：" prop="unnum">
          <el-input v-model="addCateForm.unnum"></el-input>
        </el-form-item>
        <el-form-item label="父级分类：" prop="parent">
          <!-- options 用来指定数据源 -->
          <!-- options用来指定数据源 -->
          <!-- props用来指定配置对象 -->
          <!-- v-model进行双向绑定 -->
          <el-cascader
            ref="addCascader"
            expand-trigger="hover"
            :options="parentCateList"
            :props="cascaderProps"
            v-model="selectedKeys"
            @change="parentCateChanged"
            clearable
            change-on-select="false"
          >
          </el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addCateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCate">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改分类的对话框 -->
    <el-dialog
      title="修改分类信息"
      :visible.sync="editCateDialogdVisible"
      width="30%"
    >
      <el-form
        :model="editCateForm"
        :rules="editCateFormRules"
        ref="editCateFormRef"
        label-width="110px"
      >
        <el-form-item label="分类名称：" prop="unname">
          <el-input v-model="editCateForm.unname"></el-input>
        </el-form-item>
        <el-form-item label="分类号：" prop="unnum">
          <el-input v-model="editCateForm.unnum"></el-input>
        </el-form-item>
        <el-form-item label="父层分类号：" prop="parent">
          <el-input v-model="editCateForm.parent"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC层次" prop="unlevel">
          <el-select v-model="editCateForm.unlevel" placeholder="请选择" style="width:340px">
            <el-option
              v-for="item in levelList"
              :key="item.level"
              :label="item.level"
              :value="item.level"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editCateDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="editcateInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        queryInfo: {
          // type: 3,
          pagenum: 1,
          pagesize: 10,
          unnum:''
        },
        uncate: [],
        //总数据条数
        total: 0,
        //为table指定列的定义
        columns: [
          {
            label: "分类名称",
            prop: "unname",
          },
          {
            label: "分类号",
            prop: "unnum",
          },
          {
            label: "是否有效",
            //表示，将当前列定义为模板列
            type: "template",
            //表示当前这一列使用模板名称
            template: "isok",
          },
          {
            label: "层次",
            //表示，将当前列定义为模板列
            type: "template",
            //表示当前这一列使用模板名称
            template: "order",
          },
          {
            label: "操作",
            //表示，将当前列定义为模板列
            type: "template",
            //表示当前这一列使用模板名称
            template: "opt",
          },
        ],
        // 控制添加分类对话框的显示与隐藏
        addCateDialogVisible: false,
        // 添加分类的表单数据对象
        addCateForm: {
          // 将要添加的分类的名称
          unname: "",
          // 父级分类的Id
          parent: 0,
          //将要添加的分类号
          unnum:"",
          // 分类的等级，默认要添加的是1级分类
          unlevel: 0,
        },
        // 添加分类表单的验证规则对象
        addCateFormRules: {
          unname: [
            { required: true, message: "请输入分类名称", trigger: "blur" },
          ],
          unnum: [
            { required: true, message: "请输入分类号", trigger: "blur" },
          ],
          parent: [
            { required: true, message: "请选择父级分类", trigger: "blur" },
          ],
        },
        // 父级分类的列表
        parentCateList: [],
        // 指定级联选择器的配置对象
        cascaderProps: {
          value: "id",
          label: "unname",
          children: "children",
          level:"unlevel",
        },
        // 选中的父级分类的Id数组
        selectedKeys: [],
        editCateDialogdVisible:false,
        editCateForm:{},
        editCateFormRules: {
          unname: [
            { required: true, message: "请输入分类名称", trigger: "blur" },
          ],
          unnum: [
            { required: true, message: "请输入分类号", trigger: "blur" },
          ],
          unlevel: [
            { required: true, message: "请选择un层次", trigger: "blur" },
          ],
          parent: [
            { required: true, message: "请输入父层分类号", trigger: "blur" },
          ],
        },
        selectedlevel:'',
        levelList:[{level:"1",id:"1"},{level:"2",id:"2"},{level:"3",id:"3"},{level:"4",id:"4"}]
      };
    },
    created() {
      this.getUncate();
    },
    methods: {
      //获取un分类列表
     getUncate(){
        console.log(this.queryInfo)
        this.$axios.get('/un/getUnList',{params:this.queryInfo}).then(res => {
          console.log(res)
          // this.getunate2();
          this.uncate=res.data.list;
          this.total =res.data.total;
        }).catch(err => {
          console.log(err);
        })
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.queryInfo.pagesize = val;
        this.getUncate();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.queryInfo.pagenum = val;
        this.getUncate();
      },
      showAddCateDialog() {
        this.getParentCateList();
        this.addCateDialogVisible = true;
      },
      // 获取父级分类的数据列表
      async getParentCateList() {
       // console.log("获取父级分类")
        this.$axios.get('/un/queryUnList').then(res => {
          // console.log(res)
          this.parentCateList=res.data;
          this.digui(this.parentCateList)
        }).catch(err => {
          console.log(err);
        })
      },
      //递归得到的树形结构 将第三层的children为undifined
      digui(data){
        for(let i =0;i<data.length;i++){
          if(data[i].children.length<1||data[i].unlevel ==3){
            data[i].children = undefined;
          }else {
            this.digui(data[i].children)
          }
        }
      },
      //选择项发生变化触发 得到选中的父级分类与当前的层次
      parentCateChanged() {
        var id = this.selectedKeys[this.selectedKeys.length-1];
        this.$axios.get('/un/findun/'+id).then(res => {
          //console.log(res);
          this.addCateForm.parent=res.data.unnum;
        }).catch(err => {
          console.log(err);
        })
        //如果selectedKeys数组中的length大于0，证明选中的父级分类
        //反之，就说明没有选中任何父级分类
        if (this.selectedKeys.length > 0) {
          //为当前分类的等级赋值
          this.addCateForm.unlevel= this.selectedKeys.length+1;
          return;
        } else {
          this.addCateForm.parent = 0;
          //为当前分类的等级赋值
          this.addCateForm.unlevel = 0;
        }
      },
      //点击按钮，添加新的分类
      addCate() {
        this.$refs.addCateFormRef.validate(async (valid) => {
          if(!valid) return
          console.log(this.addCateForm)
          this.$axios.post('/un/addun',this.addCateForm).then(res => {
             // console.log(res)
            if(!res.data){
              return this.$message.error("添加分类失败!")
            }
            this.$message.success("添加分类成功!")
            this.getUncate()
            this.addCateDialogVisible = false
          }).catch(err => {
            console.log(err);
          })
        });
      },
      //监听对话框的关闭事件，重置表单数据
      addCateDialogClosed(){
        this.$refs.addCateFormRef.resetFields()
        this.selectedKeys=[]
        this.selectedKeys.unlevel = 0
        this.selectedKeys.parent = 0
      },
      //打开修改信息
      showEditCateDialog(id){
        this.$axios.get('/un/findun/'+id).then(res => {
          this.editCateForm = res.data;
        }).catch(err => {
          console.log(err);
        })
        this.editCateDialogdVisible = true
      },
      //提交更新信息
      editcateInfo(){
        this.$refs.editCateFormRef.validate(async (valid) => {
          if(!valid)return;
          this.$axios.post('/un/updateun',this.editCateForm).then(res => {
            // console.log(res)
            if (!res.data)
              return this.$message.error("修改分类信息失败!");
            //刷新数据列表
            this.getUncate()
            this.editCateDialogdVisible = false;
            return this.$message.success("修改分类信息成功!");
          }).catch(err => {
            console.log(err);
          })
        })
      },
      //删除分类
      async removeCateById(id){
        const confirmResult = await this.$confirm(
          "此操作将永久删除该分类, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).catch((err) => err);
        if(confirmResult !== "confirm"){
          return this.$message.info("已取消删除!")
        }
        this.$axios.get('/un/deleteun/'+id).then(res => {
          //console.log(res);
          if(!res.data){
            return this.$message.error("删除分类失败")
          }
          this.getUncate()
          return this.$message.success("删除分类成功")
        }).catch(err => {
          console.log(err);
        })
      },

    },
  };
</script>

<style scoped>
  .el-cascader {
    width: 100%;
  }
  .blank{
    height: 15px;
  }
</style>

