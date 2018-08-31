<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>办公用品领用记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-select v-model="select_goodsName" placeholder="物品名称" clearable class="handle-select mr10">
                    <el-option v-for="(item,key,index) in useNames" :key="key" :label="item"
                               :value="item"></el-option>
                </el-select>
                <el-select v-model="select_type" placeholder="类别" clearable class="handle-select mr10">
                    <el-option v-for="(item,key,index) in types" :key="key" :label="item"
                               :value="item"></el-option>
                </el-select>
                <el-select v-model="select_department" placeholder="部门名称" clearable class="handle-select mr10">
                    <el-option v-for="(item,key,index) in departments" :key="key" :label="item"
                               :value="item"></el-option>
                </el-select>
                <el-input v-model="select_user" placeholder="领用人" clearable class="handle-input mr10"></el-input>
                <el-date-picker type="date" v-model="select_useDate" value-format="yyyyMMdd" placeholder="领用日期"
                                clearable
                                class="date-input"></el-date-picker>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
                <el-button icon="info" type="success" @click="handleInsert" style="position: absolute;right:10%;">[ + ]
                    新增领用
                </el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border style="width: 100%"
                      ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="序号" sortable width="70">
                    <template slot-scope="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="goodsuseId" v-if="goodsuseId>0" sortable width="150">
                </el-table-column>
                <el-table-column prop="goodsName" label="物品名称" width="100">
                </el-table-column>
                <el-table-column prop="type" label="类型" width="80">
                </el-table-column>
                <el-table-column prop="useDate" label="领用日期" width="150">
                </el-table-column>
                <el-table-column prop="department" label="部门名称" width="150">
                </el-table-column>
                <el-table-column prop="user" label="领用人" width="100">
                </el-table-column>
                <el-table-column prop="count" label="数量" width="80">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="80">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="260">
                </el-table-column>
                <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
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
        <!--领用新增弹出框 -->
        <el-dialog title="领用新增" :visible.sync="insertVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="物品名称" prop="goodsName">
                    <el-select v-model="form.goodsName" placeholder="物品名称" class="handle-select mr10">
                        <el-option v-for="(item,key,index) in goodsNames" :key="key" :label="item"
                                   :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="部门名称" prop="department">
                    <el-select v-model="form.department" placeholder="选择部门" class="handle-select mr10">
                        <el-option v-for="item in departments" :key="item.deptId" :label="item.deptName"
                                   :value="item.deptName"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="领用人" prop="user">
                    <el-input type="text" placeholder="选择领用人" v-model="form.user"></el-input>
                </el-form-item>
                <el-form-item label="领用日期" prop="useDate">
                    <el-date-picker type="date" value-format="yyyyMMdd" placeholder="选择领用日期"
                                    v-model="form.useDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-input type="number" placeholder="选择数量" v-model.number="form.count"></el-input>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-select v-model="form.type" placeholder="请选择类别">
                        <el-option v-for="item in types" :key="index" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="单位" prop="unit">
                    <el-select v-model="form.unit" placeholder="选择单位" class="handle-select mr10">
                        <el-option v-for="(item,key,index) in units" :key="key" :label="item"
                                   :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="removeValidate('form')">取 消</el-button>
                <el-button type="primary" @click="saveInsert('form')">确 定</el-button>
            </span>
        </el-dialog>
        <!--编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="100px">
                <el-form-item v-if="form.goodsuseId>0">
                    <el-input type="text" :disabled="true" v-model="form.goodsName"></el-input>
                </el-form-item>
                <el-form-item label="物品名称">
                    <el-input type="text" placeholder="选择名称" :disabled="true" v-model="form.goodsName"></el-input>
                </el-form-item>
                <el-form-item label="部门名称">
                    <el-input type="text" placeholder="选择部门" :disabled="true" v-model="form.department"></el-input>
                </el-form-item>
                <el-form-item label="领用人">
                    <el-input type="text" placeholder="选择领用人" :disabled="true" v-model="form.user"></el-input>
                </el-form-item>
                <el-form-item label="领用日期">
                    <el-date-picker type="date" placeholder="选择领用日期" value-format="yyyyMMdd" :disabled="true"
                                    v-model="form.useDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="数量">
                    <el-input placeholder="选择领用数量" :disabled="true" v-model="form.count"></el-input>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-input type="text" v-model="form.type"></el-input>
                </el-form-item>
                <el-form-item label="单位">
                    <el-input placeholder="选择单位" :disabled="true" v-model="form.unit"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    export default {
        data() {
            return {
                types: ['办公文具', '通用工具', '专用工具', '替换件', '周转材料', '管理用具', '劳动用品', '其他'],
                tableData: [],
                cur_page: 1,
                multipleSelection: [],
                select_department: '',
                select_type: '',
                select_goodsName: '',
                select_user: '',
                select_useDate: '',
                del_list: [],
                is_search: false,
                editVisible: false,
                insertVisible: false,
                delVisible: false,
                pageSize: 8,
                currentPage: 1,
                useNames: [],
                goodsNames: [],
                departments: ['java研发', 'C++研发', '运维部', '人事部', '财务部', '其他'],
                units: ['支', '盒', '个', '把', '张', '条', '包', '件', '本'],
                form: {
                    goodsuseId: '0',
                    goodsName: '',
                    useDate: '',
                    count: '',
                    type: '',
                    unit: '',
                    user: '',
                    department: '',
                    remark: ''
                },
                idx: -1,
                rules: {
                    goodsName: [
                        {required: true, message: '请输入物品名称', trigger: 'blur'},
                        {min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur'}
                    ],
                    department: [
                        {required: true, message: '请选择部门', trigger: 'change'}
                    ],
                    user: [
                        {required: true, message: '请输入物品规格', trigger: 'blur'}
                    ],
                    useDate: [
                        {required: true, message: '请选择领用日期', trigger: 'change'}
                    ],
                    type: [
                        {required: true, message: '请选择类型', trigger: 'change'}
                    ],
                    unit: [
                        {required: true, message: '请选择单位', trigger: 'change'}
                    ],
                    count: [
                        {required: true, message: '请输数量', trigger: 'blur'}
                    ],
                }
            }
        },
        created() {
            this.getData();
        },
        mounted(){
            this.getgoodsNames();
            this.getDepartment();
        },
        methods: {
            getDepartment(){
                this.$axios.post('/base/dept/selectAll').then((res) => {
                    console.log("部门信息："+res.data.data[0])
                    this.departments = res.data.data;
                })
            },
            getgoodsNames(){
                this.$axios.post('/assets/officeGoods/ALlUseGoodsName', {
                    headers:
                        {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                }).then((res) => {
                    this.useNames = res.data.data.use;
                    this.goodsNames = res.data.data.goods;
                })
            },
            // 获取 easy-mock 的模拟数据
            getData() {
                this.$axios.post('/assets/officeGoods/use').then((res) => {
                    this.tableData = res.data.data;
                })
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage - 1) * this.pageSize + index;
                const item = this.tableData[(this.currentPage - 1) * this.pageSize + index];
                this.form = {
                    gooduseId: item.gooduseId,
                    goodsName: item.goodsName,
                    department: item.department,
                    useDate: item.useDate,
                    count: item.count,
                    type: item.type,
                    unit: item.unit,
                    user: item.user,
                    remark: item.remark
                }
                this.editVisible = true;
            },
            handleInsert() {
                this.form = {
                    goodsName: '',
                    department: '',
                    user: '',
                    useDate: '',
                    count: '',
                    type: '',
                    unit: '',
                    remark: ''
                }
                this.insertVisible = true;
            },
            // 保存新增
            saveInsert(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/assets/officeGoods/addOneGoodsUse', this.form).then((res) => {
                                this.$message.success(res.data.msg);
                                this.tableData.push(this.form)
                                this.removeValidate(formName)
                        });
                    } else {
                        this.$message.error("输入的信息有误，请重新输入！！");
                        return false;
                    }
                });
            },
            // 保存编辑
            saveEdit() {
                let params = new URLSearchParams();
                params.append("gooduseId", this.form.gooduseId);
                params.append("remark", this.form.remark);
                this.$axios.post('/assets/officeGoods/updateGoodsUseById', params, {
                    headers:
                        {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                }).then((res) => {
                    this.$set(this.tableData, this.idx, this.form);
                    this.editVisible = false;
                    this.$message.success(`修改第 ${this.idx + 1} 行成功`);
                })

            },
            OnSearch() {
                if (this.select_goodsName == '' && this.select_useDate == '' && this.select_type == '' && this.select_department == '' && this.select_user == '') {
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("goodsName", this.select_goodsName);
                params.append("useDate", this.select_useDate);
                params.append("type", this.select_type);
                params.append("department", this.select_department);
                params.append("user", this.select_user);
                this.$axios.post('/assets/officeGoods/selectGoodsUseByCondition', params, {
                    headers:
                        {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                }).then((res) => {
                    this.tableData = res.data.data;
                    console.log(res.data.msg)

                })
            },
            formClear(formName) {
                this.insertVisible = false;
                this.$refs[formName].resetFields();
            },
            removeValidate(form) {
                this.form = {
                    goodsName: '',
                    department: '',
                    useDate: '',
                    count: '',
                    unit: '',
                    user: '',
                    remark: ''
                },
                    this.$refs[form].clearValidate();
                this.insertVisible = false;
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
        width: 130px;
    }

    .handle-input {
        width: 100px;
        display: inline-block;
    }

    .date-input {
        width: 200px;
        display: inline-block;
    }
</style>
