<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo" :class="{ 'collapsed-logo': isCollapse }">
        <img src="../assets/img_2.png" alt="logo">
        <span v-show="!isCollapse">管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        :collapse="isCollapse"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/layout">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="1">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/layout/user-management">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/layout/product-management">
            <el-icon><ShoppingCart /></el-icon>
            <span>产品管理</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>文档管理</span>
          </template>
          <el-menu-item index="2-1">
            <el-icon><Files /></el-icon>
            <span>文件列表</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 主要内容区 -->
    <el-container class="main-container" :class="{ 'collapsed-main': isCollapse }">
      <el-header class="header">
        <div class="header-left">
          <el-icon 
            class="toggle-sidebar" 
            @click="toggleSidebar"
          >
            <component :is="isCollapse ? Expand : Fold" />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/layout' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRoute }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-space>
            <div class="search-wrapper" :class="{ 'is-active': showSearch }">
              <el-icon class="search-icon" @click="handleSearchClick">
                <Search />
              </el-icon>
              <el-input
                v-model="searchKeyword"
                class="search-input"
                placeholder="请输入菜单内容"
                ref="searchInput"
                @blur="handleSearchBlur"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </div>

            <div class="icon-button" @click="toggleFullScreen">
              <el-tooltip :content="isFullscreen ? '退出全屏' : '全屏'" placement="bottom">
                <el-icon>
                  <component :is="isFullscreen ? Aim : FullScreen" />
                </el-icon>
              </el-tooltip>
            </div>
            
            <div class="icon-button">
              <el-tooltip content="消息" placement="bottom">
                <el-badge :value="3">
                  <el-icon>
                    <Bell />
                  </el-icon>
                </el-badge>
              </el-tooltip>
            </div>

            <el-dropdown @command="handleCommand">  
              <span class="user-info">
                <el-avatar 
                  size="small" 
                  :src="avatarUrl" 
                  @error="handleAvatarError"
                >
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span>{{ username }}</span>
                <el-icon class="el-icon--right">
                  <CaretBottom />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人信息
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-space>
        </div>
      </el-header>

      <!-- 添加标签页导航 -->
      <div class="tabs-nav">
        <el-tabs
          v-model="activeTab"
          type="card"
          closable
          @tab-click="handleTabClick"
          @tab-remove="handleTabRemove"
        >
          <el-tab-pane
            v-for="tab in visitedViews"
            :key="tab.path"
            :label="tab.title"
            :name="tab.path"
            :closable="tab.path !== '/layout'"
            @contextmenu.prevent="handleContextMenu($event, tab)"
          >
          </el-tab-pane>
        </el-tabs>

        <!-- 右键菜单 -->
        <ul v-show="contextMenuVisible" 
            :style="{ left: contextMenuX + 'px', top: contextMenuY + 'px' }" 
            class="contextmenu"
        >
          <li @click="refreshSelected">
            <el-icon><Refresh /></el-icon>
            重新加载
          </li>
          <li @click="closeSelected" v-if="selectedTab.path !== '/layout'">
            <el-icon><Close /></el-icon>
            关闭标签页
          </li>
          <li @click="closeOthers">
            <el-icon><CircleClose /></el-icon>
            关闭其他标签页
          </li>
          <li @click="closeLeft">
            <el-icon><ArrowLeftBold /></el-icon>
            关闭左侧标签页
          </li>
          <li @click="closeRight">
            <el-icon><ArrowRightBold /></el-icon>
            关闭右侧标签页
          </li>
          <li @click="closeAll">
            <el-icon><Delete /></el-icon>
            关闭全部标签页
          </li>
        </ul>
      </div>

      <!-- 搜索结果下拉框 -->
      <div v-show="showSearch && searchResults.length" class="search-dropdown">
        <div
          v-for="item in searchResults"
          :key="item.path"
          class="search-item"
          @click="handleSearchItemClick(item)"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </div>
      </div>

      <el-main class="main">
        <router-view v-slot="{ Component }">
          <component :is="Component" :key="refreshKey" />
        </router-view>
      </el-main>
    </el-container>

    <!-- 搜索对话框 -->
    <el-dialog
      v-model="showSearch"
      title="搜索菜单"
      width="500px"
      :show-close="false"
      @closed="handleSearchClose"
    >
      <el-input
        v-model="searchKeyword"
        placeholder="搜索菜单"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <div class="search-result" v-if="searchResults.length">
        <div
          v-for="item in searchResults"
          :key="item.path"
          class="search-item"
          @click="handleSearchItemClick(item)"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </div>
      </div>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted, onBeforeUnmount, nextTick, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  House, 
  User, 
  Fold,
  Expand,
  Setting,
  Document,
  Files,
  ShoppingCart,
  Bell,
  FullScreen,
  CaretBottom,
  SwitchButton,
  Refresh,
  Close,
  CircleClose,
  ArrowLeftBold,
  ArrowRightBold,
  Delete,
  Search,
  Aim
} from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const username = ref(localStorage.getItem('username') || '管理员')
const isCollapse = ref(false)
const activeMenu = ref(route.path)
const activeTab = ref('/layout')
const visitedViews = ref([
  {
    title: '首页',
    path: '/layout'
  }
])

