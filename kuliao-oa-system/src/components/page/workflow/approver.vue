<template>
    <div class="container">
        <el-table :data="tableData" border ref="multipleTable">
            <el-table-column prop="stepName" label="步骤名称">
            </el-table-column>
            <el-table-column label="指定审批人">
                <template slot-scope="scope">
                    <template v-if="userList[scope.row.stepId]!=undefined && userList[scope.row.stepId].length > 1">
                        <el-select v-model="handler[scope.row.stepId]" placeholder="请选择" style="width: 70%"
                                   @change="onUser">
                            <el-option
                                v-for="(item ,i) in userList[scope.row.stepId]"
                                :key="i"
                                :label="item.username"
                                :value="item.empNo">
                                <span style="float: left">{{ item.username }}</span>
                                <span style="float: right; color: #8492a6; font-size: 13px">{{item.deptName}} {{ item.empNo }}</span>
                            </el-option>
                        </el-select>
                    </template>
                    <template v-if="userList[scope.row.stepId]!=undefined && userList[scope.row.stepId].length == 1">
                        <span>
                            <span style="float: left">
                                {{ userList[scope.row.stepId][0].username }}
                                <span style=" color: #8492a6; font-size: 13px; margin-left: 10px">
                                     {{userList[scope.row.stepId][0].deptName}}
                                     {{ userList[scope.row.stepId][0].empNo }}
                                </span>
                            </span><!--
                            <span style="float: right; color: #8492a6; font-size: 13px"></span>-->
                        </span>
                    </template>
                </template>
            </el-table-column>
        </el-table>
        <br/>
    </div>

</template>
<script>

    export default {
        props: ['value', 'processId'],
        data() {
            return {
                tableData: [],
                userList: {},
                handler: {},
                value6: ''
            }
        },
        created() {
            if (this.processId) {
                this.getData();
            }
        },
        computed: {},
        methods: {
            //获取流程的数据，开始设置审批人
            getData() {
                let that = this;
                let params = new URLSearchParams();
                if (!this.processId || this.processId === undefined) return;
                console.log(that.processId)
                params.append('processId', this.processId);
                that.$axios.post("/workflow/process/queryStep", params, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }
                ).then((res) => {
                    //在这里拿到流程步骤定义
                    // console.log('成功拿到后台的步骤数据：' + res.data.data)
                    // 在查询以后就遍历
                    that.tableData = res.data.data;
                    that.getEmpNameByEmpId();
                }).catch(error => {
                    console.error(error)
                });
            },
            //组织员工列表 根据userid
            getEmpNameByEmpId() {
                let that = this;
                for (let i = 0; i < that.tableData.length; i++) {
                    let idArr = that.tableData[i].userId.split(','); //获取到当前步骤的所有审批人的员工编号数组
                    if (idArr.length != 0) {
                        //console.log(idArr)
                        if(idArr.length == 1){
                            that.handler[that.tableData[i].stepId] = idArr[0];
                        }
                        this.$set(that.userList, that.tableData[i].stepId, [])
                        for (let j = 0; j < idArr.length; j++) {
                            let empNo = idArr[j];
                          //  console.log('员工编号:' + empNo);
                            let params = new URLSearchParams();
                            params.append('empNo', empNo);
                            that.$axios.post("/base/user/query", params).then((res) => {
                                if (res.data.data.length == 0) {
                                    console.log(empNo + "不存在")
                                } else {
                                    // this.$set(that.userList,that.tableData[i].stepId, 24)
                                    that.userList[that.tableData[i].stepId].push(res.data.data[0]);
                                }
                               // console.log(res)
                                // console.log(that.userList);
                            }).catch(error => {
                                console.log('获取有误')
                            });
                        }
                    } else {
                        //用户数据不正确
                        console.error('参数有误')
                        console.error(idArr)
                    }
                }
            },
            onUser(a) {

                this.getHandler();
            },


            //获取最终的handler
            getHandler() {
                let that = this;
                let queryStep = [];
                for (var key in that.handler) {
                    queryStep.push({
                        stepId: key,
                        userId: that.handler[key]
                    })
                }

                /* that.handler.forEach(v=>{
                     console.log(v);
                 })*/
                console.log(queryStep);
                this.$emit('input', queryStep)

            }
        }
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }
</style>
