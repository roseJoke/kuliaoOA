<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i> 固定资产新增</el-breadcrumb-item>
                <el-breadcrumb-item>录入表单</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" :rules="rules" label-width="150px">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="固定资产编号" prop="assetsNumber">
                                <el-input v-model="form.assetsNumber" class="handle-input"
                                           clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="固定资产名称" prop="assetsName">
                                <el-input v-model="form.assetsName" class="handle-input" clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="物品类别" prop="typeId">
                                <el-select v-model="form.typeId" class="handle-select" placeholder="请选择类别">
                                    <el-option v-for="item in types" :key="item.typeId" :label="item.typeName"  :value="item.typeId" clearable></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="单位" prop="unit" >
                                <el-select v-model="form.unit" class="handle-select" placeholder="请选择单位">
                                    <el-option v-for="(item,key,index) in units" :key="index" :label="item" :value="item" clearable></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单价（元）" prop="price">
                                <el-input type="number" v-model.number="form.price" class="handle-input" clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="原值（元）" prop="originalPrice">
                                <el-input  type="originalPrice" v-model.number="form.originalPrice" class="handle-input" clearable ></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="购置日期" prop="buyDate">
                                <el-col :span="9">
                                    <el-date-picker type="date" value-format="yyyyMMdd" placeholder="选择日期" v-model="form.buyDate" class="date-input" clearable></el-date-picker>
                                </el-col>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="折旧截至日期" prop="depreciationDeadline" >
                                <el-col :span="9">
                                    <el-date-picker type="date" value-format="yyyyMMdd" placeholder="选择日期" v-model="form.depreciationDeadline" class="date-input" clearable></el-date-picker>
                                </el-col>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="预计使用年限（年）" prop="estimatedAge">
                                <el-input  type="estimatedAge" v-model.number="form.estimatedAge" class="date-input" clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="残值率（%）" prop="residualRate">
                                <el-input  type="residualRate" v-model.number="form.residualRate" class="handle-input" clearable> </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="存放地点" prop="locationId">
                                <el-select v-model="form.locationId" class="handle-select" placeholder="请选择存放地点">
                                    <el-option v-for="item in locations" :key="item.locationId" :label="item.locationName" :value="item.locationId" clearable></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="负责人" prop="user">
                                <el-input v-model="form.user" class="handle-input" clearable></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item  prop="format" label="规格（详情）">
                                <el-input type="textarea" style="width:400px;height:50px;" v-model="form.format" clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="remark">
                                <el-input type="textarea" style="width:400px;height:50px;" v-model="form.remark" class="handle-input" clearable></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item>

                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit('form')" style="position:relative;left: 30%;">表单提交</el-button>
                        <el-button @click="formClear('form')" style="position:relative;left: 35%;">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>

    </div>
</template>

<script>

    export default {

        data: function () {
            var validateNumber = (rule, value, callback) => {
                this.$axios.get('/assets/fixedAssets/assetsNumberIsExit',{
                    params:{
                        assetsNumber:value}}
                ).then((res) => {
                    console.log("错误信息："+res.data.msg)
                    if (res.data.msg === 'null' || res.data.msg === '') {
                        callback();
                    } else {
                       callback("该规定资产编号已被占用！！！请重新输入");
                    }
                })
            };
            return {
                types: [],
                locations: [],
                units: ['台', '个', '把', '张', '件', '条','支','片','副'],
                form: {
                    assetsNumber: '',
                    assetsName: '',
                    typeId: '',
                    unit: '',
                    format: '',
                    buyDate: '',
                    estimatedAge: '',
                    depreciationDeadline: '',
                    originalPrice: '',
                    price: '',
                    residualRate: '',
                    user: '',
                    locationId: '',
                    remark: ''
                },
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
                    ]
                }
            }
        },
        created() {
            this.getTypeAndLocation();
        },
        methods: {
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
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post("/assets/fixedAssets/inStock", this.form,).then(res => {
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
    .handle-select {
        width: 180px;
    }

    .handle-input {
        width: 180px;
    }
    .date-input {
        width: 180px;
        display: inline-block;
    }
</style>
