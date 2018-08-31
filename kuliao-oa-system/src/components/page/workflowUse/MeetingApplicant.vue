<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 会议室申请</el-breadcrumb-item>
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
                <el-table-column  label="序列" width="55">
                    <template slot-scope="scope">
                        {{scope.$index+1+ (currentPage-1)*pagesize}}
                    </template>
                </el-table-column>
                <el-table-column prop="meetingName" label="会议室名称" width="350"></el-table-column>
                <el-table-column prop="meetingMessage" label="会议室信息"></el-table-column>
                <el-table-column prop="meetingState" label="会议室状态" width="350"></el-table-column>
                <el-table-column prop="meetingRemark" label="备注"></el-table-column>
                <el-table-column fixed="right" label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" :disabled="scope.row.meetingState == '使用中'" plain slot="reference" @click="openApprovalUI(scope.row)">发起申请</el-button>
                    </template>
                </el-table-column>
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

        <!--发起审批对话框-->
        <el-dialog title="发起申请" :visible.sync="approval" width="30%" center>
            <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
                <el-form-item label="会议室Id" prop="meetingId" style="display: none">
                    <el-input v-model="ruleForm.meetingId" style="display: none"></el-input>
                </el-form-item>
                <el-form-item label="用户id" prop="applicantId" style="display: none">
                    <el-input v-model="ruleForm.applicantId" style="display: none"></el-input>
                </el-form-item>
                <el-form-item label="用户名称" prop="applicantName" style="display: none">
                    <el-input v-model="ruleForm.applicantName" style="display: none"></el-input>
                </el-form-item>

                <el-form-item label="部门id" prop="departmentId" style="display: none">
                    <el-input v-model="ruleForm.departmentId" style="display: none"></el-input>
                </el-form-item>
                <el-form-item label="部门名称" prop="departmentName" style="display: none">
                    <el-input v-model="ruleForm.departmentName" style="display: none"></el-input>
                </el-form-item>

                <el-form-item label="会议室名称" prop="meetingName">
                    <el-input class="disabled-style handle-input" v-model="ruleForm.meetingName" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="会议室信息">
                    <el-input class="disabled-style handle-input" v-model="ruleForm.meetingMessage" :disabled="true"></el-input>
                </el-form-item>

                <el-form-item label="申请日期" prop="meetingTime">
                    <el-date-picker
                        v-model="ruleForm.meetingTime"
                        value-format="yyyyMMdd"
                        format="yyyy年MM月dd日"
                        type="date"
                        :editable="false"
                        placeholder="选择日期"
                        class="date-width">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="申请事由">
                    <el-input type="textarea" v-model="ruleForm.meetingCause"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="ruleForm.meetingRemark"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="clearForm('ruleForm')">取 消</el-button>
                <el-button type="primary" @click="sendApproval('ruleForm')">发 起</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<script>
    export default {

        data() {
            return{
                processId: '',
                select: [{value: 0, lable: '未使用'}, {value: 1, lable: '使用中'}],
                meeting_name: '',
                meeting_state: '',
                tables:[],
                number: '',
                multipleSelection: [],
                approval: false,
                editVisible: false,
                delVisible: false,
                ruleForm:{},
                currentPage:1,
                pagesize:10,
                idx: -1,
                rules: {
                    meetingTime:[
                        { required: true, message: '请输入会申请日期', trigger: 'blur' }
                    ]
                }
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

                that.$axios.post('/workflow/meeting/selectMeetings',param).then((res) => {
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

            openApprovalUI(row) { //打开发起审批对话框
                let that = this;
                let empId = localStorage.getItem('empNo');
                let param = {
                    empNo: empId
                };

                that.$axios.post('/base/user/query',param).then((res) => {

                    that.ruleForm.applicantId = res.data.data[0].empNo;
                    that.ruleForm.applicantName = res.data.data[0].username;
                    that.ruleForm.departmentName = res.data.data[0].deptName;

                });

                that.ruleForm.meetingId = row.meetingId;

                that.ruleForm.processId = '232';  //todo 流程id
                // that.ruleForm.approvalResult = '通过'; //todo
                that.ruleForm.meetingName = row.meetingName;
                that.ruleForm.meetingMessage = row.meetingMessage;
                that.approval = true;

            },

            clearForm(formName) {//关闭并清理对话框中的表单
                this.approval = false;
                this.ruleForm = [];
                this.$refs[formName].resetFields();

            },

            sendApproval(formName) { //发起审批

                let that = this;
                console.log('发送的数据33'+ that.ruleForm);

                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        that.$axios.post("/workflow/meetingRecord/newRecord", that.ruleForm).then(res => {

                            that.approval = false;
                            that.$message.success(res.data.msg);
                        });
                        that.editVisibles = false;
                    }else{
                        this.$message.error('请输入完整信息');
                        return false;
                    }
                });

            },

            handleSelectionChange(val) {
                console.log("选择")
                console.log(val);
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
