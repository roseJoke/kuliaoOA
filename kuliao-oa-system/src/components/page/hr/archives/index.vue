<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>档案管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-row>
                    <el-col :span="3">
                        <el-input placeholder="员工编号" v-model="select.empNo" class="handle-del mr10"></el-input>
                    </el-col>
                    <el-col :span="3" :offset="1">
                        <el-input placeholder="员工姓名" v-model="select.name" class="handle-del mr10"></el-input>
                    </el-col>
                    <el-col :span="2" :offset="1">
                        <el-select v-model="select.sex"
                                   placeholder="请选择">
                            <el-option :key="3" label="全部" :value="null"></el-option>
                            <el-option :key="1" label="男" value="男"></el-option>
                            <el-option :key="2" label="女" value="女"></el-option>

                        </el-select>
                    </el-col>
                    <el-col :span="5" :offset="1">
                        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table :data="table" border stripe   v-loading="loading">
                <el-table-column prop="name" label="姓名">
                </el-table-column>
                <el-table-column prop="empno" label="编号">
                </el-table-column>
                <el-table-column prop="sex" label="性别">
                </el-table-column>
                <el-table-column prop="age" label="年龄">
                </el-table-column>
                <el-table-column prop="dept" label="部门">
                </el-table-column>
                <el-table-column prop="post" label="岗位">
                </el-table-column>
                <el-table-column prop="phone" label="电话">
                </el-table-column>
                <el-table-column prop="email" label="邮箱">
                </el-table-column>

                <el-table-column label="具体信息">
                    <template slot-scope="scope">
                        <!-- <el-button type="text" class="el-icon-view"  @click="onLook(1)">预览</el-button>--><!--
                        <el-button type="text" class="el-icon-download">下载</el-button>-->
                        <el-button type="text" class="el-icon-edit" @click="onEdit(scope.row.id)">查看具体信息</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" @current-change="list"
                               :total="select.total" :page-size="select.pageSize" :current-page="select.pageNum">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>


    export default {
        data() {
            return {
                select: {
                    empNo: null,
                    sex: null,
                    name: null,
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                form: {},
                table: [],
                loading: true
            }
        },

        created() {
            this.list();
        },
        methods: {
            search() {

                this.list();
            },
            onEdit(id) {
                this.$router.push({path: "/hr/archives/index/edit", query: {id: id, enEdit: false}});
            },
            list(pageNum) {
                if (pageNum) {
                    this.select.pageNum = pageNum;
                }
                this.loading = true;
                this.$axios.get("/hr/archives", {
                    params: this.select
                }).then(res => {
                    console.log(res);
                    this.table = res.data.data.table;
                    this.select.total = res.data.data.total;
                    this.select.pageNum = res.data.data.pageNum;
                    this.loading = false;
                });

            },


        }
    }
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }


</style>
