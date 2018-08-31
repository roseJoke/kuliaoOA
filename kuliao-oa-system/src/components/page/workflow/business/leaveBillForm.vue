<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-date"></i> 员工请假</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">

                <el-form :disabled="readonly" :rules="rules" ref="ruleForm" :model="ruleForm" label-width="102px">

                    <el-form-item label="请假人编号" prop="staffId" style="display: none">
                        <el-input class="input-width" :disabled="true" v-model="ruleForm.askForLeave.staffId"></el-input>
                    </el-form-item>

                    <el-row>
                        <el-col :xs="24" :sm="24" :md="24" :lg="5" :xl="4">
                            <el-form-item label="请假人" prop="staffName">
                                <el-input :inline-message="true" class="input-width" :disabled="true" v-model="ruleForm.askForLeave.staffName"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="24" :md="{span: 5, offset: 0}" :lg="5" :xl="3">
                            <el-form-item label="所属部门" prop="department">
                                <el-input class="input-width" :disabled="true" v-model="ruleForm.askForLeave.department"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="{span: 15, offset: 0}" :md="{span: 8, offset: 1}" :lg="6" :xl="3">
                            <el-form-item label="抄送人" prop="askForLeave.cc">
                                <el-input class="input-width" v-model="ruleForm.askForLeave.cc"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <!--请假时间start-->
                    <date-and-time :msg="!readonly" v-on:change='getDays' :askForLeaveTimeData ="ruleForm.leaveTimeList" ref="dateAndTime" class="label-width"></date-and-time>
                    <!--请假时间end-->
                    <el-form-item label="请假类型" prop="askForLeave.type">
                        <el-radio-group v-model="ruleForm.askForLeave.type">
                            <el-radio v-for="item in radio" :key="item" :label="item">{{item}}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item class="textarea-width" label="请假事由" prop="askForLeave.reason">
                        <el-input type="textarea" rows="5"
                                  v-model="ruleForm.askForLeave.reason"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import dateAndTime from "./common/dateAndTime.vue";
    export default {
        props: ['readonly'],
        data: function () {
            return {
                flag1: true, //日期选项显示控制
                ruleForm: {
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
                    ],
                },
                days: '',
                rules: {
                    'askForLeave.type': [
                        {required: true, message: '请假类型不能为空', trigger: 'blur'}
                    ],
                    'askForLeave.reason': [
                        {required: true, message: '请假事由不能为空', trigger: 'blur'}
                    ],
                    'askForLeave.cc': [
                        {required: true, message: '抄送人不能为空', trigger: 'blur'}
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
            "dateAndTime": dateAndTime
        },
        created() {
            let that = this;
            //console.log(this.$route.query.processKey);
            //console.log(this.$route.query.processName);
            this.getEmpMessage();

        },
        methods: {

            getDays() { //计算请假天数
                let num = 0;
                this.ruleForm.leaveTimeList = this.$refs.dateAndTime.askForLeaveTime;
                for (let i = 0; i < this.ruleForm.leaveTimeList.length; i++) {
                    num =num+parseInt(this.ruleForm.leaveTimeList[i].manHour);
                }
                this.ruleForm.days = Math.ceil(num/8);
            },

            getEmpMessage() {

                let that = this;
                let empId = localStorage.getItem('empNo');
                let param = {
                    empNo: empId
                };

                that.$axios.post('/base/user/query',param).then((res) => {
                    that.ruleForm.askForLeave.staffId = res.data.data[0].empNo;
                    console.log('员工编号 '+ that.ruleForm.askForLeave.staffId);
                    that.ruleForm.askForLeave.staffName = res.data.data[0].username;
                    console.log('员工姓名 '+ that.ruleForm.askForLeave.staffName);
                    that.ruleForm.askForLeave.department = res.data.data[0].deptName;
                    console.log('员工部门 '+ that.ruleForm.askForLeave.department);

                });

            },
            // 重置表单
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //提交表单
            onSubmit(formName) {
                let that = this;
                that.ruleForm.leaveTimeList = that.$refs.dateAndTime.askForLeaveTime;
                if(that.ruleForm.leaveTimeList === undefined || that.ruleForm.leaveTimeList.length < 1) {
                    that.$message.error('请选择请假日期');
                    return;
                } else {
                    for (let i = 0; i < that.ruleForm.leaveTimeList.length; i++) {
                        if(that.ruleForm.leaveTimeList[i].manHour === undefined || that.ruleForm.leaveTimeList[i].manHour === '') {
                            that.$message.error('请填写工时');
                            return
                        }
                    }
                }
                that.ruleForm.askForLeave.askForLeaveProcessId = 'qq'; //todo 流程id
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (that.ruleForm.leaveTimeList.length === 0) {
                            this.$message.error('请选择请假日期')
                        }
                        else {
                            that.ruleForm.askForLeave.date = that.getNowTime();
                            this.$axios.post("/evaluate/askForLeave/add", that.ruleForm).then((res) => {
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
        width: 170px;
    }

    .label-width {
        padding-left: 35px;
    }

    .textarea-width {
        width: 53%;
    }
    .container {
        padding: 30px;
        background: #fff;
        border: 1px solid #ddd;
        border-radius: 5px;
        height: 400px;
        height: auto;
    }
</style>
