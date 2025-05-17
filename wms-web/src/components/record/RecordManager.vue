<script setup lang="ts">
import {ref, onMounted, getCurrentInstance, nextTick, reactive, computed} from "vue";
import  {type AxiosResponse} from "axios";
import { Search } from '@element-plus/icons-vue'
import {ElMessage, type FormInstance, type FormRules} from "element-plus";


const tableData =ref([]);

const instance = getCurrentInstance();

const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
const searchText = ref('');
const storageData:any = ref([]);
const typeData:any = ref([]);
const storage = ref('');
const goodsType = ref('');
const form = ref({
  id:'',
  name:'',
  storage:'',
  goodsType:'',
  adminId:'',
  userId:'',
  createtime:'',
  count:'',
  remark:''
})

const token = ref<any | null>(null);
const tokenStr = sessionStorage.getItem('token');
if (tokenStr) {
  try {
    // 解析 token 字符串为 JSON 对象
    token.value = JSON.parse(tokenStr);
    console.log(token.value);
  }catch (error) {
    console.error('解析 token 失败:', error);
    ElMessage.error('获取管理员信息失败，请重新登录');
  }
}

const loadPostData = () => {
  if (instance) {
    // 使用全局挂载的 $axios
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/record/page', {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      params: {
        name:searchText.value,
        // 这里需要将 storage 和 goodsType 转换为字符串类型
        storage:storage.value+'',
        goodsType:goodsType.value+'',
        roleId:token.value.roleId+'',
        userId:token.value.id+''
      }
    })
        //双重请求只要有用的data数据
        .then((res: AxiosResponse) => res.data) // 第一次解包获取响应体
        .then((res:any) => {
          console.log(res);
          if(res.code === 200){
            tableData.value = res.data;
            total.value = res.total;
          }else{
            alert('获取数据失败');
          }
        });
  }
};

//获取仓库页面数据
const loadStorage=()=>{
  if (instance) {
    // 使用全局挂载的 $axios
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.get('/storage/list')
        .then((res: AxiosResponse) => res.data) // 第一次解包获取响应体
        .then((res:any) => {
          console.log(res);
          if(res.code === 200){
            storageData.value = res.data;
            //console.log(storageData.value);
          }else{
            alert('获取数据失败');
          }
        });
  }
}

//获取分类页面数据
const loadType=()=>{
  if (instance) {
    // 使用全局挂载的 $axios
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.get('/goods/list')
        .then((res: AxiosResponse) => res.data) // 第一次解包获取响应体
        .then((res:any) => {
          console.log(res);
          if(res.code === 200){
            typeData.value = res.data;
            console.log(typeData.value);
          }else{
            alert('获取数据失败');
          }
        });
  }
}

// 每页显示数量改变时触发的函数
const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`)
  pageSize.value = val;
  pageNum.value = 1;
  loadPostData();
}

// 页码改变时触发的函数
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`)
  pageNum.value = val;
  loadPostData();
}

// 重置参数
const resetParam = () => {
  searchText.value = '';
  storage.value = '';
  goodsType.value = '';
  pageNum.value = 1;
  loadPostData();
}

onMounted(() => {
  loadStorage();
  loadType();
  loadPostData();
});

</script>

<template>
  <div class="search">
    <el-input
        v-model="searchText"
        placeholder="请输入种类名"
        :suffix-icon="Search"
        style="width: 200px"
        @keyup.enter.native="loadPostData"
    >
    </el-input>

    <el-select v-model="storage" placeholder="请选择仓库" style="width: 200px;margin-left: 5px">
      <el-option
          v-for="item in storageData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
      />
    </el-select>
    <el-select v-model="goodsType" placeholder="请选择分类" style="width: 200px;margin-left: 5px">
      <el-option
          v-for="item in typeData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
      />
    </el-select>
    <div class="btn">
      <el-button type="primary" style="margin-left: 5px" @click="loadPostData">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
  </div>
  <div class="tab">
    <div class="table-container">
      <el-scrollbar height="100%">
        <el-table
            :data="tableData"
            :header-cell-style="{background:'#818186',color:'#fff'}"
            border
            style="width: 100%"
            max-height="calc(100vh - 120px)"
        >
          <el-table-column prop="id" label="ID" width="180" />

          <el-table-column prop="goodsname" label="物品名" width="180" />

          <el-table-column prop="storagename" label="仓库名" width="180" />

          <el-table-column prop="goodstypename" label="分类名" width="180" />


          <el-table-column prop="adminname" label="操作人" width="100" />

          <el-table-column prop="username" label="申请人" width="100" />

          <el-table-column prop="count" label="数量" width="100" />

          <el-table-column prop="createtime" label="操作时间" width="180" />

          <el-table-column prop="remark" label="备注"/>

        </el-table>
      </el-scrollbar>
    </div>

    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[2, 5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />


  </div>
</template>

<style scoped>
.search{
  margin-bottom: 5px;
  display: flex;
}
el-main {
  padding: 5px;
}
</style>