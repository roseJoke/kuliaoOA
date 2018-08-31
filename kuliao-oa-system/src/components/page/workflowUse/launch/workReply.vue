
<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 审批信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <br/>
        <div class="container">
            <div class="form-box">
                <div class="crumbs">
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item><i class="el-icon-tickets"></i> 审批</el-breadcrumb-item>
                    </el-breadcrumb>
                    <router-view ref="form" :flag="true"></router-view>
                </div>
                <br>
                <hr>
                <br>
                <!--审批-->
                <el-form ref="opinion" :model="opinion" label-width="100px">
                    <el-form-item label="是否通过">
                        <el-radio-group v-model="opinion.flag">
                            <el-radio v-for="item in opinionRedio" :key="item.key" :label="item.key">{{item.name}}</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="审批意见">
                        <el-input class="textarea-width" placeholder="审批意见" type="textarea" rows="5" v-model="opinion.opinion"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :round="true" @click="onSubmit">提交</el-button>
                        <el-button type="primary" :round="true" @click="cancel">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        data: function () {
            return {
                processId:null,
                instanceId:null,
                opinion: {
                    flag: true,
                    opinion: ''
                },
                opinionRedio: [
                    {key: true, name:'同意'}, {key: false, name:'拒绝'}
                ]
            }
        },
        components: {
           // "selectNextApprover": selectNextApprover
        },

        mounted(){
            let that = this;
            this.instanceId = this.$route.query.instanceId;
            this.processId = this.$route.query.processId;
            console.log(that)
            let params = new URLSearchParams();
            params.append('instanceId',this.instanceId);
               // ?instanceId=
            this.$axios.post("/workflow/activate/queryByInstanceId",params).then(res=>{
                console.log(res);
            })
            this.$axios.post("/workflow/definition/query",{processId:this.processId}).then(res=>{
                //console.log(res);
                console.log(res.data.data)
                that.$router.push("/work/reply"+res.data.data.relationUrl)
            })

        },
        methods: {
            onSubmit() {
                this.$router.push({path:'/leaveBill_approvalDone'})
                this.$message.success('提交成功');
            },
            cancel() {
                this.$router.push({path:'/leaveBill_forMyApproval'});
                this.$message.success('取消成功');
            }
        }
    }
</script>
<style scoped>
    .input-width {
        width: 120px;
    }

    .input-day-width {
        width: 60px;
    }

    .date-width {
        width: 390px;
    }

    .option-width {
        width: 120px;
    }

    .demo-div-width {
        width: 1200px;
        margin: auto;
    }
    .crumbs {
        margin-bottom: 3px;
    }
</style>

