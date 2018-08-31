import Vue from 'vue';
import 'babel-polyfill';
import axios from 'axios';

export default axios;
//axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';


// http响应拦截器
//统一的错误处理
axios.interceptors.response.use(response => {
        if (response.data && response.data.code) {
            if (response.data.code == "000000") {
                return response;
            } else if (response.data.code == "100006") {
                console.error("登录过期");
                localStorage.removeItem('ms_username');
                localStorage.removeItem('empNo');
                console.log(Vue)
               // console.log(this.$router);
            }
            else {
                Vue.prototype.$message({
                    showClose: true,
                    dangerouslyUseHTMLString: true,
                    message: response.data.msg + "\n错误码：" + response.data.code,
                    type: "error"
                });
                return false;
            }
        }
        //对于不是oa系统的统一格式消息，不做处理
        return response;
    },
    error => {
        console.error(error.toString());
        if (error.response.status == 401) {
            localStorage.removeItem('ms_username');
            this.$router.push('/login');
        } else if (error.response.status >= 500) {
            Vue.prototype.$message({
                showClose: true,
                dangerouslyUseHTMLString: true,
                message: "服务器处理后的的错误！错误码：" + error.response.data.code + "\n" +
                error.response.data.msg,
                type: "error"
            });
        } else if (error.response.status >= 400) {
            Vue.prototype.$message({
                showClose: true,
                dangerouslyUseHTMLString: true,
                message: "请求错误！错误码：" + error.response.data.code + "\n" +
                error.response.data.msg,
                type: "error"
            });
        }
        return Promise.reject(error);
    }
);

//统一的提交前处理
axios.interceptors.request.use(data => {
        console.log(data);
        if (data.headers['Content-Type'] == 'application/x-www-form-urlencoded' ) {
            //console.log(data.data);
            let detKey = [];
            data.data.forEach((v, k) => {
                console.log(k + ":" + v);
                if (v === '' || v === null || v === undefined || v === ' ' || v === 'null' || v === 'undefined') {
                    detKey.push(k);
                }
            })
            detKey.forEach(value => {
                data.data.delete(value);
            })
        } else {
            deleteEmptyProperty(data.data);
        }
        // console.log(data)
        data.headers['Authorization'] = localStorage.getItem('token');
        return data;
    },
    error => {
        return error;
    }
)


function deleteEmptyProperty(object) {
    for (var i in object) {
        var value = object[i];
        if (typeof value === 'object') {
            if (Array.isArray(value)) {
                if (value.length == 0) {
                    delete object[i];
                    // console.log('delete Array', i);
                    continue;
                }
            }
            deleteEmptyProperty(value);
            if (isEmpty(value)) {
                //console.log('isEmpty true', i, value);
                delete object[i];
                // console.log('delete a empty object');
            }
        } else {
            if (value === '' || value === null || value === undefined || value === ' ' || value === 'null' || value === 'undefined') {
                delete object[i];
                //console.log('delete ', i);
            } else {
                // console.log('check ', i, value);
            }
        }
    }
}

function isEmpty(object) {
    for (var name in object) {
        return false;
    }
    return true;
}
