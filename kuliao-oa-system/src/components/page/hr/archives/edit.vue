<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i>档案管理</el-breadcrumb-item>
                <el-breadcrumb-item>档案查看</el-breadcrumb-item>
            </el-breadcrumb>

        </div>
        <div class="container">
            <el-row style="margin-bottom: 10px">
                <el-col :span="2">
                    <el-button style="width: 100%;" type="primary" round @click="$router.back()">返回</el-button>
                </el-col>
                <el-col :span="3" :offset="1">
                    <el-switch style="display: block;"
                               v-model="enEdit"
                               active-color="#13ce66"
                               inactive-color="#ff4949"
                               active-text="只读"
                               inactive-text="编辑">
                    </el-switch>
                </el-col>
                <el-col :span="3">
                    <template v-if="!enEdit">
                        <el-button type="warning" @click="onSubmit">确认修改</el-button>
                    </template>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="5" :offset="1">
                    <div class="form-box">
                        <el-form ref="form" :model="form" :disabled="enEdit" label-width="110px">
                            <el-form-item label="姓名">
                                <el-input v-model="form.archives.name"></el-input>
                            </el-form-item>

                            <el-form-item label="性别" prop="archives.sex"
                                          :rules="[  { required: true, message: '必须选择性别', trigger: 'change' }]">
                                <template>
                                    <el-radio v-model="form.archives.sex" label="男">男</el-radio>
                                    <el-radio v-model="form.archives.sex" label="女">女</el-radio>
                                </template>
                            </el-form-item>
                            <el-form-item label="生日">
                                <el-col :span="16">
                                    <el-date-picker type="date" placeholder="选择日期" v-model="form.archives.birthday"
                                                   value-format="yyyyMMdd"  style="width: 100%;"></el-date-picker>
                                </el-col>
                            </el-form-item>
                            <el-form-item label="电话号码">
                                <el-input v-model="form.archives.phone"></el-input>
                            </el-form-item>

                            <el-form-item label="电子邮件">
                                <el-input v-model="form.archives.email"></el-input>
                            </el-form-item>
                            <el-form-item label="身份证">
                                <el-input v-model="form.archives.idcard"></el-input>
                            </el-form-item>
                            <el-form-item label="现住址">
                                <el-input v-model="form.archives.address"></el-input>
                            </el-form-item>
                            <el-form-item label="户口所在地">
                                <el-input v-model="form.archives.reg_address"></el-input>
                            </el-form-item>
                            <el-form-item label="婚姻状态">
                                <el-select v-model="form.archives.marriage" placeholder="请选择">
                                    <el-option key="yh" label="已婚" value="1"></el-option>
                                    <el-option key="wh" label="未婚" value="2"></el-option>
                                </el-select>
                            </el-form-item>

                        </el-form>
                    </div>
                </el-col>
                <el-col :span="5" :offset="2">
                    <div class="form-box">
                        <el-form ref="form" :model="form" :disabled="enEdit" label-width="110px">
                            <el-form-item label="员工CODE">
                                <el-input v-model="form.emp.code"></el-input>
                            </el-form-item>
                            <el-form-item label="员工编号">
                                <el-input v-model="form.emp.empno"></el-input>
                            </el-form-item>
                            <el-form-item label="员工部门">
                                <el-input v-model="form.emp.dept"></el-input>
                            </el-form-item>
                            <el-form-item label="岗位名称">
                                <el-input v-model="form.emp.postName"></el-input>
                            </el-form-item>
                            <el-form-item label="岗位等级">
                                <el-select v-model="form.emp.postLevel" placeholder="请选择">
                                    <el-option key="1" label="初级" value="1"></el-option>
                                    <el-option key="2" label="中级" value="2"></el-option>
                                    <el-option key="3" label="高级" value="3"></el-option>
                                    <el-option key="4" label="特技" value="4"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="社保情况">
                                <el-input type="textarea" rows="5" v-model="form.emp.socialSecurity"></el-input>
                            </el-form-item>


                            <el-form-item label="是否在职">
                                <el-select v-model="form.emp.onJob" placeholder="请选择">
                                    <el-option key="nan" label="在职" :value="true"></el-option>
                                    <el-option key="nv" label="离职" :value="false"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <el-col :span="5" :offset="2">
                    <el-form ref="form" :model="form" :disabled="enEdit" label-width="110px">
                        <el-form-item>
                            <upload-img v-model="img_file" :img_url="form.archives.photoUrl"></upload-img>
                        </el-form-item>
                        <el-form-item label="交通补贴">
                            <el-input v-model="form.emp.subsidyTraffic"></el-input>
                        </el-form-item>
                        <el-form-item label="话补">
                            <el-input v-model="form.emp.subsidyPhone"></el-input>
                        </el-form-item>
                        <el-form-item label="试用期工资">
                            <el-input v-model="form.emp.wagesTryout"></el-input>
                        </el-form-item>
                        <el-form-item label="正式工资">
                            <el-input v-model="form.emp.wagesOfficial"></el-input>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input type="textarea" v-model="form.archives.desc"></el-input>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="22" :offset="2">
                    <el-col :span="9">
                        <family v-model="form.family" :delHandel="delFamily"></family>
                    </el-col>
                    <el-col :span="7" :offset="1">
                        <urgent v-model="form.urgent" :delHandel="delUrgent"></urgent>
                    </el-col>

                </el-col>
            </el-row>

        </div>

    </div>
