<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>固定资产维修记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-select v-model="select_type" placeholder="物品类别" class="handle-select mr10" clearable>
                    <el-option v-for="item in types" :key="item.typeId" :label="item.typeName"
                               :value="item.typeName"></el-option>
                </el-select>
                <el-input v-model="select_name" placeholder="物品名称" clearable class="handle-select mr10"></el-input>
                <el-input v-model="select_user" placeholder="使用人" clearable class="handle-input mr10"></el-input>
                <el-input v-model="select_server" placeholder="维修人" clearable class="handle-input mr10"></el-input>
                <el-date-picker type="date" v-model="select_serviceDate" clearable value-format="yyyyMMdd" placeholder="维修日期" class="date-input"></el-date-picker>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
                <el-button  icon="info" type="success" style="position: absolute;right: 10%; " @click="handleInsert">[ + ] 新增维修</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="序号" sortable width="70">
                    <template slot-scope="scope">
                        {{scope.$index+1}}
                    </template>
                </el-table-column>
                <el-table-column prop="serviceId" v-if="serviceId>0" width="100">
                </el-table-column>
                <el-table-column prop="assetsId" v-if="assetsId>0" width="100">
                </el-table-column>
                <el-table-column prop="assetsNumber" label="固定资产编号" width="200">
                </el-table-column>
                <el-table-column prop="assetsName" label="物品名称" width="120">
                </el-table-column>
                <el-table-column prop="type" label="物品类型" width="80">
                </el-table-column>
                <el-table-column prop="user" label="负责人" width="100">
                </el-table-column>
                <el-table-column prop="server" label="维修人" width="100">
                </el-table-column>
                <el-table-column prop="serviceDate" label="维修日期" width="150">
                </el-table-column>
                <el-table-column prop="serviceAmount" label="维修费用(元)" width="100">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="220">
                </el-table-column>
                <el-table-column label="操作" width="170">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[8,10, 20, 50, 100]"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="tableData.length">
                </el-pagination>
            </div>
        </div>

        <!--维修新增弹出框 -->
        <el-dialog title="维修新增" :visible.sync="insertVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="固定资产编号" prop="assetsNumber">
                    <el-input placeholder="输入固定资产编号" v-model="form.assetsNumber" ></el-input>
                </el-form-item>
                <el-form-item label="物品名称" prop="assetsName">
                    <el-select v-model="form.assetsName" placeholder="请选择物品名称">
                        <el-option v-for="(item,key,index) in names" :key="index" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="物品类型" prop="type">
                    <el-select v-model="form.type" placeholder="请选择物品名称">
                        <el-option v-for="item in types" :key="item.typeId" :label="item.typeName" :value="item.typeName"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="负责人" prop="user">
                    <el-input type="text" placeholder="选择负责人" v-model="form.user" ></el-input>
                </el-form-item>
                <el-form-item label="维修人" prop="server">
                    <el-input type="text" placeholder="选择维修人" v-model="form.server" ></el-input>
                </el-form-item>
                <el-form-item label="维修日期" prop="serviceDate">
                    <el-date-picker type="date"  value-format="yyyyMMdd" placeholder="维修日期" v-model="form.serviceDate" class="date-input"></el-date-picker>
                </el-form-item>
                <el-form-item label="维修费用" prop="serviceAmount">
                    <el-input placeholder="维修费用" v-model="form.serviceAmount" ></el-input>
                </el-form-item>
                <el-form-item label="备注" >
                    <el-input v-model="form.remark"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="removeValidate('form')">取 消</el-button>
                <el-button type="primary" @click="saveInsert('form')">确 定</el-button>
            </span>
        </el-dialog>

         <!--编辑弹出框 -->
        <el-dialog title="维修记录编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="固定资产编号">
                    <el-input type="text" placeholder="输入固定资产编号" :disabled="true" v-model="form.assetsNumber" ></el-input>
                </el-form-item>
                <el-form-item label="物品名称">
                    <el-input type="text" :disabled="true" v-model="form.assetsName"  ></el-input>
                </el-form-item>
                <el-form-item label="物品类型">
                    <el-input type="text" :disabled="true" v-model="form.type"  ></el-input>
                </el-form-item>
                <el-form-item label="负责人">
                    <el-input type="text" placeholder="领用人" :disabled="true" v-model="form.user" ></el-input>
                </el-form-item>
                <el-form-item label="维修人" prop="server">
                    <el-input type="text" placeholder="选择维修人"   v-model="form.server" ></el-input>
                </el-form-item>
                <el-form-item label="维修日期" prop="serviceDate">
                    <el-date-picker type="date" value-format="yyyyMMdd"  placeholder="维修日期" v-model="form.serviceDate" class="date-input"></el-date-picker>
                </el-form-item>
                <el-form-item label="维修费用">
                    <el-input type="text" placeholder="维修费用" :disabled="true" v-model="form.serviceAmount" ></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit('form')">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import ElInput from "../../../../../node_modules/element-ui/packages/input/src/input.vue";

    export default {
        components: {ElInput},
        data() {
            return {
                tableData: [],
                cur_page: 1,
                multipleSelection: [],
                select_type:'',
                select_server: '',
                select_name:'',
                select_user:'',
                select_serviceDate: '',
                del_list: [],
                editVisible: false,
                insertVisible: false,
                delVisible: false,
                pageSize:8,
                currentPage:1,
                form: {
                    serviceId:'',
                    assetsId: '',
                    assetsNumber: '',
                    assetsName: '',
                    type:'',
                    serviceAmount: '',
                    server: '',
                    user: '',
                    remark: '',
                    serviceDate:''
                },
                names:[],
                types:[],
                oldDate:'',
                idx: -1,
                rules: {
                    assetsNumber: [
                        { required: false, message: '请输入资产编号', trigger: 'blur'},
                        { pattern : 'KLZN_YFB.*', message: '必须以KLZN_YFB开头', trigger: 'blur' },
                        { min: 20, max: 20, message: '长度必须为20个字符', trigger: 'blur'},
                    ],
                    assetsName: [
                        {required: true, message: '请选择物品名称', trigger: 'change'}
                    ],
                    assetsType: [
                        {required: true, message: '请选择类型', trigger: 'change'}
                    ],
                    user: [
                        {required: true, message: '请输入物品负责人', trigger: 'blur'}
                    ],
                    serviceDate: [
                        {required: true, message: '请选择维修日期', trigger: 'change'}
                    ],
                    server: [
                        {required: true, message: '请选择维修人员', trigger: 'blur'}
                    ],
                    serviceAmount: [
                        {required: true, message: '请输入维修费用', trigger: 'blur'}
                    ]
                }
            }
        },
        created() {
            this.getData();
            this.getTypeAndLocation();
        },
        methods: {
            // 获取初始化数据
            getData() {
                // 开发环境使用 easy-mock 数据，正式环境使用 json 文件
                this.$axios.post('/assets/fixedAssets/service',{header:{ 'Content-Type' : 'application/x-www-form-urlencoded'}}).then((res) => {
                    console.log('数据：'+res.data.data)
                    this.tableData = res.data.data;
                })
            },
            getTypeAndLocation(){
                this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation', {
                    headers:
                        {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then((res) => {
                    console.log(res.data.data.type)
                    this.types = res.data.data.type;
                })
            },
            handleInsert() {
                this.form={
                    serviceId:'',
                    assetsNumber: '',
                    assetsName: '',
                    type:'',
                    serviceAmount: '',
                    server: '',
                    user: '',
                    remark: '',
                    serviceDate:''
                },
                    this.insertVisible = true;
                this.findNames();
            },
            // 保存新增
            saveInsert(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/assets/fixedAssets/insertService',this.form).then((res) => {
                                this.$message.success(res.data.msg);
                                this.tableData.push(this.form)
                                this.getData();
                                this.removeValidate(formName);

                        });
                    } else {
                        this.$message.error("信息输入有误，请重新输入！！");
                        return false;
                    }
                });
            },
            findNames(){
                this.$axios.post('/assets/fixedAssets/findAllNamesAndType').then((res) => {
                    this.names=res.data.data.names;
                    this.types=res.data.data.types;
                    console.log(this.names)
                })
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    serviceId: item.serviceId,
                    serviceDate: item.serviceDate,
                    assetsNumber: item.assetsNumber,
                    serviceAmount: item.serviceAmount,
                    server: item.server,
                    assetsId: item.assetsId,
                    assetsName: item.assetsName,
                    type: item.type,
                    user: item.user,
                    remark: item.remark
                }
                this.oldDate = item.serviceDate;
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit(formName) {
                this.$refs[formName].validate((valid) => {
                        if (valid) {
                            let params = new URLSearchParams();
                            params.append('serviceId', this.form.serviceId);
                            if(this.oldDate == this.form.serviceDate){
                                this.form.serviceDate = this.oldDate.substr(0,4)+this.oldDate.charAt(5)+this.oldDate.charAt(6)+this.oldDate.charAt(8)+this.oldDate.charAt(9);
                            }
                            params.append('serviceDate', this.form.serviceDate);
                            params.append('server', this.form.server);
                            params.append('remark', this.form.remark);
                            this.$axios.post('/assets/fixedAssets/updateService', params, {
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                }
                            }).then((res) => {
                                this.$message.success("成功更新维修记录");
                                if(this.oldDate == this.form.serviceDate){
                                    this.form.serviceDate = this.oldDate;
                                }
                                this.$set(this.tableData, this.idx, this.form);
                                this.$message.success(`修改第 ${this.idx + 1} 行成功`);
                            })
                        }else{
                            this.$message.error("信息输入有误，请重新输入！！");
                            return false;
                        }
                        this.editVisible = false;

                    });
            },

                    //单条删除
            handleDelete(index, row) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    serviceId: item.serviceId
                }
                this.delVisible = true;
            },
            // 确定删除
            deleteRow(){
                let params = new URLSearchParams();
                params.append('serviceId',  this.form.serviceId);
                this.$axios.post('/assets/fixedAssets/deleteServiceById',params,{
                    headers:{
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).then((res) => {
                    this.$message.success("成功删除维修记录");
                    this.tableData.splice(this.idx, 1);
                    this.$message.success(`修改第 ${this.idx+1} 行成功`);
                })
                this.delVisible = false;
            },
            //批量删除
            delAll() {
                const length = this.multipleSelection.length;
                let str = '';
                this.del_list = this.del_list.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].serviceId + ' ';
                }
                alert(str)
                let params = new URLSearchParams();
                params.append('listId', str);
                this.$axios.post("/assets/fixedAssets/batchDeleteService", params).then(res => {
                    this.getData();
                    this.$message.error('删除了' + length+'条维修记录！');
                });
                this.multipleSelection = [];
            },
            //清空表单
            formClear(formName) {
                this.$refs[formName].resetFields();
            },
            //去除校验
            removeValidate(form){
                this.insertForm={
                        assetsNames: '',
                        serviceAmounts: '',
                        servers: '',
                        assetsType: '',
                        users: '',
                        remarks: '',
                        serviceDates:''
                },
                this.$refs[form].clearValidate();
                this.insertVisible = false;
            },
            OnSearch() {
                if(this.select_name==''&&this.select_type==''&&this.select_user==''&&this.select_server==''&&this.select_serviceDate==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("assetsName", this.select_name);
                params.append("type", this.select_type);
                params.append("user", this.select_user);
                params.append("server", this.select_server);
                params.append("serviceDate", this.select_serviceDate);
                this.$axios.post('/assets/fixedAssets/selectAssetsServiceByCondition', params, {
                    headers:
                        {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                }).then((res) => {
                    this.tableData = res.data.data;
                })
            },
            //批量处理
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            //配置分页查询
            handleSizeChange: function (size) {
                this.pageSize = size;
            },
            handleCurrentChange: function(currentPage){
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
        width: 100px;
        display: inline-block;
    }
    .date-input {
        width: 200px;
        display: inline-block;
    }
    .del-dialog-cnt{
        font-size: 16px;
        text-align: center
    }
</style>
