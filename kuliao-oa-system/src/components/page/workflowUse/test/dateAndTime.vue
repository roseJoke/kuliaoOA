<template>
    <div>
        <el-row v-if="msg">
            <el-col :xs="24" :sm="24" :md="24" :lg="6" :xl="4">
                <div class="block">
                    <span class="demonstration font-color">请假日期</span>
                    <el-date-picker
                        v-model="value1"
                        value-format="yyyyMMdd"
                        type="date"
                        @change="addDate();modify()"
                        :editable="false"
                        placeholder="选择日期"
                        class="date-width"
                        :picker-options="pickerOptions">
                    </el-date-picker>
                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="{span: 15, offset: 0}" :lg="14" :xl="14">
                <span class="demonstration font-color">时间段&nbsp</span>
                <template>
                    <el-time-select
                        placeholder="起始时间"
                        v-model="startTime"
                        @change="addTime"
                        :disabled="flag"
                        :picker-options="{
                            start: '09:00',
                            step: '00:30',
                            end: '18:00'
                                        }"
                        class="date-width">
                    </el-time-select>
                    <span> ~ </span>
                    <el-time-select
                        placeholder="结束时间"
                        v-model="endTime"
                        :disabled="flag"
                        @change="addTime"
                        :picker-options="{
                            start: '09:00',
                            step: '00:30',
                            end: '18:00',
                            minTime: startTime
                         }"
                        class="date-width">
                    </el-time-select>
                </template>
            </el-col>
        </el-row>
        <br>
        <el-row>
            <el-col class="table-padding">
                <el-table
                    :data="askForLeaveTime"
                    :span-method="arraySpanMethod"
                    border
                    style="width: 100%">
                    <el-table-column
                        label="日期">
                        <template slot-scope="scope">
                            <i class="el-icon-time"></i>
                            <span style="margin-left: 10px">{{ scope.row.date }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        label="时间">
                        <template slot-scope="scope">
                            <i class="el-icon-time"></i>
                            <span style="margin-left: 10px">{{ scope.row.time }}</span>
                        </template>
                    </el-table-column>

                    <el-table-column
                        prop="name"
                        label="工时"
                        align="center">
                        <template slot-scope="scope">
                            <el-input :disabled="!msg" @change="checkManHour(scope.row)" size="small" v-model="scope.row.manHour"  placeholder="请输入工时"></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column v-if="msg" label="操作">
                        <template slot-scope="scope">
                            <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, askForLeaveTime)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
        </el-row>
        <br>
    </div>
</template>
<script>
    export default {
        inheritAttrs: false,
        model: {
            prop: 'askForLeaveTime',
            event: 'synchData'
        },
        props: {
            askForLeaveTime: Array,
            msg: Boolean
        },
        data() {
            return {
                pickerOptions: {
                    disabledDate(time) {
                        return time.getTime() < Date.now();
                    }
                },
                index: '', //插入tableDate中最新日期的索引
                flag: true, //日期为空的时候，时间不可点击
                value1: '', //date日期
                startTime: '',
                endTime: '',
            }
        },
        methods: {

            sendDataToParent() {//自定义组件数据同步
                this.$emit('synchData', this.askForLeaveTime);
            },

            handleDelete(index, row) {
                if(index === this.index) {
                    this.index = '';
                }
                row.splice(index, 1);
                this.value1 = '';
                this.startTime = '';
                this. endTime = '';
                this.flag = true;
                this.sendDataToParent(); //自定义组件数据同步
            },
            arraySpanMethod({row, column, rowIndex, columnIndex}) { //
                if (rowIndex === this.askForLeaveTime.length) {
                    if (columnIndex === 0) {
                        return [1, 3];
                    }
                }
            },
            modify() { //当直接点击日期清空clearable
                if(this.value1 ==='' && this.index !== '' && this.askForLeaveTime[this.index].manHour !== undefined && this.askForLeaveTime[this.index].manHour !== '') {
                    this.index = '';
                    this.sendDataToParent(); //自定义组件数据同步
                }
            },
            addDate() {
                let that = this;
                if(that.index !== '') {
                    that.$message.warning('请填写工时');
                    that.value1 = '';
                    that.flag = true;
                    return;
                }
                if (that.value1 != null) {
                    let obj = {
                        date: '',
                        time: ''
                    };
                    obj.date = that.value1;
                    this.askForLeaveTime.push(obj);
                    this.sendDataToParent(); //自定义组件数据同步

                    for (let i = 0; i < that.askForLeaveTime.length; i++) {
                        if (that.value1 === that.askForLeaveTime[i].date) {
                            that.index = i;
                        }
                    }
                    that.flag = false;
                }
            },
            addTime() {
                let that = this;
                // console.log('测试 '+that.manHour);
                if (that.value1 !== '' && that.endTime !== '' && that.startTime !== '') {
                    let time = that.startTime + ' ~ ' + that.endTime;
                    that.askForLeaveTime[that.index]['time'] = time;
                    this.sendDataToParent(); //自定义组件数据同步
                    that.value1 = '';
                    that.startTime = '';
                    that.endTime = '';
                    that.flag = true;
                }
            },
            checkManHour(row) {
                if(row.manHour !== undefined || row.manHour !== '') {
                    this.index = '';
                    this.sendDataToParent(); //自定义组件数据同步
                };
            },
        }
    }
</script>

<style scoped>
    .date-width {
        width: 140px;
    }
    .font-color {
        color: #606266
    }
    .table-padding {
        padding-left: 60px;
        width: 55%;
    }
</style>

