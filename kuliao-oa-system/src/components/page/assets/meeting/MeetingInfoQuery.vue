<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 会议室信息查看</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">

                <el-input clearable v-model.trim="meeting_name" placeholder="请输入会议室名称" class="handle-input mr10"></el-input>
                <el-select clearable value="会议室状态" v-model="meeting_state">
                    <el-option
                        v-for="item in select"
                        :label="item.lable"
                        :value="item.value"
                        :key="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
            </div>
            <el-table :data="tables.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column label="序列" width="55">
                    <template slot-scope="scope">
                        {{scope.$index+1+ (currentPage-1)*pagesize}}
                    </template>
                </el-table-column>
                <el-table-column prop="meetingName" label="会议室名称" width="350"></el-table-column>
                <el-table-column prop="meetingMessage" label="会议室信息"></el-table-column>
                <el-table-column prop="meetingState" label="会议室状态" width="350"></el-table-column>
                <el-table-column prop="meetingRemark" label="备注"></el-table-column>
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
                select: [{value: 0, lable: '未使用'}, {value: 1, lable: '使用中'}],
                meeting_name: '',
                meeting_state: '',
                tables:[],
                number: '',
                multipleSelection: [],
                editVisible: false,
                form:{},
                currentPage:1,
                pagesize:10,
                idx: -1
            }

        },
        watch: {
            // 如果 `meeting_name` 发生改变，这个函数就会运行
            meeting_name: function () {
                if(this.meeting_state.length == 0 && this.meeting_name.length == 0) {
                    this.search();
                }
            },
            meeting_state: function () {
                if(this.meeting_state.length == 0 && this.meeting_name.length == 0) {
                    this.search();
                }
            }
        },
        created() {
            this.search();
        },

        computed:{
            isdisabledFn(){
                console.log('是否为进入'+ this.approval);
                return this.approval;
            }
        },

        methods: {
            search() {
                let that = this;
                let param = {
                    meetingState: that.meeting_state,
                    meetingName: that.meeting_name
                };

                that.$axios.post('/assets/meeting/selectMeetings',param).then((res) => {
                    console.log(res.data);
                    let datas = [];
                    datas =res.data.data;
                    let approval = false;
                    for (let i = 0; i < datas.length; i++) {
                        if(datas[i].meetingState == '0') {
                            datas[i].meetingState = '未使用';

                        } else if(datas[i].meetingState == '1') {
                            datas[i].meetingState = '使用中';
                        }

                    }
                    that.tables = datas;
                    that.currentPage = 1; //每次调用search方法时，初始化
                })

            },

            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleEdit(index) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];

                this.form = {
                    meetingName: item.meetingName,
                    meetingMessage: item.meetingMessage,
                    meetingState: item.meetingState,
                    meetingRemark: item.meetingRemark
                }
                this.editVisible = true;
            },
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
    .handle-del{
        margin-bottom: 20px;
    }
    .handle-input{
        width: 150px;
        display: inline-block;
        margin-bottom: 20px;
    }
    .right_insert{
        float: right;
    }

    .disabled-style {
        -webkit-text-fill-color:#606266;
    }

</style>
