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
                <el-form :rules="rules" :inline-message="true" ref="ruleForm" :model="ruleForm" label-width="100px">
                    <el-form-item label="请假人编号" prop="staffId" style="display: none">
                        <el-input class="input-width" :disabled="true" v-model="ruleForm.staffId"></el-input>
                    </el-form-item>
                    <el-row type="flex" class="row-bg">
                        <el-col :span="5">
                            <el-form-item label="请假人" prop="staffName">
                                <el-input class="input-width" :disabled="true" v-model="ruleForm.staffName"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5" :offset="1">
                            <el-col :span="5">
                                <el-form-item label="所属部门" prop="department">
                                    <el-input class="input-width" :disabled="true" v-model="ruleForm.department"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5"  :offset="14">
                                <el-form-item label="抄送人" prop="cc">
                                    <el-input class="input-width" v-model="ruleForm.cc"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-col>
                    </el-row>
                    <!--请假时间start-->
                    <date-and-time :msg="flag" ref="dateAndTime" class="label-width"></date-and-time>
                    <!--请假时间end-->
                    <el-form-item label="请假类型">
                        <el-radio-group v-model="ruleForm.type">
                            <el-radio v-for="item in radio" :key="item" :label="item">{{item}}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="请假事由" prop="reason">
                        <el-input class="textarea-width" type="textarea" rows="10"
                                  v-model="ruleForm.reason"></el-input>
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
        props: ['value','readonly'],
        data: function () {
            return {
                flag: true, //日期选项显示控制
                ruleForm: {
                    staffId: '',
                    staffName: '',
                    department: '',
                    type: '',
                    reason: '',
                    cc: '',
                    askForLeaveTime:[],
                    manHour: '',
                    askForLeaveProcessId: ''
                },
                rules: {
                    type: [
                        {required: true, message: '请假类型不能为空', trigger: 'change'}
                    ],
                    reason: [
                        {required: true, message: '请假事由不能为空', trigger: 'change'}
                    ],
                    cc: [
                        {required: true, message: '抄送人不能为空', trigger: 'change'}
                    ]
                },
                radio: [
                    '事假', '病假', '年假', '婚嫁', '产假', '丧假', '其他'
                ]
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
            console.log(this.$route.query.processId);
            console.log(this.$route.query.handler);
            // this.getEmpMessage();
        },
        methods: {
            getEmpMessage() {
                let that = this;
                let empId = localStorage.getItem('empNo');
                console.log('员工编号 '+ empId);
                let param = {
                    empNo: empId
                };
                that.$axios.post('/base/user/query',param).then((res) => {
                    console.log('员工编号 '+ empId);
                    that.ruleForm.staffId = res.data.data[0].empNo;
                    that.ruleForm.staffName = res.data.data[0].username;
                    console.log('员工姓名 '+ that.form.staffName);
                    that.ruleForm.department = res.data.data[0].deptName;

                });that.$axios.post('/base/user/query',param).then((res) => {
                    console.log('员工编号 '+ empId);
                    that.ruleForm.staffId = res.data.data[0].empNo;
                    that.ruleForm.staffName = res.data.data[0].username;
                    console.log('员工姓名 '+ that.ruleForm.staffName);
                    that.ruleForm.department = res.data.data[0].deptName;
                });
            },
            // 重置表单
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //提交表单
            onSubmit(formName) {
                let that = this;
                that.ruleForm.askForLeaveTime = that.$refs.dateAndTime.askForLeaveTime;
                if(that.ruleForm.askForLeaveTime === undefined || that.ruleForm.askForLeaveTime.length < 1) {
                    that.$message.error('请选择请假日期');
                    return;
                } else {
                    for (let i = 0; i < that.ruleForm.askForLeaveTime.length; i++) {
                        if(that.ruleForm.askForLeaveTime[i].manHour === undefined || that.ruleForm.askForLeaveTime[i].manHour === '') {
                            that.$message.error('请填写工时');
                            return
                        }
                    }
                }
                that.ruleForm.askForLeaveProcessId = 'qq'; //todo 流程id
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (that.ruleForm.askForLeaveTime.length === 0) {
                            this.$message.error('请选择请假日期')
                        }
                        else {
                            let params = {
                                processId:this.$route.query.processId,
                                activityUserId:localStorage.getItem('empNo'),
                                busi:{
                                    test:'测试',
                                    reason:this.ruleForm.reason,
                                    days:this.ruleForm.manHour,
                                },
                                handler:this.$route.query.handler
                            }
                            this.$axios.post("/workflow/activate/initiate",params).then((res)=>{
                                if(res.data.code==="000000"){
                                    this.$message.success("流程发起已成功")
                                    // 发起流程成功后获取流程实例id
                                    let instanceId = res.data.data.instanceId;
                                    // 然后将请假申请记录添加到考勤子系统中
                                }else {
                                    this.$message.error('出状况了')
                                }
                            });
                            // this.$axios.post("/evaluate/askForLeave/insert", that.form).then((res) => {
                            //     this.$message.success(res.data);
                            // });
                            // //跳转至我发起的页面。
                            // this.$router.push({path:'/leaveBill_forMySend'});
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }
</script>
<style scoped>
    .input-width {
        width: 120px;
    }

    .input-day-width {
        width: 60px;
    }

    .date-width {
        width: 390px;
    }

    .option-width {
        width: 120px;
    }

    .demo-div-width {
        width: 1200px;
        margin: auto;
    }

    .label-width {
        padding-left: 35px;
    }

    .textarea-width {
        width: 800px;
    }
</style>
