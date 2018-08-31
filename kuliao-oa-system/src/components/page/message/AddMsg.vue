<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i> 表单</el-breadcrumb-item>
                <el-breadcrumb-item>添加消息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-row>
                <el-col :span="14">
                    <div class="form-box">
                        <el-form ref="form" :model="form">
                            <el-form-item label="消息主题">
                                <el-input v-model="form.topic"></el-input>
                            </el-form-item>
                            <el-form-item label="消息类型">
                                <el-col :span="11">
                                    <el-radio-group v-model="form.type">
                                        <el-radio label="1">站内信</el-radio>
                                        <el-radio label="2">审批提醒</el-radio>
                                        <el-radio label="3">通知公告</el-radio>
                                    </el-radio-group>
                                </el-col>
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                    type="textarea"
                                    :autosize="{ minRows: 15, maxRows: 30}"
                                    placeholder="请输入内容"
                                    v-model="form.content">
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="onSubmit">表单提交</el-button>
                                <el-button>取消</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <el-col :span="7" style="margin-left: 30px">
                    <el-card class="box-card" :body-style="{padding: '0px',height: '344px'}">
                        <div slot="header" class="clearfix">
                            <span>选择消息接收人</span>
                            <el-input style="float: right; padding: 0px 0;height: 24px;width: 50% "
                                      placeholder="输入关键字进行过滤"
                                      v-model="filterText">
                            </el-input>
                        </div>
                        <template>
                            <el-tree
                                class="filter-tree"
                                :data="empList"
                                show-checkbox
                                node-key="deptId"
                                :filter-node-method="filterNode"
                                ref="empTree"
                                :props="defaultProps">
                            </el-tree>
                        </template>
                    </el-card>
                </el-col>
            </el-row>
        </div>

    </div>


</template>

<script>
    import 'quill/dist/quill.core.css';
    import 'quill/dist/quill.snow.css';
    import 'quill/dist/quill.bubble.css';
    import {quillEditor} from 'vue-quill-editor';

    export default {
        watch: {
            filterText(val) {
                this.$refs.empTree.filter(val);
            }
        },
        data: function () {
            return {
                filterText: '',
                empList: [],
                defaultProps:{
                    // id: 'deptId',
                    label: 'deptName',
                    children: 'deptEmps'
                },

                content: '',
                form: {
                    topic: '',
                    type: '1',
                    content: '',
                }
            }
        },
        created(){
            this.$axios.post("/base/dept/selectDeptEmps").then((res) => {
                console.log(res.data.data);
                this.empList = res.data.data;

            })
        },
        components: {
            quillEditor
        },
        methods: {
            getCheckedNodes(){
                let userList = this.$refs.empTree.getCheckedNodes();

                if (userList.length === 0) {
                    this.$message.warning('没有选择接收消息的人');
                    return;
                }
                let userIds;
                let userNames;
                userList.forEach((user) => {

                    if(typeof user.deptEmps === 'undefined'){
                        userIds.push(user.deptId);
                        userNames.push(user.deptName);
                    }
                });
                console.log(userIds);
                console.log(userNames);

            },
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            onSubmit() {
                let that = this;
                let userList = this.$refs.empTree.getCheckedNodes();

                if (userList.length === 0) {
                    this.$message.warning('没有选择接收消息的人');
                    return;
                }
                let userIds = [];
                let userNames = [];
                userList.forEach((user) => {

                    if(typeof user.deptEmps === 'undefined'){
                        userIds.push(user.deptId);
                        userNames.push(user.deptName);
                    }
                });

                console.log(this.form.content);
                let params = new URLSearchParams();
                params.append('userIdList', userIds);
                params.append('userNameList',userNames);

                params.append('messageTopic',  this.form.topic);
                params.append('messageContent',this.form.content);
                params.append('messageType', this.form.type);
                params.append('createUserId', localStorage.getItem('userId'));
                params.append('createUserName', localStorage.getItem('ms_username'));

                this.$axios.post("/message/saveOaMessage", params).then(res => {
                    this.$message.success(res.data.msg);
                    that.form.topic='';
                    that.form.content='';
                    that.form.type='';
                    that.$refs.empTree.setCheckedKeys([]);
                });

            }
        },
        deactivated() {
            //this.$refs.form.resetFields();
        },
    }
</script>
