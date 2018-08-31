<template>
    <div>
        <el-card shadow="never">
            <div slot="header" class="clearfix">
                <span>紧急联系人</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="dialogFormVisible = true">添加
                </el-button>
            </div>
            <div v-for="(o,i) in form" :key="i" class="text item">
                {{o.emergencyContactName}}&nbsp; {{o.emergencyContactRel}} &nbsp;电话:{{o.emergencyContactPhone}}
                <el-button style="float: right; padding: 3px 0" type="text"  @click="delHandel?delHandel(i):del(i)">X
                </el-button>
            </div>
        </el-card>
        <el-dialog title="添加紧急" :visible.sync="dialogFormVisible">
            <el-form :model="urgent">
                <el-form-item label="姓名"  >
                    <el-input v-model="urgent.emergencyContactName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="关系"  >
                    <el-input v-model="urgent.emergencyContactRel" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="联系电话"  >
                    <el-input v-model="urgent.emergencyContactPhone" auto-complete="off"
                              @keyup.enter.native="add"
                    ></el-input>
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
        props: ['value','delHandel'],
        data() {
            return {
                dialogFormVisible: false,
                form: this.value,
                urgent: {
                   emergencyContactName:null,
                   emergencyContactRel:null,
                   emergencyContactPhone:null
                }
            }

        },
        methods: {
            add() {
                this.form.push(this.urgent);
                this.urgent = {}
                this.$emit('input', this.form) //触发 input 事件，并传入新值
                this.dialogFormVisible = false;
            },
            del(i) {
                this.form.splice(i, 1);
                this.$emit('input', this.form) //触发 input 事件，并传入新值
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
