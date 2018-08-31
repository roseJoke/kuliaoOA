<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i>文件</el-breadcrumb-item>
                <el-breadcrumb-item>目录查看</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="header-box">
                <el-row>
                    <el-col :xs="24" :sm="6" :md="6" :lg="6" :xl="4">

                        <el-button type="primary" round @click="getFileList(file.partentId)">返回上一层</el-button>
                        <el-button type="success" round @click="createFile">创建文件夹</el-button>
                    </el-col>
                    <el-col :xs="24" :sm="18" :md="18" :lg="18" :xl="20">
                        <el-breadcrumb separator="/">
                            <template v-for="f in path " >
                                <el-breadcrumb-item>
                                    <a @click="getFileList(f.id)">{{ f.name }}</a></el-breadcrumb-item>
                            </template>

                        </el-breadcrumb>
                    </el-col>
                </el-row>
            </div>
            <el-row>
                <el-col :xs="6" :sm="4" :md="4" :lg="3" :xl="2" v-for="(ff,index) in file.child" :key="ff.id"
                        :offset="1">
                    <el-card :body-style="{ padding: '0px' }" style="margin-bottom: 50px">
                        <img :src="typeImg(ff.type)" class="image" v-on:dblclick="getFileList(ff.id)"
                             @contextmenu="showMenu(index)">
                        <div style="padding: 2px; height: 20px">
                            <span class="file_name">{{ ff.name }}</span>
                        </div>
                        <vue-context-menu :contextMenuData="menuData"
                                          :transferIndex="transferIndex"
                                          @del="delFile(ff.id)"></vue-context-menu>
                    </el-card>

                </el-col>
                <el-col :xs="6" :sm="4" :md="4" :lg="3" :xl="2" :offset="1">
                    <el-card :body-style="{ padding: '0px' }" style="margin-bottom: 50px">
                        <img class="image" src="/static/img/upload.png" @click="dialogUpLoadVisible = true">
                        <div style="padding: 2px; height: 20px">
                            <!--  <div class="bottom clearfix">-->
                            <span class="file_name">上传文件</span>
                            <!--</div>-->
                        </div>
                    </el-card>
                </el-col>
            </el-row>
            <el-dialog title="上传文件" :visible.sync="dialogUpLoadVisible" @close='getFileList(file.id)'>
                <uoload-file :id="file.id"></uoload-file>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import uoloadFile from "./uploadFile.vue"
    export default {
        data() {
            return {
                path: [],
                file: {
                        id: 0,
                    childFile: [
                        {}
                    ]
                },
                dialogUpLoadVisible: false,
                instance: null,
                transferIndex: null,
                menuData: {
                    menuName: 'name1',
                    axios: {x: null, y: null},
                    menulists: [
                        // {fnHandler: 'refresh', icoName: 'el-icon-setting', btnName: '刷新'},
                        // {fnHandler: 'add', icoName: 'el-icon-more', btnName: '添加'},
                        {fnHandler: 'del', icoName: 'el-icon-delete', btnName: '删除'},
                    ],
                },
            }
        },
        created() {
            this.getFileList();

        },
        components:{
            uoloadFile
        },
        methods: {
            showMenu(index) {
                this.transferIndex = index // tranfer index to child component
                event.preventDefault()
                var x = event.clientX
                var y = event.clientY
                this.menuData.axios = {
                    x, y
                }
            },
            typeImg(type) {
                if (type && type.imgUrl) {
                    return type.imgUrl;
                } else {
                    return '/static/img/img.jpg';
                }
            },
            /*  opFile(id){
                  this.getFileList(id);
              },*/
            delFile(id){
                this.$axios.delete("/file/delete", {params: {id: id}}).then(res=>{
                    this.$message({
                        showClose: true,
                        message:res.data.msg,
                        type: "info"
                    });
                    this.getFileList(this.file.id);
                })
            },
            getFileList(id) {
                id = id ? id : 0;
                this.$axios.all([
                    this.$axios.get("/file/fileList", {params: {id: id}}),
                    this.$axios.get("/file/getPath", {params: {id: id}})
                ]).then(this.$axios.spread((listResp, pathResp) => {

                    this.file = listResp.data.data;
                    this.path = pathResp.data.data;
                    console.log(this.path);

                }));
            },
            createFile() {
                this.$prompt('请输入', '文件夹名', {}).then(({value}) => {
                    let file = this.file;
                    let that = this;
                    let param = new FormData(); //创建form对象
                    param.append('dirId', file.id);//通过append向form对象添加数据
                    param.append('file_name', value);
                    console.log(file);
                    let config = {
                        headers: {'Content-Type': 'multipart/form-data'}
                    };
                    this.$axios.post('/file/uploadFile', param, config)
                        .then(response => {
                            that.getFileList(file.id);
                        })
                });
            }
        }
    }
</script>

<style scoped>
    .file_name {
        font-size: 13px;
        color: #000000;
        word-break: break-all;
        white-space: normal;
    }

    .bottom {
        margin-top: 7px;
        line-height: 20px;
        text-align: center;
    }

    .button {
        padding: 0;
        float: right;
    }

    .image {
        width: 100%;
        display: block;
    }

    .clearfix:before,
    .clearfix:after {
        /*display: table;*/
        content: "";
    }

    .clearfix:after {
        clear: both
    }

    .header-box {
        margin-bottom: 20px;

        border-bottom-style: inset;
        border-bottom-width: 5px;
    }
</style>
