<!--添加员工测试-->
<!--<template>-->
<!--<div>-->
<!--<el-button type="primary" round="true" @click="dialogVisibleMax = true">指定所有审批人</el-button>-->

<!--<el-dialog-->
<!--title="提示"-->
<!--:visible.sync="dialogVisibleMax"-->
<!--width="50%"-->
<!--:before-close="handleCloseMax">-->

<!--&lt;!&ndash;选择成员start&ndash;&gt;-->
<!--<template>-->
<!--<div>-->

<!--<el-table-->
<!--:data="dynamicTags"-->
<!--style="width: 100%">-->
<!--<el-table-column-->
<!--label="审批节点"-->
<!--width="180">-->
<!--<template slot-scope="scope">-->
<!--<el-tag type="success">{{scope.row.nodeName}}</el-tag>-->
<!--</template>-->
<!--</el-table-column>-->
<!--<el-table-column-->
<!--label="审批人"-->
<!--width="180">-->
<!--<template slot-scope="scope">-->
<!--<el-tag-->
<!--:key="scope.row.empName"-->
<!--closable-->
<!--v-if="scope.row.empName != null"-->
<!--:disable-transitions="false"-->
<!--@close="handleCloseTag(scope.row.empName)">-->
<!--{{scope.row.empName}}-->
<!--</el-tag>-->

<!--</template>-->
<!--</el-table-column>-->
<!--<el-table-column label="添加审批人">-->
<!--<template slot-scope="scope">-->
<!--<el-button-->
<!--type="success"-->
<!--size="mini"-->
<!--@click="setEmp(scope.$index, scope.row)">选择成员-->
<!--</el-button>-->

<!--<el-dialog-->
<!--title="设置审批人"-->
<!--:visible.sync="dialogVisibleMin"-->
<!--width="40%"-->
<!--:append-to-body="true"-->
<!--:before-close="handleCloseMin">-->
<!--&lt;!&ndash;成员树&ndash;&gt;-->
<!--<emp-tree ref="empTree"></emp-tree>-->

<!--<span slot="footer" class="dialog-footer">-->
<!--<el-button @click="dialogVisibleMin = false">取 消</el-button>-->
<!--<el-button type="primary" @click="getEmpTreeMessage()">确 定</el-button>-->
<!--</span>-->
<!--</el-dialog>-->

<!--</template>-->
<!--</el-table-column>-->
<!--</el-table>-->
<!--</div>-->
<!--</template>-->
<!--&lt;!&ndash;选择成员end&ndash;&gt;-->

<!--<span slot="footer" class="dialog-footer">-->
<!--<el-button @click="dialogVisibleMax = false">取 消</el-button>-->
<!--<el-button type="primary" @click="dialogVisibleMax = false">确 定</el-button>-->
<!--</span>-->
<!--</el-dialog>-->
<!--</div>-->
<!--</template>-->

<!--<script>-->
<!--import empTree from "./empTree.vue"-->

<!--export default {-->
<!--data() {-->
<!--return {-->
<!--index: 0, //当前添加审批人所在行-->
<!--dialogVisibleMax: false, //外面弹框值-->
<!--dialogVisibleMin: false, //里面弹框值-->
<!--dynamicTags: [-->
<!--{-->
<!--nodeId: '',-->
<!--nodeName: '组长审批',-->
<!--empName: '张三',-->
<!--empId: null-->
<!--},-->
<!--{-->
<!--nodeId: '',-->
<!--nodeName: '老总审批',-->
<!--empName: '老总',-->
<!--empId: null-->
<!--}-->
<!--],-->
<!--inputVisible: false,-->

<!--};-->
<!--},-->
<!--components: {-->
<!--"empTree": empTree-->
<!--},-->
<!--methods: {-->
<!--handleCloseMax(done) { //外面弹框-->
<!--this.$confirm('确认关闭？')-->
<!--.then(_ => {-->
<!--done();-->
<!--})-->
<!--.catch(_ => {-->
<!--});-->
<!--},-->
<!--handleCloseMin(done) { //里面弹框-->
<!--this.$confirm('确认关闭？')-->
<!--.then(_ => {-->
<!--done();-->
<!--})-->
<!--.catch(_ => {-->
<!--});-->
<!--},-->
<!--handleCloseTag(tag) {-->
<!--this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);-->
<!--},-->
<!--getEmpTreeMessage() { //获取子组件的数据，并创建tag标签-->
<!--this.dialogVisibleMin = false;-->
<!--let that = this;-->
<!--let arr = this.$refs.empTree.getCheckedNodes();-->
<!--if (arr.length > 1) {-->
<!--this.$message.error('错了哦，只能选取一个人');-->
<!--} else {-->
<!--arr.forEach(function (arrs) {-->
<!--let lables = arrs.label;-->
<!--let empId = arrs.id;-->
<!--console.log('测试2' + lables+'|||'+empId);-->
<!--if (lables) {-->
<!--that.dynamicTags[that.index].empName = lables;-->
<!--that.dynamicTags[that.index].empId = empId;-->
<!--console.log('测试3'+that.dynamicTags[that.index].empName +'|||'+that.dynamicTags[that.index].empId);-->
<!--}-->
<!--})-->
<!--}-->
<!--},-->

<!--setEmp(index, row) { //记录当前行-->
<!--this.index = index;-->
<!--this.dialogVisibleMin = true-->
<!--console.log('测试1' + this.index)-->
<!--}-->
<!--}-->
<!--};-->
<!--</script>-->

