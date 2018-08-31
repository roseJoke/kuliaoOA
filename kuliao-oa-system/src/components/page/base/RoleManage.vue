<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 角色管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-container style="height: 500px; border: 1px solid #eee">
                <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                    <el-input placeholder="请输入内容" v-model="findRole" class="input-with-select">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                    <el-menu class="role-el-menu" :collapse="collapse">
                        <el-submenu :index="1">
                            <template slot="title"><i class="el-icon-message"></i>角色管理</template>
                            <el-menu-item-group>
                                <template v-for="(item,index) in items">
                                    <el-menu-item :index="'/'+index" :key="item.roleId" @click="changeRole(item)">
                                        <i :class="item.icon"></i><span slot="title">{{ item.roleName }}</span>
                                    </el-menu-item>
                                </template>
                            </el-menu-item-group>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-container>
                    <el-header style="text-align: left; font-size: 12px">
                        <el-row>
                            <el-col :span="4" style="font-size: 18px"><span>{{ roleName }}</span></el-col>
                            <el-button @click="toAddRole()">新增角色</el-button>
                            <el-button type="primary" @click="toEdit()">编辑角色</el-button>
                            <el-button type="primary" @click="toSetMenuAuth()">权限设置</el-button>
                            <el-button type="primary" @click="toAddMember()">添加成员</el-button>
                            <el-button type="danger" @click="deleteMember()">批量删除</el-button>
                        </el-row>

                    </el-header>

                    <el-main>
                        <el-table :data="tableData" @select="isSelect">
                            <el-table-column
                                type="selection"
                                width="55">
                            </el-table-column>
                            <el-table-column prop="empNo" label="员工编号">
                            </el-table-column>
                            <el-table-column prop="username" label="员工姓名">
                            </el-table-column>
                            <el-table-column prop="deptName" label="部门">
                            </el-table-column>
                        </el-table>
                    </el-main>
                    <el-dialog title="新增角色" :visible.sync="addFormVisible" width="30%" top="30vh">
                        <el-form :model="form">
                            <el-form-item label="新角色名称" :label-width="formLabelWidth">
                                <el-input v-model="form.roleName" auto-complete="off"></el-input>
                            </el-form-item>

                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="addRole()">确 定</el-button>
                            <el-button @click="addFormVisible = false">取 消</el-button>
                        </div>
                    </el-dialog>
                    <el-dialog title="设置权限" :visible.sync="setAuthVisible" width="30%" top="30vh">
                        <div slot="header" class="dialog-header">
                            <el-input style="float: right; padding: 0px 0;height: 24px;width: 50% "
                                      placeholder="输入关键字进行过滤"
                            >
                            </el-input>
                        </div>
                        <template>
                            <el-tree
                                class="filter-tree"
                                :data="menuList"
                                show-checkbox
                                node-key="menuId"
                                ref="menuTree"
                                :props="defaultProps">
                            </el-tree>
                        </template>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="setMenuAuth()">确 定</el-button>
                            <el-button @click="setAuthVisible = false">取 消</el-button>
                        </div>
                    </el-dialog>
                    <el-dialog title="编辑角色" :visible.sync="editFormVisible" width="30%" top="30vh">
                        <el-form :model="form">
                            <el-form-item label="原角色名称" :label-width="formLabelWidth">
                                <el-input auto-complete="off" :placeholder="roleName" :disabled="true"></el-input>
                            </el-form-item>
                            <el-form-item label="新角色名称" :label-width="formLabelWidth">
                                <el-input v-model="form.roleName" auto-complete="off"></el-input>
                            </el-form-item>

                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="editRole()">确 定</el-button>
                            <el-button type="danger" @click="deleteRole()">删 除</el-button>
                            <el-button @click="editFormVisible = false">取 消</el-button>
                        </div>
                    </el-dialog>
                    <el-dialog title="添加成员" :visible.sync="addMemberVisible" width="30%" top="30vh">
                        <el-transfer
                            v-model="selectedEmp"
                            :data="toAddEmp"
                            :titles="['待选', '已选']">
                        </el-transfer>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="addMember()">确 定</el-button>
                            <el-button @click="addMemberVisible = false">取 消</el-button>
                        </div>
                    </el-dialog>

                </el-container>
            </el-container>
        </div>

    </div>
