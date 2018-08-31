<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 转正考评</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_staff"  placeholder="员工编号/姓名" class="handle-input mr10" maxlength="6" clearable onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                <el-select v-model="select_department" placeholder="部门" class="handle-select mr10" :clearable="true">
                    <el-option key="所有部门" label="所有部门" value="所有部门"></el-option>
                    <el-option  v-for="item in dep_options" :key="item" :label="item" :value="item"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="getPositive(1)">搜索</el-button>
            </div>
            <el-table :data="positive_data.list" border stripe style="width: 100%" >
                <el-table-column prop="staffId" label="员工编号"  align="center">
                </el-table-column>
                <el-table-column prop="staffName" label="员工姓名" align="center">
                </el-table-column>
                <el-table-column prop="postTitle" label="岗位名称" align="center">
                </el-table-column>
                <el-table-column prop="department" label="部门" align="center">
                </el-table-column>
                <el-table-column prop="entryTime" label="入职时间" align="center">
                </el-table-column>
                <el-table-column prop="beginTime" label="申请转正时间" align="center">
                </el-table-column>
                <el-table-column prop="positiveTime" label="转正时间" align="center" >
                </el-table-column>
                <el-table-column label="转正申请书" width="150px" align="center" prop="positiveApplicationForm">
                    <template slot-scope="scope" >
                        <el-button type="text" class="el-icon-view" @click="getpreviewFile(scope.row.positiveApplicationForm)">预览</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a :href="scope.row.positiveApplicationForm" class="el-icon-download" :download="scope.row.staffName+'转正申请书'"> 下载</a>
                    </template>
                </el-table-column>
                <el-table-column label="转正考评表" width="150px" align="center" prop="positiveTable">
                    <template slot-scope="scope" >
                        <a :href="scope.row.positiveTable" class="el-icon-download" :download="scope.row.staffName+'转正考评表'"> 下载</a>
                    </template>
                </el-table-column>
                <!--<el-table-column label="相关领导意见"  align="center" width="100px" >-->
                    <!--<template slot-scope="scope" >-->
                    <!--<el-popover placement="top" width="400"-->
                        <!--trigger="click">-->
                        <!--<el-form>-->
                            <!--<el-form-item label="直属领导意见：">-->
                                <!--<span>{{scope.row.direct_opinion}}</span>-->
                            <!--</el-form-item>-->
                            <!--<el-form-item label="人事部门意见：">-->
                                <!--<span>{{scope.row.hr_opinion }}</span>-->
                            <!--</el-form-item>-->
                            <!--<el-form-item label="部门经理意见：">-->
                                <!--<span>{{scope.row.manager_opinion }}</span>-->
                            <!--</el-form-item>-->
                            <!--<el-form-item label="总经理意见：">-->
                                <!--<span>{{scope.row.general_opinion }}</span>-->
                            <!--</el-form-item>-->
                        <!--</el-form>-->
                        <!--<el-button slot="reference">查看详情</el-button>-->

                    <!--</el-popover>-->
                    <!--</template>-->

                <!--</el-table-column>-->
                <el-table-column prop="positiveResult" label="最终考评结果" align="center">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="positive_data.totalRecord" @current-change="getPositive">
                </el-pagination>
            </div>
        </div>

        <el-dialog :visible.sync="readFile">
            <preview-file :url="fileUrl"></preview-file>
        </el-dialog>



    </div>
</template>

<script>
    import previewFile from "../file/index/readfile";

    export default {
        components:{
            previewFile
        },

        data(){
            return{
                //部门下拉框
                dep_options:[],
                select_department:'',
                //输入框
                input_staff:'',
                //表格数据
                positive_data:[{
                    list:[],
                    totalRecord:''
                }
                ],
                //弹框
                readFile:false,
                fileUrl:''
            }
        },
        mounted(){
            this.getPositive(1);
            this.getPositiveDepartment();
        },
        methods:{
            getPositive(currentPage){
                this.$axios.post("/evaluate/positive/record",{
                    staff:this.input_staff,
                    department:this.select_department,
                    pageNum:currentPage
                }).then(res =>{
                    this.positive_data = res.data.data;
                    this.$message.success(res.data.msg);
                });
            },
            getPositiveDepartment(){
                this.$axios.get("/evaluate/positive/department").then(res=>{
                    this.dep_options=res.data.data;
                })
            },
            getpreviewFile(url){
                this.readFile=true;
                this.fileUrl=url;
            }

        },
        name: "Positive"
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
