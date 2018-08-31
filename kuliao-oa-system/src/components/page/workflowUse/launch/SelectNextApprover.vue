<template>
    <div>
        <el-button type="primary" round @click="dialogVisibleMax = true">指定下级审批人</el-button>
        <el-dialog
            title="指定审批"
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
                                    <emp-tree ref="em"></emp-tree>
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
                        nodeId: '11',
                        nodeName: '组长审批',
                        empName: '张三',
                        empId: '000254'
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
            //根据员工编号获取员工姓名
            getEmpNameByEmpNO(id){
                let that = this;
                that.$axios.post().then((res)=>{}).catch(error => {

                });
            },
            getEmpTreeMessage() { //获取子组件的数据，并创建tag标签
                this.dialogVisibleMin = false;
                let that = this;
                let arr = that.$refs.em.assign;
                // 根据id拿到name: 最终将name付给empName
                console.log('员工列表'+ arr);
                //将此时需要的员工数据保存 todo

                // 在这里得到数据
                // 根据assign中提供的数据查询
                if (arr.length > 1) {
                  that.$message.error('不好意思哈，一个任务只能指定一个处理人')
                } else {
                    that.dynamicTags[that.index].empName = arr[0];
                }
            },

            setEmp(index, row) { //记录当前行
                this.index = index;
                this.dialogVisibleMin = true
                console.log('第' + this.index+'行')
            }
        }
    };
</script>

