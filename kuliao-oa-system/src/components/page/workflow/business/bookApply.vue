<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-date"></i>图书借阅</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
        <el-row type="flex">
            <el-col :span="15" v-if="!readonly">
                <div class="container">
                    <div class="handle-box">
                        <el-input clearable v-model.trim="bookname" placeholder="请输入图书名称" class="handle-input mr10"></el-input>
                        <el-select value="图书状态" v-model="bookstate">
                            <el-option value="0" label="未借阅"></el-option>
                            <el-option value="1" label="已借阅"></el-option>
                        </el-select>
                        <el-button type="primary" icon="search" @click="search">搜索</el-button>
                    </div>
                    <el-table :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)" border
                              style="width: 100%" ref="multipleTable"   highlight-current-row
                              @current-change="selectRow">
                        <el-table-column label="序列" width="55">
                            <template slot-scope="scope">
                                {{scope.$index+1+ (currentPage-1)*pagesize}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="bookId" label="图书编号" v-if="bookIds"></el-table-column>
                        <el-table-column prop="bookName" label="图书名称" width="100"></el-table-column>
                        <el-table-column prop="author" label="作者" width="100"></el-table-column>
                        <el-table-column prop="publishingPage" label="出版版次" width="100"></el-table-column>
                        <el-table-column prop="currentState" label="状态" width="100"></el-table-column>
                        <el-table-column prop="press" label="出版社"></el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination class="pull-right clearfix"
                                       @size-change="handleSizeChange"
                                       @current-change="handleCurrentChange"
                                       :current-page="currentPage"
                                       :page-sizes="[5, 10]"
                                       :page-size="pagesize"
                                       layout="total, sizes, prev, pager, next, jumper"
                                       :total="tableData.length">
                        </el-pagination>
                    </div>
                </div>
            </el-col>
            <el-col  :span="9">
                <div class="container">
                    <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px" :disabled="readonly">
                        <el-form-item label="图书编号" prop="bookSquence">
                            <el-input v-model="ruleForm.bookSquence" disabled class="handle"></el-input>
                        </el-form-item>
                        <el-form-item label="图书名称" prop="bookName">
                            <el-input v-model="ruleForm.bookName" disabled class="handle"></el-input>
                        </el-form-item>
                        <el-form-item label="借阅人" prop="borrower">
                            <el-input v-model="ruleForm.borrower" disabled class="handle"></el-input>
                        </el-form-item>
                        <el-form-item label="借阅时间"  prop="borrowTime">
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
                        <el-form-item label="归还时间"  prop="backTime">
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
                        <el-form-item label="备注" >
                            <el-input v-model="ruleForm.bookRemark" auto-complete="off" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
        </el-row>
        </div>
    </div>
</template>
<script>
    export default {
        props: ['value', 'readonly'],
        data() {
            return {
                bookstate: '',//当前图书的借阅状态
                bookname: '',//模糊搜索框里面书名
                select: [{value: 0, lable: '未借阅'}, {value: 1, lable: '已借阅'}],
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
                //归还时间必须要晚于接的时间
                ruleForm: {
                    bookSquence: '',
                    bookName: '',
                    borrower: '',
                    borrowTime: '',
                    backTime: '',
                    bookRemark: ''
                },
                currentPage: 1,
                pagesize: 5,
                idx: -1,
                bookIds: false,
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
            search(){
                let params = new URLSearchParams();
                params.append('bookName',this.bookname);
                params.append('bookState',this.bookstate);
                params.append('borrower','');
                this.$axios.post("/bookInfo/selectBy", params,{
                    headers:{
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).then(res => {
                    this.$message.success(res.data.msg);
                    this.tableData = res.data.data;
                    this.handler();
                });
            },
            handler(){
                this.tableData.forEach((item) => {
                    if (item.bookState === 0) {
                        this.$set(item, 'currentState', '未借阅');
                    } else if (item.bookState === 1) {
                        this.$set(item, 'currentState', '已借阅');
                    }
                })
            },
            getAllBook() {
                let that = this;
                that.$axios.post('/bookInfo/selectAll').then((res) => {
                    that.tableData = res.data.data;
                    this.handler();
                });
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let params = new URLSearchParams();
                        params.append('bookName', this.form.bookName);
                        params.append('bookSquence', this.form.bookSquence);
                        params.append('borrower', this.form.borrower);
                        params.append('borrowTime', this.form.borrowTime);
                        params.append('backTime', this.form.backTime);
                        params.append('borrowRemark', this.form.borrowRemark);
                        params.append('bookId', this.form.bookId);

                        this.$axios.post("/borrowInfo/insertBookBorrow", params, {
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
            },
            selectRow(val){
            console.log('选中单行'+val.bookName);
            this.ruleForm.bookName = val.bookName;
            this.ruleForm.bookSquence = val.bookSquence;
            this.ruleForm.borrower = localStorage.getItem('ms_username');
            }
        }
    }
</script>

<style scoped>
    .handle-input {
        width: 220px;
        display: inline-block;
        margin-bottom: 20px;
    }

    .handle{
        width: 220px;
    }

</style>
