<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-date"></i>图书借阅</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">

            <el-table :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%" ref="multipleTable">
                <el-table-column label="序列" width="55">
                    <template slot-scope="scope">
                        {{scope.$index+1+ (currentPage-1)*pagesize}}
                    </template>
                </el-table-column>
                <el-table-column prop="bookId" label="图书编号" v-if="bookIds"></el-table-column>
                <el-table-column prop="bookName" label="图书名称"></el-table-column>
                <el-table-column prop="author" label="作者"></el-table-column>
                <el-table-column prop="press" label="出版社"></el-table-column>
                <el-table-column fixed="right" label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button size="small" @click="openUI(scope.row)">发起申请</el-button>
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
                               :total="tableData.length">
                </el-pagination>
            </div>


        </div>
        <el-dialog title="借阅图书" :visible.sync="dialogFormVisible" width="600px">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item label="借阅时间" :label-width="formLabelWidth" prop="borrowTime">
                    <el-date-picker
                        v-model="ruleForm.borrowTime"
                        value-format="yyyyMMdd"
                        format="yyyy年MM月dd日"
                        type="date"
                        :editable="false"
                        :picker-options="pickerOptions0"
                        placeholder="选择借书日期"
                        class="date-width">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="归还时间" :label-width="formLabelWidth" prop="back">
                    <el-date-picker
                        v-model="ruleForm.backTime"
                        value-format="yyyyMMdd"
                        format="yyyy年MM月dd日"
                        type="date"
                        :editable="false"
                        :picker-options="pickerOptions1"
                        placeholder="选择还书日期"
                        class="date-width">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="备注" :label-width="formLabelWidth">
                    <el-input v-model="ruleForm.borrowRemark" auto-complete="off" style="width: 220px"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
                <el-button @click="dialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: "book-apply",
        data() {
            return {
                //归还时间必须要晚于接的时间
                ruleForm: {},
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() > Date.now() - 8.64e7;
                    }
                },
                pickerOptions1: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                dialogFormVisible: false,
                tableData: [],
                ruleForm: {
                    borrowtime: '',
                    back: ''
                },

                currentPage: 1,
                pagesize: 10,
                idx: -1,
                bookIds: false,

                formLabelWidth: '120px',
                rules: {
                    borrowTime: [
                        {required: true, message: '请选择借书日期', trigger: 'change'}
                    ],
                    backTime: [
                        {
                            required: true, message: '请选择还书日期', trigger: 'change'
                        }
                    ]
                }
            }
        },
        created() {
            this.getAllBook();
        },
        methods: {

            getAllBook() {
                let that = this;
                that.$axios.post('/bookInfo/selectAll').then((res) => {

                    that.tableData = res.data.data;

                });

            },

            openUI(row) {
                let that = this;
                that.dialogFormVisible = true;
                that.ruleForm.bookId = row.bookId;
                that.ruleForm.bookName = row.bookName;
                that.ruleForm.bookSquence = row.bookSquence;
                let userName = localStorage.getItem('ms_username')
                that.ruleForm.borrower = userName;

            },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        let params = new URLSearchParams();
                        params.append('bookName', this.ruleForm.bookName);
                        params.append('bookSquence', this.ruleForm.bookSquence);
                        params.append('borrower', this.ruleForm.borrower);
                        params.append('borrowTime', this.ruleForm.borrowTime);
                        params.append('backTime', this.ruleForm.backTime);
                        params.append('borrowRemark', this.ruleForm.borrowRemark);
                        params.append('bookId', this.ruleForm.bookId);

                        this.$axios.post("/borrowInfo/insertTemp", params, {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).then(res => {
                            this.$message.success(res.data.msg);
                        });
                        this.dialogFormVisible = false;
                    } else {
                        this.$message.error('请填写完整信息');
                        return false;
                    }
                });
            },
            getNowFormatDate() {
                var date = new Date();
                var seperator1 = "-";
                var seperator2 = ":";
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                    + " " + date.getHours() + seperator2 + date.getMinutes()
                    + seperator2 + date.getSeconds();
                return currentdate;
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

</style>
