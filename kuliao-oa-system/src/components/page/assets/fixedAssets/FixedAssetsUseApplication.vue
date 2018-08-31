<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i> 固定资产领用申请</el-breadcrumb-item>
                <el-breadcrumb-item>申请表单</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" :rules="rules" label-width="150px">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="物品类别" prop="type">
                                <el-select v-model="form.type" class="handle-select" placeholder="请选择类别">
                                    <el-option v-for="item in types" :key="item.typeId" :label="item.typeName" :value="item.typeName" clearable></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="物品名称" prop="assetsName" >
                                <el-select v-model="form.assetsName" class="handle-select" placeholder="请选择单位">
                                    <el-option v-for="(item,key,index) in names" :key="index" :label="item" :value="item" clearable></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item  prop="reason" label="申请事由">
                                <el-input type="textarea" style="width:400px;height:50px;" v-model="form.reason" clearable></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="remark">
                                <el-input type="textarea" style="width:400px;height:50px;" v-model="form.remark" class="handle-input" clearable></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit('form')" style="position:relative;left: 26%;">表单提交</el-button>
                        <el-button @click="formClear('form')" style="position:relative;left: 26%;">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>

    </div>
</template>

<script>
    import ElOption from "../../../../../node_modules/element-ui/packages/select/src/option.vue";
    import ElRow from "element-ui/packages/row/src/row";
    import ElCol from "element-ui/packages/col/src/col";

    export default {
        components: {
            ElCol,
            ElRow,
            ElOption
        },
        data: function () {
            return {
                types: [],
                names: [],
                form: {
                    assetsName: '',
                    type: '',
                    reason: '',
                    remark: ''
                },
                rules: {
                    assetsName: [
                        {required: true, message: '请输入物品名称', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择类型', trigger: 'change'}
                    ],
                    reason: [
                        {required: true, message: '请输入申请事由', trigger: 'blur'}
                    ]
                }
            }
        },
        created() {
            this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation').then((res) => {
                console.log("数据："+res.data.data.type)
                this.types = res.data.data.type;
            })
            this.$axios.post('/assets/fixedAssets/findAllAssetsNames').then((res) => {
                console.log("数据："+res.data.data.names)
                this.names = res.data.data.names;
            })
        },
        methods: {
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let params = new URLSearchParams();
                        params.append('assetsNumber', this.form.assetsNumber);
                        params.append('assetsName', this.form.assetsName);
                        params.append('type', this.form.type)
                        params.append('unit', this.form.unit);
                        params.append('format', this.form.format);
                        params.append('price', this.form.price);
                        params.append('originalPrice', this.form.originalPrice);
                        params.append('buyDate', this.form.buyDate);
                        params.append('estimatedAge', this.form.estimatedAge);
                        params.append('depreciationDeadline', this.form.depreciationDeadline);
                        params.append('residualRate', this.form.residualRate);
                        params.append('inStockPerson', this.form.user);
                        params.append('location', this.form.location);
                        params.append('remark', this.form.remark);
                        this.$axios.post("/assets/fixedAssets/inStock", params, {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).then(res => {
                            if(res.data.code=='000000'){
                                this.$message.success(res.data.msg);
                                this.formClear('form');
                            }else{
                                this.$message.error(res.data.data.msg);
                            }

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
