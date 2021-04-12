<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>分类标准信息管理</el-breadcrumb-item>
      <el-breadcrumb-item>信息列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 搜索框头部 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入内容"
            v-model="input"
            class="input-with-select"
          >
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="goAddpage">添加商品</el-button>
        </el-col>
      </el-row>
      <!-- 表格区域 -->
      <el-table :data="goodsList" border stripe>
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="goods_name" label="商品名称"></el-table-column>
        <el-table-column prop="goods_un" label="UNSPSC分类号"></el-table-column>
        <el-table-column prop="goods_gpc" label="GPC分类号"></el-table-column>
        <el-table-column prop="add_time" label="创建时间">
          <template slot-scope="scope">
            {{scope.row.add_time | dateFormat}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <!-- 修改 -->
            <el-button type="primary" icon="el-icon-edit" size="mini" >编辑</el-button>
            <!-- 删除 -->
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeGoods(scope.row.goods_id)">删除</el-button>
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
  </div>
</template>

<script>
  export default {
    data() {
      return {
        queryInfo: {
          query: "",
          pagenum: 1,
          pagesize: 10,
        },
        goodsList: [],
        input: "",
        total: 0,
      };
    },
    created() {
      // this.getGoodsList();
    },
    methods: {
      //获取商品列表
      // async getGoodsList() {
      //   const { data: res } = await this.$http.get("goods", {
      //     params: this.queryInfo,
      //   });
      //   if (res.meta.status !== 200)
      //     return this.$message.error("商品列表获取失败!");
      //   //console.log(res.data)
      //   this.goodsList = res.data.goods;
      //   this.total = res.data.total;
      // },
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
        //console.log(attr_id)
        const {data:res} = await this.$http.delete(`goods/${id}`)
        //console.log(res)
        if(res.meta.status !== 200){
          return this.$message.error("删除参数失败!")
        }
        this.getGoodsList()
        this.$message.success("删除参数成功!")
      },
      goAddpage(){
        this.$router.push('/goods/add')
      }
    },
  };
</script>

<style scoped>
</style>
