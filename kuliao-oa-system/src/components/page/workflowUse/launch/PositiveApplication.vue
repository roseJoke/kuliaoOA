<template>
    <div>

        <div class="container">
            <el-form :disabled="flag" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="300px" class="demo-ruleForm">
                <el-form-item label="员工编号" prop="staffId" style="display: none">
                    <el-input v-model="ruleForm.staffId"></el-input>
                </el-form-item>
                <el-row type="flex">
                    <el-form-item label="员工姓名" prop="staffName">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.staffName"></el-input>
                    </el-form-item>

                    <el-form-item label="部门" prop="department">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.department"></el-input>
                    </el-form-item>
                </el-row>
                <el-row type="flex">
                    <el-form-item label="岗位名称" prop="postTitle">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.postTitle"></el-input>
                    </el-form-item>

                    <el-form-item label="入职时间" prop="entryTime">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.entryTime"></el-input>
                    </el-form-item>
                </el-row>

                <el-row type="flex">

                    <el-form-item v-if="!flag" label="申请转正时间" prop="beginTime">
                        <el-date-picker
                            v-model="ruleForm.beginTime"
                            value-format="yyyyMMdd"
                            type="date"
                            :editable="false"
                            placeholder="选择日期"
                            class="input-day-width">
                        </el-date-picker>
                    </el-form-item>
                    <el-col>
                        <el-row type="flex">
                            <el-col :span="9">
                                <el-form-item v-if="!flag" label="转正申请书" prop="date">
                                    <el-upload
                                        class="upload-demo"
                                        ref="upload1"
                                        action="/upload"
                                        :on-remove="handleRemove"
                                        :on-success="getFilePath1"
                                        :on-error="uploadError"
                                        :file-list="fileList1"
                                        :on-exceed="handleExceed"
                                        :data="{path:'主页/转正考评/转正申请书'}"
                                        :auto-upload="false"
                                        :limit="1">
                                        <el-button slot="trigger" size="small" type="primary">转正申请书</el-button>
                                    </el-upload>
                                </el-form-item>
                            </el-col>
                            <el-col :span="9">
                                <el-form-item label="转正考核表" prop="date">
                                    <el-upload
                                        class="upload-demo"
                                        ref="upload2"
                                        action="/upload"
                                        :on-remove="handleRemove"
                                        :on-success="getFilePath2"
                                        :on-error="uploadError"
                                        :file-list="fileList2"
                                        :data="{path:'主页/转正考评/转正考核表'}"
                                        :on-exceed="handleExceed"
                                        :auto-upload="false"
                                        :limit="1">
                                        <el-button slot="trigger" size="small" type="primary">转正考核表</el-button>
                                    </el-upload>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-col>
                </el-row>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        props:['flag'],
        data() {
            return {
                id: '',//文件上传时的父级目录id
                processId: '',
                ruleForm: {},
                fileList1: [
                    // {name: '', url: ''}
                ],
                fileList2: [],
                rules: {
                    beginTime: [
                        {required: true, message: '申请转正时间不能为空', trigger: 'change'}
                    ]
                }
            };
        },
        created() {
            // this.search();
            let that = this;
            that.ruleForm.staffId = '000000';
            that.ruleForm.staffName = '测试数据';
            that.ruleForm.department = '研发部';
            that.ruleForm.postTitle = '岗位名称';
            that.ruleForm.entryTime = '20180901';
            that.ruleForm.confirmationProcessId = 'aaa';
        },
        mounted() {
            this.$refs.upload1.$children[0].$el.classList.remove("el-upload--text");
            this.$refs.upload2.$children[0].$el.classList.remove("el-upload--text");

        },
        methods: {

            search() { //查询员工档案
                let that = this;

                let empId = localStorage.getItem('empNo');

                let param = new URLSearchParams();
                param.append('empNo', empId);

                that.$axios.post('/hr/archives', param,
                    {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }
                ).then((res) => {
                    that.ruleForm.staffId = res.data.data.empNo;
                    that.ruleForm.staffName = res.data.data.name;
                    that.ruleForm.department = res.data.data.dept;
                    that.ruleForm.postTitle = res.data.data.post;
                    that.ruleForm.entryTime = res.data.data.entryDate;

                });
            },

            submitForm(formName) {

                this.$refs.upload1.submit();
                this.$refs.upload2.submit();

                this.$refs[formName].validate((valid) => {

                    if (valid) {
                        this.$axios.post("/evaluate/positive/insert", this.ruleForm).then(res => {
                            this.$message.success(res.data.msg);
                        });

                    } else {
                        this.$message.error('请填写完整信息');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //文件上传
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            getFilePath1(response, file, fileList){//转正申请书
                console.log('转正申请书文件下载路径 '+response.data.downloadUrl);
                this.ruleForm.positiveApplicationForm = response.data.downloadUrl;

            },
            getFilePath2(response, file, fileList){//转正考核表
                console.log('转正考核表文件下载路径 '+response.data.downloadUrl);
                this.ruleForm.positiveTable = response.data.downloadUrl;

            },
            handleExceed(files, fileList) {
                this.$message.warning(`限制选择 1 个文件`);
            },
            uploadError(err, file, fileList){
                console.log(err);
                this.$message({
                    showClose: true,
                    message:file.name+"上传失败！",
                    type: "error"
                });
            }
        }
    }
</script>

<style scoped>

    .input-width {
        width: 200px;
    }

    .input-day-width {
        width: 200px;
    }

    .disabled-style {
        -webkit-text-fill-color: #606266;
    }

</style>

