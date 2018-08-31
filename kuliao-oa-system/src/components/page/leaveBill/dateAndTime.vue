<template>
    <div>
        <el-row v-if="msg">
            <el-col :span="6">
                <div class="block">
                    <span class="demonstration font-color">请假日期</span>
                    <el-date-picker
                        v-model="value"
                        value-format="yyyy-MM-dd"
                        type="date"
                        @change="addDate"
                        :editable="false"
                        placeholder="选择日期"
                        class="date-width">
                    </el-date-picker>
                </div>
            </el-col>
            <el-col :span="14">
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
                    :data="tableData"
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
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, tableData)">删除
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
        props:['msg'],
        data() {
            return {
                index: null, //插入tableDate中最新日期的索引
                flag: true, //日期为空的时候，时间不可点击
                value: '', //date日期
                startTime: '',
                endTime: '',
                tableData: [],
            }
        },
        methods: {
            handleDelete(index, row) {
                row.splice(index, 1);
                this.value = '';
                this.startTime = '';
                this. endTime = '';
                this.flag = true;
            },
            arraySpanMethod({row, column, rowIndex, columnIndex}) { //
                if (rowIndex === this.tableData.length) {
                    if (columnIndex === 0) {
                        return [1, 3];
                    }
                }
            },

            addDate() {
                let that = this;
                if (that.value != null) {
                    let obj = {
                        date: '',
                        time: ''
                    };
                    obj.date = that.value;
                    this.tableData.push(obj);

                    for (let i = 0; i < that.tableData.length; i++) {
                        if (that.value === that.tableData[i].date) {
                            that.index = i;
                        }
                    }
                    that.flag = false;
                }
            },
            addTime() {
                let that = this;
                if (that.value != '' && that.endTime != '' && that.startTime != '') {

                    let time = that.startTime + ' ~ ' + that.endTime;
                    that.tableData[that.index]['time'] = time;

                    that.value = '';
                    that.index = '';
                    that.startTime = '';
                    that.endTime = '';
                    that.flag = true;

                }

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

