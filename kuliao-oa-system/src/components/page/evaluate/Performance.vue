<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 绩效考核</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_staff"  placeholder="员工编号/姓名" class="handle-input mr10" maxlength="6" clearable onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                <el-select v-model="select_department" placeholder="部门" class="handle-select mr10" :clearable="true">
                    <el-option key="所有部门" label="所有部门" value="所有部门"></el-option>
                    <el-option  v-for="item in dep_options" :key="item" :label="item" :value="item"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="getPerformance(1)">搜索</el-button>
            </div>
            <el-table :data="performance_data.list" border stripe style="width: 100%" >
                <el-table-column prop="staffId" label="员工编号"  >
                </el-table-column>
                <el-table-column prop="staffName" label="员工姓名" >
                </el-table-column>
                <el-table-column prop="postTitle" label="岗位名称" >
                </el-table-column>
                <el-table-column prop="department" label="部门" >
                </el-table-column>
                <el-table-column prop="dateBegin" label="考核开始时间" >
                </el-table-column>
                <el-table-column prop="judge" label="评审人员" >
                </el-table-column>
                <el-table-column label="绩效考核表" width="200px" prop="performanceTable" >
                    <template slot-scope="scope" >
                        <el-button type="text" class="el-icon-view" @click="getpreviewFile(scope.row.performanceTable)">预览</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a :href="scope.row.performanceTable" class="el-icon-download" :download="scope.row.staffName+'绩效考核表'"> 下载</a>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="performance_data.totalRecord" @current-change="getPerformance" >
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
                //输入框
                input_staff:'',
                //表格数据
                performance_data:[
                    {
                        list:[],
                        totalRecord:''
                    }
                ],
            }
        },
        mounted(){
            this.getPerformance(1);
            this.getPerformanceDepartment();
        },
        methods:{
            getPerformance(currentPage){
                this.$axios.post("/evaluate/performance/record",{
                    staff:this.input_staff,
                    department:this.select_department,
                    pageNum:currentPage
                }).then(res =>{
                    this.performance_data = res.data.data;
                    this.$message.success(res.data.msg);
                });
            },
            getPerformanceDepartment(){
                this.$axios.get("/evaluate/performance/department").then(res=>{
                    this.dep_options=res.data.data;
                })
            },
            getpreviewFile(url){
                this.readFile=true;
                this.fileUrl=url;
            }
        },
        name: "Performance"
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