// 右键菜单相关
const contextMenuVisible = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)
const selectedTab = ref({})

// 添加 refreshKey
const refreshKey = ref(0)

// 全屏相关
const isFullscreen = ref(false)

// 获取页面标题
const getTitle = (path) => {
  const routeMap = {
    '/layout': '首页',
    '/layout/user-management': '用户管理',
    '/layout/product-management': '产品管理',
    '/layout/user-profile': '个人信息'
  }
  return routeMap[path]
}

// 监听路由变化，添加标签页
watch(
  () => route.path,
  (newPath) => {
    const title = getTitle(newPath)
    if (title && !visitedViews.value.find(v => v.path === newPath)) {
      visitedViews.value.push({
        title,
        path: newPath
      })
    }
    activeTab.value = newPath
  },
  { immediate: true }
)

const currentRoute = computed(() => {
  const routeMap = {
    'user-management': '用户管理',
    'product-management': '产品管理',
    'user-profile': '个人信息'
  }
  return routeMap[route.name] || '首页'
})

// 切换侧边栏
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 切换全屏
const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    isFullscreen.value = true
  } else {
    document.exitFullscreen()
    isFullscreen.value = false
  }
}

// 监听全屏变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

const userInfo = reactive({
  username: '',
  nickname: '',
  avatar: '',
  // ... other user info fields
})

// 计算头像完整URL
const avatarUrl = computed(() => {
  if (!userInfo.avatar) return ''
  console.log('原始头像路径:', userInfo.avatar)
  // 如果已经是完整URL则直接返回
  if (userInfo.avatar.startsWith('http')) {
    console.log('使用完整URL:', userInfo.avatar)
    return userInfo.avatar
  }
  // 使用后端返回的路径
  const url = `http://localhost:8080${userInfo.avatar}`
  console.log('完整的头像URL:', url)
  return url
})

// 处理头像加载错误
const handleAvatarError = () => {
  userInfo.avatar = ''
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      ElMessage.error('请先登录')
      return
    }

    const response = await axios.get('/api/users/profile', {
      headers: { 
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })

    console.log('获取用户信息响应:', response.data)

    if (response.data.code === 200) {
      const data = response.data.data
      // 更新用户信息
      Object.assign(userInfo, {
        ...data,
        avatar: data.avatar || ''
      })
      // 更新用户名显示
      username.value = data.nickname || data.username
    } else {
      ElMessage.error(response.data.message || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    if (error.response?.status === 403) {
      ElMessage.error('没有权限或 token 已过期，请重新登录')
    } else {
      ElMessage.error('获取用户信息失败')
    }
  }
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm(
        '确定要退出登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      router.push('/')
      ElMessage.success('退出成功')
    } catch (error) {
      console.log('取消退出')
    }
  } else if (command === 'profile') {
    router.push('/layout/user-profile')
  } else if (command === 'settings') {
    ElMessage.info('系统设置功能开发中...')
  }
}

const fetchUsers = async () => {
  try {
    const token = localStorage.getItem('token');
    if (!token) {
      ElMessage.error('未登录，请先登录');
      router.push('/');
      return;
    }

    const response = await axios.get('/api/users', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (response.data.code === 200) {
      console.log('用户数据:', response.data.data);
    } else {
      ElMessage.error(response.data.message || '获取用户数据失败');
    }
  } catch (error) {
    console.error('获取用户数据错误:', error);
    if (error.response?.status === 403) {
      ElMessage.error('登录已过期，请重新登录');
      router.push('/');
    } else {
      ElMessage.error('获取用户数据失败');
    }
  }
};

// 处理标签页点击
const handleTabClick = (tab) => {
  if (tab.props.name !== route.path) {
    router.push(tab.props.name)
  }
}

// 处理标签页关闭
const handleTabRemove = (targetPath) => {
  const tabs = visitedViews.value
  let activePath = activeTab.value
  if (activePath === targetPath) {
    tabs.forEach((tab, index) => {
      if (tab.path === targetPath) {
        const nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          activePath = nextTab.path
        }
      }
    })
  }
  visitedViews.value = tabs.filter(tab => tab.path !== targetPath)
  if (activePath) {
    router.push(activePath)
  }
}

