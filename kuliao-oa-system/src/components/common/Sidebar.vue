<template>
    <div class="sidebar"><!-- background-color="#324157"
            text-color="#bfcbd9" active-text-color="#20a0ff"-->
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs && item.subs.length>0">
                    <el-submenu :index="'/'+item.index" :key="item.index">    <!--选项卡-->
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.menuName }}</span>
                        </template>
                        <template v-for="(subItem,i) in item.subs">
                            <template v-if="subItem.subs && subItem.subs.length>0">
                                <el-submenu :key="i" :index="'/'+subItem.index">
                                    <template slot="title">
                                        <i :class="subItem.icon"></i><span slot="title">{{ subItem.menuName }}</span>
                                    </template>
                                    <el-menu-item v-for="(subItem,i) in subItem.subs" :key="i" :index="'/'+subItem.index">
                                        <i :class="subItem.icon"></i><span slot="title">{{ subItem.menuName }}</span>
                                    </el-menu-item>
                                </el-submenu>
                            </template>
                            <template v-else>
                                <el-menu-item :index="'/'+subItem.index" :key="'/'+subItem.index">      <!--基础表格-->
                                    <i :class="subItem.icon"></i><span slot="title">{{ subItem.menuName }}</span>
                                </el-menu-item>
                            </template>

                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="'/'+item.index" :key="item.index">      <!--基础表格-->
                        <i :class="item.icon"></i><span slot="title">{{ item.menuName }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from '../common/bus';
    import menu from '../../js/menu.js';
    export default {
        data() {
            return {
                collapse: false,
                items: menu
                //[   http://172.20.0.24:8080/menu菜单接口
                //  需要个参数code，传zhangsan
            }
        },
        computed: {
            onRoutes() {
                return this.$route.path.replace('/', '');
            }
        },
        created() {
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
           // console.log(menu);
            //let params = new URLSearchParams();
            //console.log(localStorage.getItem('empNo'))
            //params.append('empNo', localStorage.getItem('empNo'));
          /*  this.$axios.get("./static/menu.json").then((res) => {
                this.items = res.data;
            })*/
            //params.append('empNo', localStorage.getItem('empNo'));
            /* this.$axios.get("/user/menu",{
                 params:{
                     empNo:localStorage.getItem('empNo')
                 }
             }).then((res) => {
                 console.log(res);
                 this.items = res.data.data;

             })*/
        }
    }
</script>

<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
    }

    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 250px;
    }

    .sidebar > ul {
        height: 100%;
    }
</style>
