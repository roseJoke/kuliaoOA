<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i class="el-icon-menu"></i>
        </div>
        <div class="logo">酷聊OA</div>
        <div class="header-right">

            <div class="header-user-con">
                <!-- 全屏显示 -->
                <div class="btn-fullscreen" @click="handleFullScreen">
                    <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                        <i class="el-icon-rank"></i>
                    </el-tooltip>
                </div>
                <!-- 消息中心 -->
                <div class="btn-bell">
                    <el-tooltip effect="dark" :content="(message||message>0)?`有${message}条未读消息`:`消息中心`" placement="bottom">
                        <router-link to="/tabs">
                            <i class="el-icon-bell"></i>
                        </router-link>
                    </el-tooltip>
                    <span class="btn-bell-badge" v-if="message"></span>
                </div>
                <!-- 用户头像 -->
                <div class="user-avator"><img src="static/img/img.jpg"></div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{username}} <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <a href="#" target="_blank">
                            <el-dropdown-item>关于酷聊OA</el-dropdown-item>
                        </a>
                        <el-dropdown-item divided command="editPwd">修改密码</el-dropdown-item>
                        <el-dropdown-item command="loginout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%" top="30vh">
                    <el-form :model="form">
                        <el-form-item label="原密码" :label-width="formLabelWidth">
                            <el-input v-model="form.oldPwd" auto-complete="off" type="password"></el-input>
                        </el-form-item>
                        <el-form-item label="新密码" :label-width="formLabelWidth">
                            <el-input v-model="form.newPwd" auto-complete="off" type="password"></el-input>
                        </el-form-item>
                        <el-form-item label="确认新密码" :label-width="formLabelWidth">
                            <el-input v-model="form.newPwd2" auto-complete="off" type="password"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="modifyPwd()">确 定</el-button>
                    </div>
                </el-dialog>

            </div>
        </div>
    </div>
</template>
<script>

    import bus from '../common/bus';
    import CryptoJS from 'crypto-js'

    export default {
        data() {
            return {
                eventMessage: '',
                collapse: false,
                fullscreen: false,
                name: 'linxin',
                message: 1,
                websocket : null ,//new WebSocket("ws://localhost:8082/websocket/"+localStorage.getItem('empNo'))
                tempMessageId:null,
                dialogFormVisible: false,
                form: {
                    oldPwd: '',
                    newPwd: '',
                    newPwd2: ''
                },
                formLabelWidth: '120px'
            }
        },
        created() {
          this.connOaMessage();
        },
        deactivated() {
        },
        computed: {
            username() {
                let username = localStorage.getItem('ms_username');
                return username ? username : this.name;
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
            connOaMessage(){
                let that = this;
                this.websocket = new WebSocket("ws://localhost:8082/websocket/"+localStorage.getItem('userId'));
                this.websocket.onopen = function(event){
                    that.websocket.send("OK");
                };
                //连接关闭后重新链接
                this.websocket.onclose = function(){
                    that.connOaMessage();
                };
                //接收到消息的回调方法
                this.websocket.onmessage = function(event){
                    that.messageEvent(event);
                };
            },
            messageEvent(event) {
                console.log(event);
                if(event.data == '已登录')return;
                let strs = event.data.split(';');
                console.log(strs);
                if(strs[0] == this.tempMessageId)return;
                this.tempMessageId = strs[0];
                if(strs.length != 4)return;
                let params = new URLSearchParams();
                params.append('id',strs[0]);
                this.$axios.post("/message/getOaMessage", params).then(res => {

                    this.$notify({
                        title: res.data.data.messageTopic,
                        dangerouslyUseHTMLString: true,
                        message:  res.data.data.messageContent
                    });

                });

                this.message = strs[3];
            },
            // 用户名下拉菜单选择事件
            handleCommand(command) {
                if (command == 'loginout') {
                    localStorage.removeItem('ms_username')
                    this.$router.push('/login');
                }
                if (command == 'editPwd') {

                    this.dialogFormVisible = true;

                }
            },
            modifyPwd(){
                this.form.oldPwd = '';
                this.form.newPwd = '';
                this.form.newPwd2 = '';
                console.log(localStorage.getItem("password"));
                console.log(this.encryptByDES(this.form.oldPwd, localStorage.getItem("token")));
                if(this.form.newPwd !== this.form.newPwd2){
                    this.$message.warning("两次密码不一致");
                    this.form.oldPwd = '';
                    this.form.newPwd = '';
                    this.form.newPwd2 = '';
                }else if(localStorage.getItem("password") !== this.encryptByDES(this.form.oldPwd, localStorage.getItem("token"))){
                    this.$message.warning("原密码错误");
                    this.form.oldPwd = '';
                    this.form.newPwd = '';
                    this.form.newPwd2 = '';
                }else {
                    this.dialogFormVisible = false;
                    let params = new URLSearchParams();
                    params.append("empNo", localStorage.getItem("empNo"));
                    params.append("password", this.form.newPwd);
                    this.$axios.post("/base/pwd/modify", params).then((res) => {
                        if (!res) return;
                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });
                    });
                }




            },
            // 侧边栏折叠
            collapseChage() {
                this.collapse = !this.collapse;
                bus.$emit('collapse', this.collapse);
            }
            ,
            // 全屏事件
            handleFullScreen() {
                let element = document.documentElement;
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                this.fullscreen = !this.fullscreen;
            }
        }
    }

</script>
<style scoped>
    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 70px;
        font-size: 22px;
        color: #fff;
    }

    .collapse-btn {
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 70px;
    }

    .header .logo {
        float: left;
        width: 250px;
        line-height: 70px;
    }

    .header-right {
        float: right;
        padding-right: 50px;
    }

    .header-user-con {
        display: flex;
        height: 70px;
        align-items: center;
    }

    .btn-fullscreen {
        transform: rotate(45deg);
        margin-right: 5px;
        font-size: 24px;
    }

    .btn-bell, .btn-fullscreen {
        position: relative;
        width: 30px;
        height: 30px;
        text-align: center;
        border-radius: 15px;
        cursor: pointer;
    }

    .btn-bell-badge {
        position: absolute;
        right: 0;
        top: -2px;
        width: 8px;
        height: 8px;
        border-radius: 4px;
        background: #f56c6c;
        color: #fff;
    }

    .btn-bell .el-icon-bell {
        color: #fff;
    }

    .user-name {
        margin-left: 10px;
    }

    .user-avator {
        margin-left: 20px;
    }

    .user-avator img {
        display: block;
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .el-dropdown-link {
        color: #fff;
        cursor: pointer;
    }

    .el-dropdown-menu__item {
        text-align: center;
    }
</style>
