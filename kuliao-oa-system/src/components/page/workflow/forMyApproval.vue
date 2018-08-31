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
                <el-input v-model="input_founder" placeholder="发起人" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
            </div>
            <el-table :data="ApprovalList" border stripe style="width: 100%">
                <el-table-column prop="activateData.processName" label="流程名称">
                </el-table-column>
                <el-table-column prop="userName" label="发起人" width="100">
                </el-table-column>
                <el-table-column prop="Launch_time" label="发起时间"  sortable>
                </el-table-column>
                <el-table-column label="审批">
                    <template slot-scope="scope">
                        <el-button
                            @click.native.prevent="approval(scope.row)"
                            type="text"
                            size="mini">
                            审批
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <br>
            <div class="block" align="right">
                <el-pagination
                    :current-page="currentPage"
                    :page-size="pagesize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="performance_data.length">
                </el-pagination>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                pagesize: 4,//当前页面的条目
                userNames: [],//当前的员工列表
                currentPage: 1,//当前页码
                waitMeProceesList: [],//带我审批
                //输入框
                input_founder: '',
                //表格数据
                performance_data: [],
                idx: -1,
            }
        },
        mounted() {
            let that = this;
            let params = new URLSearchParams();
             params.append('userId', localStorage.getItem('empNo'));
           // params.append('userId', 123);
            that.$axios.post("/workflow/activate/queryWaitDeal", params).then((res) => {
                if (res.data) {
                    // that.$message.success(res.data.msg);
                    console.log(res)
                    that.performance_data = res.data.data;
                    // 获取发起人姓名
                    that.performance_data.forEach((item) => {
                        let username = '';
                        let params = new URLSearchParams();
                        params.append('empNo', item.activateData.activityUserId);
                        that.$axios.post("/base/user/query", params).then((res) => {
                            console.log(res);
                            username = res.data.data[0].username;
                            that.$set(item, 'userName', res.data.data[0].deptName + "-" + username);//vue 的给对象新增属性的方法
                            that.$set(item, 'Launch_time',new Date().getFullYear()+'年'+(new Date().getMonth()+1)+'月'+new Date().getDate()+'日');
                            // 此处不能写成item.userName = username;原因在于视图无法立即更新。
                            that.waitMeProceesList = that.performance_data;
                        })
                    });

                }

            })
        },
        computed: {
            ApprovalList: function () {
                return this.performance_data.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
            }
        },
        watch: {},
        methods: {
            //根据模糊人
            search() {
                let that = this;
                let temp_data = that.performance_data.filter((item) => {
                    if (item.userName.indexOf(that.input_founder) != -1) {
                        return item;
                    }
                })
                if (that.input_founder == '') {
                    that.performance_data = that.waitMeProceesList;
                } else {
                    that.performance_data = temp_data;
                }
            },
            //发起审批
            approval(index) {
                let that = this;
                // 跳转到审批页面
                console.log(index);
                // that.$router.push({path: '/work/reply', query: {'processId': this.processId}})
                // instanceId
                this.$axios.post("/workflow/definition/query", {processId: index.processId}).then(res => {
                    //console.log(res);
                    console.log(res.data.data)
                    that.$router.push({path: "/work/reply" + res.data.data.relationUrl, query: {process: index}});
                })
                //that.$router.push({path: '/work/reply', query: {process:index/*instanceId: index.instanceId,processId:index.processId*/}})
                // that.$router.push({path:'/leaveBill_test'})
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
