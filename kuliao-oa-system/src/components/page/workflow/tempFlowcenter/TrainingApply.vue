<template>
    <div>

        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 培训申请</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div class="container">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="200px" class="demo-ruleForm">
                <el-row type="flex">

                    <el-form-item label="培训讲师" prop="trainer">
                        <el-input class="input-width disabled-style" v-model="ruleForm.trainer"></el-input>
                    </el-form-item>

                    <el-form-item label="培训地点" prop="trainingPlace">
                        <el-select v-model="ruleForm.trainingPlace"
                                   placeholder="请选会议室" :inline-message="true">
                            <el-option v-for="item in options" :key="item.key" :label="item.value"
                                       :value="item.key"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="培训时间" prop="trainingTime">
                        <el-row type="flex">
                        <el-date-picker
                            v-model="date"
                            value-format="yyyy-MM-dd"
                            type="date"
                            :editable="false"
                            placeholder="选择日期"
                            class="input-day-width">
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
                            placeholder="选择时间"
                            class="input-day-width">
                        </el-time-select>
                        </el-row>
                    </el-form-item>
                </el-row>

                <el-form-item label="培训内容" prop="trainingContent">
                    <el-input type="textarea" class="textarea-style" v-model="ruleForm.trainingContent" rows="5"></el-input>
                </el-form-item>

                <el-form-item label="培训对象" prop="trainees">
                    <el-input  type="textarea" class="textarea-style" v-model="ruleForm.trainees" rows="5"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">发起</el-button>
                    <el-button @click="resetForm('ruleForm')">取消</el-button>
                </el-form-item>
            </el-form>


        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                processId: '',
                date: '',
                time: '',
                ruleForm: {
                },

                //部门下拉框
                options: [],
                rules: {
                    trainer: [
                        {required: true, message: '培训讲师不能为空', trigger: 'change'}
                    ],
                    trainingPlace: [
                        {required: true, message: '会议室地点不能为空', trigger: 'change'}
                    ],
                    date: [
                        {required: true, message: '请选择日期', trigger: 'change'}
                    ],
                    time: [
                        {required: true, message: '请选择时间', trigger: 'change'}
                    ],
                    trainingContent: [
                        {required: true, message: '培训内容不能为空', trigger: 'change'}
                    ],
                    trainees: [
                        {required: true, message: '培训对象不能为空', trigger: 'change'}
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
                this.$refs[formName].validate((valid) => {

                    console.log('时间 '+ this.date+' '+this.time);
                    if (valid) {
                        this.ruleForm.trainingTime = this.date+' '+this.time;
                        this.$message.success('成功');
                        // this.$axios.post("/workflow/meeting/modifyMeeting", ruleForm).then(res => {
                        //     this.search();
                        //     this.$message.success(res.data.msg);
                        // });

                    } else {
                        this.$message.error('请填写完整信息');
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

</style>
