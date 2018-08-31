<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 我发起的</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container ">
            <div class="handle-box">
                <el-input v-model="input_pname" placeholder="流程名称" class="handle-input mr10" clearable></el-input>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
            </div>
            <el-table :data="ApprovalList" border stripe style="width: 100%" :row-class-name="tableRowClassName">
                <el-table-column prop="processName" label="流程名称">
                </el-table-column>
                <el-table-column prop="Launch_time" label="发起时间" sortable>
                </el-table-column>
                <el-table-column label="审批情况">
                    <template slot-scope="scope">
                        <el-button
                            @click="LookProcess(scope.row.instanceId)"
                            type="text"
                            size="mini">
                            详情查看
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block" align="right">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[4, 6,8, 10]"
                    :page-size="pagesize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="performance_data.length">
                </el-pagination>
            </div>
            <!--显示当前流程的处理步骤-->
            <el-dialog title="审批情况" :visible.sync="processdialogTableVisible" width="500px">
                <!--<div>
                    <el-progress :show-text="true" :text-inside="true" :percentage="handlerResult.percentage" :status="handlerResult.status":stroke-width="18" ></el-progress>
                </div>-->
                <template >
                    <workflow-status :instanceId="instanceId?instanceId:null"></workflow-status>
                </template>
                <el-table :data="processsStep">
                    <el-table-column property="stepId" label="审批节点" width="150"></el-table-column>
                    <el-table-column property="stepName" label="审批名称" width="200"></el-table-column>
                    <el-table-column prop="handelstatus" label="当前处理状态" width="100">
                        <template slot-scope="scope">
                            <el-tag :type="scope.row.handelstatus === '拒绝' ? 'danger' : 'info'" disable-transitions>
                                {{scope.row.handelstatus}}
                            </el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </el-dialog>
        </div>
    </div>
</template>
<script>
    import workflowStatus from './business/common/workflowStatus.vue'
    export default {
        components:{
            workflowStatus
        },
        data() {
            return {
                //查看步骤的编辑框
                processdialogTableVisible: false,
                currentPage: 1,//当前页码
                pagesize: 4,//当前页面的条目
                //部门下拉框
                options: [{
                    value: '正在审批'
                }, {
                    value: '审批结束'
                }],
                //输入框 流程名称
                input_pname: '',
                //表格数据
                performance_data: [],
                //流程步骤
                processsStep: [],
                handlerResult: {}, //流程处理结果
                instanceId:null
            }
        },
        mounted() {
            let that = this;
            let params = new URLSearchParams();
            params.append('userId', localStorage.getItem('empNo'));
            that.$axios.post("/workflow/activate/queryByUserId", params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }
            ).then((res) => {
                that.performance_data = res.data.data
                // 然后在这里需要查看每个流程处理状态
                that.performance_data.forEach((item)=>{
                    that.$set(item,'Launch_time',new Date().getFullYear()+'年'+(new Date().getMonth()+1)+'月'+new Date().getDate()+'日');
                })
            })
        },
        computed: {
            ApprovalList: function () {
                return this.performance_data.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
            }
        },
        watch: {
            select_department() {
            }
        },
        methods: {
            tableRowClassName({row, rowIndex}) {
                console.log(row);
                return '';
            },
            //查询实例处理情况
            LookProcess(instanceId) {
                let that = this;
                let params = new URLSearchParams();
                params.append('instanceId', instanceId);
                this.$axios.post("/workflow/activate/queryByInstanceId", params, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }
                ).then((res) => {
                    if (res.data.code === "000000") {

                        // 将获取到的数据显示出来；
                        that.handlerResult = res.data.data;  //拿到当前流程处理的状态
                        let result = res.data.data.status;
                        that.instanceId=instanceId;
                        that.processdialogTableVisible = true;
                        this.processsStep = res.data.data.stepResult;
                        let num = this.processsStep.length;
                        let success = 0;
                        for (let i = 0;i<num;i++){
                            if(this.processsStep[i].status==='3'){
                                this.handlerResult.percentage = (i+1)/this.processsStep.length *100;
                                this.handlerResult.status = 'exception';
                                break;
                            }else if (this.processsStep[i].status==='2'){
                                success++;
                                this.handlerResult.percentage = success/this.processsStep.length*100;
                                this.handlerResult.status = 'success';
                            }else {
                                this.handlerResult.percentage = 0;
                                this.handlerResult.status = 'success';
                            }
                        }
                        //对拿到的数据进行判断
                        this.processsStep.forEach((item) => {
                            if (item.status === '0') {
                                this.$set(item, 'handelstatus', '未到达');
                            } else if (item.status === '1') {
                                this.$set(item, 'handelstatus', '待处理');
                            } else if (item.status === '2') {
                                this.$set(item, 'handelstatus', '通过');
                            } else if (item.status === '3') {
                                this.$set(item, 'handelstatus', '通过');
                            } else {
                                console.log('不做处理')
                            }
                        })
                    }
                })
            },
            //修改条目大小
            handleSizeChange(pagesize) {
                this.pagesize = pagesize;
            },
            //修改页面的页码
            handleCurrentChange(currentPage) {
                this.currentPage = currentPage;
            }
        }
    }
</script>

<style scoped>

    .el-table .warning-row {
        background: oldlace;
    }

    .el-table .success-row {
        background: #f0f9eb;
    }

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
