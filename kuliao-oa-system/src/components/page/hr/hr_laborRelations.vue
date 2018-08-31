<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 劳动关系管理</el-breadcrumb-item>
            </el-breadcrumb>

        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="contractId" placeholder="合同编号" class="handle-input mr10" clearable></el-input>
                <el-input v-model="employeeName" placeholder="员工姓名" class="handle-input mr10" clearable></el-input>
                <el-select v-model="contractType" placeholder="协议类型" class="handle-select mr10">
                    <el-option v-for="item in options" :key="item.value" :label="item.value"
                               :value="item.value"></el-option>
                </el-select>

                <el-button type="primary" icon="el-icon-search" @click="selectLaborRelation">搜索</el-button>
            </div>


            <div class="handle-box">
                <el-button type="primary" @click="addLaborRelation('addform')"
                           style="float:right;margin-bottom: 20px;margin-right: 4%">[ + ] 新建劳动关系
                </el-button>
            </div>


            <el-table :data="tabledata.slice((currentPage-1)*pageSize,currentPage*pageSize)" border stripe
                      style="width: 100%"  v-loading="loading">
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
        <el-dialog title="用工信息" :visible.sync="dialogFormVisible1">
            <el-form ref="addform1" :rules="rule" :model="addform" label-width="80px">

                <el-form-item prop="contractId" label="协议、合同编号">
                    <el-input v-model="addform.contractId"></el-input>
                </el-form-item>

                <el-form-item prop="employeeName" label="员工姓名">
                    <el-input v-model="addform.employeeName"></el-input>
                </el-form-item>

                <el-form-item prop="employeeNumber" label="员工编号">
                    <el-input v-model="addform.employeeNumber"></el-input>
                </el-form-item>


                <el-form-item prop="contractType" label="协议合同类型">
                    <el-select v-model="addform.contractType" placeholder="请选择协议类型">
                        <el-option label="试用期协议" value="试用期协议"></el-option>
                        <el-option label="劳动合同" value="劳动合同"></el-option>
                        <el-option label="三方协议" value="三方协议"></el-option>

                    </el-select>
                </el-form-item>

                <el-form-item prop="contractStartDate" label="开始时间">
                    <el-date-picker
                        type="date"
                        v-model="addform.contractStartDate"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日"
                        value-format="yyyyMMdd">
                    </el-date-picker>
                </el-form-item>

                <el-form-item prop="contractEndDate" label="结束时间">
                    <el-date-picker
                        type="date"
                        v-model="addform.contractEndDate"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日"
                        value-format="yyyyMMdd">
                    </el-date-picker>
                </el-form-item>

                <!--  <el-form-item prop="contractStartDate" label="开始时间">
                      <el-col :span="11">
                          <el-date-picker type="date" placeholder="选择日期" v-model.date="addform.contractStartDate"
                                          style="width: 100%;"></el-date-picker>
                      </el-col>
                  </el-form-item>
                  <el-form-item prop="contractEndDate" label="结束时间">
                      <el-col :span="11">
                          <el-date-picker type="date" placeholder="选择日期" v-model.date="addform.contractEndDate"
                                          style="width: 100%;"></el-date-picker>
                      </el-col>
                  </el-form-item>-->

                <el-form-item prop="laborReamrk" label="备  注">
                    <el-input type="textarea" v-model="addform.laborReamrk"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit('addform1')">立即创建</el-button>
                    <el-button @click="closedialogv1('addform1')">取消</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>

        <el-dialog title="编辑用工信息" :visible.sync="dialogFormVisible2">
            <el-form ref="addform2" :rules="rule" :model="addform" label-width="80px">

                <el-form-item prop="contractId" label="协议、合同编号">
                    <el-input v-model="addform.contractId"></el-input>
                </el-form-item>

                <el-form-item prop="employeeName" label="员工姓名">
                    <el-input v-model="addform.employeeName"></el-input>
                </el-form-item>

                <el-form-item prop="employeeNumber" label="员工编号">
                    <el-input v-model="addform.employeeNumber"></el-input>
                </el-form-item>


                <el-form-item prop="contractType" label="协议合同类型">
                    <el-select v-model="addform.contractType" placeholder="请选择协议类型">
                        <el-option label="试用期协议" value="试用期协议"></el-option>
                        <el-option label="劳动合同" value="劳动合同"></el-option>
                        <el-option label="三方协议" value="三方协议"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item prop="contractStartDate" label="开始时间">
                    <el-date-picker
                        type="date"
                        v-model="addform.contractStartDate"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日"
                        value-format="yyyyMMdd">
                    </el-date-picker>
                </el-form-item>

                <el-form-item prop="contractEndDate" label="结束时间">
                    <el-date-picker
                        type="date"
                        v-model="addform.contractEndDate"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日"
                        value-format="yyyyMMdd">
                    </el-date-picker>
                </el-form-item>

              <!--  <el-form-item prop="contractStartDate" label="开始时间">
                    <el-col :span="11">
                        <el-date-picker type="date" placeholder="选择日期" v-model.date="addform.contractStartDate"
                                        style="width: 100%;"></el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item prop="contractEndDate" label="结束时间">
                    <el-col :span="11">
                        <el-date-picker type="date" placeholder="选择日期" v-model.date="addform.contractEndDate"
                                        style="width: 100%;"></el-date-picker>
                    </el-col>
                </el-form-item>
