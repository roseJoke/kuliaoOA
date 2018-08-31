<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 员工管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_empNo"  placeholder="员工编号" class="handle-input mr10" clearable></el-input>
                <el-input v-model="input_name"  placeholder="员工姓名" class="handle-input mr10" clearable></el-input>
                <el-select v-model="select_department" clearable placeholder="部门" class="handle-select mr10">
                    <template v-for="item in options">
                        <el-option   :key="item.deptName" :label="item.deptName" :value="item.deptName"></el-option>
                    </template>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="query()">搜索</el-button>
            </div>
            <el-table :data="employer_data" border stripe style="width: 100%" >
                <el-table-column prop="empNo" label="员工编号"  >
                </el-table-column>
                <el-table-column prop="username" label="员工姓名" >
                </el-table-column>
                <el-table-column prop="deptName" label="部门" >
                </el-table-column>
                <el-table-column label="员工操作" width="200px" >
                    <template slot-scope="scope" >
                        <el-button type="text" class="el-icon-view" @click="lock(scope.row.empNo)">账户停用</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <el-button type="text" class="el-icon-download" @click="resetPwd(scope.row.empNo)">重置密码</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[10, 20, 50]"
                               :page-size="pagesize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="employer_data.length">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        data(){
            return{
                //部门下拉框
                options:'',
                input_empNo:'',
                //输入框
                input_name:'',
                //表格数据
                employer_data:'',
                select_department:'',
                currentPage: 1,
                pagesize: 10
            }
        },
        created() {
            console.log(process.env.NODE_ENV);
            this.$axios.post("/base/dept/selectAll").then((res) => {
                  console.log(res);
                  this.options = res.data.data;

            })
        },
        methods:{
            query(){
                let params = new URLSearchParams();
                params.append("empNo",this.input_empNo);
                params.append("username",this.input_name);
                params.append("deptName",this.select_department);
                this.$axios.post("/base/user/query",params
                    ,{
                    headers:{
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }
                ).then((res) => {

                    this.employer_data = res.data.data;

                })
            },
            lock(empNo) {
                this.$confirm('此操作将停用/恢复该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = new URLSearchParams();
                    params.append("empNo",empNo);
                    this.$axios.post("/base/user/lockUser", params).then((res) => {

                        console.log(res.data);
                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });
                    });

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });
            },
            resetPwd(empNo) {

                this.$confirm('即将重置该用户的密码, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = new URLSearchParams();
                    params.append("empNo",empNo);
                    this.$axios.post("/base/pwd/reset", params).then((res) => {

                        console.log(res.data);
                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });
                    });

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });
            },
            handleSizeChange: function (size) {
                this.pagesize = size;
            },
            handleCurrentChange: function(currentPage){
                this.currentPage = currentPage;
            }
        }
       ,
        name: "Employer"
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
</style>
