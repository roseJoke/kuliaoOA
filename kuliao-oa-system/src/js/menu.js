import Vue from 'vue';
import modules from "../../static/module.json"

var menu = [];
export default menu;

if (process.env.NODE_ENV === 'development') {

    modules.forEach(function (module) {
        let moduleConfig = require('../.././' + module.path + '/dev.js')
        if (!moduleConfig.isCommon) {
            if (!moduleConfig.menu) {
                console.error(module.name + '模块未定义菜单');
                console.error(module.name + '使用了默认生成的菜单！！！！');
                console.error(module.name + '使用了默认生成的菜单！！！！');
                menu.push(createDefultMenu(module));
            } else {
                moduleConfig.menu.forEach(function (page) {
                    menu.push(page);
                });
            }
        }


    });

    console.log(menu);
} else {
    //生产环境 build
    //从服务器处获取
    Vue.prototype.$axios.get()
    this.$axios.get("/user/menu", {
        params: {
            empNo: localStorage.getItem('empNo')
        }
    }).then((res) => {
        menu = res.data.data;

    })
}


function createDefultMenu(module) {
    var menu = {
        icon: 'el-icon-error',
        menuName: module.meta.title,
        index: module.name,
        subs: []
    };


        let moduleConfig = require('../.././' + module.path + '/dev.js')
        if (!moduleConfig.route) {
            console.error(module.name + '模块未定义route');
            return null;
        }else{
            moduleConfig.route.forEach(function (page) {
                console.log(page);
                menu.subs.push(
                {
                    icon: "el-icon-tickets",
                    index: page.path.substring(1,page.path.length),
                    menuName: page.meta.title
                });
            })
        }
return menu;


}
