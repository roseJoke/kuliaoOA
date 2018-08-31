<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 我已审批</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container table-div">
            <div class="handle-box">
                <el-input v-model="input_staff" placeholder="主题" class="handle-input mr10" clearable></el-input>
                <el-select v-model="select_department" placeholder="处理过程" class="handle-select mr10">
                    <el-option v-for="item in options" :key="item.value" :label="item.value" :value="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
            </div>
            <el-table :data="performance_data" border stripe style="width: 100%">
                <el-table-column label="主题">
                    <template slot-scope="scope">
                        <el-button
                            @click.native.prevent="startProcess(scope.$index)"
                            type="text"
                            size="mini">
                            请假。。。。。。
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column prop="treat_process" label="处理过程">
                </el-table-column>
                <el-table-column prop="treat_time" label="处理时间">
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
                    value: '同意'
                }, {
                    value: '拒绝'
                }],
                select_department: '',
                //输入框
                input_staff: '',
                //表格数据
                performance_data: [{
                    title: '报销申请',
                    treat_process: '同意',
                    treat_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    treat_process: '拒绝',
                    treat_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    treat_process: '同意',
                    treat_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    treat_process: '拒绝',
                    treat_time: '2018-02-03'
                }, {
                    title: '报销申请',
                    treat_process: '同意',
                    treat_time: '2018-02-03'
                }
                ]
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
        margin-bottom: 20px;
    }
</style>
