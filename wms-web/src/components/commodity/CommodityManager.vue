<script setup lang="ts">
import {ref, onMounted, getCurrentInstance, nextTick, reactive, computed} from "vue";
import  {type AxiosResponse} from "axios";
import { Search } from '@element-plus/icons-vue'
import {ElMessage, type FormInstance, type FormRules} from "element-plus";
import dayjs from 'dayjs';
import SelectUser from "@/components/user/SelectUser.vue";

const tableData =ref([]);

const instance = getCurrentInstance();

const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
const searchText = ref('');
const centerDialogVisible = ref(false);
const inDialogVisible = ref(false);
const storageData = ref<any>([]);
const typeData = ref<any>([]);
const storage = ref('');
const goodsType = ref('');
const currentRow = ref<any>({});
const innerVisible = ref(false);
const tempUser = ref<any>({});
const form = ref({
  id:'',
  name:'',
  storage:'',
  goodsType:'',
  count:'',
  remark:''
})

const formRef = ref<FormInstance>();

const goodsForm = ref({
  goods:'',
  goodsname:'',
  count:'',
  username:'',
  userId:'',
  createtime:'',
  adminId:'',
  remark:'',
  action:'',
})

const goodsFormRef = ref<FormInstance>();

const goodsRules = reactive<FormRules<typeof goodsForm>>({

})

const ruleForm = reactive({
  name:'',
  storage:'',
  goodsType:'',
  count:'',
  remark:''
})

// 定义一个变量来标记当前是新增还是编辑
const isEdit = ref(false);

let checkCount = (rule:any, value:any, callback:any) => {
  if(value>9999){
    callback(new Error('数量输入过大'));
  }else {
    callback();
  }
};

