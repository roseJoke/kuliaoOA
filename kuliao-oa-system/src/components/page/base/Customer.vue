<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 客户管理</el-breadcrumb-item>
                <el-breadcrumb-item>客户信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                        <el-input v-model="select_cid" placeholder="请输入编号" class="handle-input mr10" type="number"></el-input>
                        <el-input v-model="select_cname" placeholder="请输入姓名" class="handle-input mr10"></el-input>
                        <el-select v-model="select_ctype" placeholder="请选择客户类型"  class="handle-select mr10">
                            <el-option key="1" label="一般" value="一般"></el-option>
                            <el-option key="2" label="重要" value="重要"></el-option>
                        </el-select>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
            </div>
            <el-button type="primary" icon="el-icon-circle-plus" @click="addFormVisible = true">添加客户</el-button>
            <!--添加客户对话框-->
            <el-dialog title="添加客户" :visible.sync="addFormVisible" center width="700px">
                <el-form :model="addform" :rules="rules" ref="addform" status-icon label-width="80px">
                    <el-form-item label="姓名"  prop="name">
                        <el-input v-model="addform.name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="工作单位"  prop="workunit">
                        <el-input v-model="addform.workunit" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话" prop="cphone">
                        <el-input v-model="addform.cphone" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="E-mail"  prop="email">
                        <el-input v-model="addform.email" auto-complete="off" type="email" ></el-input>
                    </el-form-item>
                    <el-form-item label="工作沟通"  prop="comuna">
                        <el-input v-model="addform.comuna" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="备注"  prop="beizhu">
                        <el-input v-model="addform.beizhu" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="客户类型"  prop="type">
                        <el-select v-model="addform.type" placeholder="请选择客户类型">
                            <el-option label="重要" value="重要"></el-option>
                            <el-option label="一般" value="一般"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitForm('addform')">保存</el-button>
                    <el-button  @click="resetForm('addform')">取消</el-button>
                </div>
            </el-dialog>
            <!--修改客户对话框-->
            <el-dialog title="修改客户" :visible.sync="editFormVisible" width="700px" center>
                <el-form :model="form" :rules="rules" ref="form" status-icon label-width="80px">
                    <el-form-item label="编号"  style="width: 600px" prop="id">
                        <el-input v-model="form.id" auto-complete="off" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="姓名"  style="width: 600px" prop="name">
                        <el-input v-model="form.name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="工作单位"  style="width: 600px" prop="workunit">
                        <el-input v-model="form.workunit" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话" style="width: 600px" prop="cphone">
                        <el-input v-model="form.cphone" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="E-mail"  style="width: 600px" prop="email">
                        <el-input v-model="form.email" auto-complete="off" type="email"></el-input>
                    </el-form-item>
                    <el-form-item label="工作沟通"  style="width: 600px" prop="comuna">
                        <el-input v-model="form.comuna" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="备注"  style="width: 600px">
                        <el-input v-model="form.beizhu" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="客户类型" prop="type">
                        <el-select v-model="form.type" placeholder="请选择客户类型" style="width: 260px">
                            <el-option label="重要" value="重要"></el-option>
                            <el-option label="一般" value="一般"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitForm('form')">修改</el-button>
                    <el-button  @click="resetForm('form')">取消</el-button>
                </div>
            </el-dialog>
            <!--基本客户信息-->
            <el-table :data="CustomerList" border style="width: 100%">
                <el-table-column prop="id" label="编号" sortable width="110">
                </el-table-column>
                <el-table-column prop="cname" label="姓名" width="110">
                </el-table-column>
                <el-table-column prop="cwork" label="工作单位">
                </el-table-column>
                <el-table-column prop="cphone" label="联系电话">
                </el-table-column>
                <el-table-column prop="cemail" label="E-mail" ><!--:formatter="formatter"-->
                </el-table-column>
                <el-table-column prop="conmunacation" label="工作沟通">
                </el-table-column>
                <el-table-column prop="beizhu" label="备注">
                </el-table-column>
                <el-table-column prop="ctype" label="客户类别">
                </el-table-column>
                <el-table-column label="可用操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="default"   size="mini" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
                        <el-button type="danger"    size="mini"icon="el-icon-delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页信息-->
            <div class="block" align="right" style="margin-top: 20px">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[4,6,8,10]"
                    :page-size="pagesize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total=tableData.length>
                </el-pagination>
            </div>
        </div>
    </div>
