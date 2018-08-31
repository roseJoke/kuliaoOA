<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 图书信息维护</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="bookName" placeholder="请输入图书名称" class="handle-input mr10" clearable></el-input>
                <el-input v-model="borrower" placeholder="请输入借阅人" class="handle-input mr10" clearable></el-input>
                <el-select value="图书状态" v-model="bookStates" clearable>
                    <el-option value="0" label="未借出"></el-option>
                    <el-option value="1" label="已借出"></el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>

                <el-button size="small" class="right_insert" type="primary" @click="uploadVisible = true">批量导入</el-button>

                <el-button type="danger" icon="insert" class="right_insert" @click="insertBookInfo('form')">添加</el-button>
            </div>
                <el-table :data="tables.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                    <el-table-column label="序列" width="55">
                        <template slot-scope="scope">
                            {{scope.$index+1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="bookId" label="图书Id" v-if = "bookIds"></el-table-column>
                    <el-table-column prop="bookName" label="图书名称" width="130"></el-table-column>
                    <el-table-column prop="author" label="作者" width="130"></el-table-column>
                    <el-table-column prop="press" label="出版社" width="130"></el-table-column>
                    <el-table-column prop="publishingPage" label="出版版次" width="130"></el-table-column>
                    <el-table-column prop="bookPrice" label="图书价格" width="130"></el-table-column>
                    <el-table-column prop="bookSquence" label="图书编号" width="130"></el-table-column>
                    <el-table-column prop="bookState" :formatter="judge" label="图书状态" width="130"></el-table-column>
                    <el-table-column prop="borrower" label="借阅人" width="130"></el-table-column>
                    <el-table-column prop="bookInput" label="入库时间" width="130"></el-table-column>
                    <el-table-column prop="bookRemark" label="备注" width="130"></el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button size="small" type="primary" @click="handleEdit(scope.$index)">编辑</el-button>
                            <el-button size="small" type="danger" @click="deleteRow(scope.$index)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                    <div class="pagination">
                            <el-pagination class="pull-right clearfix"
                                           @size-change="handleSizeChange"
                                           @current-change="handleCurrentChange"
                                           :current-page="currentPage"
                                           :page-sizes="[10, 20, 50, 100]"
                                           :page-size="pagesize"
                                           layout="total, sizes, prev, pager, next, jumper"
                                           :total="tables.length">
                            </el-pagination>
                    </div>
        </div>

        <!--添加提示框-->
        <el-dialog title="添加图书信息" :visible.sync="createVisible" width="30%" center>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="图书名称"  prop="bookName">
                    <el-input v-model="form.bookName"></el-input>
                </el-form-item>
                <el-form-item label="图书作者" prop="author">
                    <el-input v-model="form.author"></el-input>
                </el-form-item>
                <el-form-item label="出版社" prop="press">
                    <el-input v-model="form.press"></el-input>
                </el-form-item>
                <el-form-item label="出版版次" prop="publishingPage">
                    <el-input v-model="form.publishingPage"></el-input>
                </el-form-item>
                <el-form-item label="图书价格" prop="bookPrice">
                    <el-input v-model="form.bookPrice"></el-input>
                </el-form-item>
                <el-form-item label="图书编号" prop="bookSquence">
                    <el-input v-model="form.bookSquence"></el-input>
                </el-form-item>
                <el-form-item label="图书状态" prop="bookState">
                    <el-select value="图书状态" v-model="form.bookState" style="width: 450px">
                        <el-option :value="0" label="未借出"></el-option>
                        <el-option :value="1" label="已借出"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="借阅人" prop="borrower">
                    <el-input v-model="form.borrower"></el-input>
                </el-form-item>
                <el-form-item label="入库时间" prop="bookInput">
                    <el-input v-model="form.bookInput"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="bookRemark">
                    <el-input v-model="form.bookRemark" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="createVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveInsert('form')">确 定</el-button>
            </span>
        </el-dialog>

        <!--编辑提示框-->
        <el-dialog title="编辑图书信息" :visible.sync="editVisible" width="30%" center>
            <el-form ref="formEdit" :model="formEdit" label-width="80px">
                <el-form-item label="图书Id" style="display: none">
                    <el-input v-model="formEdit.bookId" style="display: none"></el-input>
                </el-form-item>
                <el-form-item label="图书名称">
                    <el-input v-model="formEdit.bookName" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="图书作者">
                    <el-input v-model="formEdit.author" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="出版社">
                    <el-input v-model="formEdit.press" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="出版版次">
                    <el-input v-model="formEdit.publishingPage" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="图书价格">
                    <el-input v-model="formEdit.bookPrice" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="图书编号">
                    <el-input v-model="formEdit.bookSquence" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="图书状态">
                    <el-select value="图书状态" v-model="formEdit.bookState" style="width: 450px">
                        <el-option :value="0" label="未借出"></el-option>
                        <el-option :value="1" label="已借出"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="借阅人">
                    <el-input v-model="formEdit.borrower" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="入库时间">
                    <el-input v-model="formEdit.bookInput" disabled="disabled"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="formEdit.bookRemark" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog :visible.sync="uploadVisible" width="25%" title="批量导入" top="30vh">
            <el-upload
                action="/assets/bookInfo/upload"
                :show-file-list="false"
                :file-list="fileList"
                :on-success="insertAll">
                <el-button size="small" type="primary">点击选择excel文件</el-button>
            </el-upload>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        data() {
            return{
                url: '/assets/bookInfo/selectAll',
                bookName: '',
                borrower: '',
                bookStates: '',
                borrow_date: '',
                fileList: [],
                tables:[],
                cur_page: 1,
                multipleSelection: [],
                uploadVisible: false,
                editVisible: false,
                createVisible: false,
                delVisible: false,
                bookIds: false,
                form:{
                    bookState: 0,
                    bookName: '',
                    author: '',
                    press:'',
                    publishingPage: '',
                    borrower:'',
                    bookPrice: '',
                    bookSquence: '',
                    bookInput: '',
                    bookRemark: ''
                },
                formEdit:{
                    bookId: 0,
                    bookState: 0,
                    bookName: '',
                    author: '',
                    press:'',
                    publishingPage: '',
                    borrower:'',
                    bookPrice: '',
                    bookSquence: '',
                    bookInput: '',
                    bookRemark: ''
                },

                currentPage:1,
                pagesize:10,
                idx: -1,
                rules: {
                    bookName:[{ required: true, message: '请输入图书名称', trigger: 'blur' }],
                    author:[{required: true, message: '请输入作者', trigger: 'blur'}],
                    press:[{required: true, message: '请输入出版社', trigger: 'blur'}],
                    publishingPage:[{required: true, message: '请输入图书名称', trigger: 'blur'}],
                    bookPrice:[{required: true, message: '请输入图书价格', trigger: 'blur'}],
                    bookSquence:[{required: true, message: '请输入图书编号', trigger: 'blur'}],
                    bookInput:[{required: true, message: '请选择入库时间', trigger: 'change'}]
                },
            }

        },
        created() {
            this.getData();
        },

        methods: {
            //根据状态值显示借出状态
            judge(data){
                return data.bookState===1 ? '已借出' : '未借出'
            },
            getData() {
                // 开发环境使用 easy-mock 数据，正式环境使用 json 文件
                if (process.env.NODE_ENV === 'development') {
                    this.url = '/assets/bookInfo/selectAll';
                };
                this.$axios.post(this.url, {
                    page: this.cur_page
                }).then((res) => {
                    console.log(res.data);
                    this.tables = res.data.data;
                })
            },
            search() {
                let params = new URLSearchParams();
                params.append('bookName',this.bookName);
                params.append('bookState',this.bookStates);
                params.append('borrower',this.borrower);
                this.$axios.post("/assets/bookInfo/selectBy", params,{
                    headers:{
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).then(res => {
                    this.$message.success(res.data.msg);
                    console.log(res.data.data);
                    this.tables = res.data.data;
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage-1)*(this.pagesize)+index;
                const item = this.tables[this.idx];
                console.log(item);

                this.formEdit = {
                    bookId: item.bookId,
                    bookName: item.bookName,
                    author: item.author,
                    press: item.press,
                    publishingPage: item.publishingPage,
                    bookPrice: item.bookPrice,
                    bookSquence: item.bookSquence,
                    bookState:item.bookState,
                    borrower: item.borrower,
                    bookInput: item.bookInput,
                    bookRemark: item.bookRemark

                };
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit() {

                this.$set(this.tables, this.idx, this.formEdit);



                this.$axios.post("/assets/bookInfo/updateBookInfo", this.formEdit,{
                    headers:{
                        'Content-Type' : 'application/json'
                    }
                }).then(res => {
                    this.$message.success(res.data.msg);
                });
                this.editVisible = false;
                this.$message.success(`修改第 ${this.idx+1} 行成功`);
            },
            // 确定删除
            deleteRow(index) {
                this.idx = (this.currentPage - 1) * (this.pagesize) + index;
                this.$confirm('将要删除一条第' + (this.idx+1) + "条数据", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = new URLSearchParams();
                    params.append("bookId",this.tables[this.idx].bookId);
                    this.$axios.post("/assets/bookInfo/deleteBookInfo", params,{
                        headers:{
                            'Content-Type' : 'application/x-www-form-urlencoded'
                        }
                    }).then(res => {
                        this.tables.splice(this.idx, 1);
                        this.$message.success(res.data.msg);

                    });

                });
            },
            // 添加图书信息
            insertBookInfo(formName){

                this.createVisible = true;
                this.$refs[formName].resetFields();
            },

            saveInsert(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        this.$axios.post("/assets/bookInfo/insertBookInfo", this.form).then(res => {
                            this.tables.push(this.form);
                            this.$message.success(res.data.msg);

                        });
                        this.createVisible = false;
                    } else {
                        alert('请完善表单数据！');
                        return false;
                    }
                });
            },
            // 批量添加图书信息
            insertAll(response){
                this.uploadVisible=false;
                this.$confirm('已发现'+response.data.length+'条数据，是否确定上传？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {

                    response.data.forEach((item) => {
                        // let params = new URLSearchParams();
                        // params.append('bookName', item.bookName);
                        // params.append('author', item.author);
                        // params.append('press', item.press);
                        // params.append('publishingPage', item.publishingPage);
                        // params.append('bookPrice', item.bookPrice);
                        // params.append('bookSquence', item.bookSquence);
                        // params.append('bookState', item.bookState);
                        // params.append('borrower',item.borrower);
                        // params.append('bookInput', item.bookInput);
                        // params.append('bookRemark', item.bookRemark);
                        this.$axios.post("/assets/bookInfo/insertBookInfo", item).then(res => {
                            this.tables.push(item);
                            this.$message.success(res.data.msg);

                        });
                    });

                });
            },
            handleSizeChange: function (size) {
                this.pagesize = size;
            },
            handleCurrentChange: function(currentPage){
                this.currentPage = currentPage;
            },
        },
    }

</script>
<style scoped>

    .handle-input{
        width: 150px;
        display: inline-block;
        margin-bottom: 20px;
    }
    .right_insert{
        float: right;
    }
    #but{
        background-color: #C0C0C0;
    }


</style>
