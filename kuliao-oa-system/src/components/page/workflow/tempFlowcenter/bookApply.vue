<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-date"></i>图书借阅</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-table
                :data="tableData"
                border
                style="width: 100%">
                <el-table-column
                    prop="bookId"
                    label="图书编号"
                    width="480">
                </el-table-column>
                <el-table-column
                    prop="bookName"
                    label="图书名称"
                    width="480">
                </el-table-column>
                <el-table-column label="借书" width="480">
                    <template slot-scope="scope">
                        <el-button size="small" @click="dialogFormVisible = true">发起申请</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog title="借阅图书" :visible.sync="dialogFormVisible" width="600px">
            <el-form :model="form" :rules="rules" ref="form">
                <el-form-item label="借阅时间" :label-width="formLabelWidth" prop="borrowtime">
                    <el-date-picker
                        v-model="form.borrowtime"
                        value-format="yyyyMMdd"
                        format="yyyy年MM月dd日"
                        type="date"
                        :editable="false"
                        :picker-options="pickerOptions0"
                        placeholder="选择借书日期"
                        class="date-width" >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="归还时间" :label-width="formLabelWidth" prop="back">
                    <el-date-picker
                        v-model="form.back"
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
                    <el-input v-model="form.remark" auto-complete="off" style="width: 220px"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('form')">确 定</el-button>
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
                form:{
                    borrowtime:'',
                },
                pickerOptions0:{
                    disabledDate(time) {
                        return time.getTime()> Date.now() - 8.64e7 ;
                    }
                },
                pickerOptions1: {
                    disabledDate(time) {
                        return time.getTime()< Date.now() - 8.64e7;
                    }
                },
                dialogFormVisible:false,
                tableData: [{
                    bookId: '001',
                    bookName: '一千零一夜',
                    address: '上海市普陀区金沙江路 1518 弄'
                }, {
                    bookId: '002',
                    bookName: '一千零一夜',
                    address: '上海市普陀区金沙江路 1517 弄'
                }, {
                    bookId: '003',
                    bookName: '一千零一夜',
                    address: '上海市普陀区金沙江路 1519 弄'
                }, {
                    bookId: '004',
                    bookName: '一千零一夜',
                    address: '上海市普陀区金沙江路 1516 弄'
                }],
                form:{
                    borrowtime:'',
                    back:''
                },
                formLabelWidth: '120px',
                rules:{
                    borrowtime:[
                        { type: 'date', required: true, message: '请选择借书日期', trigger: 'change' }
                    ],
                    back:[
                        {
                            type: 'date', required: true, message: '请选择还书日期', trigger: 'change'
                        }
                    ]
                }
            }
        },
        methods:{
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        that.dialogFormVisible = false;
                        alert('submit!');
                    } else {
                        console.log('error submit!!');
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
            }
        }
    }
</script>

<style scoped>

</style>
