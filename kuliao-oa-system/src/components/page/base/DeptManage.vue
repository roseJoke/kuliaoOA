<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 部门管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-container style="height: 500px; border: 1px solid #eee">
                <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                    <el-input placeholder="请输入内容" v-model="findRole" class="input-with-select">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                    <el-menu class="role-el-menu" :collapse="collapse" @open="isOpen">
                        <el-submenu :index="'0'">
                            <template slot="title"><i class="el-icon-message"></i>部门管理</template>
                            <el-menu-item-group>
                                <template v-for="item in items">
                                    <template v-if="item.subs.length>0">
                                        <el-submenu :index="item.deptId" :key="item.deptId">    <!--选项卡-->

                                            <template slot="title" @click="changeDept(item.deptId)">
                                                <i :class="item.icon"></i><span slot="title">{{ item.deptName }}</span>
                                            </template>
                                            <template v-for="subItem in item.subs">
                                                <template v-if="subItem.subs.length>0">
                                                    <el-submenu :key="subItem.deptId" :index="subItem.deptId">
                                                        <template slot="title">
                                                            <i :class="subItem.icon"></i><span slot="title">{{ subItem.deptName }}</span>
                                                        </template>
                                                        <el-menu-item v-for="subItem in subItem.subs"
                                                                      :key="subItem.deptId"
                                                                      :index="subItem.deptId"
                                                                      @click="changeDept(subItem.deptId)">
                                                            <i :class="subItem.icon"></i><span slot="title">{{ subItem.deptName }}</span>
                                                        </el-menu-item>
                                                    </el-submenu>
                                                </template>
                                                <template v-else>
                                                    <el-menu-item :index="subItem.deptId" :key="subItem.deptId"
                                                                  @click="changeDept(subItem.deptId)">
                                                        <!--基础表格-->
                                                        <i :class="subItem.icon"></i><span slot="title">{{ subItem.deptName }}</span>
                                                    </el-menu-item>
                                                </template>

                                            </template>
                                        </el-submenu>
                                    </template>
                                    <template v-else>
                                        <el-menu-item :index="'/'+item.deptId" :key="item.deptId">      <!--基础表格-->
                                            <i :class="item.icon"></i><span slot="title">{{ item.deptName }}</span>
                                        </el-menu-item>
                                    </template>
                                </template>
                            </el-menu-item-group>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-container>
                    <el-main>
                        <el-row>
                            <el-col :span="4" style="font-size: 20px"><span>{{ deptName }}</span></el-col>
                            <el-button @click="toAddDept()">添加子部门</el-button>
                            <el-button type="primary" @click="toEditDept()">编辑部门</el-button>
                        </el-row>
                        <el-row>
                            <el-col :span="4" style="font-size: 14px"><span>下级子部门</span></el-col>
                        </el-row>
                        <el-table :data="subDept">
                            <el-table-column prop="deptName" label="部门名称">
                            </el-table-column>
                            <el-table-column prop="total" label="人数">人
                            </el-table-column>

                        </el-table>
                        <el-row>
                            <el-col :span="4" style="font-size: 14px"><span>部门人员</span></el-col>

                            <el-button type="primary" @click="toChangeDept()">调整部门</el-button>
                            <el-button type="danger" @click="deleteMember()">批量删除</el-button>
                        </el-row>
                        <el-table :data="deptMember" @select="isSelect" @select-all="isSelect">
                            <el-table-column
                                type="selection"
                                width="55">
                            </el-table-column>
                            <el-table-column prop="userId" label="用户编号">
                            </el-table-column>
                            <el-table-column prop="empNo" label="员工编号">
                            </el-table-column>
                            <el-table-column prop="username" label="员工姓名">
                            </el-table-column>
                            <el-table-column prop="deptName" label="部门">
                            </el-table-column>
                        </el-table>
                    </el-main>

                    <el-dialog title="添加子部门" :visible.sync="addFormVisible" width="30%" top="30vh">
                        <el-form :model="form">
                            <el-form-item label="新部门名称" :label-width="formLabelWidth">
                                <el-input v-model="form.deptName" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="上级部门" :label-width="formLabelWidth">
                                <el-input v-model="deptName" auto-complete="off" :disabled="true"></el-input>
                            </el-form-item>

                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="addDept()">确 定</el-button>
                            <el-button @click="addFormVisible = false">取 消</el-button>
                        </div>
                    </el-dialog>
                    <el-dialog title="编辑部门" :visible.sync="editFormVisible" width="30%" top="30vh">
                        <el-form :model="form">
                            <el-form-item label="部门名称" :label-width="formLabelWidth">
                                <el-input auto-complete="off" v-model="form.deptName"></el-input>
                            </el-form-item>
                            <el-form-item label="原部门主管" :label-width="formLabelWidth">
                                <el-input auto-complete="off" :placeholder="userName" :disabled="true"></el-input>
                            </el-form-item>
                            <el-form-item label="部门主管" :label-width="formLabelWidth">
                                <el-select v-model="form.user" placeholder="请选择">
                                    <el-option
                                        v-for="item in deptMember"
                                        :key="item.userId"
                                        :label="item.username"
                                        :value="item.userId">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="editDept(deptId)">确 定</el-button>
                            <el-button type="danger" @click="deleteDept(deptId)">删 除</el-button>
                            <el-button @click="editFormVisible = false">取 消</el-button>
                        </div>
                    </el-dialog>
                    <el-dialog title="调整部门" :visible.sync="changeDeptVisible" width="30%" top="30vh">
                        <el-transfer
                            v-model="selectedDept"
                            :data="toSelectDept"
                            :titles="['待选', '已选']">
                        </el-transfer>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="tochangeDeptforMember()">确  定</el-button>
                            <el-button @click="changeDeptVisible = false">取  消</el-button>
                        </div>
                    </el-dialog>

                </el-container>
            </el-container>
        </div>

    </div>
