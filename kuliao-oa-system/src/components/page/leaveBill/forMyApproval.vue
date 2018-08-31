<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 待我审批</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_staff" placeholder="发起人" class="handle-input mr10" clearable></el-input>
                <el-select v-model="select_department" placeholder="部门" class="handle-select mr10">
                    <el-option v-for="item in options" :key="item.value" :label="item.value" :value="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
            </div>
            <el-table :data="performance_data" border stripe style="width: 100%">
                <el-table-column label="主题">
                    <template slot-scope="scope">
                        <el-button
                            @click.native.prevent="approval(scope.$index)"
                            type="text"
                            size="mini">
                            请假。。。。。。
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column prop="now_step" label="当前步骤">
                </el-table-column>
                <el-table-column prop="approval_emp" label="发起人">
                </el-table-column>
                <el-table-column prop="type" label="类型">
                </el-table-column>
                <el-table-column prop="approval_time" label="发起时间">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="100">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                //部门下拉框
                options: [{
                    value: '行政'
                }, {
                    value: '财务'
                }, {
                    value: '人力'
                }],
                select_department: '',
                //输入框
                input_staff: '',
                //表格数据
                performance_data: [{
                    title: '报销申请',
                    now_step: '张三',
                    approval_emp: '董事长',
                    type: '董事局',
                    approval_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    now_step: '张三',
                    approval_emp: '董事长',
                    type: '董事局',
                    approval_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    now_step: '张三',
                    approval_emp: '董事长',
                    type: '董事局',
                    approval_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    now_step: '张三',
                    approval_emp: '董事长',
                    type: '董事局',
                    approval_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    now_step: '张三',
                    approval_emp: '董事长',
                    type: '董事局',
                    approval_time: '2018-02-03'
                }
                ]
            }
        },
        methods:{
            approval(index){
                let that = this;
                //跳转至审批信息页面。
                that.$router.push({path:'/leaveBill_test'});
            }
        }
    }
</script>

<style scoped>

    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }
    .handle-input {
        width: 120px;
        display: inline-block;
    }
    .crumbs {
        margin: auto;
        margin-bottom: 20px;
    }
</style>
