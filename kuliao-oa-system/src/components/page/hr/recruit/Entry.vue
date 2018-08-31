<template>
    <div>
    <div class="crumbs">
        <el-breadcrumb separator="/">
            <el-breadcrumb-item><i class="el-icon-tickets"></i> 简历录入</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
    <div class="container">
        <el-button slot="reference" @click="" style="width: 100px; right: 100px;" >选择文件</el-button>
    <div class="form-box">
    <el-form ref="form" :model="form" label-width="80px" style="width: 400px;">
        <el-form-item label="姓名">
            <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别">
            <el-select v-model="form.sex" placeholder="性别">
                <el-option label="男" ></el-option>
                <el-option label="女" ></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="年龄">
            <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="应聘岗位">
            <el-input v-model="form.position "></el-input>
        </el-form-item>
        <el-form-item label="学历">
            <el-input v-model="form.education"></el-input>
        </el-form-item>
        <el-form-item label="专业">
            <el-input v-model="form.major"></el-input>
        </el-form-item>
        <el-form-item label="毕业院校">
            <el-input v-model="form.university"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
            <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="个人邮箱">
            <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="备注">
            <el-input v-model="form.des"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit">立即创建</el-button>
            <el-button>取消</el-button>
        </el-form-item>
    </el-form>
    </div>
    </div>
    </div>
</template>

<script>
    export default {
        data: function(){
            return {
                form: {
                    name: '',
                    sex: '',
                    position : '',
                    age: '',
                    education: '',
                    major: '',
                    university:'',
                    phone:'',
                    email:'',
                    des:'',
                }
            }
        },
        methods: {
            onSubmit() {
                this.$message.success('提交成功！');
            }
        },
        deactivated() {
            // this.$destroy()
            //  console.log("没了")
        },
    }
</script>
