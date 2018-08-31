<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>请假记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_staff"  placeholder="员工编号/姓名" class="handle-input mr10" maxlength="6" clearable onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                <el-select v-model="select_department" placeholder="部门" class="handle-select mr10" :clearable="true">
                    <el-option key="所有部门" label="所有部门" value="所有部门"></el-option>
                    <el-option  v-for="item in dep_options" :key="item" :label="item" :value="item"></el-option>
                </el-select>
                <el-select v-model="select_type" placeholder="请假类型" class="handle-select mr10">
                    <el-option key="所有类型" label="所有类型" value="所有类型"></el-option>
                    <el-option  v-for="item in type_options" :key="item" :label="item" :value="item"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="getAskForLaveRecord(1)">搜索</el-button>
            </div>
            <el-table :data="askForLeave_data.list" border stripe style="width: 100%" >
                <el-table-column prop="staffId" label="员工编号"  align="center">
                </el-table-column>
                <el-table-column prop="staffName" label="员工姓名" align="center">
                </el-table-column>
                <el-table-column prop="department" label="部门" align="center">
                </el-table-column>
                <el-table-column prop="date" label="请假发起日期" align="center">
                </el-table-column>
                <el-table-column prop="dateAll" label="请假时间" align="center" width="220px">
                </el-table-column>
                <el-table-column prop="manHour" label="请假工时" align="center">
                </el-table-column>
                <el-table-column prop="type" label="请假类型" align="center">
                </el-table-column>
                <el-table-column prop="reason" label="请假原因" align="center" width="280px">
                </el-table-column>
                <el-table-column prop="result" label="最终结果" align="center">
                </el-table-column>
                <el-table-column prop="cc" label="抄送人" align="center">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="askForLeave_data.totalRecord" @current-change="getAskForLaveRecord">
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
                dep_options:[],
                select_department:'',
                //部门下拉框
                type_options:[],
                select_type:'',
                //输入框
                input_staff:'',
                //表格数据
                askForLeave_data:[]
            }
        },
        mounted(){
            this.getAskForLaveRecord(1);
            this.getAskForLaveDepartment();
            this.getAskForLaveType();
        },
        methods:{
            getAskForLaveRecord(currentPage){
                this.$axios.post("/evaluate/askForLeave/record",{
                    staff:this.input_staff,
                    department:this.select_department,
                    type:this.select_type,
                    pageNum:currentPage
                }).then(res=>{
                    this.askForLeave_data=res.data.data;
                    this.$message.success(res.data.msg);
                });
            },
            getAskForLaveDepartment(){
                this.$axios.get("/evaluate/askForLeave/department").then(res=>{
                    this.dep_options=res.data.data;
                })
            },
            getAskForLaveType(){
                this.$axios.get("/evaluate/askForLeave/type").then(res=>{
                    this.type_options=res.data.data;
                })
            }
        },
        name: "AskForLeave"
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
        width: 130px;
        display: inline-block;
    }
</style>
