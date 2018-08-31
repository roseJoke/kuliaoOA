import Vue from 'vue';
import Router from 'vue-router';
import 'babel-polyfill';

Vue.use(Router);
export default new Router(createRouter());

import modules from "../../static/module.json"

function createRouter() {

    var rot = require('../components/common/dev.js').route;
    let mainPage = {
        path: "/",
        component: "components/common/Home",
        meta: {
            "title": "主显示页面"
        },
        children: []
    }
    console.log(rot)

    modules.forEach(function (module) {

        let moduleConfig = require('../.././' + module.path + '/dev.js')
        if (!moduleConfig.route) {
            console.error(module.name + '模块未定义route');
            return null;
        }
        if (!moduleConfig.isCommon) {
            moduleConfig.route.forEach(function (page) {
                mainPage.children.push(page);
            })
        }
    })
    rot.push(mainPage)

    return {routes: setResolve(rot)};
}
//绑定vue文件与路由
function setResolve(route) {
    route.forEach(function (rotePath) {
        if (rotePath.children) setResolve(rotePath.children);
        if (rotePath.component) {
            let rr = rotePath.component;
            rotePath.component = resolve => require(['../' + rr + '.vue'], resolve);
        }
    });
    return route;
}

