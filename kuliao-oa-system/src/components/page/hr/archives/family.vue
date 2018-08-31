<template>
    <div>
        <el-card shadow="never">
            <div slot="header" class="clearfix">
                <span>家庭成员</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="dialogFormVisible = true">添加
                </el-button>
            </div>
            <div v-for="(o,i) in form" :key="i" class="text item">
                {{o.name}} &nbsp;&nbsp;关系{{o.rel}} &nbsp;&nbsp;生日{{o.birthday}}
                <el-button style="float: right; padding: 3px 0" type="text" @click="delHandel?delHandel(i):del(i)">X
            </el-button>
            </div>
        </el-card>
        <el-dialog title="添加家庭成员" :visible.sync="dialogFormVisible">
            <el-form :model="fam" >
                <el-form-item label="姓名"  >
                    <el-input v-model="fam.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="关系"  >
                    <el-input v-model="fam.rel" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="出生年月日"  >
                    <el-date-picker  v-model="fam.birthday" auto-complete="off"  placeholder="选择日期"
                              @keyup.enter.native="add"    value-format="yyyyMMdd"
                    ></el-date-picker >
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="add">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        props: ['data','value','delHandel'],
        data() {
            return {
                //val: this.data,
                dialogFormVisible: false,
                form: this.value,
                fam: {
                    rel: '',
                    name: '',
                    birthday: ''
                }
            }

        },
        methods: {
            add() {
                console.log(this.fam);
                this.form.push(this.fam);
                this.fam = {}
                this.$emit('input', this.form) //触发 input 事件，并传入新值
                this.dialogFormVisible = false;
            },
            del(i) {
                this.form.splice(i, 1);
                this.$emit('input', this.form) //触发 input 事件，并传入新值
            },
            formatDate(date, fmt) {
                if (/(y+)/.test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
                }
                let o = {
                    'M+': date.getMonth() + 1,
                    'd+': date.getDate(),
                    'h+': date.getHours(),
                    'm+': date.getMinutes(),
                    's+': date.getSeconds()
                };
                for (let k in o) {
                    if (new RegExp(`(${k})`).test(fmt)) {
                        let str = o[k] + '';
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : ('00' + str).substr(str.length));
                    }
                }
                return fmt;
            }
        },
        watch:{
            value: function (val) {
                this.form = val;
            },
        }
    }
</script>

<style scoped>
    .box-card {
        width: 480px;
    }

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
</style>
