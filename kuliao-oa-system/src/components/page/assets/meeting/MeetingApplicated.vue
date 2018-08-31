<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 会议室申请记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">

                <el-input clearable v-model.trim="meeting_name" placeholder="请输入会议室名称" class="handle-input mr10"></el-input>
                <el-input clearable v-model.trim="meeting_applicate" placeholder="请输入申请人" class="handle-input mr10"></el-input>
                <el-select clearable value="开发部" v-model="department">
                    <el-option  v-for="item in options" :key="item.value" :label="item.value" :value="item.value"></el-option>
                </el-select>
                <el-input v-model.trim="meeting_time" type="date" placeholder="申请时间" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
            </div>
            <el-table :data="tables.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%" ref="multipleTable" >
                <el-table-column label="序列" width="55">
                    <template slot-scope="scope">
                        {{scope.$index+1+ (currentPage-1)*pagesize}}
                    </template>
                </el-table-column>
                <el-table-column prop="meetingName" label="会议室名称" width="250"></el-table-column>
                <el-table-column prop="applicantName" label="申请人" width="250"></el-table-column>
                <el-table-column prop="departmentName" label="申请部门" width="250"></el-table-column>
                <el-table-column prop="meetingTime" label="申请时间" width="250"></el-table-column>
                <el-table-column prop="meetingCause" label="申请事由" width="250"></el-table-column>
                <el-table-column prop="meetingRemark" label="备注" ></el-table-column>
                <el-table-column prop="approvalResult" label="审批结果" ></el-table-column>
            </el-table>

            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[5, 10, 20, 50]"
                               :page-size="pagesize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="tables.length">
                </el-pagination>
            </div>

        </div>
    </div>
</template>
<script>

    export default {

        data() {
            return{
                meeting_name: '',
                meeting_applicate: '',
                meeting_time: '',
                department: '',
                tables:[],
                multipleSelection: [],
                currentPage:1,
                pagesize:10,
                idx: -1,
                options:[{
                    value:'研发部'
                },{
                    value:'测试部'
                }]
            }

        },
        watch: {
            // 如果 `meeting_name` 发生改变，这个函数就会运行
            meeting_name: function () {
                if(this.meeting_name.length == 0 && this.meeting_time.length == 0 && this.meeting_applicate.length == 0 && this.department.length == 0) {
                    this.search();
                }
            },
            meeting_applicate: function () {
                if(this.meeting_name.length == 0 && this.meeting_time.length == 0 && this.meeting_applicate.length == 0 && this.department.length == 0) {
                    this.search();
                }
            },
            department: function () {
                if(this.meeting_name.length == 0 && this.meeting_time.length == 0 && this.meeting_applicate.length == 0 && this.department.length == 0) {
                    this.search();
                }
            },
            meeting_time: function () {
                if(this.meeting_name.length == 0 && this.meeting_time.length == 0 && this.meeting_applicate.length == 0 && this.department.length == 0) {
                    this.search();
                }
            }
        },
        created() {
            this.search();
        },

        methods: {

            //查询会议室申请记录
            search() {
                let that = this;
                console.log(that.meeting_name);
                let param = {
                    meetingName: that.meeting_name,
                    applicantName: that.meeting_applicate,
                    meetingTime: that.meeting_time,
                    departmentName: that.department

                };

                that.$axios.post('/assets/meetingRecord/selectRecords',param).then((res) => {
                    that.tables = res.data.data;
                    for (let i = 0; i < that.tables.length; i++) {
                        let param = that.tables[i].meetingTime;
                        let year = param.substring(0, 4);
                        let month = param.substring(4, 6);
                        let day = param.substring(6, 8);
                        let hours = param.substring(8,10);
                        let minutes = param.substring(10,12);
                        that.tables[i].meetingTime = year+'年'+month+'月'+day+'日'+hours+'点'+minutes+'分';
                    }
                    that.currentPage = 1;
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            //分页
            handleSizeChange: function (size) {
                this.pagesize = size;
            },
            handleCurrentChange: function(currentPage){
                this.currentPage = currentPage;
            }
        }
    }

</script>
<style scoped>
    .handle-input{
        width: 150px;
        display: inline-block;
        margin-bottom: 20px;
    }
    .disabled-style {
        -webkit-text-fill-color:#606266;
    }

</style>