-->
                <el-form-item prop="laborReamrk" label="备  注">
                    <el-input type="textarea" v-model="addform.laborReamrk"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="updateLaborRelationStateById('addform2')">提交更改</el-button>
                    <el-button @click="closedialogv2('addform2')">取消</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>


    </div>
</template>

<script>
    export default {
        data() {
            return {
                options: [
                    {
                        value: '劳动合同'
                    },
                    {
                        value: '三方协议'
                    },
                    {
                        value: '试用期协议'
                    },
                    {
                        value: ''
                    }],             //写前台或数组  后面解决
                data1: [
                    {
                        table_data_item: '协议、合同编号',
                        // length: 150,
                        des: 'contractId'
                    },
                    {
                        table_data_item: '员工姓名',
                        // length: 150,
                        des: 'employeeName'
                    },
                    {
                        table_data_item: '员工编号',
                        // length: 175,
                        des: 'employeeNumber'
                    },
                    {
                        table_data_item: '协议合同类型',
                        // length: 150,
                        des: 'contractType'
                    },
                    {
                        table_data_item: '协议合同开始日期',
                        // length: 200,
                        des: 'contractStartDate'
                    },
                    {
                        table_data_item: '协议合同结束日期',
                        // length: 200,
                        des: 'contractEndDate'
                    },
                    {
                        table_data_item: '备注',
                        //length: 175,
                        des: 'laborReamrk'
                    },
                    {
                        table_data_item: '操作',
                        //length: 175,
                        des: 'mycomponent'
                    }
                ],
                tabledata: [
                    {
                        contractId: '10010',
                        employeeName: '张三丰呀',
                        employeeNumber: '000250',
                        contractType: '正式员工',
                        contractStartDate: '2018.3.21',
                        contractEndDate: '2021.3.21',
                        laborReamrk: '三好员工，为领导挡子弹。'

                    }
                ],
                dialogFormVisible1: false,
                dialogFormVisible2: false,
                addform: {
                    contractId: '',
                    employeeName: '',
                    employeeNumber: '',
                    contractType: '',
                    contractStartDate: '',
                    contractEndDate: '',
                    laborReamrk: ''

                },           // form表单数据
                formLabelWidth: '120px',
                rule: {
                    contractId: [
                        {required: true, message: '请输入协议编号', trigger: 'blur'}
                    ],
                    employeeName: [
                        {required: true, message: '请输入员工姓名', trigger: 'blur'}
                    ],
                    employeeNumber: [
                        {required: true, message: '请输入员工编号', trigger: 'blur'}
                    ],
                    contractType: [
                        {required: true, message: '请选择协议类型', trigger: 'blur'}
                    ],
                    contractStartDate: [
                        {type: 'string', required: true, message: '请选择起始日期', trigger: 'blur'}
                    ],
                    contractEndDate: [
                        {type: 'string', required: true, message: '请选择终止日期', trigger: 'blur'}
                    ],


                },
                pageSize: 7,
                currentPage: 1,
                loading:false,
                contractId:'',
                contractType:'',
                employeeName:'',
            }
        },
        created() {
            this.selectLaborRelation();
        },
        methods: {
            //获取所有有价值记录
            selectLaborRelation(){
                this.loading=true;
                 var OaLaborRelations={};
                OaLaborRelations['contractId'] = this.contractId;
                OaLaborRelations['contractType'] = this.contractType;
                OaLaborRelations['employeeName'] = this.employeeName;
                this.$axios.post('/hr/OaLaborRelation/selectOaLaborRelations', OaLaborRelations, {
                    headers: {
                        'Content-Type': 'application/json'
                    }}).then((res) => {
                    this.loading=false;
                    console.log(res.data.data);
                    this.tabledata = res.data.data;
                })
            },

            /**
             * 新建劳动关系
             * @param form
             */
            addLaborRelation(form){
                this.addform={
                    contractId: '',
                    employeeName: '',
                    employeeNumber: '',
                    contractType: '',
                    contractStartDate: '',
                    contractEndDate: '',
                    laborReamrk: ''
                }
                this.dialogFormVisible1=true;
            },
            //填写劳动关系并提交
            onSubmit(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        var OaLaborRelations={};
                        OaLaborRelations['contractId'] = this.addform.contractId;
                        OaLaborRelations['employeeName'] = this.addform.employeeName;
                        OaLaborRelations['employeeNumber'] = this.addform.employeeNumber;
                        OaLaborRelations['contractType'] = this.addform.contractType;
                        OaLaborRelations['contractStartDate'] = this.addform.contractStartDate;
                        OaLaborRelations['contractEndDate'] = this.addform.contractEndDate;
                        OaLaborRelations['laborReamrk'] = this.addform.laborReamrk;
                        this.$axios.post("/hr/OaLaborRelation/insertLaborRelation", OaLaborRelations, {
                                headers: {
                                    'Content-Type': 'application/json'
                                }
                            }).then(res => {
                            if (res.data.code == '000000') {
                                this.$message.success(res.data.msg);
                                this.selectLaborRelation();       //重新 加载数据
                                this.closedialogv1('addform1');

                            } else
                            {
                                this.$message.error(res.data.data.msg);
                            }

                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //关闭addform1
            closedialogv1(form) {
                this.form = {
                    contractId: '',
                    employeeName: '',
                    employeeNumber: '',
                    contractType: '',
                    contractStartDate: '',
                    contractEndDate: '',
                    laborReamrk: ''
                };
                this.$refs[form].clearValidate();
                console.log(form + "添加验证去除")
                this.dialogFormVisible1 = false;
            },

            /**
             * 业务删除一条劳动关系记录
             * @param index
             */
          //劳动关系记录业务删除     未测试
            handleDelete(index){
                this.idx = index;
                this.$confirm('您确定要删除该客户信息吗?', '温馨提示', {
                    cancelButtonText: '取消',
                    confirmButtonText: '确定',
                    type: 'warning'
                }).then(() => {
                    this.changeLaborRelationState(index);
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
            changeLaborRelationState(index){
                let id=this.tabledata[(this.currentPage - 1) * this.pageSize + index].id;
                console.log(id);
                this.$axios.post("hr/OaLaborRelation/updateLaborRelationStateById", id, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.$message.success(res.data.msg);
                    this.selectLaborRelation();
                })
            },

            /**
             * 编辑劳动关系
             */
            //编辑劳动关系记录
            handleEdit(index, row) {
                this.idx = row.id;
                const item = this.tabledata[(this.currentPage - 1) * this.pageSize + index];
                this.addform = {
                    contractId:item.contractId,
                    employeeName: item.employeeName,
                    employeeNumber: item.employeeNumber,
                    contractType: item.contractType,
                    contractStartDate: item.contractStartDate.substring(0,4)+item.contractStartDate.substring(5,7)+item.contractStartDate.substring(8,10),
                    contractEndDate: item.contractEndDate.substring(0,4)+item.contractEndDate.substring(5,7)+item.contractEndDate.substring(8,10),
                    laborReamrk:item.laborReamrk
                }
                this.dialogFormVisible2 = true;
            },
            updateLaborRelationStateById(form){
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        console.log(this.addform);
                        var oaLaborRelations = {};
                        oaLaborRelations['id']=this.idx;
                        oaLaborRelations['contractId'] = this.addform.contractId;
                        oaLaborRelations['employeeName'] = this.addform.employeeName;
                        oaLaborRelations['employeeNumber'] = this.addform.employeeNumber;
                        oaLaborRelations['contractType'] = this.addform.contractType;
                        oaLaborRelations['contractStartDate'] = this.addform.contractStartDate;
                        oaLaborRelations['contractEndDate'] = this.addform.contractEndDate;
                        oaLaborRelations['laborState'] = true;
                        oaLaborRelations['laborReamrk'] = this.addform.laborReamrk;

                        this.$axios.post("hr/OaLaborRelation/updateLaborRelation", oaLaborRelations,  {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(res =>
                        {
                            if (res.data.code == '000000') {
                                this.selectLaborRelation();
                                this.$message.success(res.data.msg);
                                this.dialogFormVisible2=false;

                            } else
                            {
                                this.$message.error(res.data.data.msg);
                            }
                        })
                    } else {
                        console.log('error update!!');
                        return false;
                    }
                });
            },
            //关闭addform2
            closedialogv2(form) {
                // this.$refs[form].clearValidate();
                this.dialogFormVisible2 = false;
                this.$refs[form].resetFields();
                console.log(form + "表单验证去除");

            },

            //重置表单
            formClear(form) {
                this.$refs[form].resetFields();
            },

            //配置分页查询
            handleSizeChange: function (size) {
                this.pageSize = size;
            },
            handleCurrentChange: function (currentPage) {
                this.currentPage = currentPage;
            },

           /* changeWelfareState(id) {
                alert(id);
                this.$axios.post("hr/changeWelfareState", {
                    welfare_state: false
                }).then(res => {
                    console.log(res.data)
                })
            },
            updateWelfareById() {
                this.$axios.post("hr/updateWelfare", {}).then(
                    res => {
                        this.tableData = res.data;
                    }
                )
            },
            closeInsert(form) {
                this.form = {
                    employee_name: '',
                    employee_number: '',
                    contract_type: '',
                    contract_start_date: '',
                    contract_end_date: '',
                    labor_reamrk: ''
                };
                this.$refs[form].clearValidate();
                console.log(form + "添加验证去除")
                this.dialogFormVisible1 = false;
            },
            closehandleEdit(form) {
                // this.$refs[form].clearValidate();
                this.$refs[form].resetFields();
                console.log(form + "表单验证去除");
                this.dialogFormVisible2 = false;
            },*/

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
