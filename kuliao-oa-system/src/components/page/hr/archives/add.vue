<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i>人事档案</el-breadcrumb-item>
                <el-breadcrumb-item>档案录入</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-form ref="form" :model="form" :disabled="enEdit" label-width="110px">
                <el-row>
                    <el-col :span="5" :offset="1">
                        <div class="form-box">

                            <el-form-item label="姓名" prop="archives.name">
                                <el-input v-model="form.archives.name"></el-input>
                            </el-form-item>
                            <el-form-item label="性别" prop="archives.sex"
                                          :rules="[  { required: true, message: '必须选择性别', trigger: 'change' }]">
                                <template>
                                    <el-radio v-model="form.archives.sex" label="男">男</el-radio>
                                    <el-radio v-model="form.archives.sex" label="女">女</el-radio>
                                </template>


                            </el-form-item>
                            <el-form-item label="生日" prop="archives.birthday">
                                <el-col :span="16">
                                    <el-date-picker type="date" placeholder="选择日期" v-model="form.archives.birthday"
                                                    value-format="yyyyMMdd"    style="width: 100%;"></el-date-picker>
                                </el-col>
                            </el-form-item>
                            <el-form-item label="电话号码" prop="archives.phone">
                                <el-input v-model="form.archives.phone"></el-input>
                            </el-form-item>

                            <el-form-item label="电子邮件" prop="archives.email" :rules="[
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur'] }
    ]">
                                <el-input v-model="form.archives.email"></el-input>
                            </el-form-item>
                            <el-form-item label="身份证" prop="archives.idcard">
                                <el-input v-model="form.archives.idcard"></el-input>
                            </el-form-item>
                            <el-form-item label="现住址" prop="archives.address">
                                <el-input v-model="form.archives.address"></el-input>
                            </el-form-item>
                            <el-form-item label="户口所在地" prop="archives.regAddress">
                                <el-input v-model="form.archives.regAddress"></el-input>
                            </el-form-item>
                            <el-form-item label="婚姻状态" prop="archives.marriage">
                                <el-select v-model="form.archives.marriage" placeholder="请选择">
                                    <el-option key="yh" label="已婚" value="已婚"></el-option>
                                    <el-option key="wh" label="未婚" value="未婚"></el-option>
                                </el-select>
                            </el-form-item>


                        </div>
                    </el-col>
                    <el-col :span="5" :offset="2">
                        <div class="form-box">

                            <el-form-item label="员工CODE" prop="emp.code">
                                <el-input v-model="form.emp.code"></el-input>
                            </el-form-item>
                            <el-form-item label="员工编号" prop="emp.empno">
                                <el-input v-model="form.emp.empno"></el-input>
                            </el-form-item>
                            <el-form-item label="员工部门" prop="emp.dept">
                                <el-input v-model="form.emp.dept"></el-input>
                            </el-form-item>
                            <el-form-item label="岗位名称" prop="emp.postName">
                                <el-input v-model="form.emp.postName"></el-input>
                            </el-form-item>
                            <el-form-item label="岗位等级" prop="emp.postLevel">
                                <el-select v-model="form.emp.postLevel" placeholder="请选择">
                                    <el-option key="1" label="初级" value="1"></el-option>
                                    <el-option key="2" label="中级" value="2"></el-option>
                                    <el-option key="3" label="高级" value="3"></el-option>
                                    <el-option key="4" label="特技" value="4"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="社保情况" prop="emp.socialSecurity">
                                <el-input type="textarea" rows="5" v-model="form.emp.socialSecurity"></el-input>
                            </el-form-item>


                            <el-form-item label="是否在职" prop="emp.onJob">
                                <el-select v-model="form.emp.onJob" placeholder="请选择">
                                    <el-option key="nan" label="在职" :value="true"></el-option>
                                    <el-option key="nv" label="离职" :value="false"></el-option>
                                </el-select>
                            </el-form-item>

                        </div>
                    </el-col>
                    <el-col :span="5" :offset="2">

                        <el-form-item>
                            <upload-img v-model="img_file"
                                        :img_url="form.archives.photoUrl?form.archives.photoUrl:'/static/img/timg.jpg'"></upload-img>
                        </el-form-item>
                        <el-form-item label="交通补贴" prop="emp.subsidyTraffic">
                            <el-input v-model="form.emp.subsidyTraffic"></el-input>
                        </el-form-item>
                        <el-form-item label="话补" prop="emp.subsidyPhone">
                            <el-input v-model="form.emp.subsidyPhone"></el-input>
                        </el-form-item>
                        <el-form-item label="试用期工资" prop="emp.wagesTryout">
                            <el-input v-model="form.emp.wagesTryout"></el-input>
                        </el-form-item>
                        <el-form-item label="正式工资" prop="emp.wagesOfficial">
                            <el-input v-model="form.emp.wagesOfficial"></el-input>
                        </el-form-item>
                        <el-form-item label="备注" prop="archives.desc">
                            <el-input type="textarea" v-model="form.archives.desc"></el-input>
                        </el-form-item>

                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="22" :offset="2">
                        <el-col :span=9>
                            <family v-model="form.family"></family>
                        </el-col>
                        <el-col :span="7" :offset="1">
                            <urgent v-model="form.urgent"></urgent>
                        </el-col>
                       <!-- <el-col :span="5" :offset="1">
                            <skill v-model="form.skill"></skill>
                        </el-col>-->
                    </el-col>
                </el-row>
                <el-row style="margin-top: 20px">

                    <el-col :span="4" :offset="20">
                        <el-button type="primary" style="height: 40px;width: 80px" @click="onSubmit">提交</el-button>
                    </el-col>

                </el-row>
            </el-form>
        </div>

    </div>
