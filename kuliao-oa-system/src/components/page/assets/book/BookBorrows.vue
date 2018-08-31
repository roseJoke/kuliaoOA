<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 图书借阅记录查看 </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="book_name" placeholder="请输入图书名称" class="handle-input mr10" clearable></el-input>
                <el-input v-model="book_squence" placeholder="请输入图书编号" class="handle-input mr10" clearable></el-input>
                <el-input v-model="borrower" placeholder="请输入借阅人" class="handle-input mr10" clearable></el-input>
                <el-input type="date" v-model="borrow_date" placeholder="请输入借阅日期" class="handle-input mr10" clearable></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
            </div>
            <el-table :data="borrowerTables.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column label="序列" width="55">
                    <template slot-scope="scope">
                        {{scope.$index+1}}
                    </template>
                </el-table-column>
                <el-table-column prop="bookName" label="图书名称" width="240"></el-table-column>
                <el-table-column prop="bookSquence" label="图书编号" width="240"></el-table-column>
                <el-table-column prop="borrower" label="借阅人" width="240"></el-table-column>
                <el-table-column prop="borrowTime" label="借阅日期" width="240"></el-table-column>
                <el-table-column prop="backTime" label="归还日期" width="240"></el-table-column>
                <el-table-column prop="bookRemark" label="备注" ></el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[10, 20, 50, 100]"
                               :page-size="pagesize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="borrowerTables.length">
                </el-pagination>
            </div>
        </div>

    </div>
</template>
<script>

    export default {


        data() {
            return{
                url: '/assets/borrowInfo/selectAll',
                book_name: '',
                book_squence: '',
                borrower: '',
                borrow_date: '',
                borrowerTables:[],
                cur_page: 1,
                multipleSelection: [],
                is_search: false,
                editVisible: false,
                delVisible: false,
                form:{

                },
                currentPage:1,
                pagesize:10,
                idx: -1
            }

        },
        created() {
            this.getData();
        },

        methods: {

            // 获取 easy-mock 的模拟数据
            getData() {
                // 开发环境使用 easy-mock 数据，正式环境使用 json 文件
                if (process.env.NODE_ENV === 'development') {
                    this.url = '/assets/borrowInfo/selectAll';
                };
                this.$axios.post(this.url, {
                    page: this.cur_page
                }).then((res) => {

                    this.borrowerTables = res.data.data;
                })
            },
            delAll() {
                const length = this.multipleSelection.length;
                let str = '';
                this.del_list = this.del_list.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].name + ' ';
                }
                this.$message.error('删除了' + str);
                this.multipleSelection = [];

            },
            search() {
                let params = new URLSearchParams();
                params.append('bookName',this.book_name);
                params.append('bookSquence',this.book_squence);
                params.append('borrower',this.borrower);
                params.append('borrowTime',this.borrow_date);
                console.log("拿到数据"+this.book_name);
                this.$axios.post("/assets/borrowInfo/selectBy", params,{
                    headers:{
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).then(res => {
                    this.$message.success(res.data.msg);
                    this.borrowerTables = res.data.data;
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleDelete(index, row) {
                this.idx = index;
                this.delVisible = true;
            },

            // 保存编辑
            saveEdit() {
                this.$set(this.borrowerTables, this.idx, this.form);
                this.editVisible = false;
                this.$message.success(`修改第 ${this.idx} 行成功`);
            },
            // 确定删除
            deleteRow(){
                this.borrowerTables.splice(this.idx, 1);
                this.$message.success('删除成功');
                this.delVisible = false;
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



</style>
