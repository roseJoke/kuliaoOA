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
                <el-form ref="ruleForm" :model="ruleForm" label-width="80px" :rules="rules">
                    <el-form-item label="员工名称" style="display: none" prop="userId">
                        <el-input v-model="ruleForm.userId" style="width: 300px;"></el-input>
                    </el-form-item>
                    <el-form-item label="员工名称" prop="userName">
                        <el-input v-model="ruleForm.userName" style="width: 300px" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="所属部门" prop="department">
                        <el-input v-model="ruleForm.department" style="width: 300px" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="类型" prop="assetsType">
                        <el-select @change="getAssetName" v-model="ruleForm.assetsType" placeholder="请选择">
                            <el-option
                                v-for="item in options"
                                :key="item"
                                :label="item"
                                :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="资产名称" prop="assetsName">
                        <el-select :disabled="flag" v-model="ruleForm.assetsName" placeholder="请选择">
                            <el-option
                                v-for="item in assetList"
                                :key="item"
                                :label="item"
                                :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="领取理由" prop="applicationReason">
                        <el-input type="textarea" rows="5" v-model="ruleForm.applicationReason"></el-input>
                    </el-form-item>
                    <el-form-item label="附加信息" prop="remark">
                        <el-input type="textarea" rows="5" v-model="ruleForm.remark"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit('ruleForm')">发起申请</el-button>
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
        data: function () {
            return {
                rules: {
                    assetsType: [
                        {required: true, message: '请选择资产类型', trigger: 'change'}
                    ],
                    assetsName: [
                        {
                            required: true, message: '请选择资产名称', trigger: 'change'
                        }
                    ],
                    applicationReason: [
                        {
                            required: true, message: '请输入申请理由', trigger: 'blur'
                        }
                    ]
                },
                assetList: [],
                options: [],
                ruleForm: {
                    department:
                        "",
                    userId:
                        "",
                    userName: ''
                },
                flag: true
            }
        },
        created() {
            this.getTypes();
            this.getEmp();
        },
        methods: {

            getEmp() {

                let that = this;
                let empId = localStorage.getItem('empNo');
                let params = new URLSearchParams();
                params.append('empNo', empId);

                that.$axios.post('/base/user/query', params).then((res) => {
                    console.log('进来了');
                    that.ruleForm.userId = res.data.data[0].empNo;
                    that.ruleForm.userName = res.data.data[0].username;
                    that.ruleForm.department = res.data.data[0].deptName;
                    console.log(that.ruleForm)

                });

            },

            getTypes() {//获取资产类型
                let that = this;
                that.$axios.post('/assets/fixedAssets/selectAllTypes').then((res) => {
                    // that.assetList = '';
                    that.options = res.data.data;
                });

            },
            getAssetName() {//获取资产名称
                if (this.ruleForm.assetsType !== undefined || this.ruleForm.assetsType !== '') {
                    let param = new URLSearchParams();
                    param.append('typeName', this.ruleForm.assetsType);
                    this.$axios.post('/assets/fixedAssets/selectAllAssetsNameByType', param, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    }).then((res) => {
                        this.assetList = res.data.data;
                        this.flag = false;

                    });
                }
            },
            onSubmit(formName) {
                let that = this;
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        that.ruleForm.applicationTime = this.getNowTime();
                        that.ruleForm.flowId = 'aaaa';
                        that.$axios.post("/assets/fixedAssets/addOneNewUseApplication", that.ruleForm).then(res => {
                            that.$message.success(res.data.msg);
                        });

                    } else {
                        that.$message.error('请填写完整信息');
                        return false;
                    }
                });
            },
            getNowTime() {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                var currentdate = year + month + strDate;
                return currentdate;
            },
            //取消申请就返回上一级
            back() {
                let that = this;
                this.$router.push({path: '/leaveBill_newApproval'});
            },
            //根据类型取到相应类型的固定资产列表
            changeAssginName(value) {

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
