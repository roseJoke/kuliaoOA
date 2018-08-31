<template>
    <div>

        <div class="container">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="300px" class="demo-ruleForm">
                <el-form-item label="员工编号" prop="staffId" style="display: none">
                    <el-input v-model="ruleForm.staffId"></el-input>
                </el-form-item>
                <el-row>
                    <el-col :xs="24" :sm="24" :md="24" :lg="9" :xl="8">
                    <el-form-item label="员工姓名" prop="staffName">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.staffName"></el-input>
                    </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="24" :md="24" :lg="5" :xl="4">
                    <el-form-item label="部门" prop="department">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.department"></el-input>
                    </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :xs="24" :sm="24" :md="24" :lg="9" :xl="8">
                    <el-form-item label="岗位名称" prop="postTitle">
                        <el-input :disabled="true" class="input-width disabled-style" v-model="ruleForm.postTitle"></el-input>
                    </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="24" :md="24" :lg="5" :xl="4">
                    <el-form-item label="入职时间" prop="entryTime">
                        <el-date-picker
                            :disabled="true"
                            v-model="ruleForm.entryTime"
                            format="yyyy年MM月dd日"
                            value-format="yyyyMMdd"
                            type="date"
                            :editable="false"
                            placeholder="选择日期"
                            class="input-day-width">
                        </el-date-picker>
                    </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="8">
                    <el-form-item :disabled="readonly" label="转正申请时间" prop="beginTime">
                        <el-date-picker
                            v-model="ruleForm.beginTime"
                            :disabled="readonly"
                            format="yyyy年MM月dd日"
                            value-format="yyyyMMdd"
                            type="date"
                            :editable="false"
                            placeholder="选择日期"
                            class="input-day-width">
                        </el-date-picker>
                    </el-form-item>
                    </el-col>
                    <el-col v-if="!readonly" :xs="24" :sm="24" :md="24" :lg="12" :xl="8">
                        <el-form-item label="转正申请书" prop="date">
                            <el-upload
                                class="upload-demo"
                                ref="upload1"
                                action="/upload"
                                :on-remove="handleRemove"
                                :on-success="getFilePath1"
                                :on-error="uploadError"
                                :file-list="fileList1"
                                :on-exceed="handleExceed"
                                accept=".pdf"
                                :data="{path:'主页/转正考评/转正申请书'}"
                                :auto-upload="true"
                                :limit="1">
                                <el-button slot="trigger" size="small" type="primary">转正申请书</el-button>
                                <div slot="tip" class="el-upload__tip">只支持pdf格式</div>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                    <el-col v-if="!readonly" :xs="24" :sm="24" :md="24" :lg="13" :xl="8">
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
                                :auto-upload="true"
                                :limit="1">
                                <el-button slot="trigger" size="small" type="primary">转正考核表</el-button>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        props:{
            readonly: Boolean
        },
        data() {
            return {
                id: '',//文件上传时的父级目录id
                processId: '',
                ruleForm: {},
                fileList1: [],
                fileList2: [],
                rules: {
                    beginTime: [
                        {required: true, message: '申请转正时间不能为空', trigger: 'change'}
                    ]
                }
            };
        },
        mounted() {
            this.$refs.upload1.$children[0].$el.classList.remove("el-upload--text");
            this.$refs.upload2.$children[0].$el.classList.remove("el-upload--text");
            this.search();
        },
        methods: {

            // search() { //查询员工档案
            //     let that = this;
            //
            //     let empId = localStorage.getItem('empNo');
            //
            //     let param = new URLSearchParams();
            //     param.append('empNo', empId);
            //
            //     that.$axios.post('/hr/archives/emp/', param,
            //         {
            //             headers: {
            //                 'Content-Type': 'application/x-www-form-urlencoded'
            //             }
            //         }
            //     ).then((res) => {
            //         that.ruleForm.staffId = res.data.data.empNo;
            //         that.ruleForm.staffName = res.data.data.name;
            //         that.ruleForm.department = res.data.data.dept;
            //         that.ruleForm.postTitle = res.data.data.post;
            //         that.ruleForm.entryTime = res.data.data.entryDate;
            //
            //     });
            // },
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
                        this.$axios.post("/evaluate/positive/add", this.ruleForm).then(res => {
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
                this.$set(this.ruleForm, 'positiveApplicationForm',response.data.downloadUrl);

            },
            getFilePath2(response, file, fileList){//转正考核表
                console.log('转正考核表文件下载路径 '+response.data.downloadUrl);
                this.$set(this.ruleForm, 'positiveTable',response.data.downloadUrl);
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

