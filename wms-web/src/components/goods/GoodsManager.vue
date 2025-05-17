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
const centerDialogVisible = ref(false);
const form = ref({
  id:'',
  name:'',
  remark:''
})

const formRef = ref<FormInstance>();

const ruleForm = reactive({
  name:'',
  remark:''
})

// 定义一个变量来标记当前是新增还是编辑
const isEdit = ref(false);

// 表单验证规则
const rules = reactive<FormRules<typeof ruleForm>>({
  name: [{required: true, message: '请输入仓库名', trigger: 'blur'}]
});

const loadPostData = () => {
  if (instance) {
    // 使用全局挂载的 $axios
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/goods/page', {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      params: {
        name:searchText.value
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


// 打开提交表单
const add =  () => {
  isEdit.value = false;
  centerDialogVisible.value = true;
  nextTick(); // 等待对话框渲染完成
  resetForm();
  form.value.name = '';
};

// 修改编辑方法
const mod = (row:any) => {
  isEdit.value = true;
  centerDialogVisible.value = true;
  form.value.id = row.id;
  form.value.name = row.name;
}
const del = (id:any)=>{
  console.log(id);
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.get('/goods/del?id='+id,form.value)
        .then((res: AxiosResponse) => res.data)
        .then(() => {
          ElMessage({
            message: '删除成功！',
            type: 'success',
          })
          loadPostData();
        })
  }else {
    ElMessage.error('删除失败！')
  }
}

const doSave = () => {
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/goods/save', form.value)
        .then((res: AxiosResponse) => res.data)
        .then(() => {
          centerDialogVisible.value = false;
          resetForm();
          ElMessage({
            message: '提交成功！',
            type: 'success',
          })
          loadPostData();
        })
  }else {
    ElMessage.error('提交失败！')
  }
}

const doMod = () => {
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/goods/update', form.value)
        .then((res: AxiosResponse) => res.data)
        .then(() => {
          centerDialogVisible.value = false;
          resetForm();
          ElMessage({
            message: '提交成功！',
            type: 'success',
          })
          loadPostData();
        })
  }else {
    ElMessage.error('提交失败！')
  }
}

//表单数据提交到后台
const save = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (form.value.id) {
        doMod();
      } else {
        doSave();
      }
    } else {
      console.log('error submit!!');
    }
  });
};

//表单数据重置
const resetForm = () => {
  form.value = {
    id: '',
    name: '',
    remark: ''
  };
  (formRef.value as FormInstance)?.resetFields();
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
        placeholder="请输入种类名"
        :suffix-icon="Search"
        style="width: 200px"
        @keyup.enter.native="loadPostData"
    >
    </el-input>
    <div class="btn">
      <el-button type="primary" style="margin-left: 5px" @click="loadPostData">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

      <el-button type="primary"  @click="add">新增</el-button>

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

          <el-table-column prop="name" label="种类名" width="180" />


          <el-table-column prop="remark" label="备注"/>

          <el-table-column prop="properte" label="操作">
            <template #default="scope">
              <el-button  @click="mod(scope.row)">
                编辑
              </el-button>
              <el-popconfirm title="要进行删除吗?" @confirm="del(scope.row.id)">
                <template #reference>
                  <el-button
                      type="danger"
                  >
                    删除
                  </el-button>
                </template>
              </el-popconfirm>

            </template>
          </el-table-column>
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

<!--    新增编辑表单-->
    <el-dialog
        v-model="centerDialogVisible"
        title="Warning"
        width="500"
        align-center
    >

      <el-form :model="form"
               :rules="rules"
               ref="formRef"
               label-width="auto"
               style="max-width: 600px"
      >

        <el-form-item label="种类名" prop="name">
          <el-col :span="11">
            <el-input v-model="form.name" :disabled="isEdit"/>
          </el-col>
        </el-form-item>


        <el-form-item label="备注" prop="remark">
          <el-col :span="11">
            <el-input type="textarea" v-model="form.remark" />
          </el-col>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>

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