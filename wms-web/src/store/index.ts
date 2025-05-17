import { createStore } from 'vuex'
import router from "@/router";

interface State {
    menu: any[] // 可根据实际菜单类型替换 any
}

function addMenu(menuList: any[]) {
    menuList.forEach(menuItem => {
        const childRoute = {
            path: `/${menuItem.menuClick}`,
            name: menuItem.menuName,
            meta: {
                title: menuItem.menuName
            },
            component: () => import(`../components/${menuItem.menuComponent}`)
        };
        router.addRoute('Index', childRoute);
    });
}

// 尝试从 sessionStorage 中获取菜单数据
const savedMenu = sessionStorage.getItem('menu');
const initialMenu = savedMenu ? JSON.parse(savedMenu) : [];

// 在 store 创建时就添加已保存的菜单对应的路由
if (initialMenu.length > 0) {
    addMenu(initialMenu);
}

export default createStore<State>({
    state: {
        menu: initialMenu
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    mutations: {
        setMenu(state, menuList: any[]) {
            state.menu = menuList;
            addMenu(menuList);
            // 将菜单数据存储到 sessionStorage 中
            sessionStorage.setItem('menu', JSON.stringify(menuList));
        }
    },
    modules: {}
});