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

const centerDialogVisible = ref(false);
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

const ruleForm = reactive({
  name:'',
  no:'',
  age:'',
  password:'',
  phone:'',
  sex:'0',
  roleId:'2'
})


const checkAge = (rule: any, value: string, callback: Function) => {
  if (Number(value) > 150) {
    callback(new Error('年龄不能超过150岁'));
  } else {
    callback();
  }
};

const checkDuplicate = (rule: any, value: string, callback: Function) => {
  if (!value) return callback();

  // 添加当前编辑用户的ID参数
  const currentId = form.value.id;
  const axiosInstance = instance?.appContext.config.globalProperties.$axios;
  axiosInstance.get(`/findByNo?no=${value}${currentId ? `&currentId=${currentId}` : ''}`)
      .then((res: AxiosResponse) => res.data)
      .then((res:any) => {
        if (res.code === 200 && res.data.length > 0) {
          callback(new Error('账号已存在'));
        } else {
          callback();
        }
      }).catch(() => callback());
};

// 定义一个变量来标记当前是新增还是编辑
const isEdit = ref(false);

// 表单验证规则
const rules = reactive<FormRules<typeof ruleForm>>({
  name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
  no: computed(() => {
    if (isEdit.value) {
      return []; // 编辑时移除验证规则
    }
    return [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { required: true, message: '账号长度为 2 - 8 位', trigger: 'blur', min: 2, max: 8 },
      { validator: checkDuplicate, trigger: 'blur' }
    ];
  }),
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { required: true, message: '密码长度为 3 - 8 位', trigger: 'blur', min: 3, max: 8 }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' },
    { required: true, message: '长度在 1 - 3 个位', trigger: 'blur', min: 1, max: 3 },
    { pattern: /^([1-9][0-9]*){1,3}$/, message: '年龄必须为正整数字', trigger: 'blur' },
    { validator: checkAge, trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9][0-9]\d{8}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
});

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


// 打开提交表单
const add =  () => {
  isEdit.value = false;
  centerDialogVisible.value = true;
  nextTick(); // 等待对话框渲染完成
  resetForm();
  form.value.no = '';
};

// 编辑表单
// 修改编辑方法
const mod = (row:any) => {
  isEdit.value = true;
  centerDialogVisible.value = true;

  form.value.id = row.id;
  form.value.no = row.no;
  form.value.name = row.name;
  form.value.password = '';
  form.value.phone = row.phone;
  form.value.age = row.age + '';
  form.value.sex = row.sex + '';
  form.value.roleId = row.roleId;

}
const del = (id:any)=>{
  console.log(id);
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.get('/del?id='+id, form.value)
        .then((res: AxiosResponse) => res.data)
        .then(() => {
          ElMessage({
            message: '删除成功！',
            type: 'success',
          })
          loadPostData();
        })
  }else {
    ElMessage.error('提交失败！')
  }
}

const doSave = () => {
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/save', form.value)
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
    axiosInstance.post('/mod', form.value)
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
    no: '',
    age: '',
    password: '',
    phone: '',
    sex: '0',
    roleId: '2'
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
        <el-form-item label="账号" prop="no">
          <el-col :span="11">
            <el-input v-model="form.no" :disabled="isEdit"/>
          </el-col>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-col :span="11">
            <el-input v-model="form.password" />
          </el-col>
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-col :span="11">
            <el-input v-model="form.name" />
          </el-col>
        </el-form-item>

        <el-form-item label="年龄" prop="age">
          <el-col :span="11">
            <el-input v-model="form.age" />
          </el-col>
        </el-form-item>

        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio value="1">男</el-radio>
            <el-radio value="0">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-col :span="11">
            <el-input v-model="form.phone" />
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