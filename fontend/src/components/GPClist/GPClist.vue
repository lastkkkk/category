<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>分类标准信息管理</el-breadcrumb-item>
      <el-breadcrumb-item>分类映射表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 搜索框头部 -->
      <el-row :gutter="20">
        <el-col :span="3">
          <el-select v-model="radio" placeholder="请选择查询类别">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-input
            placeholder="请输入分类号"
            v-model="queryInfo.info"
            class="input-with-select"
            clearable
            @clear="getGpcList"
          >
            <el-button slot="append" @click="getGpcList">查询</el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogdVisible = true">添加映射</el-button>
        </el-col>
        <el-col :span="2">
          <el-upload
            action=""
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            :on-preview="pre"
            :before-remove="beforeRemove"
            :limit="1"
            :file-list="fileListUpload"
            ref="upload"
            accept="application/vnd.openxmlformats-
        officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
            :auto-upload="false"
          >
            <el-button type="primary" >批量导入映射</el-button>
          </el-upload>
        </el-col>
      </el-row>
      <!--读取表格后的卡片确认区域-->
      <el-dialog
        title="批量导入映射确认"
        :visible.sync="addManyDialogdVisible"
        width="80%"
        @close="addManyDialogClosed"
      >
        <!-- 表格区域 -->
        <el-table :data="addManyList" border stripe @selection-change="handleSelectionChange" row-key="id">
          <el-table-column type="selection"></el-table-column>
          <!--<el-table-column type="index" label="#"></el-table-column>-->
          <el-table-column type="index" label="#"></el-table-column>
          <el-table-column prop="unname" label="UNSPSC中文"></el-table-column>
          <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
          <el-table-column prop="gpcname" label="GPC中文"></el-table-column>
          <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
          <el-table-column prop="gpclevel" label="GPC层次"></el-table-column>
          <el-table-column prop="gpcdesc" label="GPC英文描述"></el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
        <el-button @click="addManyDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="addManyStand">确认</el-button>
      </span>
      </el-dialog>
      <div class="blank1"></div>
      <!--表格-->
      <el-tabs type="border-card"v-model="activeName"  @tab-click="handleClick">
        <el-tab-pane label="全部" name="0">
          <!-- 表格区域 -->
          <el-table :data="gpclist" border stripe>
            <el-table-column type="index" label="#"></el-table-column>
            <el-table-column prop="unname" label="UNSPSC中文"></el-table-column>
            <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
            <el-table-column prop="gpcname" label="GPC中文"></el-table-column>
            <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
            <el-table-column prop="gpclevel" label="GPC层次"></el-table-column>
            <el-table-column prop="gpcdesc" label="GPC英文描述"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <!-- 修改 -->
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
                <!-- 删除 -->
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeGoods(scope.row.id)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页区域 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pagenum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="queryInfo.pagesize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </el-tab-pane>
        <el-tab-pane label="第一层" name="1">
          <!-- 表格区域 -->
          <el-table :data="gpclist" border stripe>
            <el-table-column type="index" label="#"></el-table-column>
            <el-table-column prop="unname" label="UNSPSC中文"></el-table-column>
            <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
            <el-table-column prop="gpcname" label="GPC中文"></el-table-column>
            <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
            <el-table-column prop="gpclevel" label="GPC层次"></el-table-column>
            <el-table-column prop="gpcdesc" label="GPC英文描述"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <!-- 修改 -->
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
                <!-- 删除 -->
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeGoods(scope.row.id)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页区域 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pagenum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="queryInfo.pagesize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </el-tab-pane>
        <el-tab-pane label="第二层" name="2">
          <!-- 表格区域 -->
          <el-table :data="gpclist" border stripe>
            <el-table-column type="index" label="#"></el-table-column>
            <el-table-column prop="unname" label="UNSPSC中文"></el-table-column>
            <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
            <el-table-column prop="gpcname" label="GPC中文"></el-table-column>
            <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
            <el-table-column prop="gpclevel" label="GPC层次"></el-table-column>
            <el-table-column prop="gpcdesc" label="GPC英文描述"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <!-- 修改 -->
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
                <!-- 删除 -->
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeGoods(scope.row.id)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页区域 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pagenum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="queryInfo.pagesize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </el-tab-pane>
        <el-tab-pane label="第三层" name="3"><!-- 表格区域 -->
          <el-table :data="gpclist" border stripe>
            <el-table-column type="index" label="#"></el-table-column>
            <el-table-column prop="unname" label="UNSPSC中文"></el-table-column>
            <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
            <el-table-column prop="gpcname" label="GPC中文"></el-table-column>
            <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
            <el-table-column prop="gpclevel" label="GPC层次"></el-table-column>
            <el-table-column prop="gpcdesc" label="GPC英文描述"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <!-- 修改 -->
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
                <!-- 删除 -->
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeGoods(scope.row.id)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页区域 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pagenum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="queryInfo.pagesize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination></el-tab-pane>
        <el-tab-pane label="第四层" name="4">
          <!-- 表格区域 -->
          <el-table :data="gpclist" border stripe>
            <el-table-column type="index" label="#"></el-table-column>
            <el-table-column prop="unname" label="UNSPSC中文"></el-table-column>
            <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
            <el-table-column prop="gpcname" label="GPC中文"></el-table-column>
            <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
            <el-table-column prop="gpclevel" label="GPC层次"></el-table-column>
            <el-table-column prop="gpcdesc" label="GPC英文描述"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <!-- 修改 -->
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
                <!-- 删除 -->
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeGoods(scope.row.id)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页区域 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pagenum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="queryInfo.pagesize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <!-- 内容添加主体区域 -->
    <el-dialog
      title="添加映射"
      :visible.sync="addDialogdVisible"
      width="35%"
      @close="addDialogClosed"
    >
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addsFormRef"
        label-width="150px"
      >
        <el-form-item label="UNSPSC商品名称" prop="unname">
          <el-input v-model="addForm.unname"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC分类号" prop="unnum">
          <el-input v-model="addForm.unnum"></el-input>
        </el-form-item>
        <el-form-item label="GPC商品名称" prop="gpcname">
          <el-input v-model="addForm.gpcname"></el-input>
        </el-form-item>
        <el-form-item label="GPC分类号" prop="gpcnum">
          <el-input v-model="addForm.gpcnum"></el-input>
        </el-form-item>
        <el-form-item label="GPC英文描述" prop="gpcdesc">
        <el-input v-model="addForm.gpcdesc"></el-input>
      </el-form-item>
        <el-form-item label="GPC层次" prop="gpclevel">
          <el-select v-model="addForm.gpclevel" placeholder="请选择" style="width:370px">
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
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="addStand">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 内容编辑主体区域 -->
    <el-dialog
      title="修改分类映射信息"
      :visible.sync="editDialogdVisible"
      width="35%"
      @close="editDialogClosed"
    >
      <el-form
        :model="editForm"
        :rules="editFormRules"
        ref="editsFormRef"
        label-width="120px"
      >
        <el-form-item label="UNSPSC中文">
          <el-input v-model="editForm.unname" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC分类号" prop="unnum">
          <el-input v-model="editForm.unnum"></el-input>
        </el-form-item>
        <el-form-item label="GPC商品名称" prop="gpcname">
          <el-input v-model="editForm.gpcname"></el-input>
        </el-form-item>
        <el-form-item label="GPC分类号" prop="gpcnum">
          <el-input v-model="editForm.gpcnum"></el-input>
        </el-form-item>
        <el-form-item label="GPC英文描述" prop="gpcdesc">
          <el-input v-model="editForm.gpcdesc"></el-input>
        </el-form-item>
        <el-form-item label="GPC层次" prop="gpclevel">
          <el-select v-model="editForm.gpclevel" placeholder="请选择" style="width:400px">
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
        <el-button @click="editDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="editInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        queryInfo: {
          unnum: "",
          pagenum: 1,
          pagesize: 10,
          info:""
        },
        addManyList:[],
        activeName:"0",
        radio:'',
        options:[{
          value:'1',
          label:'GPC分类号'
        },
          {
            value:'2',
            label:'UNSPSC分类号'
          }],
        queryInfo1: {
          unnum: "",
          pagenum: 1,
          pagesize: 10,
          gpcnum:"",
          unnum:"",
          activeName:""
        },
        gpclist: [],
        input: {
          unnum:""
        },
        total: 0,
        addForm:{
          unnum:"",
          unname:"",
          gpcnum:"",
          gpcname:"",
          gpcdesc:"",
          gpclevel:""
        },
        addFormRules: {
          unnum: [
            {
              required: true,
              message: "请输入UNSPSC分类号",
              trigger: "blur",
            }
          ],
          unname: [
            {
              required: true,
              message: "请输入UNSPSC商品名称",
              trigger: "blur",
            }
          ],
          gpcname: [
            {
              required: true,
              message: "请输入GPC商品名称",
              trigger: "blur",
            }
          ],
          gpcnum: [
            {
              required: true,
              message: "请输入GPC分类号",
              trigger: "blur",
            }
          ],
          gpcdesc: [
            {
              required: true,
              message: "请输入GPC英文描述",
              trigger: "blur",
            }
          ],
          gpclevel: [
            {
              required: true,
              message: "请输入GPC层次",
              trigger: "blur",
            }
          ],
        },
        editForm:{
          unnum:"",
          unname:"",
          gpcnum:"",
          gpcname:"",
          gpcdesc:"",
          gpclevel:""
        },
        editManyForm:{
          unnum:"",
          unname:"",
          gpcnum:"",
          gpcname:"",
          gpcdesc:"",
          gpclevel:""
        },
        editFormRules: {
          unnum: [
            {
              required: true,
              message: "请输入UNSPSC分类号",
              trigger: "blur",
            }
          ],
          unname: [
            {
              required: true,
              message: "请输入UNSPSC商品名称",
              trigger: "blur",
            }
          ],
          gpcname: [
            {
              required: true,
              message: "请输入GPC商品名称",
              trigger: "blur",
            }
          ],
          gpcnum: [
            {
              required: true,
              message: "请输入GPC分类号",
              trigger: "blur",
            }
          ],
          // gpcdesc: [
          //   {
          //     required: true,
          //     message: "请输入GPC英文描述",
          //     trigger: "blur",
          //   }
          // ],
          gpclevel: [
            {
              required: true,
              message: "请输入GPC层次",
              trigger: "blur",
            }
          ],
        },
        addDialogdVisible:false,
        editDialogdVisible:false,
        addManyDialogdVisible:false,
        editManyDialogdVisible:false,
        selectedlevel:'',
        levelList:[{level:"1",id:"1"},{level:"2",id:"2"},{level:"3",id:"3"},{level:"4",id:"4"}],
        multipleSelection:[],
        fileListUpload:[],
        fileTemp:null,
      };
    },
    created() {
     this.getGpcList();
    },
    methods: {
      //获取商品列表
      getGpcList(){
        if(this.radio =='1'){
          this.queryInfo1.gpcnum = this.queryInfo.info;
        }else{
          this.queryInfo1.unnum = this.queryInfo.info;
        }
        if(this.queryInfo.info.length !== 0){
          this.activeName = "0"
        }
        this.queryInfo1.pagenum = this.queryInfo.pagenum;
        this.queryInfo1.pagesize = this.queryInfo.pagesize;
        this.queryInfo1.activeName = this.activeName;
          this.$axios.get('/stlist',{params:this.queryInfo1}).then(res => {
            console.log(res)
            this.gpclist=res.data.content;
            this.total =res.data.totalElements;
          }).catch(err => {
            console.log(err);
          })
      },
      //处理多少层
      handleClick(tab, event) {
        this.queryInfo.info = "";
        this.getGpcList();
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.queryInfo.pagesize = val;
        this.getGpcList();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.queryInfo.pagenum = val;
        this.getGpcList();
      },
      async removeGoods(id){
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
        // //console.log(attr_id)
        // const {data:res} = await this.$http.delete(`goods/${id}`)
        // //console.log(res)
        // if(res.meta.status !== 200){
        //   return this.$message.error("删除参数失败!")
        // }
        // this.getGoodsList()
        // this.$message.success("删除参数成功!")
        console.log(id)
        this.$axios.post('/deletelist',{id}).then(res => {
          console.log(res)
          if(res.status == 200){
            this.$message.success("删除成功!");
          }
          this.getGpcList();
        }).catch(err => {
          console.log(err);
        })
      },
      goAddpage(){
        this.$router.push('/goods/add')
      },
      goAddmultipage(){

      },
      editManyDialogClosed(){},
      //上传文件时处理方法
      handleChange(file, fileList){
        this.fileTemp = file.raw;
        if(this.fileTemp){
          if((this.fileTemp.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')
            || (this.fileTemp.type == 'application/vnd.ms-excel')){
            this.importfxx(this.fileTemp);
          } else {
            this.$message({
              type:'warning',
              message:'附件格式错误，请删除后重新上传！'
            })
          }
          // console.log(this.addManyList);
          // this.addManyDialogdVisible = true;
        } else {
          this.$message({
            type:'warning',
            message:'请上传附件！'
          })
        }
      },
      //上传文件
      pre(){
        // console.log("----")
        // console.log(this.addManyList);
        this.addManyDialogdVisible = true;
      },
      //超出最大上传文件数量时的处理方法
      handleExceed(files, fileList){
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      //移除文件的操作方法
      handleRemove(file,fileList){
        this.fileTemp = null
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      importfxx(res){
        let _this = this;
        let inputDOM = this.$refs.inputer;
        // 通过DOM取文件数据
        this.file = event.currentTarget.files[0];
        console.log( event.currentTarget)
        // console.log("----")

        var rABS = false; //是否将文件读取为二进制字符串
        var f = this.file;

        var reader = new FileReader();
        //if (!FileReader.prototype.readAsBinaryString) {
        FileReader.prototype.readAsBinaryString = function(f) {
          var binary = "";
          var rABS = false; //是否将文件读取为二进制字符串
          var pt = this;
          var wb; //读取完成的数据
          var outdata;
          var reader = new FileReader();
          reader.onload = function(e) {
            var bytes = new Uint8Array(reader.result);
            var length = bytes.byteLength;
            for (var i = 0; i < length; i++) {
              binary += String.fromCharCode(bytes[i]);
            }
            //此处引入，用于解析excel
            var XLSX = require("xlsx");
            if (rABS) {
              wb = XLSX.read(btoa(fixdata(binary)), {
                //手动转化
                type: "base64"
              });
            } else {
              wb = XLSX.read(binary, {
                type: "binary"
              });
            }
            outdata = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]);
            console.log(outdata,'outdata')
            var addlist = []
            addlist.push(outdata)
            // this.addManyList.push(outdata);
            // this.addManyList = addlist;
            this.addManyList = outdata
            this.data = [...outdata]
            // console.log(addlist)
            const arr = [];
            this.data.map(v => {
              const obj={}
              obj.unnum = v.unnum
              obj.unname = v.unname
              obj.gpcname = v.gpcname
              obj.gpcnum = v.gpcnum
              obj.gpclevel = v.gpclevel
              obj.gpcdesc = v.gpcdesc
              arr.push(obj)
            });
            // _this.da=arr;
            // _this.dalen=arr.length;
            console.log(arr,'arr')
            _this.addManyList = arr
            // return outdata;
          };
          reader.readAsArrayBuffer(f);
        };
        if (rABS) {
          reader.readAsArrayBuffer(f);
        } else {
          reader.readAsBinaryString(f);
        }
      },
      addManyDialogClosed(){},
      handleSelectionChange(val) {
        this.multipleSelection = val;
        console.log(val);
      },
      //批量添加
      addManyStand(){
        if(this.multipleSelection.length==0){
          this.$message.info("未选中任何分类！")
        }else{
          this.$axios.post('/savemanylist',this.multipleSelection).then(res =>{
            console.log(res);
            if(res.data.success == true){
              this.$message.success(res.data.msg)
            }else {
              this.$message.error(res.data.msg)
            }
          }).catch(err =>{
            console.log(err)
          })
          this.addManyDialogdVisible = false;
          this.getGpcList();
        }

      },
      editManyStand(){
        this.editManyDialogdVisible = false;
      },
      editManyDialogClose(){
        this.$refs.editManyFormRef.resetFields();
        this.editManyDialogdVisible=false
      },

      addStand(){
        this.$refs.addsFormRef.validate(async (valid) => {
          if (!valid) return;
          //添加数据
          this.$axios.post('/savelist', this.addForm).then(res => {
            console.log(res)
            if(res.status == 200){
              this.$message.success("保存成功!")
            }else{
              this.$message.error("保存失败!")
            }
            this.getGpcList();
          }).catch(err => {
            console.log(err);
          })
          this.addDialogdVisible = false;
        });
      },
      //监听添加用户对话框的关闭事件
      addDialogClosed() {
        this.$refs.addsFormRef.resetFields();
      },
      //对分类标准进行修改
      showEditDialog(id){
        this.$axios.post('/editlist',{id:id}).then(res => {
          console.log(res);
          this.editForm = res.data;
        }).catch(err => {
          console.log(err);
        })
        this.editDialogdVisible = true;
      },
      editInfo(){
        this.$refs.editsFormRef.validate(async (valid) => {
          if (!valid) return;
          console.log(this.editForm)
          this.$axios.post('/updatelist',this.editForm)
            .then(res => {
              console.log(res);
              if(res.status == 200){
                this.$message.success("修改成功!");
              }
              this.getGpcList();
            }).catch(err => {
            console.log(err);
          })
          this.editDialogdVisible = false;
        });
      },
      //监听编辑用户对话框的关闭事件
      editDialogClosed() {
        this.$nextTick(()=>{
          this.$refs.editsFormRef.resetFields();
        })
      },
    },
  };
</script>

<style scoped>
  .blank{
    width: 10px;
  }
  .blank1{
    height: 15px;
  }

</style>
