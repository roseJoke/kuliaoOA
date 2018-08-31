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
                        <select-next-approver></select-next-approver>
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
                    <el-row type="flex" class="row-bg">
                        <el-col :span="5">
                            <el-form-item label="请假人" prop="empName">
                                <el-input class="input-width" :disabled="true" v-model="form.empName"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5" :offset="1">
                            <el-form-item label="所属部门" prop="businessData[0].department">
                                <el-select class="option-width" v-model="form.businessData[0].department"
                                           placeholder="请选择" :inline-message="true">
                                    <el-option v-for="item in options" :key="item.value" :label="item.value"
                                               :value="item.value"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <!--请假时间start-->
                    <date-and-time :msg="flag" ref="dateAndTime" class="label-width"></date-and-time>
                    <!--请假时间end-->
                    <el-form-item class="date-width" label="共计">
                        <el-col :span="5">
                            <el-form-item prop="businessData[0].days">
                                <el-input class="input-day-width" v-model.number="form.businessData[0].days" ></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col class="line" :span="2">天</el-col>
                        <el-col :span="5">
                            <el-form-item prop="businessData[0].times">
                                <el-input class="input-day-width" v-model.number="form.businessData[0].times" ></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col class="line" :span="3"><span>小时</span></el-col>
                    </el-form-item>

                    <el-form-item label="请假类型">
                        <el-radio-group v-model="form.businessData[0].leaveType">
                            <el-radio v-for="item in radio" :key="item" :label="item">{{item}}</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="请假事由" prop="businessData[0].reason">
                        <el-input class="textarea-width" type="textarea" rows="10"
                                  v-model="form.businessData[0].reason"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :round="true" @click="onSubmit('form')">提交</el-button>
                        <el-button type="primary" :round="true" @click="resetForm('form')" @dbclick="$router.back(-1)">重置</el-button>
                        <el-button type="primary" :round="true" @click="$router.back(-1)">取消</el-button>
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
                    id: null, //请假id
                    empId: '3', //员工id
                    empName: '测试',
                    processKey: 'qingjia1yos', //流程定义key
                    exclusive: 1, //是否是排它 0 否， 1 是
                    data: [ //排它条件
                        {
                            variableName: 'pass',
                            variable: '3',
                        }
                    ],
                    scene: 1, //指定节点处理人 0:普通  1:指定下一级  2:指定所有
                    sceneData: [{ //节点处理人
                        variableName: 'assigneeId2',
                        variable: '某总'
                    }],
                    businessData: [{
                        startDate: '',
                        startTime: '',
                        department: '',
                        leaveDate: [],
                        leaveTime: [new Date(2018, 9, 11, 8, 40), new Date(2018, 9, 10, 9, 40)],
                        days: null,
                        times: null,
                        leaveType: '事假',
                        reason: ''
                    }]
                },
                rules: {
                    empName: [
                        {required: true, message: '没有获取到用户名', trigger: 'change'}
                    ],
                    'businessData[0].department': [
                        {required: true, message: '请选择所属部门', trigger: 'change'}
                    ],

                    'businessData[0].days': [
                        {required: true, message: '请假天数不能为空', trigger: 'change'},
                        {type: 'number', message: '年龄必须为数字值'}
                    ],
                    'businessData[0].times': [
                        {required: true, message: '请假小时不能为空', trigger: 'change'},
                        {type: 'number', message: '年龄必须为数字值'}
                    ],
                    'businessData[0].reason': [
                        {required: true, message: '请假理由不能为空', trigger: 'change'}
                    ]
                },
                radio: [
                    '事假', '病假', '年假', '婚嫁', '产假', '丧假', '其他'
                ],
                //部门下拉框
                options: [{
                    value: '人事部'
                }, {
                    value: '财务部'
                }, {
                    value: '研发部'
                }],
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
            console.log(this.$route.query.processKey);
            console.log(this.$route.query.processName);
        },
        methods: {
            // 重置表单
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //提交表单
            onSubmit(formName) {
                let that = this;
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (that.$refs.dateAndTime.tableData.length === 0) {

                        }
                        else {
                            this.$axios.post("/workflow/startProcess", that.form).then((res) => {
                                this.$message.success(res.data);
                            });
                            //跳转至我发起的页面。
                            this.$router.push({path:'/leaveBill_forMySend'});
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
