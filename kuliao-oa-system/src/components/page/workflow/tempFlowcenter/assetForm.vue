<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-date"></i>资产领用</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" label-width="80px" :rules="rules">
                    <el-form-item label="员工名称" prop="name">
                        <el-input v-model="form.name" style="width: 300px" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="所在部门" prop="dept">
                        <el-select v-model="form.dept" placeholder="请选择">
                            <el-option key="search" label="研发" value="search"></el-option>
                            <el-option key="test" label="测试" value="test"></el-option>
                            <el-option key="administration" label="行政" value="administration"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="资产类型" prop="options">
                        <el-cascader :options="options" v-model="form.options" @change="changeAssginName('form.options')"></el-cascader>
                    </el-form-item>
                    <el-form-item label="资产名称" prop="noAsset">
                        <el-select v-model="noAsset" placeholder="请选择">
                            <el-option
                                v-for="item in assetList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="领取理由" prop="desc">
                        <el-input type="textarea" rows="5" v-model="form.desc"></el-input>
                    </el-form-item>
                    <el-form-item label="备注" prop="beizhu">
                        <el-input v-model="form.beizhu"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit('form')">发起申请</el-button>
                        <el-button @click="back">取消申请</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "asset-form",
        data: function(){
            return {
                rules:{
                    dept:[
                         { required: true, message: '请选择部门名称', trigger: 'change' },
                    ],
                    options:[
                        {required: true, message: '请选择资产类型名称', trigger: 'change'}
                    ],
                    noAsset:[
                        {
                            required: true, message: '请选择资产名称', trigger: 'change'
                        }
                    ],
                    desc:[
                        {
                            required: true, message: '请输入申请理由', trigger: 'blur'
                        }
                    ]
                },
                noAsset:'',
                assetList:[
                    {
                        value: 'dell',
                        label: '台式机',
                    },
                    {
                        value: 'notebook',
                        label: '戴尔',
                    }, {
                        value: 'asus',
                        label: '华硕',
                    }, {
                        value: 'other',
                        label: '其他',
                    }
                ],
                options:[
                    {
                        value: 'desktop',
                        label: '台式机',
                    },
                    {
                        value: 'notebook',
                        label: '笔记本',
                    }, {
                        value: 'deskandchair',
                        label: '桌椅',
                    }, {
                        value: 'other',
                        label: '其他',
                    }
                ],
                form: {
                    id:localStorage.getItem('empNO'),
                    name:'',
                    dept: '',
                    beizhu:'',
                    delivery: true,
                    type: ['步步高'],
                    resource: '小天才',
                    desc: '',
                    options: []
                }
            }
        },
        methods: {
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$message.success('提交成功！');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //取消申请就返回上一级
            back(){
                let that = this;
                this.$router.push({path:'/leaveBill_newApproval'});
            },
            //根据类型取到相应类型的固定资产列表
            changeAssginName(value){

            }
        },
        deactivated() {
            // this.$destroy()
            //  console.log("没了")
        },
    }
</script>

<style scoped>

</style>
