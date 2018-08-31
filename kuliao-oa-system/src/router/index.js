import Vue from 'vue';
import Router from 'vue-router';
import 'babel-polyfill';
import axios from 'axios';

axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';

// http响应拦截器
//统一的错误处理
axios.interceptors.response.use(response => {
        if (response.data && response.data.code) {
            if (response.data.code == "000000") {
                return response;
            } else {
                Vue.prototype.$message({
                    showClose: true,
                    message: response.data.msg,
                    type: "error"
                });
                return false;
            }
        }
        //对于不是oa系统的统一格式消息，不做处理
        return response;
    },
    error => {
        if (error.response.status == 401) {
            localStorage.removeItem('ms_username');
            this.$router.push('/login');
        } else if (error.response.status >= 500) {
            Vue.prototype.$message({
                showClose: true,
                message: "服务器异常！",
                type: "error"
            });
        }else if  (error.response.status >= 400){
            Vue.prototype.$message({
                showClose: true,
                message: "请求错误！",
                type: "error"
            });
        }
        return Promise.reject(error);
    }
);

//统一的提交前处理
axios.interceptors.request.use(data => {
        data.headers['Authorization'] = localStorage.getItem('token');
        return data;
    },
    error => {
        return error;
    }
)

Vue.prototype.$axios = axios;


let rot = {
    routes: []
};

Vue.use(Router);
let r = new Router(rot);
//export default r;
//从服务器加载路径映射
Vue.prototype.$axios.get("./static/jump.json").then((res) => {
    setResolve(res.data);
    r.addRoutes(res.data);
});


function setResolve(cc) {
    cc.forEach(function (rotePath) {
        if (rotePath.children) setResolve(rotePath.children);
        if (rotePath.component) {
            let rr = rotePath.component;
            rotePath.component = resolve => require(['../' + rr + '.vue'], resolve);
        }
    });
}
