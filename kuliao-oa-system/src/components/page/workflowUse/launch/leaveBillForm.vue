<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-date"></i> 请假信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-row>
                    <el-col :span="3" :offset="17">
                        <!--指定下级审批人start-->
                        <select-next-approver ></select-next-approver>
                        <!--指定下级审批人end-->
                    </el-col>
                    <!--指定所有审批人start-->
                    <el-col :span="4">
                        <select-all-approver></select-all-approver>
                    </el-col>
                    <!--指定所有审批人end-->
                </el-row>
                <br>
                <hr>
                <br>
                <el-form :rules="rules" :inline-message="true" ref="form" :model="form" label-width="100px">

                    <el-form-item label="请假人编号" prop="staffId" style="display: none">
                        <el-input class="input-width" :disabled="true" v-model="form.askForLeave.staffId"></el-input>
                    </el-form-item>

                    <el-row type="flex" class="row-bg">
                        <el-col :span="5">
                            <el-form-item label="请假人" prop="staffName">
                                <el-input class="input-width" :disabled="true" v-model="form.askForLeave.staffName"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5" :offset="1">
                            <el-col :span="5">
                                <el-form-item label="所属部门" prop="department">
                                    <el-input class="input-width" :disabled="true" v-model="form.askForLeave.department"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5"  :offset="14">
                                <el-form-item label="抄送人" prop="askForLeave.cc">
                                    <el-input class="input-width" v-model="form.askForLeave.cc"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-col>
                    </el-row>
                    <!--请假时间start-->
                    <date-and-time :msg="flag" ref="dateAndTime" class="label-width"></date-and-time>
                    <!--请假时间end-->
                    <el-form-item label="请假类型" prop="askForLeave.type">
                        <el-radio-group v-model="form.askForLeave.type">
                            <el-radio v-for="item in radio" :key="item" :label="item">{{item}}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="请假事由" prop="askForLeave.reason">
                        <el-input class="textarea-width" type="askForLeave.reason" rows="10"
                                  v-model="form.askForLeave.reason"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import selectAllApprover from "./SelectAllApprover.vue";
    import selectNextApprover from "./SelectNextApprover.vue";
    import dateAndTime from "./dateAndTime.vue";
    import ElButton from "element-ui/packages/button/src/button";
    export default {
        data: function () {
            return {
                flag: true, //日期选项显示控制
                form: {
                    askForLeave: {
                            staffId: '',
                            staffName: '',
                            department: '',
                            type: '',
                            reason: '',
                            cc: '',
                            askForLeaveProcessId: ''
                        },
                    leaveTimeList:[
                        {
                            data: '',
                            time: '',
                            manHour: ''
                        }
                    ],
                },
                rules: {
                    'askForLeave.type': [
                        {required: true, message: '请假类型不能为空', trigger: 'change'}
                    ],
                    'askForLeave.reason': [
                        {required: true, message: '请假事由不能为空', trigger: 'change'}
                    ],
                    'askForLeave.cc': [
                        {required: true, message: '抄送人不能为空', trigger: 'change'}
                    ]
                },
                radio: [
                    '事假', '病假', '年假', '婚嫁', '产假', '丧假', '其他'
                ]
                // processKey: '',
                // processName: ''
            }
        },
        components: {
            ElButton,
            "selectAllApprover": selectAllApprover,
            "selectNextApprover": selectNextApprover,
            "dateAndTime": dateAndTime
        },
        created() {
            let that = this;
            console.log(this.$route.query.processKey);
            console.log(this.$route.query.processName);
            this.getEmpMessage();

        },
        methods: {

            getEmpMessage() {
                let that = this;
                let empId = localStorage.getItem('empNo');
                let param = {
                    empNo: empId
                };
                that.$axios.post('/base/user/query',param).then((res) => {
                    that.form.askForLeave.staffId = res.data.data[0].empNo;
                    console.log('员工编号 '+ that.form.askForLeave.staffId);
                    that.form.askForLeave.staffName = res.data.data[0].username;
                    console.log('员工姓名 '+ that.form.askForLeave.staffName);
                    that.form.askForLeave.department = res.data.data[0].deptName;
                    console.log('员工部门 '+ that.form.askForLeave.department);
                });
            },
            // 重置表单
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //提交表单
            onSubmit(formName) {
                let that = this;
                that.form.leaveTimeList = that.$refs.dateAndTime.askForLeaveTime;
                if(that.form.leaveTimeList === undefined || that.form.leaveTimeList.length < 1) {
                    that.$message.error('请选择请假日期');
                    return;
                } else {
                    for (let i = 0; i < that.form.leaveTimeList.length; i++) {
                        if(that.form.leaveTimeList[i].manHour === undefined || that.form.leaveTimeList[i].manHour === '') {
                            that.$message.error('请填写工时');
                            return
                        }
                    }
                }
                that.form.askForLeave.askForLeaveProcessId = 'qq'; //todo 流程id
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (that.form.leaveTimeList.length === 0) {
                            this.$message.error('请选择请假日期')
                        }
                        else {
                            that.form.askForLeave.date = that.getNowTime();
                            this.$axios.post("/evaluate/askForLeave/add", that.form).then((res) => {
                                this.$message.success(res.data.msg);
                            });
                            //跳转至我发起的页面。
                            this.$router.push({path:'/leaveBill_forMySend'});
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            getNowTime() {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                var currentdate = year + month + strDate;
                return currentdate;
            },
        }
    }
</script>
<style scoped>
    .input-width {
        width: 120px;
    }

    .label-width {
        padding-left: 35px;
    }

    .textarea-width {
        width: 800px;
    }
</style>
