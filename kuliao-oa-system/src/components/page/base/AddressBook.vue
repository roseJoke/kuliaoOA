<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 电子通讯录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="username" placeholder="姓名" class="handle-input mr10"></el-input>
                <el-input v-model="deptName" placeholder="部门名称" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>

            </div>
                <el-table :data="archives" border style="width: 100%" @selection-change="handleSelectionChange">
                    <el-table-column label="序列" width="55">
                        <template slot-scope="scope">
                            {{scope.$index+1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="姓名"></el-table-column>
                    <el-table-column prop="sex" label="性别"></el-table-column>
                    <el-table-column prop="dept" label="部门"></el-table-column>
                    <el-table-column prop="postName" label="职务"></el-table-column>
                    <el-table-column prop="phone" label="手机号码"></el-table-column>
                    <el-table-column prop="email" label="邮箱"></el-table-column>

                </el-table>
                    <div class="pagination">
                            <el-pagination class="pull-right clearfix"
                                           @size-change="handleSizeChange"
                                           @current-change="handleCurrentChange"
                                           :current-page="currentPage"
                                           :page-sizes="[10, 20, 50, 100]"
                                           :page-size="pageSize"
                                           layout="total, sizes, prev, pager, next, jumper"
                                           :total="archives.length">
                            </el-pagination>
                    </div>
        </div>

    </div>
</template>
<script>
    export default {
        data() {
            return{
                url: '/hr/archives',
                username: '',
                deptName: '',
                archives:[],
                cur_page: 1,
                multipleSelection: [],
                flag: 'select',
                currentPage:1,
                pageSize:10,
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
            getData() {
                // 开发环境使用 easy-mock 数据，正式环境使用 json 文件
                if (process.env.NODE_ENV === 'development') {
                    this.url = '/hr/archives/emp/';
                }
                let params={
                    "pageNum": this.currentPage,
                    "pageSize": this.pageSize
                };
                this.$axios.post(this.url, params).then((res) => {
                    this.archives = res.data.data.list;
                })
            },
            search() {
                let params = {
                    "username":this.username,
                    "deptName":this.deptName,
                };

                this.$axios.post("/hr/archives/emp/", params).then(res => {
                    // this.$message.success(res.data.msg);
                    this.archives = res.data.data.list;
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
