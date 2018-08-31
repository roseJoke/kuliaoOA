<!--请假信息与审批历史-->
<template>
        <div class="container">
            <div class="form-box">
                <div class="crumbs">
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item><i class="el-icon-tickets"></i>流程中心</el-breadcrumb-item>
                        <el-breadcrumb-item><i class="el-icon-date"></i> 请假信息</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <hr class="hr-opinion">
                <br>
                <el-form ref="form" :model="form" label-width="100px">
                    <el-row type="flex" class="row-bg padding-style">
                        <el-col :span="4" :pull="1">
                            <el-form-item label="请假人">
                                <el-input class="input-width disabled-style" :disabled="true" v-model="form.empName"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="5" :offset="1">
                            <el-form-item label="所属部门">
                                <el-input class="input-width disabled-style" :disabled="true" v-model="form.businessData[0].department"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <!--请假时间start-->
                    <date-and-time :msg="flag" ref="dateAndTime" class="label-width"></date-and-time>
                    <!--请假时间end-->
                    <el-form-item class="date-width" label="共计">
                        <el-col :span="5">
                            <el-form-item>
                                <el-input class="input-day-width disabled-style" v-model="form.businessData[0].days" :disabled="true"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col class="line disabled-style" :span="2">天</el-col>
                        <el-col :span="5">
                            <el-form-item>
                                <el-input class="input-day-width disabled-style" v-model="form.businessData[0].times" :disabled="true"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col class="line disabled-style" :span="3"><span>小时</span></el-col>
                    </el-form-item>
                    <el-form-item label="请假类型">
                        <el-radio-group v-model="form.businessData[0].leaveType">
                            <el-radio v-for="item in radio" :key="item" :label="item" :disabled="true" class="disabled-style">{{item}}</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="请假事由">
                        <el-input class="textarea-width disabled-style" type="textarea" rows="5" v-model="form.businessData[0].reason" :disabled="true"></el-input>
                    </el-form-item>
                </el-form>
                <br>

                <div class="crumbs">
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item><i class="el-icon-date"></i> 审批</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>

                <hr>
                <br>
                <el-row>
                    <template>
                        <el-table
                            :data="opinionHis"
                            stripe
                            height="250"
                            border
                            style="width: 80%">
                            <el-table-column
                                prop="name"
                                label="审批人">
                            </el-table-column>
                            <el-table-column
                                prop="opinion"
                                label="审批意见">
                            </el-table-column>
                            <el-table-column
                                prop="time"
                                label="审批时间">
                            </el-table-column>
                        </el-table>
                    </template>

                </el-row>

            </div>
        </div>
</template>

<script>
    import selectNextApprover from "./SelectNextApprover.vue";
    import dateAndTime from "./dateAndTime.vue";

    export default {
        data: function () {
            return {

                flag: false, //日期选项显示控制
                form: {
                    id: null, //请假id
                    empId: '3', //员工id
                    empName: '测试',
                    processKey: 'qingjia1yos', //流程定义key
                    exclusive: 1, //是否是排它 0 否， 1 是
                    data:[ //排它条件
                        {
                            variableName:'pass',
                            variable: '3',
                        }
                    ],
                    scene:1, //指定节点处理人 0:普通  1:指定下一级  2:指定所有
                    sceneData:[{ //节点处理人
                        variableName:'assigneeId2',
                        variable:'某总'
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
                radio: [
                    '事假', '病假', '年假', '婚嫁', '产假', '丧家', '其他'
                ],
                opinionHis: [
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'},
                    {name: '张三', time: '2018-01-25', opinion: '同意'}
                ]
            }
        },
        components: {
            "selectNextApprover": selectNextApprover,
            "dateAndTime": dateAndTime
        },
        methods: {
            onSubmit() {
                this.$message.success('提交成功');
            },
            resetForm(formName) {
                this.opinion.flag = null;
                this.opinion.opinion = null;
                this.$message.success('提交成功');
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



    .crumbs {
        margin-bottom: 3px;
    }
    .div-opinion{
        border-right: #E4E7ED solid 1px;
        border-left: #E4E7ED solid 1px;
        border-bottom: #E4E7ED solid 1px;
    }
    .padding-style {
        padding-left: 17px;
    }
    .disabled-style {
        -webkit-text-fill-color:#606266;
    }
    .hr-opinion {
        border-bottom: 1px solid #909399;
    }
</style>
