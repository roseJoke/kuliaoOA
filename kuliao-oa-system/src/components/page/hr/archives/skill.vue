<template>
    <div>
        <el-card shadow="never">
            <div slot="header" class="clearfix">
                <span>掌握技能</span>
                <el-button style="float: right; padding: 3px 0" type="text"  @click="dialogFormVisible = true">添加</el-button>
            </div>
            <div v-for="(o,i) in form" :key="i" class="text item">
                {{o.name}} &nbsp; &nbsp;&nbsp;{{o.sit}}
            </div>
        </el-card>
        <el-dialog title="添加技能" :visible.sync="dialogFormVisible">
            <el-form :model="skill">
                <el-form-item label="选择技能">
                    <el-input v-model="skill.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="技能掌握情况">
                    <el-input v-model="skill.sit" auto-complete="off"
                              @keyup.enter.native="add"></el-input>
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
        props: [ 'value'],
        data() {
            return {
                dialogFormVisible: false,
                form: this.value,
                skill: {}
            }

        },
        methods: {
            add() {
                this.form.push(this.skill);
                this.skill = {}
                this.$emit('input', this.form) //触发 input 事件，并传入新值
                this.dialogFormVisible = false;
            },
            del(i) {
                this.form.splice(i, 1);
                this.$emit('input', this.form) //触发 input 事件，并传入新值
            },

        },
        watch:{
            value: function (val) {
                this.form = val;
            },
        }
    }
</script>

<style scoped>

</style>
