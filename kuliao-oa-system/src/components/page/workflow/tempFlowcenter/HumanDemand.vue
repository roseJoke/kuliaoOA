<template>
    <div>

        <div class="container">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="200px" class="demo-ruleForm">
                    <el-row type="flex">
                        <el-form-item label="申请岗位" prop="post">
                            <el-input class="input-width disabled-style" v-model="ruleForm.post"></el-input>
                        </el-form-item>

                        <el-form-item label="需求人数" prop="count">
                            <el-input class="input-width disabled-style" v-model.number="ruleForm.count"></el-input>
                        </el-form-item>

                        <el-form-item label="申请部门" prop="dept">
                            <el-select v-model="ruleForm.dept"
                                       placeholder="请选择部门" :inline-message="true">
                                <el-option v-for="item in options" :key="item.key" :label="item.value"
                                           :value="item.key"></el-option>
                            </el-select>
                        </el-form-item>
                </el-row>

                <el-row type="flex">
                <el-form-item label="到岗时间" prop="endTime">
                    <el-date-picker
                        v-model="ruleForm.endTime"
                        value-format="yyyy-MM-dd"
                        type="date"
                        :editable="false"
                        placeholder="选择日期"
                        class="input-day-width">
                    </el-date-picker>
                </el-form-item>

                <el-form-item label="面试负责人" prop="transactor_name">
                    <el-input class="input-width disabled-style" v-model="ruleForm.transactor_name"></el-input>
                </el-form-item>

                    <el-form-item label="需求等级" prop="grade">
                        <el-select v-model="ruleForm.grade"
                                   placeholder="请选择等级" :inline-message="true">
                            <el-option v-for="item in grades" :key="item.value" :label="item.value"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-row>

                <el-row type="flex">
                    <el-form-item label="需求类型" prop="type">
                        <el-select v-model="ruleForm.type"
                                   placeholder="请选择类型" :inline-message="true">
                            <el-option v-for="item in types" :key="item.value" :label="item.value"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-row>
                <el-form-item label="岗位要求" prop="require">
                    <el-input type="textarea" class="textarea-style" v-model="ruleForm.require" rows="5"></el-input>
                </el-form-item>

                <el-form-item label="备注" prop="desc">
                    <el-input  type="textarea" class="textarea-style" v-model="ruleForm.desc" rows="5"></el-input>
                </el-form-item>

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

                //部门下拉框
                options: [{
                    key:1, value: '人事部'
                }, {
                    key:2, value: '财务部'
                }, {
                    key:3, value: '研发部'
                }],

                //需求等级
                grades: [{
                    value: '非紧急'
                }, {
                    value: '一般'
                }, {
                    value: '紧急'
                }],
                //需求类型
                types: [{
                    value: '缺员补充'
                }, {
                    value: '人员储备'
                }, {
                    value: '岗位扩编'
                }, {
                    value: '突发人员需求'
                }],

                rules: {
                    dept: [
                        {required: true, message: '申请部门不能为空', trigger: 'change'}
                    ],
                    post: [
                        {required: true, message: '申请岗位不能为空', trigger: 'change'}
                    ],
                    count: [
                        {required: true, message: '请填写需求人数', trigger: 'change'},
                        { type: 'number', message: '必须为数字值'}
                    ],
                    endTime: [
                        {required: true, message: '请选择日期', trigger: 'change'}
                    ],
                    transactor_name: [
                        {required: true, message: '面试人名称不能为空', trigger: 'change'}
                    ],
                    grade: [
                        {required: true, message: '需求等级不能为空', trigger: 'change'}
                    ],
                    type: [
                        {required: true, message: '需求类型不能为空', trigger: 'change'}
                    ],
                    require: [
                        {required: true, message: '岗位要求不能为空', trigger: 'change'}
                    ],
                }
            };
        },
        methods: {

            search() { //查询部门
                let that = this;
                let arr = [];
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

            submitForm(formName) { //提交
                this.$refs[formName].validate((valid) => {

                    if (valid) {
                        this.ruleForm.publish_time = this.getNowTime();//获取当前时间作为发起时间
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
            resetForm(formName) { //清空
                this.$refs[formName].resetFields();
            },

            getNowTime() {
            var date = new Date();
            var seperator1 = "-";
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var currentdate = year + seperator1 + month + seperator1 + strDate;
            return currentdate;
        }
        }
    }
</script>

<style scoped>

    .disabled-style {
        -webkit-text-fill-color: #606266;
    }
    .input-width {
        width: 120px;
    }

    .input-day-width {
        width: 130px;
    }

    .textarea-style {
        width: 50%;
    }

</style>
