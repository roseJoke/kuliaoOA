<template>
    <div class="login-wrap">
        <div class="ms-title">后台管理系统</div>
        <div class="ms-login">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                <el-form-item prop="username">
                    <el-input v-model="ruleForm.username" placeholder="username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="ruleForm.password"
                              @keyup.enter.native="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                </div>
                <!--<p style="font-size:12px;line-height:30px;color:#999;">Tips : 用户名和密码随便填。</p>-->
            </el-form>
        </div>
    </div>
</template>

<script>
    import CryptoJS from 'crypto-js'
    //引入封装方法

    export default {
        data: function () {
            return {
                message: '',
                ruleForm: {
                    username: '000002',
                    password: 'abc123',
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            //DES加密 Pkcs7填充方式
            encryptByDES(message, key) {
                const keyHex = CryptoJS.enc.Utf8.parse(key);
                const encrypted = CryptoJS.DES.encrypt(message, keyHex, {
                    mode: CryptoJS.mode.ECB,
                    padding: CryptoJS.pad.Pkcs7
                });
                return encrypted.toString();
            },
            submitForm(formName) {
                let that = this;
                this.$refs[formName].validate((valid) => {
                        //效验不通过
                        if (valid) {
                            //在这里提交用户名和密码
                            that.login();
                            /*localStorage.setItem('ms_username', this.ruleForm.username);
                            localStorage.setItem('empNo', this.ruleForm.username);
                            this.$router.push('/');*/
                        } else {
                            return false;
                        }
                    }
                );
            },
            login() {
                let params = new URLSearchParams();
                params.append('empNo', this.ruleForm.username);
                params.append('from', "login");
                //获取服务端的秘钥
                //需要判断code是否等于000000且data不为空
                this.$axios.post("/base/user/generate", params)
                    .then(res => this.todologin(res.data.data))
            },
            todologin(token) {
                if(!token)return;
                let params = new URLSearchParams();
                localStorage.setItem('token', token);
                params.append('empNo', this.ruleForm.username);
                params.append('password', this.encryptByDES(this.ruleForm.password, token));
                this.$axios.post("/base/user/login", params).then((res) => {
                    if(!res)return;
                    localStorage.setItem('ms_username', res.data.data.username);
                    localStorage.setItem('userId', res.data.data.userId);
                    localStorage.setItem('empNo', res.data.data.empNo);
                    //localStorage.setItem('password', res.data.data.password);
                    this.$router.push('/');
                })
            }
        }
    }
</script>

<style scoped>
    .login-wrap {
        position: relative;
        width: 100%;
        height: 100%;
    }

    .ms-title {
        position: absolute;
        top: 50%;
        width: 100%;
        margin-top: -230px;
        text-align: center;
        font-size: 30px;
        color: #fff;

    }

    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 300px;
        height: 160px;
        margin: -150px 0 0 -190px;
        padding: 40px;
        border-radius: 5px;
        background: #fff;
    }

    .login-btn {
        text-align: center;
    }

    .login-btn button {
        width: 100%;
        height: 36px;
    }
</style>
