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
const sex = ref('');
const emit = defineEmits(["doSelectUser"]);
const sexs = [
  {
    value: '1',
    label: '男'
  },
  {
    value: '0',
    label: '女'
  }
]

const form = ref({
  id:'',
  name:'',
  no:'',
  age:'',
  password:'',
  phone:'',
  sex:'0',
  roleId:'2'
})

const formRef = ref<FormInstance>();


const loadPostData = () => {
  if (instance) {
    // 使用全局挂载的 $axios
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/pageC1', {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      params: {
        name:searchText.value,
        sex:sex.value,
        roleId:'2'
      }
    })
        //双重请求只要有用的data数据
        .then((res: AxiosResponse) => res.data) // 第一次解包获取响应体
        .then((res:any) => {  // 这里应该已经是响应体数据，去掉AxiosResponse类型
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
  sex.value = '';
  pageNum.value = 1;
  loadPostData();
}


const handleSelectionChange = (val:any) => {
  //currentRow.value = val
  emit('doSelectUser', val);

}


onMounted(() => {
  // loadGetData();
  loadPostData();
});

</script>

<template>
  <div class="search">
    <el-input
        v-model="searchText"
        placeholder="请输入姓名"
        :suffix-icon="Search"
        style="width: 200px"
        @keyup.enter.native="loadPostData"
    >
    </el-input>
    <el-select v-model="sex" placeholder="请选择性别" style="width: 115px;margin-left: 5px">
      <el-option
          v-for="item in sexs"
          :key="item.value"
          :label="item.label"
          :value="item.value"
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
            highlight-current-row
            @row-click="handleSelectionChange"
        >
          <el-table-column prop="id" label="ID" width="180" />
          <el-table-column prop="no" label="账号" width="180"/>
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="sex" label="性别" width="100">
            <template #default="scope">
              <el-tag
                  :type="scope.row.sex === 1 ? 'primary' : 'success'"
                  disable-transitions
              >{{ scope.row.sex === 1 ? '男' : '女'}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="roleId" label="角色" width="180">
            <template #default="scope">
              <el-tag
                  :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                  disable-transitions
              >{{ scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="age" label="年龄" width="100"/>
          <el-table-column prop="phone" label="电话" width="180"/>

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