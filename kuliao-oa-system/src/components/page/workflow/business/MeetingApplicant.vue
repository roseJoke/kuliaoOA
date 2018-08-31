<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 会议室申请</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row type="flex" justify="center">
            <!--table-->
            <el-col v-if="!readonly" :span="14">
                <div class="container">
                    <div class="handle-box">
                        <el-input clearable v-model.trim="meeting_name" placeholder="请输入会议室名称" class="handle-input mr10"></el-input>
                        <el-button type="primary" icon="search" @click="search">搜索</el-button>
                    </div>

                    <el-table :data="tables.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%"
                              ref="multipleTable"
                              highlight-current-row
                              @current-change="handleSelectionChange" >
                        <el-table-column prop="meetingName" label="会议室名称" width="100"></el-table-column>
                        <el-table-column prop="meetingMessage" label="会议室信息"></el-table-column>
                        <el-table-column prop="meetingRemark" label="备注"></el-table-column>
                        <el-table-column prop="meetingState" label="状态" width="80"></el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination class="pull-right clearfix"
                                       @size-change="handleSizeChange"
                                       @current-change="handleCurrentChange"
                                       :current-page="currentPage"
                                       :page-sizes="[5]"
                                       :page-size="pagesize"
                                       layout="total, sizes, prev, pager, next, jumper"
                                       :total="tables.length">
                        </el-pagination>
                    </div>
                    <el-breadcrumb-item><i class="el-icon-info"></i>单击表格选择某条会议室信息</el-breadcrumb-item>
                </div>
            </el-col>
            <!--table-->
            <!--form-->
            <el-col :span="!readonly?10:24">
                <div class="container">
                    <el-form :disabled="readonly" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="110px">
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
                        <el-form-item label="申请事由">
                            <el-input style="width: 50%" type="textarea" v-model="ruleForm.meetingCause" rows="4"></el-input>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input style="width: 50%" type="textarea" v-model="ruleForm.meetingRemark" rows="3"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
            <!--form-->

        </el-row>
    </div>
</template>
<script>
    export default {
        props:{
            readonly: Boolean
        },

        data() {
            return {
                processId: '',
                select: [{value: 0, lable: '未使用'}, {value: 1, lable: '使用中'}],
                meeting_name: '',
                tables: [],
                ruleForm: {
                    meeting_name: '',
                    meetingCause: ''
                },
                currentPage: 1,
                pagesize: 5,
                idx: -1,
                rules: {
                    meetingTime: [
                        {required: true, message: '请输入会申请日期', trigger: 'blur'}
                    ]
                }
            }

        },
        watch: {
            // 如果 `meeting_name` 发生改变，这个函数就会运行
            meeting_name: function () {
                if (this.meeting_name.length == 0) {
                    this.search();
                }
            }
        },
        created() {
            this.search();
            this.getEmp();
        },

        methods: {
            search() {
                let that = this;
                let param = {
                    meetingState: 0, //只显示未使用的会议室
                    meetingName: that.meeting_name
                };
                if(that.readonly === true) { //审批时，不调用该接口
                    return;
                };

                that.$axios.post('/assets/meeting/selectMeetings', param).then((res) => {
                    console.log(res.data);
                    let datas = [];
                    datas = res.data.data;
                    let approval = false;
                    for (let i = 0; i < datas.length; i++) {
                        if (datas[i].meetingState == '0') {
                            datas[i].meetingState = '未使用';

                        } else if (datas[i].meetingState == '1') {
                            datas[i].meetingState = '使用中';
                        }

                    }
                    that.tables = datas;
                    that.currentPage = 1; //每次调用search方法时，初始化
                })

            },

            handleSelectionChange(row) { //对选中的数据进行判断
                let that = this;
                if (row !== undefined || row !== '') {
                    // that.ruleForm.processId = '232';  //todo 流程id
                    // that.ruleForm.approvalResult = '通过'; //todo
                    that.ruleForm.meetingTime = that.getNowTime();//申请日期
                    that.$set(that.ruleForm, 'meetingId', row.meetingId);
                    that.$set(that.ruleForm, 'meetingName', row.meetingName);
                    that.$set(that.ruleForm, 'meetingMessage', row.meetingMessage);
                    that.$set(that.ruleForm, 'state', '0'); //审批状态：0-审批中，1-审批结束
                }
            },
            getEmp() { //获取员工信息
                let that = this;
                let empId = localStorage.getItem('empNo');
                let param = {
                    empNo: empId
                };

                that.$axios.post('/base/user/query', param).then((res) => {
                    that.$set(that.ruleForm, 'applicantId', res.data.data[0].empNo);
                    that.$set(that.ruleForm, 'applicantName', res.data.data[0].username);
                    that.$set(that.ruleForm, 'departmentName', res.data.data[0].deptName);
                });
            },
            getNowTime() {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                var hours = date.getHours();
                var minutes = date.getMinutes();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                if (hours >= 0 && hours <= 9) {
                    hours = "0" + hours;
                }
                if (minutes >= 0 && minutes <= 9) {
                    minutes = "0" + minutes;
                }
                var currentdate = year + month + strDate + hours + minutes;
                return currentdate;
            },

            clearForm(formName) {//关闭并清理对话框中的表单
                this.ruleForm = [];
                this.$refs[formName].resetFields();

            },

            sendApproval(formName) { //发起审批

                let that = this;
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        // todo 会议室信息是否选中校验
                        if(that.ruleForm.meetingName === undefined || that.meetingName === ''
                            || that.ruleForm.meetingMessage === undefined || that.meetingMessage === ''
                            || that.ruleForm.meetingId === undefined || that.meetingId === '') {
                            this.$message.warning('请单击选中表格中的某行会议室信息');
                        }
                        that.$axios.post("/workflow/meetingRecord/newRecord", that.ruleForm).then(res => {

                            that.approval = false;
                            that.$message.success(res.data.msg);
                        });
                    } else {
                        this.$message.error('请输入完整信息');
                        return false;
                    }
                });

            },
            handleSizeChange: function (size) {
                this.pagesize = size;
            },
            handleCurrentChange: function (currentPage) {
                this.currentPage = currentPage;
            }
        }
    }

</script>
<style scoped>
    .handle-input {
        width: 150px;
        display: inline-block;
        margin-bottom: 20px;
    }

    .container {
        padding: 30px;
        background: #fff;
        border: 1px solid #ddd;
        border-radius: 5px;
        height: 400px;
        overflow: auto;
    }

    .disabled-style {
        -webkit-text-fill-color: #606266;
    }

</style>