// 处理右键菜单
const handleContextMenu = (e, tab) => {
  e.preventDefault()
  contextMenuVisible.value = true
  contextMenuX.value = e.clientX
  contextMenuY.value = e.clientY
  selectedTab.value = tab
}

// 关闭右键菜单
const closeContextMenu = () => {
  contextMenuVisible.value = false
}

// 修改刷新方法
const refreshSelected = () => {
  refreshKey.value = Date.now()
  closeContextMenu()
  ElMessage.success('刷新成功')
}

// 关闭选中标签页
const closeSelected = () => {
  handleTabRemove(selectedTab.value.path)
  closeContextMenu()
}

// 关闭其他标签页
const closeOthers = () => {
  visitedViews.value = visitedViews.value.filter(
    tab => tab.path === '/layout' || tab.path === selectedTab.value.path
  )
  if (selectedTab.value.path !== route.path) {
    router.push(selectedTab.value.path)
  }
  closeContextMenu()
}

// 关闭左侧标签页
const closeLeft = () => {
  const selectedIndex = visitedViews.value.findIndex(tab => tab.path === selectedTab.value.path)
  visitedViews.value = visitedViews.value.filter((tab, index) => 
    tab.path === '/layout' || index >= selectedIndex
  )
  if (!visitedViews.value.find(tab => tab.path === route.path)) {
    router.push(selectedTab.value.path)
  }
  closeContextMenu()
}

// 关闭右侧标签页
const closeRight = () => {
  const selectedIndex = visitedViews.value.findIndex(tab => tab.path === selectedTab.value.path)
  visitedViews.value = visitedViews.value.filter((tab, index) => 
    tab.path === '/layout' || index <= selectedIndex
  )
  if (!visitedViews.value.find(tab => tab.path === route.path)) {
    router.push(selectedTab.value.path)
  }
  closeContextMenu()
}

// 关闭全部标签页
const closeAll = () => {
  visitedViews.value = visitedViews.value.filter(tab => tab.path === '/layout')
  router.push('/layout')
  closeContextMenu()
}

// 点击其他区域关闭右键菜单
onMounted(() => {
  getUserInfo()
  fetchUsers();
  document.addEventListener('click', closeContextMenu)
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeContextMenu)
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})

// 搜索相关
const showSearch = ref(false)
const searchKeyword = ref('')
const searchResults = ref([])
const searchInput = ref(null)

const menuList = [
  { path: '/layout', title: '首页', icon: 'House' },
  { path: '/layout/user-management', title: '用户管理', icon: 'User' },
  { path: '/layout/product-management', title: '产品管理', icon: 'ShoppingCart' },
  { path: '/layout/user-profile', title: '个人信息', icon: 'User' }
]

const handleSearchClick = () => {
  showSearch.value = true
  nextTick(() => {
    searchInput.value.focus()
  })
}

const handleSearchBlur = () => {
  if (!searchKeyword.value) {
    showSearch.value = false
  }
}

const handleSearch = () => {
  if (!searchKeyword.value) {
    searchResults.value = []
    return
  }
  searchResults.value = menuList.filter(item => 
    item.title.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
}

const handleSearchItemClick = (item) => {
  router.push(item.path)
  showSearch.value = false
  searchKeyword.value = ''
}

const handleSearchClose = () => {
  searchKeyword.value = ''
  searchResults.value = []
}

// 监听搜索关键词变化
watch(searchKeyword, handleSearch)
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background-color: #f0f2f5;
}

.aside {
  background-color: #001529;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: auto;
  transition: width 0.3s;
  z-index: 1000;
}

.logo {
  height: 64px;
  line-height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  background-color: #002140;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  overflow: hidden;
  white-space: nowrap;
  transition: all 0.3s;
}

.collapsed-logo {
  padding: 0 16px;
}

