<template>
    <div>

        <div class="container">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="300px" class="demo-ruleForm">
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
                <el-form-item label="申请转正时间" prop="beginTime">
                    <el-date-picker
                        v-model="ruleForm.beginTime"
                        value-format="yyyy-MM-dd"
                        type="date"
                        :editable="false"
                        placeholder="选择日期"
                        class="input-day-width">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="转正申请书" prop="date">
                    <el-upload
                        class="avatar-uploader"
                        action="https://jsonplaceholder.typicode.com/posts/"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                </el-row>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">发起</el-button>
                    <el-button @click="resetForm('ruleForm')">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                processId: '',
                ruleForm: {

                },
                imageUrl: '',
                rules: {
                    beginTime: [
                        {required: true, message: '申请转正时间不能为空', trigger: 'change'}
                    ]
                }
            };
        },
        methods: {

            search() { //查询员工档案
                let that = this;
                let param = {};
                // that.$axios.post('/workflow/meeting/selectMeetings', param).then((res) => {
                //     let datas = [];
                //     datas =res.data.data;
                //     for (let i = 0; i < datas.length; i++) {
                //         if(datas[i].meetingState == '0') {
                //             let obj = {};
                //             obj.key = datas[i].meetingId;
                //             obj.value = datas[i].meetingName;
                //             that.options.push(obj);
                //         }
                //     }
                // });
            },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {

                    if (valid) {
                        this.$message.success('成功');
                        // this.$axios.post("/workflow/meeting/modifyMeeting", ruleForm).then(res => {
                        //     this.search();
                        //     this.$message.success(res.data.msg);
                        // });

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
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw);
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isJPG && isLt2M;
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

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

    .disabled-style {
        -webkit-text-fill-color: #606266;
    }

</style>
