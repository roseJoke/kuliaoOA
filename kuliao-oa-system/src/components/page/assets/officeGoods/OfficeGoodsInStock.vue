<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i>办公用品新增</el-breadcrumb-item>
                <el-breadcrumb-item>录入表单</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" :rules="rules" label-width="150px">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="办公用品名称"  prop="goodsName">
                                <el-input v-model="form.goodsName" class="handle-input"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单价"  prop="price">
                                <el-input type="price" v-model.number="form.price" class="handle-input"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="原值"  prop="originalPrice">
                                <el-input type="originalPrice" v-model.number="form.originalPrice" class="handle-input"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="物品类别"  prop="type">
                                <el-select v-model="form.type" placeholder="请选择类别">
                                    <el-option v-for="item in types" :key="index" :label="item" :value="item"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单位"  prop="unit">
                                <el-select v-model="form.unit" placeholder="请选择单位">
                                    <el-option v-for="(item,key,index) in units" :key="index" :label="item" :value="item"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="购置日期"  prop="buyDate">
                                <el-col :span="9">
                                    <el-date-picker type="date" placeholder="选择日期" format="yyyyMMdd" value-format="yyyyMMdd" v-model="form.buyDate" class="date-input"></el-date-picker>
                                </el-col>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="数量"  prop="count">
                                <el-input type="count" v-model.number="form.count" class="handle-input"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="存放地点"  prop="location">
                                <el-select v-model="form.location" placeholder="请选择存放地点">
                                    <el-option v-for="item in locations" :key="item.locationId" :label="item.locationName" :value="item.locationName"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="负责人" prop="manager">
                                <el-input v-model="form.manager" class="handle-input"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="规格"  prop="format">
                                <el-input type="textarea" v-model="form.format" style="width: 350px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="remark">
                                <el-input type="textarea" v-model="form.remark" style="width:350px;"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit('form')" style="position:relative;left: 30%;">提交</el-button>
                        <el-button @click="formClear('form')" style="position:relative;left: 35%;">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        data: function(){
            return {
                types:['办公文具','通用工具','专用工具','替换件','周转材料','管理用具','劳动用品','其他'],
                units: ['支','本','根','件','把','盒'],
                locations:[],
                form: {
                    goodsName: '',
                    type: '',
                    format: '',
                    buyDate: '',
                    count:'',
                    originalPrice: '',
                    price: '',
                    unit:'',
                    manager:'',
                    location:''
                },
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
                    buyDate: [
                        {required: true, message: '请选择日期', trigger: 'change'}
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
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                this.$axios.post("/assets/officeGoods/inStock",this.form).then(res => {
                        this.$message.success(res.data.msg);
                        this.formClear('form');
                });
                    } else {
                        this.$message.error("提交失败！！");
                        return false;
                    }
                });
            },
            formClear(form) {
                this.$refs[form].resetFields();
            }
        }

    }
</script>
<style scoped>

    .handle-input {
        width: 150px;
    }
    .date-input {
        width: 200px;
        display: inline-block;
    }
</style>