// 表单验证规则
const rules = reactive<FormRules<typeof ruleForm>>({
  name: [{required: true, message: '请输入仓库名', trigger: 'blur'}],
  storage: [{required: true, message: '请选择仓库', trigger: 'blur'}],
  goodsType: [{required: true, message: '请选择分类', trigger: 'blur'}],
  count: [{required: true, message: '请输入数量', trigger: 'blur'},
    {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数', trigger: 'blur'},
    {validator: checkCount, trigger: 'blur'}],
});

const loadPostData = () => {
  if (instance) {
    // 使用全局挂载的 $axios
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/commodity/page', {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      params: {
        name:searchText.value,
        // 这里需要将 storage 和 goodsType 转换为字符串类型
        storage:storage.value+'',
        goodsType:goodsType.value+'',
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

//仓库比对
const formatStorage = (row: any) => {
  const temp:any = storageData.value.find((item: any) => {
    return item.id === row.storage;
  });
  return temp ? temp.name : '未找到仓库';
};

//类型比对
const formatType = (row: any) => {
  const temp:any = typeData.value.find((item: any) => {
    return item.id === row.goodsType;
  });
  return temp? temp.name : '未找到分类';
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
  storage.value = '';
  goodsType.value = '';
  pageNum.value = 1;
  loadPostData();
}


// 打开提交表单
const add =  () => {
  isEdit.value = false;
  centerDialogVisible.value = true;
  nextTick(() => {
    resetForm();
  });
};

//打开入库表单
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

const pushIn = () => {
  if (!currentRow.value.id) {
    alert('请选择要入库的商品');
    return;
  }
  inDialogVisible.value = true;
  nextTick(() => {
    resetFormIn();
    if (tokenStr) {
      try {
        // 解析 token 字符串为 JSON 对象
        goodsForm.value.goodsname = currentRow.value.name;
        goodsForm.value.goods = currentRow.value.id;
        goodsForm.value.action = '1'
        // 从解析后的对象中获取 adminId
        goodsForm.value.adminId = token.value.id;
      } catch (error) {
        console.error('解析 token 失败:', error);
        ElMessage.error('获取管理员信息失败，请重新登录');
        return;
      }
    } else {
      ElMessage.error('未获取到登录信息，请重新登录');
      return;
    }
    console.log(token.value);
    console.log(goodsForm.value);
  });
}


//出库
const pushOut = () => {
  if (!currentRow.value.id) {
    alert('请选择要出库的商品');
    return;
  }
  inDialogVisible.value = true;
  nextTick(() => {
    resetFormIn();
    const tokenStr = sessionStorage.getItem('token');
    if (tokenStr) {
      try {
        // 解析 token 字符串为 JSON 对象
        token.value = JSON.parse(tokenStr);
        goodsForm.value.goodsname = currentRow.value.name;
        goodsForm.value.goods = currentRow.value.id;
        goodsForm.value.action = '2'
        // 从解析后的对象中获取 adminId
        goodsForm.value.adminId = token.value.id;
      } catch (error) {
        console.error('解析 token 失败:', error);
        ElMessage.error('获取管理员信息失败，请重新登录');
        return;
      }
    } else {
      ElMessage.error('未获取到登录信息，请重新登录');
      return;
    }
    console.log(token.value);
    console.log(goodsForm.value);
  });
}


const handleSelectionChange = (val:any) => {
  currentRow.value = val
}

// 修改编辑方法
const mod = (row:any) => {
  isEdit.value = true;
  centerDialogVisible.value = true;
  form.value.id = row.id;
  form.value.name = row.name;
  form.value.storage = row.storage;
  form.value.goodsType = row.goodsType;
  form.value.count = row.count;
  form.value.remark = row.remark;
}
const del = (id:any)=>{
  console.log(id);
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.get('/commodity/del?id='+id,form.value)
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
    axiosInstance.post('/commodity/save', form.value)
        .then((res: AxiosResponse) => res.data)
        .then((res: any) => {
          if(res.code === 200) {
            centerDialogVisible.value = false;
            ElMessage.success('提交成功！');
            loadPostData();
          } else {
            ElMessage.error(res.msg || '提交失败');
          }
        })
        .catch((error: any) => {
          ElMessage.error('请求错误: ' + error.message);
        });
  }
};

const doInSave = () => {
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/record/save', goodsForm.value)
        .then((res: AxiosResponse) => res.data)
        .then((res: any) => {
          if(res.code === 200) {
            inDialogVisible.value = false;
            ElMessage.success('提交成功！');
            loadPostData();
            resetFormIn();
          } else {
            ElMessage.error(res.msg || '提交失败');
          }
        })
        .catch((error: any) => {
          ElMessage.error('请求错误: ' + error.message);
        });
  }
};

const selectUser = () => {
  innerVisible.value = true;
}

const confirmUser = () => {
  innerVisible.value = false;
  console.log('tempUser.value:', tempUser.value);
  if (tempUser.value && tempUser.value.id) {
    goodsForm.value.userId = tempUser.value.id.toString();
    goodsForm.value.username = tempUser.value.name;
    console.log('赋值后的 goodsForm:', goodsForm.value);
  } else {
    ElMessage.error('申请人信息获取失败，请重新选择');
  }
};


const doSelectUser = (val:any)=>{
  console.log(val);
  tempUser.value = val
}

const doMod = () => {
  if(instance){
    const axiosInstance = instance.appContext.config.globalProperties.$axios;
    axiosInstance.post('/commodity/update', form.value)
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

//入库表单数据提交到后台
const inSave = () => {
  goodsFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      // 生成当前时间并转换为后端期望的格式
      const currentTime = dayjs().format('YYYY-MM-DD HH:mm:ss');
      goodsForm.value.createtime = currentTime;
      console.log('即将提交的入库表单数据:', goodsForm.value); // 打印提交的数据
      if (!goodsForm.value.userId) {
        ElMessage.error('请选择申请人');
        return;
      }
      doInSave();
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
    storage:'',
    goodsType:'',
    count:'',
    remark: ''
  };
  (formRef.value as FormInstance)?.resetFields();
}

const resetFormIn = () => {
  goodsForm.value = {
    goods: '',
    goodsname: '',
    createtime: '',
    count: '',
    username:'',
    userId:'',
    adminId: '',
    remark: '',
    action: ''
  };
  (goodsFormRef.value as FormInstance)?.resetFields();
}

onMounted(() => {
  loadStorage();
  loadType();
  loadPostData();
  console.log(token.value);
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

      <el-button type="primary"  @click="add" v-if="token.roleId!=2">新增</el-button>
      <el-button type="primary"  @click="pushIn" v-if="token.roleId!=2">入库</el-button>
      <el-button type="primary"  @click="pushOut" v-if="token.roleId!=2">出库</el-button>

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
            stripe
            highlight-current-row
            @row-click="handleSelectionChange"
        >
          <el-table-column prop="id" label="ID" width="180" />

          <el-table-column prop="name" label="物品名" width="180" />

          <el-table-column label="仓库名" width="180">
            <template #default="scope">
              {{ formatStorage(scope.row) }}
            </template>
          </el-table-column>

          <el-table-column prop="goodsType" label="分类名" width="180">
            <template #default="scope">
              {{ formatType(scope.row) }}
            </template>
          </el-table-column>

          <el-table-column prop="count" label="数量" width="100" />

          <el-table-column prop="remark" label="备注"/>

          <el-table-column prop="properte" label="操作" v-if="token.roleId!=2">
            <template #default="scope">
              <el-button  @click="mod(scope.row)" >
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

        <el-form-item label="物品名" prop="name">
          <el-col :span="11">
            <el-input v-model="form.name" :disabled="isEdit"/>
          </el-col>
        </el-form-item>

        <el-form-item label="仓库名" prop="storage">
          <el-col :span="11">
            <el-select v-model="form.storage" placeholder="请选择仓库" style="width: 180px">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>

          </el-col>
        </el-form-item>

        <el-form-item label="分类名" prop="goodsType">
          <el-col :span="11">
            <el-select v-model="form.goodsType" placeholder="请选择分类" style="width: 180px">
              <el-option
                  v-for="item in typeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="数量" prop="count">
          <el-col :span="11">
            <el-input v-model="form.count" />
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

    <!--    出入库表单-->
    <el-dialog
        v-model="inDialogVisible"
        title="操作面板"
        width="500"
        align-center
    >
      <el-dialog
          v-model="innerVisible"
          width="75%"
          title="申请人选择"
          append-to-body
      >
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="innerVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmUser">
              提交
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-form :model="goodsForm"
               :rules="goodsRules"
               ref="goodsFormRef"
               label-width="auto"
               style="max-width: 600px"
      >

        <el-form-item label="物品名">
          <el-col :span="11">
            <el-input v-model="goodsForm.goodsname" readonly/>
          </el-col>
        </el-form-item>

        <el-form-item label="申请人">
          <el-col :span="11">
            <el-input v-model="goodsForm.username" readonly @click="selectUser"/>
          </el-col>
        </el-form-item>

        <el-form-item label="数量" prop="count">
          <el-col :span="11">
            <el-input v-model="goodsForm.count" />
          </el-col>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-col :span="11">
            <el-input type="textarea" v-model="goodsForm.remark" />
          </el-col>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="inDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="inSave">
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