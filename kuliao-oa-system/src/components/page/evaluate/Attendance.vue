<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>考勤统计</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_staff" placeholder="员工编号/姓名" onkeyup="this.value=this.value.replace(/\s+/g,'')"
                          class="handle-input mr10" clearable maxlength="6"></el-input>
                <el-date-picker v-model="statistics_month" :picker-options="pickerOptions1" type="month"
                                placeholder="选择考勤月" class="handle-date-picker" value-format="yyyyMM" :clearable="false"
                                format="yyyy年MM月">
                </el-date-picker>
                <el-button type="primary" icon="el-icon-search" @click="getAllAttendanceStatistics(1)">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="alterRecordMethod(1)">异常考勤变更记录查询</el-button>
                <el-button type="primary" icon="el-icon-search" @click="dueDayAllMethod">应出勤天数编辑</el-button>
            </div>
            <el-table :data="attendance_data.list" border stripe style="width: 100%">
                <el-table-column prop="staffId" label="员工编号" align="center">
                </el-table-column>
                <el-table-column prop="staffName" label="员工姓名" align="center">
                </el-table-column>
                <el-table-column prop="month" label="考勤月" align="center">
                </el-table-column>
                <el-table-column prop="dueDay" label="应出勤天数" align="center">
                    <template slot-scope="scope">
                        <el-button type="text"
                                   @click="dueDayOneMethod(scope.row.staffId,scope.row.month,scope.row.dueDay)">
                            {{scope.row.dueDay}}
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column prop="manHourOfAskForLeaveAndAbsenteeism" label="请假/缺勤情况" align="center">
                </el-table-column>
                <el-table-column prop="manHourOfTardyAndEarly" label="迟到/早退情况" align="center">
                </el-table-column>
                <el-table-column prop="manHourSchuqin" label="实际出勤情况" align="center">
                </el-table-column>
                <el-table-column label="异常出勤详细" align="center" width="100px">
                    <template slot-scope="abc">
                        <el-popover placement="left" width="500" trigger="click">
                            <el-button type="primary" icon="el-icon-search"
                                       @click="errAbsenceAlterMethod(abc.row.staffId,abc.row.staffName,'旷工',getNowFormatDate(),'',false,'','')">
                                增加异常出勤
                            </el-button>
                            <el-table :data="errAbsenceData.list">
                                <el-table-column width="120" prop="errType" label="异常出勤类型"></el-table-column>
                                <el-table-column width="120" prop="errDate" label="异常出勤时间"></el-table-column>
                                <el-table-column width="100" prop="errManHour" label="异常出勤工时"></el-table-column>
                                <el-table-column label="编辑" width="150px" align="center">
                                    <template slot-scope="scope">
                                        <el-button type="text"
                                                   @click="errAbsenceAlterMethod(abc.row.staffId,abc.row.staffName,scope.row.errType,scope.row.errDate,scope.row.errManHour,true,scope.row.errType,scope.row.errManHour)">
                                            编辑
                                        </el-button>
                                    </template>

                                </el-table-column>
                            </el-table>
                            <div class="pagination">
                                <el-pagination layout="prev, pager, next" :total="errAbsenceData.totalRecord"
                                               @current-change="getAttendanceErrMethod(abc.row.staffId,abc.row.month)">
                                </el-pagination>
                            </div>
                            <el-button slot="reference" type="text"
                                       @click="getAttendanceErrMethod(abc.row.staffId,abc.row.month,1)">查看
                            </el-button>
                        </el-popover>
                    </template>

                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="attendance_data.totalRecord"
                               @current-change="getAllAttendanceStatistics">
                    <!--<el-pagination layout="prev, pager, next" :total="attendance_data.totalRecord">-->
                </el-pagination>
            </div>
        </div>

        <!--异常考勤变更查询-->
        <el-dialog title="异常考勤变更记录" :visible.sync="attendanceAlterRecord">
            <div class="handle-box">
                <el-input v-model="alterStaff" placeholder="员工编号/姓名" maxlength="6" class="handle-input mr10" clearable onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                <el-date-picker v-model="alterMonth" type="month" placeholder="选择考勤月" class="handle-date-picker"
                                value-format="yyyyMM" :clearable="false" format="yyyy年MM月"></el-date-picker>
                <el-button type="primary" icon="el-icon-search" @click="alterRecordMethod(1)">搜索</el-button>
            </div>
            <el-table :data="attendanceAlterData.list" border stripe style="width: 100%">
                <el-table-column property="staffId" label="员工编号" align="center" width="80px"></el-table-column>
                <el-table-column property="staffName" label="员工姓名" align="center" width="100px"></el-table-column>
                <el-table-column label="异常出勤类型" align="center" width="120px">
                    <el-table-column property="beforeAttendanceType" label="变更前" align="center"></el-table-column>
                    <el-table-column property="afterAttendanceType" label="变更后" align="center"></el-table-column>
                </el-table-column>
                <el-table-column property="attendanceDays" label="异常出勤时间" align="center"
                                 width="120px"></el-table-column>
                <el-table-column label="异常出勤工时" align="center" width="120px">
                    <el-table-column property="beforeAttendanceManHour" label="变更前" align="center"></el-table-column>
                    <el-table-column property="afterAttendanceManHour" label="变更后" align="center"></el-table-column>
                </el-table-column>
                <el-table-column label="修改原因" align="center">
                    <template slot-scope="scope">
                        <el-popover placement="top-start" width="200" trigger="hover" :content="scope.row.reason">
                            <el-button slot="reference" type="text">详细</el-button>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column property="attendanceMonth" label="考勤月份" align="center" width="120px"></el-table-column>
                <el-table-column property="mender" label="修改人姓名" align="center"></el-table-column>
                <el-table-column property="changeTime" label="修改时间" align="center" width="120px"></el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="attendanceAlterData.totalRecord"
                               @current-change="alterRecordMethod">
                </el-pagination>
            </div>

        </el-dialog>
        <!-- 应出勤天数编辑弹出框 -->
        <el-dialog title="应出勤天数" :visible.sync="dueDaysAlter" width="300px">
            <el-form>
                <el-form-item label="员工编号:" label-width="100px" v-if="dueDayStaffIdBool">
                    <el-input v-model="dueDaysStaffId" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="考勤月份:" label-width="100px">
                    <el-date-picker v-model="dueDaysAttendanceMonth" type="month" class="handle-date-picker"
                                    value-format="yyyyMM" format="yyyy年MM月"></el-date-picker>
                </el-form-item>
                <el-form-item label="应出勤天数:" label-width="100px">
                    <el-input-number v-model="dueDaysDueDays" controls-position="right" :min="0" :max="31"
                                     class="handle-date-picker"></el-input-number>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dueDaysAlter = false">取 消</el-button>
                <el-button type="primary" @click="dueDayAlterMethod">确 定</el-button>
            </div>
        </el-dialog>
        <!--异常出勤编辑弹出框-->
        <el-dialog title="编辑异常出勤" :visible.sync="errAbsenceAlter" width="400px">
            <el-form :rules="errRules" ref="errForm" :model="errForm" class="demo-ruleForm">
                <el-form-item label="员工编号:" label-width="110px">
                    <el-input v-model="errStaffId" property="errStaffId" :disabled="true" width="270px"></el-input>
                </el-form-item>
                <el-form-item label="员工姓名:" label-width="110px">
                    <el-input v-model="errStaffName" property="errStaffName" :disabled="true" width="270px"></el-input>
                </el-form-item>
                <el-form-item label="异常出勤时间:" label-width="110px" prop="errAlterDateTime">
                    <el-date-picker v-model="errAlterDateTime" property="errAlterDateTime" :disabled="input_errDateTime"
                                    width="270px" value-format="yyyyMMdd" :picker-options="pickerOptions0"
                                    :clearable="false" format="yyyy年MM月dd日">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="异常出勤类型:" label-width="110px">
                    <el-select v-model="errAlterType" property="errAlterType" width="300px">
                        <el-option v-for="item in attendanceType_options" :key="item" :label="item"
                                   :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="异常出勤工时:" label-width="110px">
                    <el-input-number v-model="errAlterManHour" :precision="0" :step="1" :max="8" :min="0" width="270px">
                    </el-input-number>
                </el-form-item>
                <el-form-item label="修改原因:" label-width="110px" prop="errReason">
                    <el-input v-model="errForm.errReason" type="textarea" :autosize="{ minRows: 2, maxRows: 5}"
                              width="270px" maxlength="100"
                              onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <!--<div slot="footer" class="dialog-footer">-->
                    <el-button @click="errAbsenceAlter = false">取 消</el-button>
                    <el-button type="primary" @click="judgeMethod('errForm')">确 定</el-button>
                    <!--</div>-->
                </el-form-item>
            </el-form>
        </el-dialog>


    </div>
