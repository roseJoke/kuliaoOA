<template>
    <div>
        <router-view v-model="data" ref="form" readonly="true"></router-view>
        <query-step v-model="queryStep" :processId="processId"></query-step>
        <el-button type="primary" @click="submitForm('ruleForm')">发起</el-button>
        <el-button @click="resetForm('ruleForm')">取消</el-button>
    </div>
</template>
<script>
    import HumanDemand from './launch/HumanDemand.vue'
    import queryStep from './launch/approver.vue'

    export default {
        data() {
            return {
                data: {},
                radio3: {},
                processId: '',
                queryStep: []
            }
        },
        components: {
            HumanDemand, queryStep
        },
        created() {
            let that = this;
            this.processId = this.$route.query.processId;
            //this.processId = 'P20180808_000001';
            let params = new URLSearchParams();
            params.append('processId', that.processId);
            that.$axios.post("/workflow/process/queryStep", params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }
            ).then((res) => {
                //在这里拿到流程步骤定义
                console.log('成功拿到后台的步骤数据：' + res.data.data)
            }).catch(error => {
                console.log(error)
            });

        },
        methods: {
            /*  onclick(card) {
                  console.log(card)
                  this.$router.push({path: '/work/launch/' + card});
              },*/
            submitForm(formName) { //提交
                let that = this;
                let form = this.$refs.form.$refs[formName];
                form.validate((valid) => {
                    if (valid) {
                        console.log(form);
                        this.$refs.form.ruleForm.publish_time = this.getNowTime();//获取当前时间作为发起时间
                        this.$message.success('成功');
                        console.log(this.queryStep);
                        console.log(this.$refs.form.ruleForm)
                        this.$axios.post("/workflow/activate/initiate", {
                            busi: that.$refs.form.ruleForm,
                            handler: that.queryStep,
                            processId: this.processId,
                            activityUserId: localStorage.getItem('empNo')
                        }).then(res => {
                            //this.search();
                            console.log(res);
                            this.$message.success(res.data.msg);
                        });
                    } else {
                        this.$message.error('请填写完整信息');
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
            }
        },
        resetForm(formName) { //清空
            this.$refs.form.$refs[formName].resetFields();
        },

    }
</script>

<style scoped>

</style>
