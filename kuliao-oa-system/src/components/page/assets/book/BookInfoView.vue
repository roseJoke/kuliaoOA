<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 图书信息查看</el-breadcrumb-item>
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
                bookIds: false,


                currentPage:1,
                pagesize:10,

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