<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 审批信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <br/>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" label-width="100px">

                    <el-row>
                        <el-col :span="3" :offset="20">
                            <!--指定下级审批人start-->
                            <select-next-approver></select-next-approver>
                            <!--指定下级审批人end-->
                        </el-col>
                    </el-row>
                    <br>
                    <hr>
                    <br>
                    <el-row type="flex" class="row-bg">
                        <el-col>
                            <el-form-item label="请假人">
                                <el-input class="input-width" :disabled="true" v-model="form.empName"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :pull="1">
                            <el-form-item label="所属部门">
                                <el-input class="input-width" :disabled="true" v-model="form.businessData[0].department"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :pull="3">
                            <el-form-item class="date-width" label="申请时间" >
                                <el-col :span="11">
                                    <el-date-picker type="date" :disabled="true" placeholder="选择日期" v-model="form.startDate" style="width: 100%;"></el-date-picker>
                                </el-col>
                                <el-col class="line" :span="2">-</el-col>
                                <el-col :span="11">
                                    <el-time-picker placeholder="选择时间" :disabled="true" v-model="form.startTime" style="width: 100%;"></el-time-picker>
                                </el-col>
                            </el-form-item>
                        </el-col>
                    </el-row>


                    <el-row type="flex" class="row-bg">
                        <el-col :span="7">
                            <el-form-item class="date-width" label="请假日期" >
                                <div class="block">
                                    <el-date-picker
                                        type="dates"
                                        :disabled="true"
                                        v-model="form.businessData[0].leaveDate"
                                        placeholder="选择一个或多个日期">
                                    </el-date-picker>
                                </div>
                            </el-form-item>
                        </el-col>
                        <el-col>
                            <el-form-item class="date-width" label="请假时间">
                                <el-time-picker
                                    is-range
                                    :disabled="true"
                                    v-model="form.businessData[0].leaveTime"
                                    range-separator="至"
                                    start-placeholder="开始时间"
                                    end-placeholder="结束时间"
                                    placeholder="选择时间范围">
                                </el-time-picker>
                            </el-form-item>
                        </el-col>

                        <el-col>
                            <el-form-item class="date-width" label="共计">
                                <el-col :span="5">
                                    <el-form-item>
                                        <el-input class="input-day-width" :disabled="true" v-model="form.businessData[0].days"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col class="line" :span="2">天</el-col>
                                <el-col :span="5">
                                    <el-form-item>
                                        <el-input class="input-day-width" :disabled="true" v-model="form.businessData[0].times"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col class="line" :span="3"><span>小时</span></el-col>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-form-item label="请假类型">
                        <el-radio-group v-model="form.businessData[0].leaveType">
                            <el-radio :disabled="true" v-for="item in radio" :key="item" :label="item">{{item}}</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="请假事由">
                        <el-input :disabled="true" class="textarea-width" type="textarea" rows="5" v-model="form.businessData[0].reason"></el-input>
                    </el-form-item>
                </el-form>
                <div class="crumbs">
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item><i class="el-icon-tickets"></i> 审批</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <br>
                <hr>
                <br>

                <!--审批-->
                <el-form ref="opinion" :model="opinion" label-width="100px">
                    <el-form-item label="是否通过">
                        <el-radio-group v-model="opinion.flag">
                            <el-radio v-for="item in opinionRedio" :key="item.key" :label="item.key">{{item.name}}</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="审批意见">
                        <el-input class="textarea-width" placeholder="审批意见" type="textarea" rows="5" v-model="opinion.opinion"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :round="true" @click="onSubmit">提交</el-button>
                        <el-button type="primary" :round="true" @click="cancel">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import selectNextApprover from "./SelectNextApprover.vue";

    export default {
        data: function () {
            return {
                form: {
                    id: null, //请假id
                    empId: '3', //员工id
                    empName: '张超',
                    processKey: 'qingjia1yos', //流程定义key
                    exclusive: 1, //是否是排它 0 否， 1 是
                    data:[ //排它条件
                        {
                            variableName:'pass',
                            variable: '3',
                        }
                    ],
                    startDate: new Date(2018, 9, 10, 8, 40),
                    startTime: new Date(2018, 9, 10, 8, 40),
                    scene:1, //指定节点处理人 0:普通  1:指定下一级  2:指定所有
                    sceneData:[{ //节点处理人
                        variableName:'assigneeId2',
                        variable:'某总'
                    }],
                    businessData: [{
                        department: '研发部',
                        leaveDate: [new Date(2018, 9, 10, 8, 40), new Date(2018, 9, 10, 9, 40), new Date(2018, 9, 10, 9, 40)],
                        leaveTime: [new Date(2018, 9, 10, 8, 40), new Date(2018, 9, 10, 9, 40)],
                        days: '3',
                        times: '2',
                        leaveType: '事假',
                        reason: '生病了，请个假'
                    }]
                },
                opinion: {
                    flag: true,
                    opinion: ''
                },
                radio: [
                    '事假', '病假', '年假', '婚嫁', '产假', '丧家', '其他'
                ],
                opinionRedio: [
                    {key: true, name:'同意'}, {key: false, name:'拒绝'}
                ]
            }
        },
        components: {
            "selectNextApprover": selectNextApprover
        },
        methods: {
            onSubmit() {
                this.$router.push({path:'/leaveBill_approvalDone'})
                this.$message.success('提交成功');
            },
            cancel() {
                this.$router.push({path:'/leaveBill_forMyApproval'});
                this.$message.success('取消成功');
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
    .crumbs {
        margin-bottom: 3px;
    }
</style>