</template>

<script>
    export default {
        data() {
            var checkErrReason = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('修改原因不能为空'));
                }else {
                    callback();
                }
            }
            return {
                errForm: {
                    errReason: '',
                },
                errRules: {
                    errReason: [
                        {validator: checkErrReason, trigger: 'blur'}
                    ],
                },

                attendanceType_options: [],
                //设置日期选择范围，只能选今天之前
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() > Date.now() - 8.64e6
                    }
                },
                pickerOptions1: {
                    disabledDate(time) {
                        return time.getTime() > Date.now() - 2.592e9
                    }
                },
                //考勤统计月份
                statistics_month: this.getBeforeMonth(),
                //输入框
                input_staff: '',
                alterStaff: '',//变更记录查询员工编号或姓名
                alterMonth: this.getBeforeMonth(),//变更记录考勤月
                //弹出框
                dueDaysAlter: false,
                errAbsenceAlter: false,
                attendanceAlterRecord: false,
                //表格数据
                attendance_data: [],
                errAbsenceData: [{
                    list: [{
                        errType: '',
                        errDate: '',
                        errManHour: ''
                    }],
                }
                ],
                attendanceAlterData: [],
                //异常出勤编辑
                errStaffId: '',
                errStaffName: '',
                errAlterType: '',
                errAlterDateTime: '',
                errAlterManHour: '',
                errReason: '',
                beforeType: '',
                beforeManHour: '',
                //应出勤天数编辑
                dueDaysAttendanceMonth: '',
                dueDaysStaffId: 'all',//后台默认为all
                dueDaysDueDays: '',
                dueDayStaffIdBool: false,
                //异常出勤时间输入框
                input_errDateTime: '',
                //提交确认弹出框
                innerVisible: false,

                // errRules:{
                //     errAlterDateTime:[{ type: 'date', required: true, message: '请选择日期', trigger: 'change' }]
                // }

            }
        },
        mounted() {
            //this.getNowDate();
            //this.getBeforeMonth();
            this.getAllAttendanceStatistics(1);
            this.getAllType();
        },
        methods: {


            //异常出勤记录修改弹框
            errAbsenceAlterMethod(id, name, type, time, manOur, bool, beforeType, beforeManHour) {
                this.errStaffId = id;
                this.errStaffName = name;
                this.errAlterType = type;
                this.errAlterDateTime = time;
                this.errAlterManHour = manOur;
                this.errAbsenceAlter = true;
                this.input_errDateTime = bool;
                this.beforeType = beforeType;
                this.beforeManHour = beforeManHour;
                console.log(this.errAlterDateTime);
            },
            //单个员工应出勤天数修改弹框
            dueDayOneMethod(id, month, dueDays) {
                this.dueDaysStaffId = id;
                this.dueDaysAttendanceMonth = month;
                this.dueDaysDueDays = dueDays;
                this.dueDayStaffIdBool = true;
                this.dueDaysAlter = true;
            },
            //考勤月应出勤天数修改弹框
            dueDayAllMethod() {
                this.dueDaysStaffId = '';
                this.dueDaysAttendanceMonth = '';
                this.dueDaysDueDays = '';
                this.dueDayStaffIdBool = false;
                this.dueDaysAlter = true;
            },
            //所有员工考勤月内出勤统计查询
            getAllAttendanceStatistics(currentPage) {
                var staff = this.input_staff;
                var month = this.statistics_month;
                if ((staff == null || staff == "") && (month == null || month == "")) {
                    alert("员工编号/姓名与考勤月不能同时为空！")
                } else {
                    this.$axios.post("/evaluate/attendance/statistic", {
                        staff: this.input_staff,
                        month: this.statistics_month,
                        pageNum: currentPage
                    }).then(res => {
                        this.attendance_data = res.data.data;
                        this.$message.success(res.data.msg);
                    });
                }
            },
            //异常出勤信息变更记录
            alterRecordMethod(currentPage) {
                this.attendanceAlterRecord = true,
                    this.$axios.post("/evaluate/attendance/alterRecord", {
                        staff: this.alterStaff,
                        month: this.alterMonth,
                        pageNum: currentPage
                    }).then(res => {
                        this.attendanceAlterData = res.data.data;
                        this.$message.success(res.data.msg);
                    });
            },
            //单个员工考勤月内异常出勤信息查询
            getAttendanceErrMethod(staffId, month, currentPage) {
                this.$axios.post("/evaluate/attendance/errInfo", {
                    staffId: staffId,
                    month: month,
                    pageNum: currentPage
                }).then(res => {
                    this.errAbsenceData = res.data.data;
                    this.$message.success(res.data.msg);
                });
            },
            //判断是执行增加还是执行修改异常出勤信息
            judgeMethod(formName) {
                console.log( this.$refs[formName]);
                this.$refs[formName].validate((valid) => {
                    console.log(valid);
                    if (valid) {
                        this.errAbsenceAlter = false;
                        if ((this.beforeType == null || this.beforeType == "") && (this.beforeManHour == null || this.beforeType == "")) {
                            this.insertAttendanceMethod();
                        } else {
                            this.alterAttendanceMethod(this.beforeType, this.beforeManHour);
                        }
                    }else {
                        alert("修改原因不能为空！");
                        return false;
                    }
                });
            },
            //增加异常出勤信息和异常出勤信息记录修改信息
            insertAttendanceMethod() {
                var bool;
                this.$axios.post("/evaluate/attendance/insert", {
                    staffId: this.errStaffId,
                    staffName: this.errStaffName,
                    type: this.errAlterType,
                    date: this.errAlterDateTime,
                    manHour: this.errAlterManHour,
                    reason: this.errForm.errReason,
                    mender: localStorage.getItem('ms_username'),
                    changeTime: this.getNowDate(),
                }).then(res => {
                    this.$message.success(res.data.msg);

                })
            },
            //修改异常出勤信息和增加异常出勤信息记录修改信息
            alterAttendanceMethod(beforeType, beforeManHour) {
                this.$axios.post("/evaluate/attendance/update", {
                    staffId: this.errStaffId,
                    staffName: this.errStaffName,
                    date: this.errAlterDateTime,
                    beforeType: beforeType,
                    type: this.errAlterType,
                    beforeManHour: beforeManHour,
                    manHour: this.errAlterManHour,
                    reason: this.errForm.errReason,
                    mender: localStorage.getItem('ms_username'),
                    changeTime: this.getNowDate(),
                }).then(res => {
                    this.$message.success(res.data.msg);
                })
            },
            //应出勤天数添加和修改
            dueDayAlterMethod() {
                this.dueDaysAlter = false;
                this.$axios.post("/evaluate/attendance/dueDay", {
                    attendanceMonth: this.dueDaysAttendanceMonth,
                    dueDays: this.dueDaysDueDays,
                    staffId: this.dueDaysStaffId,
                }).then(res => {
                    this.$message.success(res.data.msg);
                    this.getAllAttendanceStatistics(1);

                })
            },
            //获取所有出勤类型
            getAllType() {
                this.$axios.get("/evaluate/attendance/type").then(res => {
                    this.attendanceType_options = res.data.data;
                })
            },

            //获取当前日期
            getNowDate() {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var day = date.getDate();
                if (month < 10) {
                    month = "0" + month;
                }
                if (day < 10) {
                    day = "0" + day;
                }
                return year + month + day;
            },
            //获取yyyy-MM-dd格式的当前日期
            getNowFormatDate() {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var day = date.getDate();
                if (month < 10) {
                    month = "0" + month;
                }
                if (day < 10) {
                    day = "0" + day;
                }
                return year + month + day;
            },
            //获取上一个月月份
            getBeforeMonth() {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth();
                if (month < 10) {
                    month = "0" + month;
                }
                return year + month;
            }
        },
        name: "Attendance"
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
        width: 140px;
        display: inline-block;
    }

    .handle-date-picker {
        width: 150px;
    }
</style>