</template>
<style>
    .el-row {
        margin-top: 20px;
        margin-bottom: 20px;
    }

    .el-header {
        color: #333;
        line-height: 60px;
        margin-bottom: 20px;
    }

    .el-aside {
        color: #333;
    }
</style>

<script>
    export default {
        inject: ['reload'],
        data() {
            return {
                //部门列表
                items: '',
                //待选部门
                toSelectDept: [],
                //已选部门
                selectedDept: [],
                //已选员工
                selectedEmp: [],
                //当前部门ID
                deptId: '',
                //当前上级部门ID
                deptPId: '',
                deptName: '部门管理',
                //子部门信息
                subDept: [],
                //部门成员
                deptMember: '',
                //要调整的部门ID
                changeDeptId:'',
                //要调整的部门名称
                changeDeptName: '',
                //添加部门窗口
                addFormVisible: false,
                //编辑部门窗口
                editFormVisible: false,
                //调整部门窗口
                changeDeptVisible: false,
                //部门领导ID
                userId: '',
                //部门领导名称
                userName:'' ,
                form: {
                    deptName: '',
                    user: ''
                },
                formLabelWidth: '120px'

            }
        },
        created() {
            this.$axios.post("/base/dept/generate").then((res) => {
                console.log(res.data.data);
                this.items = res.data.data;

            })
        },

        methods: {
            isOpen(index) {
                this.changeDept(index);
            },
            changeDept(deptId) {
                this.deptId = deptId;

                let params = new URLSearchParams();
                params.append("deptId", this.deptId);
                this.$axios.post("/base/dept/getDeptById", params).then((res) => {
                    this.deptName = res.data.data.deptName;
                    this.form.deptName = res.data.data.deptName;
                    this.userId = res.data.data.userId;
                    this.deptPId = res.data.data.deptPId;
                    this.subDept = [];
                    res.data.data.subs.forEach((dept) => {
                        let params1 = new URLSearchParams();
                        params1.append("deptId",dept.deptId);
                        this.$axios.post("/base/dept/userInDept", params1).then((res) => {
                            this.subDept.push({
                                deptName: dept.deptName,
                                total: res.data.data.length+"人"
                            });
                        });

                    });
                    this.selectedDept = [this.deptId];

                });
                this.$axios.post("/base/dept/userInDept", params).then((res) => {
                    this.deptMember = res.data.data;
                });
                console.log(this.deptMember);



            },
            toAddDept() {
                if(this.deptId === ''){
                    return;
                }
                this.form.deptName = '';
                this.addFormVisible = true;
            },
            toEditDept() {
                if(this.deptId === ''){
                    return;
                }
                if (this.userId != null) {
                    let params = new URLSearchParams();
                    params.append("userId", this.userId);

                    this.$axios.post("/base/user/getUserById", params).then((res) => {
                        this.form.user = res.data.data.username;
                        this.userName = res.data.data.username;

                    });

                }
                this.editFormVisible = true;



            },
            toChangeDept() {
                if(this.deptId === ''){
                    return;
                }
                this.changeDeptVisible = true;
                // console.log("abcd"+this.selectedDept);
                // let params = new URLSearchParams();
                // params.append("deptId", this.deptId);
                this.$axios.post("/base/dept/selectAll").then((res) => {
                    this.toSelectDept = [];
                    console.log(res.data.data);
                    res.data.data.forEach((dept) => {
                        this.toSelectDept.push({
                            label: dept.deptName,
                            key: dept.deptId
                        });
                    });

                });
            },
            tochangeDeptforMember(){
                if(this.selectedDept.length!==1){
                    this.$message.warning("只能选择一个部门！");
                    return;
                }
                if(this.selectedDept[0]=this.deptId){
                    this.changeDeptVisible = false;
                    this.$message.warning("未调整部门");
                    return;
                }
                this.changeDeptId = this.selectedDept[0];
                let param = new URLSearchParams();
                param.append("deptId",this.changeDeptId);
                this.$axios.post("/base/dept/getDeptById",param).then((res) => {
                    this.changeDeptName = res.data.data.deptName;
                    this.changeDeptforMember();
                });

            },
            changeDeptforMember() {

                this.selectedEmp.forEach((emp) => {
                    let params = new URLSearchParams();
                    params.append("empNo",emp.empNo);
                    params.append("deptId", this.changeDeptId);


                    this.$axios.post("/base/dept/setDeptForUser", params).then((res) => {

                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });

                    });

                    let params1 = {
                        "empNo": emp.empNo,
                        "deptId": this.changeDeptId,
                        "deptName": this.changeDeptName
                    };
                    console.log(params1);
                    this.$axios.post("/hr/archives/emp/empDept", params1).then((res) => {

                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });

                    });

                });
                this.changeDeptVisible = false;
                this.reload();
            },
            deleteMember() {
                if(this.deptId === ''){
                    return;
                }
                this.$confirm('此操作将从部门里删除成员, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.selectedEmp.forEach((user) => {

                        let params = new URLSearchParams();

                        params.append("empNo", user.empNo);
                        params.append("deptId","1");
                        this.$axios.post("/base/dept/setDeptForUser", params).then((res) => {

                            console.log(res.data);
                            this.$message({
                                type: 'success',
                                message: res.data.msg
                            });
                        });
                        this.reload();
                    });

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });

            },
            addDept() {
                let params = new URLSearchParams();
                params.append("deptName", this.form.deptName);
                params.append("deptPId",this.deptId);

                this.$axios.post("/base/dept/create", params).then((res) => {
                    this.addFormVisible = false;
                    this.$message({
                        type: 'success',
                        message: res.data.msg
                    });

                });
                this.reload();

            },
            editDept() {
                if(this.userName === this.form.user || this.deptName === this.form.deptName){
                    this.editFormVisible = false;
                    this.$message({
                        type: 'warning',
                        message: '未做任何改变！'
                    });
                    return;
                }
                let params = new URLSearchParams();
                params.append("deptId", this.deptId);
                params.append("deptName", this.form.deptName);
                if(this.userName === this.form.user){
                    params.append("userId",this.userId);
                }else{
                    params.append("userId",this.form.user);
                }

                params.append("deptPId",this.deptPId);

                this.$axios.post("/base/dept/update", params).then((res) => {
                    this.editFormVisible = false;
                    this.$message({
                        type: 'success',
                        message: res.data.msg
                    });
                });
                let params1 = {
                    "deptId": this.deptId,
                    "newName": this.form.deptName,
                    "oldName": this.deptName
                };

                this.$axios.post("/hr/archives/emp/dept", params1).then((res) => {
                    this.editFormVisible = false;
                    this.$message({
                        type: 'success',
                        message: res.data.msg
                    });
                });
                this.reload();

            },
            deleteDept() {
                this.editFormVisible = false;
                this.$confirm('此操作将删除该角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = new URLSearchParams();
                    params.append("deptId", this.deptId);
                    this.$axios.post("/base/dept/delete", params).then((res) => {

                        console.log(res.data);
                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });
                    });
                    this.reload();
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });
            },
            isSelect(row) {
                this.selectedEmp = [];
                row.forEach((user) => {
                    this.selectedEmp.push({
                        deptId: this.deptId,
                        empNo: user.empNo
                    });
                });
                console.log(this.selectedEmp);
            },
        },
        name: "RoleManage"
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
        width: 120px;
        display: inline-block;
    }
</style>
