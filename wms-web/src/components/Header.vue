<script setup lang="ts">
import {ref, defineEmits, defineProps, onMounted} from "vue";
import {ArrowDownBold,Fold,Expand} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import {ElMessage, ElMessageBox} from "element-plus";
//退出登录
const logout =async () => {
  console.log("logout")
  await ElMessageBox.confirm('确定退出登录么？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.push('/')
    sessionStorage.removeItem('token');
    ElMessage({
      type: 'success',
      message: '已登出！'
    });
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消登出！'
    });
  });

}

const router = useRouter();


/*const toUser =async () => {
  console.log("toUser")
  await ElMessageBox.confirm('前往个人中心？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.push('/Home')
    ElMessage({
      type: 'success',
      message: '操作成功！'
    });
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '操作失败！'
    });
  });

}*/

const emit = defineEmits(["collapse"]);
const isCollapse = ref(false);

// 从sessionStorage中获取token用户登录信息
const token = ref<string | null>(null);
const userName = ref<string>('');
onMounted(() => {
  router.push('/Home');
  token.value = sessionStorage.getItem('token');
  if (token.value) {
    try {
      // 直接解析token字符串为JSON对象
      const userData = JSON.parse(token.value);
      userName.value = userData.name || '未知用户';
      console.log('当前用户:', userName.value);
    } catch (e) {
      console.error('token解析失败:', e);
      userName.value = '未知用户';
    }
  }
});

const collapse = () => {
  isCollapse.value = !isCollapse.value;
  emit('collapse', isCollapse.value);
};

const props = defineProps<{
  isCollapse: boolean;
}>();
</script>

<template>
  <div class="layout">
    <div class="fold" >
      <el-icon :size="20" @click="collapse" v-if="!isCollapse" style="cursor: pointer">
        <Fold />
      </el-icon>
      <el-icon :size="20" @click="collapse" v-else style="cursor: pointer">
        <Expand />
      </el-icon>
    </div>
    <div class="title">
      <span>
      欢迎来到仓库管理系统
      </span>
    </div>
    <div class="toolbar">
      <span>{{userName}}</span>
      <el-dropdown>
        <el-icon :size="18" style="margin-left: 8px; margin-top: 1px" >
          <ArrowDownBold />
        </el-icon>
        <template #dropdown>
          <el-dropdown-menu>
<!--            <el-dropdown-item @click="toUser">个人中心</el-dropdown-item>-->
            <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  width: 100%;
}

.toolbar {
  display: flex;
  align-items: center;
  font-size: 16px;
}

.title {
  text-align: center;
  flex: 2;
  margin-top: 1px;
  font-size: 25px;
  font-weight: bold;
}

.fold {
  margin-top: 1px;
  margin-left: 10px;
}
</style>