</template>
<style>
    .el-header {

        color: #333;
        line-height: 60px;
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
                //角色中员工
                items: '',
                //要添加的员工列表
                toAddEmp: [],
                //已经选择的员工
                selectedEmp: [],
                //要删除的员工
                deleteEmp: [],
                //当前角色ID
                roleId: '',
                menuList: [],

                defaultProps: {
                    // id: 'deptId',
                    label: 'menuName',
                    children: 'subs'
                },
                setAuth: [],
                //当前角色名称
                roleName: '',
                findRole: '',
                tableData: '',
                addFormVisible: false,
                editFormVisible: false,
                setAuthVisible: false,
                addMemberVisible: false,
                form: {
                    roleName: ''
                },
                formLabelWidth: '120px'

            }
        },
        created() {
            this.$axios.post("/base/role/generate").then((res) => {
                console.log(res.data.data);
                this.items = res.data.data;

            });
        },

        methods: {
            changeRole(role) {
                // console.log(role);
                this.roleId = role.roleId;
                this.roleName = role.roleName;
                let params = new URLSearchParams();
                params.append("roleId", this.roleId);
                this.$axios.post("/base/role/userInRole", params).then((res) => {
                    this.tableData = res.data.data;
                });


            },
            isSubsExist(data) {

                data.forEach((menu) => {
                    this.setAuth.push({
                        id: menu.menuId,
                        create: "0",
                        delete: "0",
                        update: "0",
                        select: "0"

                    });
                    if (menu.subs.length === 0) {


                        menu.subs = [{
                            menuId: menu.menuId + "-1",
                            menuName: "增加"
                        }, {
                            menuId: menu.menuId + "-2",
                            menuName: "删除"
                        }, {
                            menuId: menu.menuId + "-3",
                            menuName: "修改"
                        }, {
                            menuId: menu.menuId + "-4",
                            menuName: "查看"
                        }
                        ];
                    } else {
                        this.isSubsExist(menu.subs);
                    }
                });
            },
            toSetMenuAuth() {
                if (this.roleId !== '') {
                    this.setAuthVisible = true;

                    this.$axios.post("/base/user/getAllMenu").then((res) => {
                        this.menuList = res.data.data;
                        this.setAuth = [];
                        this.isSubsExist(res.data.data);
                    });
                }
            },
            test(){
                console.log(this.$refs.menuTree.getCheckedKeys());
            },
            setMenuAuth() {

                let menuList = this.$refs.menuTree.getCheckedNodes();
                menuList.forEach((menu) => {
                    //判断选中菜单是否为最低级菜单
                    if (typeof menu.parentId === 'undefined') {

                        this.setAuth.forEach((item) => {
                            let str = menu.menuId.split("-");
                            let menuId = str[0];
                            let menuId2 = item.id + "";
                            if (menuId === menuId2) {
                                switch (str[1]) {
                                    case "1":
                                        item.create = "1";
                                        break;
                                    case "2":
                                        item.delete = "1";
                                        break;
                                    case "3":
                                        item.update = "1";
                                        break;
                                    case "4":
                                        item.select = "1";
                                        break;
                                }
                            }


                        });


                    }

                });
                this.setAuth.forEach((item) => {
                    let params = new URLSearchParams();
                    params.append("roleId", this.roleId);
                    params.append("menuId", item.id);
                    params.append("create", item.create);
                    params.append("delete", item.delete);
                    params.append("update", item.update);
                    params.append("select", item.select);
                    console.log(params.toString());
                    this.$axios.post("/base/user/setMenuAuth", params).then((res) => {
                        this.$message.success(res.data.msg);

                    });
                });
                this.setAuthVisible = false;
            },

            toAddRole() {
                this.form.roleName = '';
                this.addFormVisible = true;
            },
            toEdit() {
                if (this.roleId === '') {
                    return;
                }
                this.form.roleName = '';
                this.editFormVisible = true;
            },
            toAddMember() {
                if (this.roleId === '') {
                    return;
                }
                this.addMemberVisible = true;
                let params = new URLSearchParams();
                params.append("roleId", this.roleId);
                this.$axios.post("/base/role/userNotInRole", params).then((res) => {
                    this.toAddEmp = [];
                    res.data.data.forEach((user) => {
                        console.log(user);
                        this.toAddEmp.push({
                            label: user.username,
                            key: user.userId
                        });
                    });

                });
            },
            addMember() {

                this.selectedEmp.forEach((userId) => {
                    let params = new URLSearchParams();
                    params.append("roleId", this.roleId);
                    params.append("userId", userId);


                    this.$axios.post("/base/role/setRoleForUser", params).then((res) => {

                        this.$message({
                            type: 'success',
                            message: res.data.msg
                        });

                    });
                });
                this.addMemberVisible = false;
                this.reload();
            },
            deleteMember() {
                this.$confirm('此操作将从角色里删除成员, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteEmp.forEach((userRole) => {

                        let params = new URLSearchParams();
                        params.append("roleId", userRole.roleId);
                        params.append("empNo", userRole.empNo);
                        this.$axios.post("/base/role/deleteEmp", params).then((res) => {

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
            addRole() {
                let params = new URLSearchParams();
                params.append("roleName", this.form.roleName);

                this.$axios.post("/base/role/create", params).then((res) => {
                    this.addFormVisible = false;
                    this.$message({
                        type: 'success',
                        message: res.data.msg
                    });

                });
                this.reload();

            },
            editRole() {
                let params = new URLSearchParams();
                params.append("roleId", this.roleId);
                params.append("roleName", this.form.roleName);

                this.$axios.post("/base/role/update", params).then((res) => {
                    this.editFormVisible = false;
                    this.$message({
                        type: 'success',
                        message: res.data.msg
                    });


                });
                this.reload();

            },
            deleteRole() {
                if (this.roleId === '') {
                    return;
                }
                this.dialogFormVisible = false;
                this.$confirm('此操作将删除该角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = new URLSearchParams();
                    params.append("roleId", this.roleId);
                    this.$axios.post("/base/role/delete", params).then((res) => {

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
                this.deleteEmp = [];
                row.forEach((user) => {
                    this.deleteEmp.push({
                        roleId: this.roleId,
                        empNo: user.empNo
                    });
                });
                console.log(this.deleteEmp);
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
        width: 120px;
        display: inline-block;
    }
</style>
