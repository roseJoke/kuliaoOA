<template>
    <div>
        <el-button type="primary" round @click="dialogVisibleMax = true">指定所有审批人</el-button>

        <el-dialog
            title="提示"
            :visible.sync="dialogVisibleMax"
            width="50%"
            :before-close="handleCloseMax">

            <!--选择成员start-->
            <template>
                <div>

                    <el-table
                        :data="dynamicTags"
                        style="width: 100%">
                        <el-table-column
                            label="审批节点"
                            width="180">
                            <template slot-scope="scope">
                                <el-tag type="success">{{scope.row.nodeName}}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="审批人"
                            width="180">
                            <template slot-scope="scope">
                                <el-tag
                                    :key="scope.row.empName"
                                    closable
                                    v-if="scope.row.empName != null"
                                    :disable-transitions="false"
                                    @close="handleCloseTag(scope.row.empName)">
                                    {{scope.row.empName}}
                                </el-tag>

                            </template>
                        </el-table-column>
                        <el-table-column label="添加审批人">
                            <template slot-scope="scope">
                                <el-button
                                    type="success"
                                    size="mini"
                                    @click="setEmp(scope.$index, scope.row)">选择成员
                                </el-button>

                                <el-dialog
                                    title="设置审批人"
                                    :visible.sync="dialogVisibleMin"
                                    width="40%"
                                    :append-to-body="true"
                                    :before-close="handleCloseMin">
                                    <!--成员树-->
                                    <emp-tree ref="empTree"></emp-tree>
                                    <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="getEmpTreeMessage()">确 定</el-button>
                   <el-button @click="dialogVisibleMin = false">取 消</el-button>
                    </span>
                                </el-dialog>

                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </template>
            <!--选择成员end-->

            <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisibleMax = false">确 定</el-button>
    <el-button @click="dialogVisibleMax = false">取 消</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    import empTree from "./empTree.vue"

    export default {
        data() {
            return {
                index: 0, //当前添加审批人所在行
                dialogVisibleMax: false, //外面弹框值
                dialogVisibleMin: false, //里面弹框值
                dynamicTags: [
                    {
                        nodeId: '',
                        nodeName: '组长审批',
                        empName: '张三',
                        empId: null
                    },
                    {
                        nodeId: '',
                        nodeName: '老总审批',
                        empName: '老总',
                        empId: null
                    }
                ],
                inputVisible: false,

            };
        },
        components: {
            "empTree": empTree
        },
        methods: {
            handleCloseMax(done) { //外面弹框
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {
                    });
            },
            handleCloseMin(done) { //里面弹框
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {
                    });
            },
            handleCloseTag(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
            },
            getEmpTreeMessage() { //获取子组件的数据，并创建tag标签
                this.dialogVisibleMin = false;
                let that = this;
                let arr = this.$refs.empTree.getCheckedNodes();

                console.log('选中的人数'+arr.length)
                if (arr.length > 1) {
                    this.$message.error('错了哦，只能选取一个人');
                } else {
                    arr.forEach(function (arrs) {
                        let lables = arrs.label;
                        let empId = arrs.id;
                        console.log('测试2' + lables+'|||'+empId);
                        if (lables) {
                            that.dynamicTags[that.index].empName = lables;
                            that.dynamicTags[that.index].empId = empId;
                            console.log('测试3'+that.dynamicTags[that.index].empName +'|||'+that.dynamicTags[that.index].empId);
                        }
                    })
                }
            },

            setEmp(index, row) { //记录当前行
                this.index = index;
                this.dialogVisibleMin = true
                console.log('测试1' + this.index)
            }
        }
    };
</script>

