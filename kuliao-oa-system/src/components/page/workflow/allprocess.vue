<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 所有流程</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <!-- //流程发起人-->
                <template>
                    <el-select clearable
                               v-model="select.userId"
                               filterable
                               remote
                               reserve-keyword
                               placeholder="发起人"
                               :remote-method="remoteMethod"
                               :loading="loading">
                        <el-option
                            v-for="(item,index) in userNameList"
                            :key="index"
                            :label="item.empNo"
                            :value="item.empNo">
                            <span style="float: left">{{ item.username }}</span>

                            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.empNo }}</span>
                            <span style="float: right;margin-right: 5px">{{ item.deptName }}</span>
                        </el-option>
                    </el-select>
                </template>
                <!--流程-->
                <template>
                    <el-select v-model="select.processId" filterable placeholder="选择流程" clearable filterable>
                        <el-option
                            v-for="item in queryAll"
                            :key="item.processId"
                            :label="item.processName"
                            :value="item.processId">
                        </el-option>
                    </el-select>
                </template>
                <el-button type="primary" icon="search" @click="search(select)">搜索</el-button>
            </div>
            <el-table :data="processList" border stripe style="width: 100%">
                <el-table-column prop="processName" label="流程名称">
                </el-table-column>
                <el-table-column label="发起人" width="200">
                    <template slot-scope="scope">
                        <user-name :emp-no="scope.row?scope.row.activityUserId:null"></user-name>
                    </template>
                    <!-- -->
                </el-table-column>
                <el-table-column prop="busi.publish_time" label="发起时间" sortable>
                </el-table-column>
                <el-table-column  label="状态" >
                    <template slot-scope="scope">
                        <workflow-status :instanceId="scope.row?scope.row.instanceId:null"></workflow-status>
                    </template>
                </el-table-column>
            </el-table>
            <br/>
            <div class="block" align="right">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-size="pagesize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="process_data.length">
                </el-pagination>
            </div>
        </div>
    </div>
</template>
<script>
    import userName from './business/common/userName.vue'
    import workflowStatus from './business/common/workflowStatus.vue'

    export default {
        components: {
            userName, workflowStatus
        },
        data() {
            return {
                //   list:[],
                userNameList: [],//下拉列表清单
                // select_username:'',//下拉列表框中绑定的用户名。
                loading: false,
                process_data: [],
                pagesize: 10,
                currentPage: 1,
                queryAll: [],
                select: {
                    processId: '',
                    userId: ''
                }
            }
        },
        created() {
            this.$axios.post("/workflow/definition/queryAll").then(res => {
                this.queryAll = res.data.data;
            })
        },
        computed: {
            processList: function () {
                return this.process_data.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
            }
        },
        methods: {
            search(select) {
                let that = this;
                let params = new URLSearchParams();
                params.append('activityUserId', select.userId);
                params.append('processId', select.processId)
                //activityUserId=000002&processId=P20180808_000001
                this.$axios.post("/workflow/activate/queryAll", params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then(res => {
                    this.currentPage = 1;
                    this.process_data = res.data.data;
                })

            },
            handleSizeChange(pagesize) {
                this.pagesize = pagesize;
            },
            handleCurrentChange(currentPage) {
                this.currentPage = currentPage;
            },
            remoteMethod(user) {
                this.loading = true;
                let that = this;
                if (user !== '') {
                    let params = new URLSearchParams();
                    params.append('empNo', user);
                    that.$axios.post("/base/user/query", params).then((res) => {
                        that.userNameList = res.data.data;
                        this.loading = false;
                    })
                } else {
                    this.loading = false;
                    this.userNameList = []
                }
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
        width: 300px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }
</style>