</template>
<script>
    import ElFormItem from "element-ui/packages/form/src/form-item";
    export default {
        components: {ElFormItem},
        name: "customer",
        data() {
            return {
                pagesize:6, //默认页面的客户信息条目
                tableData: [],  //表格中的基础数据
                select_cid: '', //用户输入的模糊检索的客户编号
                select_cname:'',//用于输入的模糊检索的客户姓名
                select_ctype: '',//用户选择的模糊检索的客户类型
                // delFormVisible:false,  //删除的对话框
                editFormVisible: false,
                //添加客户信息表单
                addform:{
                    name: '',
                    workunit: '',
                    cphone: '',
                    email: '',
                    type: '',
                    comuna: '',
                    beizhu: ''
                },
                //修改客户信息表单
                form: {
                    id:'',
                    name: '',
                    workunit: '',
                    cphone: '',
                    email: '',
                    type: '',
                    comuna: '',
                    beizhu: ''
                },
                addFormVisible: false,
                currentPage:1, //当前页码
                idx:-1,  //当前的索引
            //    添加客户表单验证问题
                rules:{
                    name:[
                        {
                            required:true,message:'客户姓名不能为空',trigger:'blur'
                        },{
                            min: 2, max: 10, message: '姓名不少于两个字', trigger: 'blur'
                        }
                    ],
                    workunit:[
                        {
                            required:true,message:'客户工作单位不能为空', trigger:'blur'
                        }
                    ],
                    cphone:[
                        {
                            required:true,message:'客户联系电话不能为空',trigger:'blur'
                        },{
                            pattern:"^(\\+\\d{2}-)?0\\d{2,3}-\\d{7,8}$",message:'电话号码格式不对',trigger:'blur'
                        },
                    ],
                    email:[
                        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                    ],
                    comuna:[
                        {
                            required:true,message:'客户沟通不能为空',trigger:'blur'
                        }
                    ],
                    type:[
                        {
                            required:true,message:'客户沟通不能为空',trigger:'change'
                        }
                    ]
                }
            }
        },
        created(){
            this.getData();
        },
        computed:{
            CustomerList:function () {
                return this.tableData.slice((this.currentPage-1)*this.pagesize,this.currentPage*this.pagesize)
            }
        },
        methods: {
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid){
                        if (formName==='addform'){
                            this.addCustomer();
                        }else {
                            this.updateCustomer();
                        }
                    }else {
                        console.log('error submit');
                        return false;
                    }
                });
            },
            //添加客户信息
            addCustomer(){
                var customer={};
                customer['cname'] = this.addform.name;
                customer['cwork'] = this.addform.workunit;
                customer['cphone'] = this.addform.cphone;
                customer['cemail'] = this.addform.email;
                customer['conmunacation'] = this.addform.comuna;
                customer['beizhu'] = this.addform.beizhu;
                customer['ctype'] = this.addform.type;
                this.$axios.post("/base/customer/save",customer,{
                    headers:{
                        'Content-Type' : 'application/json'
                    }
                }).then((res)=>{
                    this.getData();
                    this.$message.success(res.data.msg);
                    //重新刷新页面
                })
                this.addFormVisible = false;
            },
            //修改客户信息
            updateCustomer(){
                var customer={};
                customer['id'] = this.form.id;
                customer['cname'] = this.form.name;
                customer['cwork'] = this.form.workunit;
                customer['cphone'] = this.form.cphone;
                customer['cemail'] = this.form.email;
                customer['conmunacation'] = this.form.comuna;
                customer['beizhu'] = this.form.beizhu;
                customer['ctype'] = this.form.type;
                this.$axios.post("/base/customer/modify",customer,{
                    headers:{
                        'Content-Type' : 'application/json'
                    }
                }).then((res)=>{
                    this.getData();
                    this.$message.success(res.data.msg);
                    //重新刷新页面
                })
                this.editFormVisible =false;
            },
            //重置表单数据
            resetForm(formName){
                this.$refs[formName].resetFields();
            },
            // 分页导航
            handleCurrentChange(currentPage) {
                this.currentPage = currentPage;
                this.getData();
            },
            // 获取表格中的数据
            getData(){
                this.$axios.post("/base/customer/query").then((res)=>{
                    // this.$message.success(res.data.msg);
                    this.tableData = res.data.data;
                }).catch(error => {
                    console.log(error);
                })
            },
            handleEdit(index,row){
                this.idx = index;
                const item = this.tableData[index];
                this.form = {
                    id:item.id,
                    name: item.cname,
                    workunit: item.cwork,
                    cphone: item.cphone,
                    email: item.cemail,
                    type: item.ctype,
                    comuna: item.conmunacation,
                    beizhu: item.beizhu
                }
                this.editFormVisible = true;
             },

            handleDelete(index,row){
                this.idx = index;
                this.$confirm('您确定要删除该客户信息吗?', '温馨提示', {
                    cancelButtonText: '取消',
                    confirmButtonText: '确定',
                    type: 'warning'
                }).then(() => {
                    this.deleteRow();
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
            //
            formatter(row, column) {
                return row.email;
            },
            // 模糊检索
            search(){
                this.is_search = true;
                let params = new URLSearchParams();
                params.append('id',this.select_cid);
                params.append('cname',this.select_cname);
                params.append('ctype',this.select_ctype);
                this.$axios.post("/base/customer/more",params).then((res)=>{
                    this.tableData=res.data.data;
                    console.log(res.data.msg);
                }).catch(error => {
                    this.$message.error(error);
                })
            },
            // 保存编辑
            saveEdit() {
                this.$set(this.tableData, this.idx, this.form);
                this.editFormVisible = false;
                this.$message.success(`修改第 ${this.idx+1} 行成功`);
            },
            //删除客户信息
            deleteRow() {
               let id =  this.tableData[this.idx].id;
                let params = new URLSearchParams();
                params.append('id',id);
                this.$axios.post("/base/customer/delete",params).then((res)=>{
                    this.$message.success(res.data.msg);
                    this.tableData.splice(this.idx, 1);
                }).catch(error => {
                    this.$message.error('操作失败!');
                })
                this.delFormVisible = false;
            },
            // 修改当前页面的客户列表数目
            handleSizeChange(size) {
                this.pagesize = size;
            }
        }
    }
</script>
<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }
    .handle-select {
        width: 220px;
    }
    .handle-input {
        width: 200px;
        display: inline-block;
    }
</style>
