<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>办公用品台账</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                搜索条件：
                <el-select v-model="select_type" placeholder="物品类别" class="handle-select mr10" clearable>
                    <el-option v-for="(item,key,index) in types" :key="index" :label="item" v-bind:value="item"></el-option>
                </el-select>
                <el-date-picker  v-model="select_buyDate" value-format="yyyyMMdd" placeholder="购置日期" clearable class="date-input"></el-date-picker>
                <el-input  v-model="select_goodsName" placeholder="物品名称" clearable class="date-input"></el-input>
                <el-select v-model="select_location" placeholder="存放地点" clearable class="handle-select mr10">
                    <el-option v-for="item in locations" :key="item.locationId" :label="item.locationName" :value="item.locationName"></el-option>
                </el-select>
                <el-select v-model="select_status" placeholder="使用状态" clearable class="handle-select mr10">
                    <el-option key="1" label="已使用" value="1"></el-option>
                    <el-option key="0" label="闲置" value="0"></el-option>
                </el-select>
                <el-input v-model="select_manager" placeholder="负责人" clearable class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border  ref="multipleTable">
                <el-table-column label="序号" sortable width="80">
                    <template slot-scope="scope">
                        {{scope.$index+1}}
                    </template>
                </el-table-column>
                <el-table-column prop="goodsId" v-if="goodsId>0"width="80">
                </el-table-column>
                <el-table-column prop="goodsName" label="物品名称" width="120">
                </el-table-column>
                <el-table-column prop="type" label="物品类别" width="100">
                </el-table-column>
                <el-table-column prop="format" label="物品规格" width="150">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="80">
                </el-table-column>
                <el-table-column prop="buyDate" label="购置日期" width="150">
                </el-table-column>
                <el-table-column prop="price" label="单价(元)" width="80">
                </el-table-column>
                <el-table-column prop="originalPrice" label="原价(元)" width="80">
                </el-table-column>
                <el-table-column prop="amount" label="总额(元)" width="100">
                </el-table-column>
                <el-table-column prop="status" label="使用状态" width="100">
                </el-table-column>
                <el-table-column prop="location" label="存放地点" width="150">
                </el-table-column>
                <el-table-column prop="manager" label="责任人" width="100">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="280">
                </el-table-column>
                <el-table-column label="操作" width="140">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
        <el-dialog title="办公用品编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="办公用品id" hidden="hidden">
                    <el-input v-model="form.goodsId" :disabled="true" ></el-input>
                </el-form-item>
                <el-form-item label="办公用品名称" prop="goodsName">
                    <el-input v-model="form.goodsName" ></el-input>
                </el-form-item>
                <el-form-item label="类别"  prop="type">
                    <el-select v-model="form.type" placeholder="请选择类别">
                        <el-option v-for="item in types" :key="index" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="规格" prop="format">
                    <el-input type="textarea" v-model="form.format"  ></el-input>
                </el-form-item>
                <el-form-item label="单位"  prop="unit">
                    <el-select v-model="form.unit" placeholder="请选择单位">
                        <el-option v-for="(item,key,index) in units" :key="index" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input v-model="form.price"  ></el-input>
                </el-form-item>
                <el-form-item label="原值" prop="originalPrice">
                    <el-input v-model="form.originalPrice" ></el-input>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-input v-model="form.count" ></el-input>
                </el-form-item>
                <el-form-item label="购置日期" prop="buyDate">
                    <el-date-picker type="date" placeholder="选择日期" value-format="yyyyMMdd" v-model="form.buyDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="存放地点"  prop="location">
                    <el-select v-model="form.location" placeholder="请选择存放地点">
                        <el-option v-for="(item,key,index) in locations" :key="index" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="负责人" prop="manager">
                    <el-input v-model="form.manager"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" class="handle-input"></el-input>
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

    import ElInput from "../../../../../node_modules/element-ui/packages/input/src/input.vue";
    import ElIcon from "../../../../../node_modules/element-ui/packages/icon/src/icon.vue";

    export default {
        components: {
            ElIcon,
            ElInput},
        data() {
            return {
                types:['办公文具','通用工具','专用工具','替换件','周转材料','管理用具','劳动用品','其他'],
                units: ['支','本','根','件','把'],
                locations:[],
                tableData: [],
                money:0.0,
                multipleSelection: [],
                select_buyDate: '',
                select_goodsName: '',
                select_type: '',
                select_location: '',
                select_status: '',
                select_manager: '',
                del_list: [],
                editVisible: false,
                pageSize:8,
                currentPage:1,
                form: {
                    goodsId:'',
                    goodsName: '',
                    buyDate: '',
                    type: '',
                    unit: '',
                    format:'',
                    price:'',
                    originalPrice:'',
                    amount:'',
                    location: '库房',
                    manager: '人事',
                    status: '0',
                    remark: '无'
                },
                oldDate:'',
                idx: -1,
                rules: {
                    goodsName: [
                        {required: true, message: '请输入物品名称', trigger: 'blur'},
                        {min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择类型', trigger: 'change'}
                    ],
                    format: [
                        {required: true, message: '请输入物品规格', trigger: 'blur'}
                    ],
                    unit: [
                        {required: true, message: '请选择单位', trigger: 'change'}
                    ],
                    count: [
                        {required: true, message: '请输数量', trigger: 'blur'},
                        { type: 'number', message: '数量必须为数字值'}
                    ],
                    price: [
                        {required: true, message: '请输入物品单价', trigger: 'blur'},
                        { type: 'number', message: '单价必须为数字值'}
                    ],
                    originalPrice: [
                        {required: true, message: '请输入物品原值', trigger: 'blur'},
                        { type: 'number', message: '原值必须为数字值'}
                    ],
                    location: [
                        {required: true, message: '请选择存放地点', trigger: 'change'}
                    ],
                    manager: [
                        {required: true, message: '请输入管理人', trigger: 'blur'}
                    ]
                }
            }
        },
        created() {
            this.getData();
        },
        mounted(){
            this.getLocation();
        },
        methods: {
            getLocation(){
                this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation', {
                    headers:
                        {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then((res) => {
                    console.log(res.data.list)
                    this.locations = res.data.data.location;
                })
            },
            // 获取初始化数据
            getData() {
                this.$axios.post('/assets/officeGoods/account').then((res) => {
                    console.log(res.data.list)
                    this.tableData = res.data.data.list;
                    this.money = res.data.data.money;
                })
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    goodsId: item.goodsId,
                    format: item.format,
                    goodsName: item.goodsName,
                    type: item.type,
                    unit: item.unit,
                    price: item.price,
                    count: item.count,
                    buyDate: item.buyDate,
                    location: item.location,
                    manager: item.manager,
                    status: item.status,
                    remark: item.remark,
                    originalPrice: item.originalPrice
                }
                this.oldDate = item.buyDate;
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.oldDate == this.form.buyDate){
                                this.form.buyDate = this.oldDate.substr(0,4)+this.oldDate.charAt(5)+this.oldDate.charAt(6)+this.oldDate.charAt(8)+this.oldDate.charAt(9);
                        }
                        this.$axios.post('/assets/officeGoods/updateGoods',this.form).then((res) => {
                            if(this.oldDate == this.form.buyDate){
                                this.form.buyDate = this.oldDate;
                            }
                            this.$set(this.tableData, this.idx, this.form);
                            this.$message.success(res.data.msg);
                        })
                    } else {
                        this.$message.error("修改失败！！");
                        return false;
                    }
                    this.editVisible = false;
                });

            },
            OnSearch() {
                if(this.select_assetsName==''&&this.select_buyDate==''&&this.select_type==''&&this.select_location==''&&this.select_manager==''&&this.select_status==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("goodsName", this.select_goodsName);
                params.append("buyDate", this.select_buyDate);
                params.append("type", this.select_type);
                params.append("location", this.select_location);
                params.append("manager", this.select_manager);
                params.append("status", this.select_status);
                this.$axios.post('/assets/officeGoods/selectOfficeGoodsByCondition', params, {
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
    .date-input {
        width: 200px;
        display: inline-block;
    }
    .handle-input {
        width: 200px;
        display: inline-block;
    }
</style>