.logo img {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.main-container {
  margin-left: v-bind('isCollapse ? "64px" : "220px"');
  min-height: 100vh;
  transition: all 0.3s;
}

.collapsed-main {
  margin-left: 64px;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 64px;
  position: fixed;
  top: 0;
  right: 0;
  left: v-bind('isCollapse ? "64px" : "220px"');
  z-index: 9;
  transition: all 0.3s;
}

.header-left {
  display: flex;
  align-items: center;
}

.toggle-sidebar {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
  color: #666;
}

.header-right {
  display: flex;
  align-items: center;
}

.action-icon {
  font-size: 20px;
  cursor: pointer;
  color: #666;
  padding: 0 12px;
  height: 48px;
  display: flex;
  align-items: center;
  transition: all 0.3s;
}

.action-icon:hover {
  color: #409EFF;
}

.notice-badge {
  height: 48px;
  display: flex;
  align-items: center;
}

.notice-badge :deep(.el-badge__content) {
  right: 8px;
  top: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 16px;
  height: 48px;
  border-radius: 4px;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(0,0,0,.025);
}

.user-info span {
  margin-left: 10px;
  margin-right: 6px;
  font-size: 15px;
  color: #666;
}

.header-right .el-space {
  height: 48px;
  align-items: center;
}

.tabs-nav {
  position: fixed;
  top: 64px;
  left: v-bind('isCollapse ? "64px" : "220px"');
  right: 0;
  height: 40px;
  padding: 0 20px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  z-index: 8;
  transition: all 0.3s;
}

.main {
  margin-top: 104px;
  padding: 20px;
  background-color: #f0f2f5;
}

:deep(.el-tabs__header) {
  margin: 0;
  border-bottom: none;
}

:deep(.el-tabs__nav) {
  border: none !important;
}

:deep(.el-tabs__item) {
  height: 40px;
  line-height: 40px;
  border: none;
  color: #666;
  background: transparent;
}

:deep(.el-tabs__item.is-active) {
  color: #409EFF;
  background: #f0f2f5;
}

:deep(.el-tabs__item:hover) {
  color: #409EFF;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

:deep(.el-menu-item.is-active) {
  background-color: #1890ff !important;
}

:deep(.el-breadcrumb__inner) {
  color: #666 !important;
  font-weight: normal !important;
}

:deep(.el-breadcrumb__inner.is-link:hover) {
  color: #1890ff !important;
}

:deep(.el-dropdown-menu__item i) {
  margin-right: 8px;
}

.contextmenu {
  position: fixed;
  z-index: 3000;
  background: #fff;
  border-radius: 4px;
  padding: 5px 0;
  margin: 0;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  list-style: none;
}

.contextmenu li {
  margin: 0;
  padding: 8px 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
  transition: all 0.3s;
}

.contextmenu li:hover {
  background: #f5f7fa;
  color: #409EFF;
}

.contextmenu li .el-icon {
  margin-right: 8px;
  font-size: 16px;
}

.icon-button {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 50%;
  transition: all 0.3s;
}

.icon-button:hover {
  background-color: rgba(0,0,0,0.04);
}

.icon-button .el-icon {
  font-size: 20px;
  color: #666;
}

.icon-button :deep(.el-badge__content) {
  top: 8px;
  right: 8px;
}

.search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  width: 32px;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.search-wrapper.is-active {
  width: 200px;
}

.search-wrapper .search-icon {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  cursor: pointer;
  color: #666;
  padding: 0 8px;
  z-index: 1;
  transition: all 0.3s;
}

.search-wrapper.is-active .search-icon {
  display: none;
}

.search-wrapper .search-input {
  position: absolute;
  right: 0;
  width: 200px;
  opacity: 0;
  transition: all 0.3s;
}

.search-wrapper.is-active .search-input {
  opacity: 1;
}

.search-wrapper :deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none;
  border-radius: 16px;
}

.search-wrapper :deep(.el-input__wrapper:hover) {
  background-color: #e6e8ea;
}

.search-wrapper :deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  box-shadow: 0 0 0 1px #409EFF;
}

.search-dropdown {
  position: absolute;
  top: 50px;
  right: 0;
  width: 250px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  padding: 8px 0;
  z-index: 2000;
}

.search-item {
  padding: 8px 16px;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.search-item:hover {
  background-color: #f5f7fa;
}

.search-item .el-icon {
  margin-right: 8px;
  font-size: 16px;
  color: #909399;
}

.search-item span {
  color: #606266;
  font-size: 14px;
}

.fullscreen-btn,
.fullscreen-text {
  display: none;
}
</style> 