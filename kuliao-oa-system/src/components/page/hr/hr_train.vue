<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 员工培训</el-breadcrumb-item>
            </el-breadcrumb>

        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="trainer" placeholder="培训讲师" class="handle-input mr10" clearable></el-input>
                <el-input v-model="trainingContent" placeholder="培训内容" class="handle-input mr10" clearable></el-input>
                <el-button type="primary" icon="el-icon-search" @click="selectOaTrain">搜索</el-button>
            </div>
            <el-table :data="tabledata.slice((currentPage-1)*pageSize,currentPage*pageSize)" border stripe
                      style="width: 100%" v-loading="loading">
                <template v-for="i1 in data1">
                    <template v-if="i1.des==='mycomponent'">
                        <el-table-column
                            label="操作"
                            width="200"
                            data-date-picker="activated">
                            <template slot-scope="scope">
                                <el-button
                                    :prop="i1.des"
                                    size="mini"
                                    @click="handleEdit(scope.$index, scope.row)">编辑
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="danger"
                                    @click="handleDelete(scope.$index)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </template>
                    <template v-else slot-scope="scope">
                        <el-table-column
                            :show-overflow-tooltip="true"
                            v-bind:label="i1.table_data_item"
                            v-bind:width="i1.length"
                            v-bind:prop="i1.des">
                        </el-table-column>
                    </template>
                </template>
            </el-table>

            <div class="pagination">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[7,8,9,10]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="tabledata.length">
                </el-pagination>
            </div>
        </div>
        <!--dialog 弹框-->
        <el-dialog title="编辑培训信息" :visible.sync="dialogFormVisible">
            <el-form ref="trainform" :rules="rrrr" :model="trainform" label-width="80px">

                <el-form-item prop="trainer" label="培训讲师">
                    <el-input v-model="trainform.trainer"></el-input>
                </el-form-item>

                <el-form-item prop="trainingContent" label="培训内容">
                    <el-input v-model="trainform.trainingContent"></el-input>
                </el-form-item>

                <el-form-item prop="trainingTime" label="培训时间">
                    <el-date-picker
                        type="datetime"
                        v-model="trainform.trainingTime"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日 HH:mm"
                        value-format="yyyyMMdd HH:mm">
                    </el-date-picker>
                </el-form-item>

                <el-form-item prop="trainees" label="培训对象">
                    <el-input v-model="trainform.trainees"></el-input>
                </el-form-item>

                <el-form-item prop="trainingPlace" label="培训地点">
                    <el-input v-model="trainform.trainingPlace"></el-input>
                </el-form-item>
                <!--   <el-form-item label="备  注">
                       <el-input type="textarea" v-model="addform.welfare_remark"></el-input>
                   </el-form-item>-->
                <el-form-item>
                    <el-button type="primary" @click="updateOaTrain('trainform')">提交更改</el-button>
                    <el-button @click="closedialogv2('trainform')">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                data1: [
                    {
                        table_data_item: '培训编号',
                        //  length: 150,
                        des: 'id'
                    },
                    {
                        table_data_item: '培训讲师',
                        // length: 150,
                        des: 'trainer'
                    },
                    {
                        table_data_item: '培训内容',
                        //length: 200,
                        des: 'trainingContent'
                    },
                    {
                        table_data_item: '培训时间',
                        // length: 200,
                        des: 'trainingTime'
                    },
                    {
                        table_data_item: '培训对象',
                        //length: 250,
                        des: 'trainees'
                    },
                    {
                        table_data_item: '培训地点',
                        //   length: 200,
                        des: 'trainingPlace'
                    },
                    {
                        table_data_item: '操作',
                        //  length: 200,
                        des: 'mycomponent'
                    }
                ],
                tabledata: [],
                dialogFormVisible: false,
                trainform: {
                    trainer: '',
                    trainingContent: '',
                    trainingTime: '',
                    trainees: '',
                    trainingPlace: '',
                },
                rrrr: {
                    trainer: [
                        {required: true, message: '讲师名不能为空', trigger: 'blur'}
                    ],

                    trainingContent: [
                        {required: true, message: '培训内容不能为空', trigger: 'blur'}
                    ],

                    trainingTime: [
                        {type: 'string', required: true, message: '培训时间不能为空', trigger: 'change'}
                    ],
                    trainees: [
                        {required: true, message: '培训对象不能为空', trigger: 'blur'}
                    ],
                    trainingPlace: [
                        {required: true, message: '培训地点不能为空', trigger: 'blur'}
                    ]
                }, //表单验证规则
                formLabelWidth: '120px',
                pageSize: 7,
                currentPage: 1,
                loading: false,
                trainer: '',
                trainingContent: '',
            }
        },
        created() {
            this.selectOaTrain();
        },
        methods: {

            //获取所有培训记录
            selectOaTrain() {
                this.loading = true;
                var oaTrain = {};
                oaTrain['trainer'] = this.trainer;
                oaTrain['trainingContent'] = this.trainingContent;
                this.$axios.post('/hr/OaTrain/selectOaTrain', oaTrain, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then((res) => {
                    this.loading = false;
                    console.log(res.data.data);
                    this.tabledata = res.data.data;
                })
            },

            //编辑培训记录内容
            handleEdit(index, row) {
                this.idx = row.id;
                const item = this.tabledata[(this.currentPage - 1) * this.pageSize + index];
                this.trainform = {
                    trainer: item.trainer,
                    trainingContent: item.trainingContent,
                    trainingTime: item.trainingTime.substring(0,4)+item.trainingTime.substring(5,7)+item.trainingTime.substring(8,10)+' '+item.trainingTime.substring(12,15)+item.trainingTime.substring(15),
                    trainees: item.trainees,
                    trainingPlace: item.trainingPlace,
                }
                this.dialogFormVisible = true;

            },
            //更新培训记录内容
            updateOaTrain(form) {
                console.log(this.trainform.trainingTime);
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        var oaTrain = {};
                        oaTrain['id'] = this.idx;
                        oaTrain['trainer'] = this.trainform.trainer;
                        oaTrain['trainingContent'] = this.trainform.trainingContent;
                        oaTrain['trainingTime'] = this.trainform.trainingTime;
                        oaTrain['trainees'] = this.trainform.trainees;
                        oaTrain['trainingPlace'] = this.trainform.trainingPlace;
                        oaTrain['trainingState'] = true;
                        this.$axios.post("hr/OaTrain/updateTrain", oaTrain, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(res => {
                            if (res.data.code == '000000') {
                                this.selectOaTrain();
                                this.$message.success(res.data.msg);
                                this.dialogFormVisible = false;

                            } else {
                                this.$message.error(res.data.data.msg);
                            }
                        })
                    } else {
                        console.log('error update!!');
                        return false;
                    }
                });
            },
            //取消对话框
            closedialogv2(form) {
                // this.$refs[form].clearValidate();
                this.$refs[form].resetFields();
                console.log(form + "表单验证去除");
                this.dialogFormVisible = false;
                this.addform = {};
            },

            //业务删除一条培训记录
            handleDelete(index){
                this.idx = index;
                this.$confirm('您确定要删除该客户信息吗?', '温馨提示', {
                    cancelButtonText: '取消',
                    confirmButtonText: '确定',
                    type: 'warning'
                }).then(() => {
                    this.updateOaTrainState(index);
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            updateOaTrainState(index) {
                let id = this.tabledata[(this.currentPage - 1) * this.pageSize + index].id;
                this.$axios.post("hr/OaTrain/updateOaTrainStateById", id, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    if (res.data.code == '000000') {
                        this.selectOaTrain();
                        this.$message.success(res.data.msg);
                        this.dialogFormVisible = false;

                    } else {
                        this.$message.error(res.data.data.msg);
                    }
                });
        },

        //配置分页查询
        handleSizeChange: function (size) {
            this.pageSize = size;
        },
        handleCurrentChange: function (currentPage) {
            this.currentPage = currentPage;
        },
    }
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
        width: 120px;
        display: inline-block;
    }

</style>
