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
                <el-select v-model="select_department" placeholder="审批状态" class="handle-select mr10">
                    <el-option v-for="item in options" :key="item.value" :label="item.value"
                               :value="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
            </div>
            <el-table :data="ApprovalList" border stripe style="width: 100%" :row-class-name="tableRowClassName">
                <el-table-column prop="processName" label="流程名称">
                </el-table-column>
                <el-table-column prop="busi.reason" label="原因">
                </el-table-column>
                <el-table-column prop="date" label="发起时间">
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
                <h1>流程处理结果</h1>
                <el-progress :percentage="processsStep.percentage" :status="processsStep.status"></el-progress>
                <hr/>
                <el-table :data="processsStep.processsStep">
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
    export default {
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
                select_department: '',
                //输入框 流程名称
                input_pname: '',
                //表格数据
                performance_data: [],
                //流程步骤
                processsStep: {},
                handlerResult: '', //流程处理结果
            }
        },
        created() {
            let that = this;
            let params = new URLSearchParams();
            params.append('userId', "263");
            that.$axios.post("/workflow/activate/queryByUserId", params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }
            ).then((res) => {
                that.performance_data = res.data.data
                // 然后在这里需要查看每个流程处理状态
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
                        //this.$message.success(res.data.msg);
                        //将获取到的数据显示出来；
                        that.handlerResult = res.data.data;
                        let n = 0,r =0;
                        that.handlerResult.stepResult.forEach(v=>{
                            n++;
                           if(v.status==1){
                               r++;
                           };
                        })
                        that.handlerResult.percentage = r/n;
                        console.log(n+"  "+r);
                        console.log(that.handlerResult.percentage);
                        let status = that.status;
                        console.log(status)
                        switch (status) {
                            case 2:
                                that.status = 'success';
                                break;
                            case 3:
                                that.status = 'exception';
                                break;
                            default:
                                break;
                        }
                        that.processdialogTableVisible = true;
                       /* this.handlerResult = res.data.data.status;
                        this.processsStep = res.data.data.stepResult;
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
                        })*/
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
