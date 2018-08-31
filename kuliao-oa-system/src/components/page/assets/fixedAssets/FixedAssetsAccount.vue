<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>固定资产台账</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                搜索条件：
                <el-select v-model="select_department" placeholder="部门名称" class="handle-select mr10" clearable>
                    <el-option v-for="item in departments" :key="item.deptId" :label="item.deptName"
                               :value="item.deptName"></el-option>
                </el-select>
                <el-select v-model="select_type" placeholder="物品类别" class="handle-select mr10" clearable>
                    <el-option v-for="item in types" :key="item.typeId" :label="item.typeName"
                               :value="item.typeId"></el-option>
                </el-select>
                <el-input v-model="select_assetsNumber" placeholder="固定资产编号" clearable class="date-input"></el-input>
                <el-input v-model="select_assetsName" placeholder="物品名称" clearable class="date-input"></el-input>
                <el-select v-model="select_location" placeholder="存放地点" clearable class="handle-select mr10">
                    <el-option v-for="item in locations" :key="item.locationId" :label="item.locationName"
                               :value="item.locationId"></el-option>
                </el-select>
                <el-select v-model="select_status" placeholder="使用状态" clearable class="handle-select mr10">
                    <el-option key="1" label="闲置" value="1"></el-option>
                    <el-option key="2" label="使用中" value="2"></el-option>
                    <el-option key="3" label="维修中" value="3"></el-option>
                </el-select>
                <el-input v-model="select_user" placeholder="使用人" clearable class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border ref="multipleTable">
                <el-table-column label="序号" sortable width="100">
                    <template slot-scope="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="assetsId" v-if='assetsId>0' width="50">
                </el-table-column>
                <el-table-column prop="department" label="部门名称" width="150">
                </el-table-column>
                <el-table-column prop="assetsNumber" label="固定资产编号" width="180">
                </el-table-column>
                <el-table-column prop="assetsName" label="物品名称" width="150">
                </el-table-column>
                <el-table-column prop="type" label="类别" width="100">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="100">
                </el-table-column>
                <el-table-column prop="format" label="规格" width="180">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="50">
                </el-table-column>
                <el-table-column prop="buyDate" label="购置日期" width="100">
                </el-table-column>
                <el-table-column prop="status" label="使用状态" width="100">
                </el-table-column>
                <el-table-column prop="location" label="存放地点" width="100">
                </el-table-column>
                <el-table-column prop="userId" v-if="userId>0"  width="100">
                </el-table-column>
                <el-table-column prop="user" label="使用人" width="100">
                </el-table-column>
                <el-table-column prop="price" label="单价(元)" width="100">
                </el-table-column>
                <el-table-column prop="originalPrice" label="原价(元)" width="100">
                </el-table-column>
                <el-table-column prop="estimatedAge" label="预计使用年限（年）" width="100">
                </el-table-column>
                <el-table-column prop="residualRate" label="残值率（%）" width="100">
                </el-table-column>
                <el-table-column prop="depreciationDeadline" label="折旧截止日期" width="100">
                </el-table-column>
                <el-table-column prop="residualValue" label="残净值（元）" width="100">
                </el-table-column>
                <el-table-column prop="useMonth" label="已用月（月）" width="100">
                </el-table-column>
                <el-table-column prop="depreciationMonth" label="折旧月（月）" width="100">
                </el-table-column>
                <el-table-column prop="accumulatedDepreciation" label="累计折旧（元）" width="100">
                </el-table-column>
                <el-table-column prop="netWorth" label="净值（元）" width="100">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="120">
                </el-table-column>
                <el-table-column label="操作" width="80">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
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
        <div class="pagination">
            <span style="color:red">总金额：<el-input v-model="money" style="width: 150px;font-size: 20px;"></el-input>&nbsp;元</span>
        </div>
        <!--编辑弹出框 -->
        <el-dialog title="固定资产编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="固定资产id" hidden="hidden">
                    <el-input v-model="form.assetsId" hidden="hidden" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="固定资产编号" prop="assetsNumber">
                    <el-input v-model="form.assetsNumber"></el-input>
                </el-form-item>
                <el-form-item label="固定资产名称" prop="assetsName">
                    <el-input v-model="form.assetsName" ></el-input>
                </el-form-item>
                <el-form-item label="类别" prop="typeId">
                    <el-select v-model="form.typeId" placeholder="物品类别" class="handle-select mr10" clearable>
                        <el-option v-for="item in types" :key="item.typeId" :label="item.typeName"
                                   :value="item.typeId"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="单位" prop="unit">
                    <el-select v-model="form.unit" class="handle-select" placeholder="请选择单位">
                        <el-option v-for="(item,key,index) in units" :key="index" :label="item" :value="item" clearable></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="规格" prop="format">
                    <el-input type="textarea" v-model="form.format" ></el-input>
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input v-model="form.price" ></el-input>
                </el-form-item>
                <el-form-item label="原值" prop="originalPrice">
                    <el-input v-model="form.originalPrice" ></el-input>
                </el-form-item>
                <el-form-item label="购置日期" prop="buyDate">
                        <el-date-picker type="date" placeholder="选择日期" value-format="yyyyMMdd" v-model="form.buyDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="预计使用年限">
                    <el-input v-model="form.estimatedAge"></el-input>
                </el-form-item>
                <el-form-item label="折旧截至日期">
                        <el-date-picker type="date" placeholder="选择日期" v-model="form.depreciationDeadline"></el-date-picker>
                </el-form-item>
                <el-form-item label="残值率">
                    <el-input v-model="form.residualRate"></el-input>
                </el-form-item>
                <el-form-item label="存放地点" prop="locationId">
                    <el-select v-model="form.locationId" placeholder="请选择">
                        <el-option v-for="item in locations" :key="item.locationId" :label="item.locationName"
                                   :value="item.locationId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="部门名称" prop="department">
                    <el-select v-model="form.department" placeholder="部门名称" class="handle-select mr10" clearable>
                        <el-option v-for="item in departments" :key="item.deptId" :label="item.deptName"
                                   :value="item.deptName"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item hidden prop="user">
                    <el-input v-model="form.userId" hidden></el-input>
                </el-form-item>
                <el-form-item label="使用人" prop="user">
                    <el-input v-model="form.user"></el-input>
                </el-form-item>
                <el-form-item label="使用状态" prop="status">
                    <el-select v-model="form.status">
                        <el-option key="1" label="闲置" value="1"></el-option>
                        <el-option key="2" label="使用中" value="2"></el-option>
                        <el-option key="3" label="维修中" value="3"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit('form')">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>


    export default {
        data() {
            var validateNumber = (rule, value, callback) => {
                if(this.oldNumber != value){
                    this.$axios.get('/assets/fixedAssets/assetsNumberIsExit',{
                        params:{
                            assetsNumber:value}}
                    ).then((res) => {
                        console.log("错误信息："+res.data.msg)
                        if (res.data.msg === 'null' || res.data.msg === '') {
                            return true;
                        } else {
                            return callback("该规定资产编号已被占用！！！请重新输入");
                        }
                    })
                }else{
                    callback()
                }

            };
            return {
                tableData: [],
                money: 0.0,
                select_department: '',
                select_type: '',
                select_assetsName: '',
                select_assetsNumber: '',
                select_location: '',
                select_status: '',
                select_user: '',
                editVisible: false,
                units: ['台', '个', '把', '张', '件', '条','支','片','副'],
                types: [],
                departments:['java研发','C++研发','人事部','财务部','运维部','其他'],
                locations: [],
                pageSize: 8,
                currentPage: 1,
                form: {
                    assetsId: '',
                    assetsNumber: '',
                    assetsName: '',
                    buyDate: '',
                    typeId: '',
                    unit: '',
                    format:'',
                    price: '',
                    originalPrice: '',
                    estimatedAge: '',
                    depreciationDeadline: '',
                    residualRate: '',
                    residualValue: '',
                    locationId: '',
                    userId: '',
                    user: '',
                    department: '',
                    useMonth: '',
                    depreciationMonth: '',
                    accumulatedDepreciation: '',
                    netWorth: '',
                    status: '',
                    remark: '无'
                },
                idx: -1,
                oldNumber:'',
                oldType:'',
                oldLocation:'',
                oldDate:'',
                oldStatus:'',
                rules: {
                    assetsNumber: [
                        {required: false, message: '请输入资产编号', trigger: 'blur'},
                        { pattern : 'KLZN_YFB.*', message: '必须以KLZN_YFB开头', trigger: 'blur' },
                        {min: 20, max: 20, message: '长度必须为20个字符', trigger: 'blur'},
                        { validator: validateNumber, trigger: 'blur' }
                    ],
                    assetsName: [
                        {required: true, message: '请输入物品名称', trigger: 'blur'},
                        {min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur'}
                    ],
                    typeId: [
                        {required: true, message: '请选择类型', trigger: 'change'}
                    ],
                    format: [
                        {required: true, message: '请输入物品规格', trigger: 'blur'}
                    ],
                    unit: [
                        {required: true, message: '请选择单位', trigger: 'change'}
                    ],
                    buyDate: [
                        {required: true, message: '请选择日期', trigger: 'change'}
                    ],
                    price: [
                        {required: true, message: '请输入物品单价', trigger: 'blur'}
                    ],
                    originalPrice: [
                        {required: true, message: '请输入物品原值', trigger: 'blur'}
                    ],
                    locationId: [
                        {required: true, message: '请选择存放地点', trigger: 'change'}
                    ],
                    user: [
                        {required: true, message: '请输入责任人', trigger: 'blur'}
                    ],
                    department: [
                        {required: true, message: '请选择部门', trigger: 'change'}
                    ],
                }
            }
        },
        created() {

            this.getData();

        },
        mounted(){
            this.getDepartment();
            this.getTypeAndLocation();
        },
        methods: {
            getDepartment(){
                this.$axios.get('/base/dept/selectAll').then((res) => {
                    console.log("部门信息："+res.data.data[0])
                    this.departments = res.data.data;
                })
            },
            getTypeAndLocation(){
                this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation', {
                    headers:
                        {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then((res) => {
                    console.log(res.data.list)
                    this.types = res.data.data.type;
                    this.locations = res.data.data.location;
                })
            },
            //初始化数据
            getData() {
                this.$axios.post('/assets/fixedAssets/account').then((res) => {
                    console.log(res.data.list)
                    this.tableData = res.data.data.list;
                    this.money = res.data.data.amount;
                })
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    assetsId: item.assetsId,
                    assetsName: item.assetsName,
                    assetsNumber: item.assetsNumber,
                    format: item.format,
                    unit: item.unit,
                    typeId: item.type,
                    buyDate: item.buyDate,
                    price: item.price,
                    originalPrice: item.originalPrice,
                    locationId: item.location,
                    userId: item.userId,
                    user: item.user,
                    department: item.department,
                    estimatedAge: item.estimatedAge,
                    residualRate: item.residualRate,
                    depreciationDeadline: item.depreciationDeadline,
                    status: item.status,
                    remark: item.remark,

                }
                this.oldNumber = item.assetsNumber;
                this.oldType = item.type;
                this.oldLocation = item.location;
                this.oldDate =  item.buyDate;
                this.oldStatus =  item.status;
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.oldType === this.form.typeId){
                            this.form.typeId = null;
                        }
                        if(this.oldLocation === this.form.locationId){
                          this.form.locationId = null;
                        }
                        if(this.oldDate == this.form.buyDate){
                            this.form.buyDate = this.oldDate.substr(0,4)+this.oldDate.charAt(5)+this.oldDate.charAt(6)+this.oldDate.charAt(8)+this.oldDate.charAt(9);
                        }
                        if(this.oldStatus == this.form.status){
                           if(this.oldStatus == '闲置'){
                               this.form.status = 1;
                           }else  if(this.oldStatus == '使用中'){
                               this.form.status = 2;
                           }else{
                               this.form.status = 3;
                           }
                        }
                        this.$axios.post('/assets/fixedAssets/updateOneAssetsMess', this.form).then((res) => {
                            this.form.buyDate = this.oldDate;
                            this.$set(this.tableData, this.idx, this.form);
                            this.getData();
                            this.editVisible = false;
                            this.$message.success(res.data.msg);
                        })
                    } else {
                        this.$message.error("提交失败！！");
                        return false;
                    }
                });
            },
            //条件查询
            OnSearch() {
                if(this.select_department==''&&this.select_assetsNumber==''&&this.select_assetsName==''&&this.select_type==''&&this.select_location==''&&this.select_status==''&&this.select_user==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("department", this.select_department);
                params.append("assetsNumber", this.select_assetsNumber);
                params.append("assetsName", this.select_assetsName);
                params.append("type", this.select_type);
                params.append("location", this.select_location);
                params.append("user", this.select_user);
                params.append("status", this.select_status);
                this.$axios.post('/assets/fixedAssets/selectFixedAssetsByCondition', params, {
                    headers:
                        {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                }).then((res) => {
                        this.tableData = res.data.data;
                })
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

    .date-input {
        width: 200px;
        display: inline-block;
    }

    .handle-input {
        width: 200px;
        display: inline-block;
    }

    .read {
        background-color: gray;
        width: 350px;
    }
</style>
