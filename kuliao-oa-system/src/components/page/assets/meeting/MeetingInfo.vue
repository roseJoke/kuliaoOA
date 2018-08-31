<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 会议室信息维护</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">

                <el-input clearable v-model.trim="meeting_name" placeholder="请输入会议室名称" class="handle-input mr10"></el-input>
                <el-select clearable value="会议室状态" v-model="meeting_state">
                    <el-option
                        v-for="item in select"
                        :label="item.lable"
                        :value="item.value"
                        :key="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button type="danger" icon="insert" class="right_insert" @click="insertMeetingInfo">添加</el-button>
            </div>
            <el-table :data="meetingTables.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column label="序列" width="55">
                    <template slot-scope="scope">
                        {{scope.$index+1+ (currentPage-1)*pagesize}}
                    </template>
                </el-table-column>
                <el-table-column prop="meetingId" label="会议室Id" v-if = "meetingIds"></el-table-column>
                <el-table-column prop="meetingName" label="会议室名称" width="300"></el-table-column>
                <el-table-column prop="meetingMessage" label="会议室信息"></el-table-column>
                <el-table-column prop="meetingState" label="会议室状态" width="300"></el-table-column>
                <el-table-column prop="meetingRemark" label="备注"></el-table-column>
                <el-table-column fixed="right" label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button size="small" type="danger" @click="handleEdit(scope.row)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[5, 10, 20, 50]"
                               :page-size="pagesize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="meetingTables.length">
                </el-pagination>
            </div>
        </div>

        <!--添加提示框-->
        <el-dialog title="新增会议室信息" :visible.sync="editVisibles" width="30%" center>
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="会议室名称" prop="meetingName">
                    <el-input class="handle-input" v-model="form.meetingName"></el-input>
                </el-form-item>
                <el-form-item label="会议室状态" prop="meetingState">
                    <el-input class="handle-input" :disabled="true" v-model="form.meetingState== '0' ?'未使用':'使用中'"></el-input>
                </el-form-item>
                <el-form-item label="会议室信息">
                    <el-input type="textarea" v-model="form.meetingMessage"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="meetingRemark">
                    <el-input type="textarea" v-model="form.meetingRemark"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisibles = false">取 消</el-button>
                <el-button type="primary" @click="saveInsert('form')">确 定</el-button>
            </span>
        </el-dialog>

        <!--编辑提示框-->
        <el-dialog title="编辑会议室信息" :visible.sync="editVisible" width="30%" center>
            <el-form ref="form" :model="form" label-width="100px">
                <el-form-item label="会议室Id" style="display: none">
                    <el-input v-model="form.meetingId" style="display: none"></el-input>
                </el-form-item>
                <el-form-item label="会议室名称">
                    <el-input class="disabled-style handle-input" v-model="form.meetingName" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="会议室状态">
                    <!--<el-input v-model="form.meetingState">{{form.meetingState>0?'使用中':'未使用'}}</el-input>-->
                    <el-select class="handle-input" value="会议室状态" v-model="form.meetingState">
                        <el-option
                            v-for="item in select"
                            :label="item.lable"
                            :value="item.value"
                            :key="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="会议室信息">
                    <el-input type="textarea" class="disabled-style" v-model="form.meetingMessage"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="form.meetingRemark"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<script>
    export default {

        data() {
            return{
                select: [{value: 0, lable: '未使用'}, {value: 1, lable: '使用中'}],
                meeting_name: '',
                meeting_state: '',
                meetingTables:[],
                multipleSelection: [],
                is_search: false,
                editVisible: false,
                editVisibles: false,
                delVisible: false,
                meetingIds: false,
                form:{},
                currentPage:1,
                pagesize:10,
                idx: -1,
                rules: {
                    meetingName:[{ required: true, message: '请输入会议室名称', trigger: 'blur' }],
                }
            }

        },
        watch: {
            // 如果 `meeting_name` 发生改变，这个函数就会运行
            meeting_name: function () {
                if(this.meeting_state.length == 0 && this.meeting_name.length == 0) {
                    this.search();
                }
            },
            meeting_state: function () {
                if(this.meeting_state.length == 0 && this.meeting_name.length == 0) {
                    this.search();
                }
            }
        },
        created() {
            this.search();
        },

        methods: {

            search() {
                let that = this;
                let param = {
                    meetingState: that.meeting_state,
                    meetingName: that.meeting_name
                }

                that.$axios.post('/assets/meeting/selectMeetings',param).then((res) => {
                    let datas = [];
                    datas =res.data.data;
                    for (let i = 0; i < datas.length; i++) {
                        if(datas[i].meetingState == '0') {
                            datas[i].meetingState = '未使用'
                        } else if(datas[i].meetingState == '1') {
                            datas[i].meetingState = '使用中'
                        }
                    }
                    that.meetingTables = datas;
                    that.currentPage = 1;
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleEdit(row) {
                this.form = {
                    meetingId: row.meetingId,
                    meetingName: row.meetingName,
                    meetingMessage: row.meetingMessage,
                    meetingState: row.meetingState,
                    meetingRemark: row.meetingRemark
                }
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit() {
                let meetingState = '';
                if(this.form.meetingState == '使用中') {
                    meetingState = 1;
                } else if(this.form.meetingState == '未使用') {
                    meetingState = 0;
                } else {
                    meetingState = this.form.meetingState;
                }
                let params = {
                    meetingId: this.form.meetingId,
                    meetingState: meetingState,
                    meetingRemark: (this.form.meetingRemark == undefined) ? '' : this.form.meetingRemark,
                    meetingName: this.form.meetingName,
                    meetingMessage: (this.form.meetingMessage == undefined) ? '' : this.form.meetingMessage,
                    state: 0 //逻辑删除：0-未删除，1-已经删除
                };
                console.log('数据 '+ this.$refs.form.form);
                this.$axios.post("/assets/meeting/modifyMeeting", params).then(res => {
                    this.search();
                    this.$message.success(res.data.msg);
                });
                this.editVisible = false;

            },
            // 确定删除
            deleteRow(){
                this.meetingTables.splice(this.idx, 1);
                this.$message.success('删除成功');
                this.delVisible = false;
            },
            // 添加会议室信息
            insertMeetingInfo(){
                this.form = {};
                this.form.meetingState = 0;
                this.editVisibles = true;
            },
            //添加一条会议室记录
            saveInsert(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let params = {
                            meetingName: this.form.meetingName,
                            meetingState: this.form.meetingState,
                            meetingMessage: (this.form.meetingMessage == undefined) ? '' : this.form.meetingMessage,
                            meetingRemark: (this.form.meetingRemark == undefined) ? '' : this.form.meetingRemark,
                            state: 0 //逻辑删除：0-未删除，1-已经删除
                        };

                        this.$axios.post("/assets/meeting/newMeeting", params).then(res => {
                            this.search();
                            this.$message.success(res.data.msg);
                        });
                        this.editVisibles = false;
                    }else{
                        alert('请输入完整会议室信息！');
                        return false;
                    }
            });
            },

            handleSizeChange: function (size) {
                this.pagesize = size;
            },
            handleCurrentChange: function(currentPage){
                this.currentPage = currentPage;
            }
        }
    }

</script>
<style scoped>
    .handle-del{
        margin-bottom: 20px;
    }
    .handle-input{
        width: 150px;
        display: inline-block;
        margin-bottom: 20px;
    }
    .right_insert{
        float: right;
    }
    .disabled-style {
        -webkit-text-fill-color:#606266;
    }


</style>