</template>

<script>
    import uploadImg from './uploadImg.vue';
    import family from './family';
    import urgent from './urgent';
   /* import skill from './skill';*/

    export default {
        data: function () {
            return {

                enEdit: true,
                name: '',
                img_file: null,
                form: {
                    archives: {},
                    emp: {},
                    family: [
                    ],
                   /* skill: [
                    ],*/
                    urgent: [
                    ]
                }
            }
        },
        components: {
            uploadImg, family, urgent,/* skill*/
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
                console.log(this.form.archives.birthday);
                if (this.img_file) {
                    let param = new FormData(); //创建form对象
                    let imgName = that.form.archives.name + that.form.archives.sex + that.form.archives.phone
                    console.log(this.img_file);
                    param.append('file', new File([this.img_file], imgName + '.png'));//通过append向form对象添加数据

                    param.append('path', '主页/人力资源/人事档案/档案照片');
                    let config = {
                        headers: {'Content-Type': 'multipart/form-data'}
                    };
                    this.$axios.post('/upload', param, config)
                        .then(response => {
                            console.log(response.data);
                            that.form.archives.photoUrl = response.data.data.downloadUrl;
                            that.saveArchives();
                            this.$message.success('提交成功！');
                        }).catch(res => {
                        this.$message.error("图像保存失败");
                        that.saveArchives();
                    })
                } else {
                    that.saveArchives();
                }


                // this.$message.success('提交成功！');
                /*  this.form.archives = {};
                  this.form.emp = {};
                  this.form.family = [];
                  this.form.skill = [];
                  this.form.urgent = [];*/
            },

            saveArchives() {
                let that = this;
                that.$axios.post('/hr/archives', that.form.archives).then(
                    res => {
                        console.log(this.form.archives);
                        this.form.archives = {};
                        console.log(res);
                        that.$axios.all([
                            that.saveEmp(res.data.data),
                            that.saveFamily(res.data.data),
                          /*  that.saveSkill(res.data.data),*/
                            that.saveUrgent(res.data.data)
                        ]).then(that.$axios.spread((emp,family,/*skill,*/urgent) =>{
                            that.form = {
                                archives:{},
                                emp:{},
                                family:[],
                              /*  skill:[],*/
                                urgent:[]
                            };
                            this.$message.success('提交成功！');
                        }));

                    }
                );
            },
            saveEmp(id) {
                let that = this;
                this.form.emp.id = id;
                return that.$axios.post('/hr/archives/emp', this.form.emp)
            },
            saveFamily(id) {
                let that = this;

                this.form.family.forEach(v=>{
                    v.archivesId = id;
                })
                return that.$axios.post('/hr/archives/family', this.form.family);
            },
            saveSkill(id) {
                let that = this;
                this.form.skill.forEach(v=>{
                    v.archivesId = id;
                })
                return that.$axios.post('/hr/archives/skill', this.form.skill);
            },
            saveUrgent(id) {
                let that = this;
                this.form.urgent.forEach(v=>{
                    v.archivesId = id;
                })
                return that.$axios.post('/hr/archives/urgent', this.form.urgent);
            },
        },


        mounted() {


            this.id = this.$route.params.id;
            console.log(this.$route);
            console.log(this.$route.query.id);
            console.log(this.$route.query.enEdit);
            if (this.$route.query.enEdit || this.$route.query.enEdit === undefined) {
                this.enEdit = false;
            } else {
                this.enEdit = true;
            }
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
