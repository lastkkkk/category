<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品分类管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
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
            placeholder="请输入查询内容"
            v-model="queryInfo.info"
            class="input-with-select"
            clearable
            @clear="getGoodsList"
          >
            <el-button slot="append"  @click=" getGoodsList">查询</el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogdVisible=true">手动添加商品分类</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogdVisible1=true">自动添加商品分类</el-button>
        </el-col>
        <el-col :span="3">
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
            <!--@click="goAddmultipage"-->
            <el-button type="primary" >批量导入商品</el-button>
          </el-upload>
        </el-col>
      </el-row>
      <!-- 表格区域 -->
      <el-table :data="goodsList" border stripe>
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="goodname" label="商品名称"></el-table-column>
        <el-table-column prop="brand" label="商品品牌"></el-table-column>
        <el-table-column prop="gpcname" label="GPC类名"></el-table-column>
        <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
        <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
        <el-table-column prop="unname" label="UNSPSC类名"></el-table-column>
        <el-table-column prop="id" label="商品条形码"></el-table-column>
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
    </el-card>
    <!--读取表格后的卡片确认区域-->
    <el-dialog
      title="批量导入商品确认"
      :visible.sync="addManyDialogdVisible"
      width="80%"
      @close="addManyDialogClosed"
    >
      <!-- 表格区域 -->
      <el-table :data="addManyList" border stripe @selection-change="handleSelectionChange" row-key="id">
        <el-table-column type="selection"></el-table-column>
        <!--<el-table-column type="index" label="#"></el-table-column>-->
        <el-table-column prop="goodname" label="商品名称"></el-table-column>
        <el-table-column prop="brand" label="商品品牌"></el-table-column>
        <el-table-column prop="gpcname" label="GPC类名"></el-table-column>
        <el-table-column prop="gpcnum" label="GPC分类号"></el-table-column>
        <el-table-column prop="unnum" label="UNSPSC分类号"></el-table-column>
        <el-table-column prop="unname" label="UNSPSC类名"></el-table-column>
        <el-table-column prop="id" label="商品条形码"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addManyDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="addManyGoods">确认</el-button>
      </span>
    </el-dialog>

    <!--批量自动添加还是手动添加-->
    <el-dialog
      title="提示"
      :visible.sync="confirmdialogVisible"
      width="30%">
      <span>自动匹配还是手动添加？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmdialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="readTable">手 动</el-button>
    <el-button type="primary" @click="readAuto">自 动</el-button>
  </span>
    </el-dialog>
    <!--手动添加商品卡片区域-->
    <el-dialog
      title="商品手动分类"
      :visible.sync="addDialogdVisible"
      width="30%"
      @close="addDialogClosed"
    >
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="100px"
      >
        <el-form-item label="商品条形码" prop="id">
          <el-input v-model="addForm.id"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodname">
          <el-input v-model="addForm.goodname"></el-input>
        </el-form-item>
        <el-form-item label="商品品牌" prop="brand">
          <el-input v-model="addForm.brand"></el-input>
        </el-form-item>
        <el-form-item label="GPC类名" prop="gpcname">
          <el-input v-model="addForm.gpcname"></el-input>
        </el-form-item>
        <el-form-item label="GPC分类号" prop="gpcnum">
          <el-input v-model="addForm.gpcnum"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC分类号" prop="unnum">
          <el-input v-model="addForm.unnum"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="addGoods">确 定</el-button>
      </span>
    </el-dialog>
    <!--自动添加商品卡片区域-->
    <el-dialog
      title="商品自动分类"
      :visible.sync="addDialogdVisible1"
      width="30%"
      @close="addDialogClosed2"
    >
      <el-form
        :model="addFormAuto"
        :rules="addFormAutoRules"
        ref="addgFormRef1"
        label-width="100px"
      >
        <el-form-item label="商品条形码" prop="id">
          <el-input v-model="addFormAuto.id"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodname">
          <el-input v-model="addFormAuto.goodname"></el-input>
        </el-form-item>
        <el-form-item label="商品品牌" prop="brand">
          <el-input v-model="addFormAuto.brand"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogdVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="addGoodsAuto">确 定</el-button>
      </span>
    </el-dialog>
    <!--匹配商品确认卡片区域-->
    <el-dialog
      title="商品信息确认"
      :visible.sync="matchDialogdVisible"
      width="30%"
      @close="addDialogClosed1"
    >
      <el-form
        :model="matchForm"
        ref="matchFormRef"
        label-width="100px"
      >
        <el-form-item label="商品条形码" prop="id">
          <el-input v-model="matchForm.id"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodname">
          <el-input v-model="matchForm.goodname"></el-input>
        </el-form-item>
        <el-form-item label="商品品牌" prop="brand">
          <el-input v-model="matchForm.brand"></el-input>
        </el-form-item>
        <el-form-item label="GPC类名" prop="gpcname">
          <el-input v-model="matchForm.gpcname"></el-input>
        </el-form-item>
        <el-form-item label="GPC分类号" prop="gpcnum">
          <el-input v-model="matchForm.gpcnum"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC类名" prop="unname">
          <el-input v-model="matchForm.unname"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC分类号" prop="unnum">
          <el-input v-model="matchForm.unnum"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="matchDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="matchGoods">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 内容编辑主体区域 -->
    <el-dialog
      title="修改商品信息"
      :visible.sync="editDialogdVisible"
      width="35%"
      @close="editDialogClosed"
    >
      <el-form
        :model="editForm"
        :rules="editFormRules"
        ref="editgFormRef"
        label-width="130px"
      >
        <el-form-item label="商品条形码" prop="id">
          <el-input v-model="editForm.id"></el-input>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="editForm.goodname" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="商品品牌" prop="brand">
          <el-input v-model="editForm.brand"></el-input>
        </el-form-item>
        <el-form-item label="GPC类名" prop="gpcname">
          <el-input v-model="editForm.gpcname"></el-input>
        </el-form-item>
        <el-form-item label="GPC分类号" prop="gpcnum">
          <el-input v-model="editForm.gpcnum"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC类名" prop="unname">
          <el-input v-model="editForm.unname"></el-input>
        </el-form-item>
        <el-form-item label="UNSPSC分类号" prop="unnum">
          <el-input v-model="editForm.unnum"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogdVisible = false">取 消</el-button>
        <el-button type="primary" @click="editGoods">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        selectable:true,
        fileListUpload:[],
        fileTemp:null,
        data:'',
        queryInfo: {
          pagenum: 1,
          pagesize: 10,
          info:"",
        },
        queryInfo1: {
          pagenum: 1,
          pagesize: 10,
          goodname:"",
          id:"",
          unnum:"",
          gpcnum:""
        },
        state1:"",
        radio:'',
        goodsList: [],
        input: "",
        total: 0,
        addDialogdVisible:false,
        addDialogdVisible1:false,
        matchDialogdVisible:false,
        addManyDialogdVisible:false,
        confirmdialogVisible:false,
        options:[{
          value:'1',
          label:'商品名称'
        },
          {
            value:'2',
            label:'条形码'
          },
          {
            value:'3',
            label:'UNSPSC分类号'
          },
          {
            value:'4',
            label:'GPC分类号'
          }],
        addForm:{
          goodname:"",
          brand:"",
          gpcname:"",
          gpcnum:"",
          id:"",
          unnum:""
        },
        addManyList:[],
        addManyAutoList:[],
        multipleSelection:[],
        addFormAuto:{
          goodname:"",
          brand:"",
          id:"",
        },
        addFormAuto1:{
          goodname:"",
          brand:"",
          id:"",
        },
        matchForm:{
          id:"",
          goodname:"",
          brand:"",
          gpcname:"",
          gpcnum:"",
          unnum:"",
          unname:""
        },
        addFormAutoRules:{
          goodname: [
            {
              required: true,
              message: "请输入商品名称",
              trigger: "blur",
            }
          ],
          brand: [
            {
              required: true,
              message: "请输入商品品牌名",
              trigger: "blur",
            }
          ],
          id: [
            {
              required: true,
              message: "请输入id号",
              trigger: "blur",
            }
          ],
        },
        addFormRules: {
          goodname: [
            {
              required: true,
              message: "请输入商品名称",
              trigger: "blur",
            }
          ],
          brand: [
            {
              required: true,
              message: "请输入商品品牌名",
              trigger: "blur",
            }
          ],
          gpcname: [
            {
              required: true,
              message: "请输入gpc分类名",
              trigger: "blur",
            }
          ],
          gpcnum: [
            {
              required: true,
              message: "请输入gpc分类号",
              trigger: "blur",
            }
          ],
          id: [
            {
              required: true,
              message: "请输入id号",
              trigger: "blur",
            }
          ],
          unnum: [
            {
              required: true,
              message: "请输入UNSPSC分类号",
              trigger: "blur",
            }
          ],
        },
        editManyDialogdVisible:false,
        editManyForm:{
          goodname:"",
          brand:"",
          gpcname:"",
          gpcnum:"",
          id:"",
          unnum:"",
          unname:""
        },
        editDialogdVisible:false,
        editForm:{
          goodname:"",
          brand:"",
          gpcname:"",
          gpcnum:"",
          id:"",
          unnum:"",
          unname:""
        },
        editFormRules: {
          goodname: [
            {
              required: true,
              message: "请输入商品名称",
              trigger: "blur",
            }
          ],
          brand: [
            {
              required: true,
              message: "请输入商品品牌名",
              trigger: "blur",
            }
          ],
          gpcname: [
            {
              required: true,
              message: "请输入gpc分类名",
              trigger: "blur",
            }
          ],
          unname: [
            {
              required: true,
              message: "请输入unspsc分类名",
              trigger: "blur",
            }
          ],
          gpcnum: [
            {
              required: true,
              message: "请输入gpc分类号",
              trigger: "blur",
            }
          ],
          id: [
            {
              required: true,
              message: "请输入id号",
              trigger: "blur",
            }
          ],
          unnum: [
            {
              required: true,
              message: "请输入UNSPSC分类号",
              trigger: "blur",
            }
          ],
        },
      };
    },
    created() {
      this.getGoodsList();
    },
    methods: {
      getGoodsList(){
        if(this.radio == '1'){
          this.queryInfo1.goodname = this.queryInfo.info;
        }else if(this.radio == '2'){
          this.queryInfo1.id = this.queryInfo.info;
        }else if (this.radio == '3') {
          this.queryInfo1.unnum = this.queryInfo.info
        }else{
          this.queryInfo1.gpcnum = this.queryInfo.info
        }
        this.queryInfo1.pagenum = this.queryInfo.pagenum;
        this.queryInfo1.pagesize = this.queryInfo.pagesize;
        this.$axios.get('/goods', {params:this.queryInfo1}).then(res => {
          console.log(res)
          this.goodsList = res.data.content;
          this.total =res.data.totalElements;
        }).catch(err => {
          console.log(err);
        })
      },
      //分页管理
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.queryInfo.pagesize = val;
        this.getGoodsList();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.queryInfo.pagenum = val;
        this.getGoodsList();
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
        this.$axios.post('/deletegood',{id}).then(res => {
          console.log(res)
          if(res.status == 200){
            this.$message.success("删除成功!");
          }
          this.getGoodsList()
        }).catch(err => {
          console.log(err);
        })
      },
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
        this.confirmdialogVisible = true;
      },
      //手动添加
      readTable(){
        this.confirmdialogVisible = false;
        this.addManyDialogdVisible = true;
      },
      //自动批量添加
      readAuto(){
        this.confirmdialogVisible = false;
        this.addManyAutoList = this.addManyList;
        this.addGoodsAuto();
        // console.log(this.addManyAutoList)
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
        // console.log( event.currentTarget)
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
            // console.log(outdata,'outdata')
            this.addManyList = outdata
            this.data = [...outdata]
            // console.log(addlist)
            const arr = [];
            this.data.map(v => {
              // let t=String(v['id'])
              // if(t.length>='11'){
              //   t=t.substring(0,11)
              // }
              const obj={}
              obj.brand = v.brand
              obj.goodname = v.goodname
              obj.gpcname = v.gpcname
              obj.gpcnum = v.gpcnum
              obj.id = v.id
              obj.unnum = v.unnum
              obj.deleted = v.deleted
              arr.push(obj)
            });
            // _this.da=arr;
            // _this.dalen=arr.length;
            // console.log(arr,'arr')
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
      handleSelectionChange(val) {
        this.multipleSelection = val;
        console.log(val);
      },
      goAddmultipage(){
      },
      addDialogClosed(){
        this.$refs.addFormRef.resetFields();
      },
      addManyDialogClosed(){
        // this.$refs.addManyFormRef.resetFields();
      },
      addDialogClosed1(){
        this.$refs.addgFormRef1.resetFields();
        // this.$refs.matchFormRef.resetFields();
      },
      addDialogClosed2(){},
      //批量添加商品
      addManyGoods(){
        if(this.multipleSelection.length==0){
          this.$message.info("未选中任何商品!")
        }else{
          this.$axios.post('/savemanygoods',this.multipleSelection).then(res =>{
            console.log(res)
            if(res.data.success==true){
              this.$message.success(res.data.msg);
            }
          }).catch(err => {
            console.log(err);
          })
          this.addManyDialogdVisible = false;
          this.getGoodsList();
        }
      },
      //添加单个商品
      addGoods(){
        // this.$refs.addgFormRef.validate(async (valid) => {
        //   if (!valid) return;
          //添加数据 await
           this.$axios.post('/savegoods', this.addForm).then(res => {
            console.log(res)
            if(res.data.success == true){
              this.$message.success(res.data.msg)
            }else{
              this.$message.error(res.data.msg)
              var userlist = []
              userlist.push(res.data.data)
              this.goodsList = userlist
              this.total = 1;
            }
            // this.getGoodsList();
          }).catch(err => {
            console.log(err);
          })
          this.addDialogdVisible = false;
          this.getGoodsList();
        // });
      },
      addGoodsAuto(){
        this.addFormAuto1 = this.addFormAuto;
        if(this.addManyAutoList.length == 0){
          this.$refs.addgFormRef1.validate(async (valid) => {
            if (!valid) return;
            this.matchForm = this.addFormAuto1;
              this.addManyAutoList.push(this.addFormAuto);
            // console.log("---yes1---")
            // console.log(this.addManyAutoList)
            //提交数据给后台
            // console.log("----")
            // console.log(this.addFormAuto)
            await this.$axios.post('/saveauto',this.addManyAutoList).then(res => {
              console.log(res)
              // var s = JSON.parse(res.data.data);
              //去除空格
              // s.data = s.data.replace(/\s*/g,'');
              // console.log(s)
              // console.log(s.gpc);
                this.matchForm.gpcnum = res.data.data.gpcnum;
                this.matchForm.gpcname = res.data.data.gpcname;
                this.matchForm.unnum = res.data.data.unnum;
                this.matchForm.unname = res.data.data.unname;
              this.matchDialogdVisible= true;
              this.addManyAutoList = [];
              this.addDialogdVisible1 = false;
              console.log(this.addManyAutoList)
            }).catch(err => {
              console.log(err);
            })
          })
        }else{
          console.log(this.addManyAutoList)
          this.$axios.post('/saveauto',this.addManyAutoList).then(res => {
            console.log(res)
            this.addManyList = res.data.data;
            this.addManyAutoList = [];
            // console.log(this.addManyList);
            this.addManyDialogdVisible = true;
            this.addDialogdVisible1 = false;
            // console.log(this.addManyAutoList)
          }).catch(err => {
            console.log(err);
          })
        }

      },
      matchGoods(){
        this.addForm = this.matchForm;
        console.log(this.addForm)
        this.addGoods();
        this.matchDialogdVisible = false;
      },
      showEditDialog(id){
        this.$axios.post('/findgoods', {id:id}).then(res => {
          console.log(res)
          this.editForm = res.data;
        }).catch(err => {
          console.log(err);
        })
        this.editDialogdVisible = true;
      },
      editDialogClosed(){
        this.$refs.editgFormRef.resetFields();
      },
      editManyDialogClosed(){},
      //提交更新信息
      editGoods(){
        this.$refs.editgFormRef.validate(async (valid) => {
          if (!valid) return;
          console.log(this.editForm)
          this.$axios.post('/updategood',this.editForm)
            .then(res => {
              if(res.status == 200){
                this.$message.success("修改成功!");
              }
              this.getGoodsList();
            }).catch(err => {
            console.log(err);
          })
          this.editDialogdVisible = false;
        });
      }
    },
  };
</script>

<style scoped>
</style>
