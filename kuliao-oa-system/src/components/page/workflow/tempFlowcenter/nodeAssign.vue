<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 指定处理人</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column prop="stepId" label="步骤id" width="270">
                </el-table-column>
                <el-table-column prop="stepName" label="步骤名称" width="270">
                </el-table-column>
                <el-table-column prop="stepType" label="步骤类型" width="270">
                </el-table-column>
                <el-table-column prop="userName" label="审批人清单" width="270">
                </el-table-column>
                <el-table-column prop="approvalType" label="审批方式" width="250">
                </el-table-column>
                <el-table-column label="指定审批人" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" :disabled="scope.row.userId.indexOf(',')===-1"
                                   @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <br/>
            <div style="text-align: right">
                <el-button @click="next()" :disabled="tableData.length!=handler.length">提交</el-button>
                <el-button>取消</el-button>
            </div>
        </div>
        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="步骤id">
                    <el-input v-model="form.stepId" disabled></el-input>
                </el-form-item>
                <el-form-item label="步骤名称">
                    <el-input v-model="form.stepName" disabled></el-input>
                </el-form-item>
                <el-form-item label="步骤类型">
                    <el-input v-model="form.stepType" disabled></el-input>
                </el-form-item>
                <el-form-item label="审批人">
                    <el-select v-model="form.userId" placeholder="请选择">
                        <el-option
                            v-for="item in arrList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="审批方式">
                    <el-input v-model="form.approvalType" disabled></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveEdit">确 定</el-button>
                <el-button @click="closeUI()">取 消</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
    import ElButton from "element-ui/packages/button/src/button";

    export default {
        components: {ElButton},
        data() {
            return {
                aa: [{key: '1', value: 'fafaf'}],
                nexturl: '', //下一步的跳转路径
                processId: '',//流程id。
                tableData: [],
                arr: [],
                empList: [],
                arrList: [],
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                editVisible: false,
                delVisible: false,
                handler: [],
                form: {
                    stepId: '',
                    stepName: '',
                    stepType: '',
                    userId: '',
                    approvalType: ''
                },
                idx: -1
            }
        },
        created() {
            this.getData();
        },
        computed: {},
        methods: {
            next() {
                let that = this;
                // this.$router.push({name:that.nexturl,params:{id:1}});//
                that.$router.push({path: that.nexturl, query: {handler: that.handler,processId: that.processId}});
            },
            getData() {
                let that = this;
                that.processId = that.$route.query.processId;
                if (that.$route.query.nextUrl != undefined) {
                    that.nexturl = that.$route.query.nextUrl;
                    console.log(that.nexturl);
                }
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
                    that.arr = res.data.data;
                    that.getEmpNameByEmpId();
                }).catch(error => {
                    console.log(error)
                });
            },
            //组织员工列表 根据userid
            getEmpNameByEmpId() {
                let that = this;
                let idArr = [];
                for (let i = 0; i < that.arr.length; i++) {
                    if (that.arr[i].userId.indexOf(',') != -1) {
                        idArr = that.arr[i].userId.split(','); //获取到当前步骤的所有审批人的员工编号数组
                        let username = '';
                        let empObj = {
                        };
                        let empArr = [];
                        for (let j = 0; j < idArr.length; j++) { //当前节点有多个受理人时，遍历查询每个员工编号对应的名字
                            let empNo = idArr[j];
                            let params = {
                                empNo: '000001',
                            };
                            that.$axios.post("/base/user/query", params).then((res) => {
                                console.log('员工编号1 '+ empNo);
                                empArr.push(
                                    {
                                        id: empNo,
                                        name: res.data.data[0].username
                                    }
                                );
                                username = res.data.data[0].username + ',' + username;
                                that.$set(that.arr[i], 'userName', username);
                            }).catch(error => {
                                console.log('获取有误')
                            });
                            //将每次查询出来的员工放到下拉列表中
                        }
                        empObj.index = i;
                        empObj.arr = empArr;
                        that.empList.push(empObj); //数组中的对象内容为 index: i, arr: empArr
                    } else if(that.arr[i].userId.indexOf(',') === -1) {
                        let empNo = that.arr[i].userId;
                        let params = {
                            empNo: '000002',
                        };
                        that.$axios.post("/base/user/query", params).then((res) => {
                            that.$set(that.arr[i], 'userName', res.data.data[0].username);
                            that.handler.push(
                                {
                                    userId: that.arr[i].userId,
                                    stepId: that.arr[i].stepId
                                }
                            );
                            console.log('进来了 '+ res.data.data[0].username)
                        }).catch(error => {
                            console.log('参数有误')
                        })
                    }
                }
                that.tableData = that.arr;
                console.log('名字 '+that.arr[0].userId);
                console.log('名字 '+that.arr[0].userName);
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },
            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                // this.getEmpNameByEmpId();
                this.form = {
                    stepId: item.stepId,
                    stepName: item.stepName,
                    stepType: item.stepType,
                    userId: '',
                    userName: '',
                    approvalType: 'S'
                }
                for(let i = 0; i < this.empList.length; i++) {
                    if(this.empList[i].index === index) {
                        this.arrList = this.empList[i].arr;
                    }
                }
                // 在修改以后对修改过后的节点另行处理。
                this.editVisible = true;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 保存编辑
            saveEdit() {
                for(let i = 0; i < this.arrList.length; i++) {
                    if(this.arrList[i].id === this.form.userId) {
                        this.form.userName = this.arrList[i].name;
                        this.form.userId = this.arrList[i].id;
                    }
                }
                this.handler.push(
                    {
                        userId: this.form.userId,
                        stepId: this.form.stepId
                    }
                );
                console.log('handler的长度'+ this.handler.length);
                for(let i = 0; i< this.handler.length; i++) {
                    console.log('handler的id'+ this.handler[i].userId +'||||'+this.handler[i].stepId);
                }
                this.$set(this.tableData, this.idx, this.form);
                this.editVisible = false;
                this.arrList = [];
                this.$message.success(`指定当前节点审批人成功`);
            },
            closeUI() {
                this.editVisible = false;
                this.arrList = [];
            },
            getId(id) {
                console.log("当前节点的" + id);
            },
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