</template>

<script>
    import uploadImg from './uploadImg.vue';
    import family from './family';
    import urgent from './urgent';
    import skill from './skill';

    export default {
        data: function () {
            return {
                id: '',
                img_file: null,
                enEdit: true,
                form: {
                    archives: {},
                    emp: {},

                }
            }
        },
        components: {
            uploadImg, family, urgent, skill
        },
        methods: {
            onSubmit() {
                let that = this;
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        that.save();
                        return true;
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            save() {
                let that = this;
                if (this.img_file) {
                    let param = new FormData(); //创建form对象
                    let imgName = that.form.archives.name + that.form.archives.sex + that.form.archives.phone
                    param.append('file', new File([this.img_file], imgName + '.png'));//通过append向form对象添加数据
                    param.append('path', '主页/人力资源/人事档案/档案照片');
                    let config = {
                        headers: {'Content-Type': 'multipart/form-data'}
                    };
                    this.$axios.post('/upload', param, config)
                        .then(response => {
                            that.form.archives.photoUrl = response.data.data.downloadUrl;
                            that.update()

                        }).catch(res => {
                        this.$message.error("图像保存失败");
                        that.update()
                    })
                } else {
                  that.update()
                }
            },
            update(){
                let that = this;
                that.$axios.all([
                    that.saveEmp(that.id),
                    that.saveArchives(that.id)
                ]).then(that.$axios.spread((emp, archives) => {
                    that.form = {
                        archives: archives.data.data,
                        emp: emp.data.data,
                    };
                    this.$message.success('提交成功！');
                    that.enEdit = true;
                }));
            }
            ,
            saveArchives(id) {
                let that = this;
                this.form.archives.id = id;
                return that.$axios.patch('/hr/archives', that.form.archives)
            },
            saveEmp(id) {
                let that = this;
                this.form.emp.id = id;
                return that.$axios.patch('/hr/archives/emp', this.form.emp)
            },
            delFamily(i) {
                let that = this;
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (that.from.family[i].id) {
                        that.$axios.delete("/hr/archives/family", {
                            params: {
                                id: that.from.family[i].id
                            }
                        }).then(res => {
                            this.form.family.splice(i, 1);
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                        })
                    } else {
                        this.form.family.splice(i, 1);
                    }

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });


            },
            delUrgent(i) {
                let that = this;
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (that.from.urgent[i].id) {
                        that.$axios.delete("/hr/archives/urgent", {
                            params: {
                                id: that.from.urgent[i].id
                            }
                        }).then(res => {
                            this.form.urgent.splice(i, 1);
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                        })
                    } else {
                        this.form.urgent.splice(i, 1);
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });


            }
        },
        created() {
            this.id = this.$route.query.id;
            console.log(this.$route);
            console.log("id: " + this.$route.query.id);
            console.log(this.$route.query.enEdit);

            this.$axios.get("/hr/archives", {params: {id: this.id}}).then(res => {
                this.form = res.data.data;
                console.log(this.form);
            })
            if (this.$route.query.enEdit || this.$route.query.enEdit === undefined) {
                this.enEdit = false;
            } else {
                this.enEdit = true;
            }
        },
        mounted() {


        },
        deactivated() {

        },
    }
</script>
<style scoped>
    .text {
        font-size: 14px;
    }

    .item {
        margin-bottom: 18px;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }

    .box-card {
        width: 480px;
    }


</style>
