<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 培训申请</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-form :disabled="readonly" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="200px" class="demo-ruleForm">
                <el-row>
                    <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="4">
                    <el-form-item label="培训讲师" prop="trainer">
                        <el-input class="input-width disabled-style" v-model="ruleForm.trainer"></el-input>
                    </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="24" :md="24" :lg="8" :xl="5">
                    <el-form-item label="培训地点" prop="trainingPlace">
                        <el-select v-model="ruleForm.trainingPlace"
                                   class="input-select-width"
                                   placeholder="请选会议室" :inline-message="true">
                            <el-option v-for="item in options" :key="item.key" :label="item.value"
                                       :value="item.key"></el-option>
                        </el-select>
                    </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
                    <el-form-item label="培训时间" prop="trainingTime">
                        <el-row type="flex">
                            <el-date-picker
                                v-model="date"
                                value-format="yyyyMMdd"
                                type="date"
                                :editable="false"
                                placeholder="选择日期">
                            </el-date-picker>
                            <span>&nbsp-&nbsp</span>
                            <el-time-select
                                v-model="time"
                                :picker-options="{
                                start: '09:00',
                                step: '00:15',
                                end: '18:00'
                                }"
                                value-format="hh:mm"
                                placeholder="选择时间">
                            </el-time-select>
                        </el-row>
                    </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="培训内容" prop="trainingContent">
                    <el-input type="textarea" class="textarea-style" v-model="ruleForm.trainingContent" rows="5"></el-input>
                </el-form-item>

                <el-form-item label="培训对象" prop="trainees">
                    <el-input  type="textarea" class="textarea-style" v-model="ruleForm.trainees" rows="5"></el-input>
                </el-form-item>
            </el-form>


        </div>
    </div>
</template>

<script>
    export default {
        props:{
            readonly: Boolean
        },

        data() {
            return {
                processId: '',
                date: '',
                time: '',
                ruleForm: {},

                //部门下拉框
                options: [],
                rules: {
                    trainer: [
                        {required: true, message: '培训讲师不能为空', trigger: 'blur'}
                    ],
                    trainingPlace: [
                        {required: true, message: '会议室地点不能为空', trigger: 'blur'}
                    ],
                    date: [
                        {required: true, message: '请选择日期', trigger: 'blur'}
                    ],
                    time: [
                        {required: true, message: '请选择时间', trigger: 'blur'}
                    ],
                    trainingContent: [
                        {required: true, message: '培训内容不能为空', trigger: 'blur'}
                    ],
                    trainees: [
                        {required: true, message: '培训对象不能为空', trigger: 'blur'}
                    ]
                }
            };
        },
        created() {
            this.search();
        },
        methods: {

            search() { //查询状态为未使用的会议室
                let that = this;
                let param = {};
                that.$axios.post('/workflow/meeting/selectMeetings', param).then((res) => {
                    let datas = [];
                    datas =res.data.data;
                    for (let i = 0; i < datas.length; i++) {
                        if(datas[i].meetingState == '0') {
                            let obj = {};
                            obj.key = datas[i].meetingId;
                            obj.value = datas[i].meetingName;
                            that.options.push(obj);
                        }
                    }
                })
            },
            submitForm(formName) { //提交
                let that = this;
                that.$refs[formName].validate((valid) => {

                    console.log('时间 '+ this.date+' '+this.time);
                    if (valid) {
                        that.ruleForm.trainingTime = this.date+' '+this.time;
                        that.$axios.post("/hr/insertTrain", that.ruleForm).then(res => {
                            that.$message.success(res.data.msg);
                        });

                    } else {
                        that.$message.error('请填写完整信息');
                        return false;
                    }
                });
            },
            resetForm(formName) { //清空
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>

    .disabled-style {
        -webkit-text-fill-color: #606266;
    }
    .input-width {
        width: 120px;
    }

    .input-day-width {
        width: 130px;
    }

    .textarea-style {
        width: 60%;
    }
    .input-select-width {
        width: 160px;
    }

</style>
