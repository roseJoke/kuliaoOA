<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>固定资产领用记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                搜索条件：
                <el-input v-model="select_name" placeholder="物品名称" clearable="true" class="handle-select mr10"></el-input>
                <el-select v-model="select_type" placeholder="物品类别" class="handle-select mr10" clearable>
                    <el-option v-for="item in types" :key="item.typeId" :label="item.typeName"
                               :value="item.typeName"></el-option>
                </el-select>
                <el-input v-model="select_user" placeholder="领用人" clearable="true" class="handle-select mr10"></el-input>
                <el-date-picker type="date" v-model="select_useDate" value-format="yyyyMMdd"
                          placeholder="领用日期" class="date-input"></el-date-picker>
                <el-date-picker type="date" v-model="select_returnDate" value-format="yyyyMMdd"
                          placeholder="归还日期" class="date-input"></el-date-picker>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
                <el-button slot="reference" @click="to('fixedAssetsAccount')">查看固定资产信息</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border style="width: 100%"
                      ref="multipleTable">
                <el-table-column label="序号" sortable width="70">
                    <template slot-scope="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="assetsuseId" v-if="assetsuseId>0" width="150"></el-table-column>
                <el-table-column prop="assetsId" v-if="assetsId>0" ></el-table-column>
                <el-table-column prop="assetsNumber" label="固定资产编号" width="200">
                </el-table-column>
                <el-table-column prop="assetsName" label="物品名称" width="100">
                </el-table-column>
                <el-table-column prop="type" label="物品类型" width="80">
                </el-table-column>
                <el-table-column prop="user" label="领用人" width="100">
                </el-table-column>
                <el-table-column prop="useDate" label="领用日期" width="150">
                </el-table-column>
                <el-table-column prop="returnDate" label="归还日期" width="150">
                </el-table-column>
                <el-table-column prop="status" label="使用状态" width="70">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="220">
                </el-table-column>
                <el-table-column label="操作" width="260">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" style="width: 100px"
                                   @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button size="small" type="danger" style="width: 100px" :disabled="scope.row.returnDate!=null"
                                   @click="handleReturn(scope.$index, scope.row)">归还
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[10, 20, 50, 100]"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="tableData.length">
                </el-pagination>
            </div>
        </div>

        <!--编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item hidden>
                    <el-input type="text" v-model="form.assetsuseId"></el-input>
                </el-form-item>
                <el-form-item hidden>
                    <el-input type="text" v-model="form.assetsId"></el-input>
                </el-form-item>
                <el-form-item label="物品类型">
                    <el-input type="text" placeholder="选择名称" :disabled="true" v-model="form.type"></el-input>
                </el-form-item>
                <el-form-item label="固定资产编号" prop="assetsNumber">
                    <el-input type="text" placeholder="输入固定资产编号" :disabled="true" v-model="form.assetsNumber" ></el-input>
                </el-form-item>
                <el-form-item label="物品名称">
                    <el-input type="text" placeholder="选择名称" :disabled="true" v-model="form.assetsName"></el-input>
                </el-form-item>
                <el-form-item label="领用人">
                    <el-input type="text" placeholder="选择领用人" :disabled="true" v-model="form.user"></el-input>
                </el-form-item>
                <el-form-item label="领用日期">
                    <el-date-picker type="date" placeholder="选择领用日期" format="yyyyMMdd" value-format="yyyyMMdd"
                                    :disabled="true" v-model="form.useDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="归还日期">
                    <el-date-picker type="date" placeholder="选择归还日期" value-format="yyyyMMdd"
                                    v-model="form.returnDate"></el-date-picker>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status" placeholder="状态">
                        <el-option v-for="item in AssetsStatus" :key="item.id" :label="item.status" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark"></el-input>
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
            return {
                tableData: [],
                types: [],
                cur_page: 1,
                multipleSelection: [],
                select_name: '',
                select_user: '',
                select_type:'',
                select_useDate: '',
                select_returnDate: '',
                del_list: [],
                is_search: false,
                editVisible: false,
                delVisible: false,
                pageSize: 10,
                currentPage: 1,
                form: {
                    assetsuseId: '0',
                    assetsId: '',
                    assetsName: '',
                    assetsNumber:'',
                    type: '',
                    user: '',
                    useDate: '',
                    returnDate: '',
                    remark: '无',
                    status:'',
                },
                AssetsStatus:[{id:1,status:'闲置'},{id:2,status:'使用中'}],
                idx: -1,
                oldDate:'',
                rules:{
                    status: [
                        {required: true, message: '状态不可为空', trigger: 'blur'}
                    ],
                }
            }
        },
        created() {
            this.getData();
        },
        mounted(){
            this.getTypeAndLocation();
        },
        methods: {
            //页面跳转
            to(send){
                this.$router.push(send);
            },
            getTypeAndLocation(){
                this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation', {
                    headers:
                        {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then((res) => {
                    console.log(res.data.list)
                    this.types = res.data.data.type;
                })
            },
            // 获取初始数据
            getData() {
                this.$axios.post('/assets/fixedAssets/use', {header: {'Content-Type': 'application/x-www-form-urlencoded'}}).then((res) => {
                    console.log(res.data.data)
                    this.tableData = res.data.data;
                })
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    assetsuseId: item.assetsuseId,
                    assetsId: item.assetsId,
                    assetsNumber: item.assetsNumber,
                    assetsName: item.assetsName,
                    type: item.type,
                    user: item.user,
                    useDate: item.useDate,
                    returnDate: item.returnDate,
                    status: item.status,
                    remark: item.remark
                }
                this.oldDate = item.returnDate;
                this.editVisible = true;
            },
            handleReturn(index, row) {
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    assetsuseId: item.assetsuseId,
                    assetsId: item.assetsId
                }
                let params = new URLSearchParams();
                params.append('assetsuseId', this.form.assetsuseId);
                params.append('assetsId', this.form.assetsId);
                this.$axios.post('/assets/fixedAssets/returnUse', params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then((res) => {
                    this.$message.success("成功归还固定资产！");
                    this.getData();
                })
            },
            // 保存编辑
            saveEdit() {
                let params = new URLSearchParams();
                params.append('assetsuseId', this.form.assetsuseId);
                params.append('assetsId', this.form.assetsId);
                if(this.oldDate == this.form.returnDate){
                    this.form.returnDate = this.oldDate.substr(0,4)+this.oldDate.charAt(5)+this.oldDate.charAt(6)+this.oldDate.charAt(8)+this.oldDate.charAt(9);
                }
                params.append('returnDate', this.form.returnDate);
                params.append('status', this.form.status);
                params.append('remark', this.form.remark);
                this.$axios.post('/assets/fixedAssets/updateUse', params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then((res) => {
                    this.$message.success("成功更新领用记录");
                    if(this.oldDate == this.form.returnDate){
                        this.form.returnDate = this.oldDate;
                    }
                    this.$set(this.tableData, this.idx, this.form);
                    this.$message.success(`修改第 ${this.idx + 1} 行成功`);
                })
                this.editVisible = false;
            },
            OnSearch() {
                if(this.select_name==''&&this.select_type==''&&this.select_useDate==''&&this.select_returnDate==''&&this.select_user==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("assetsName", this.select_name);
                params.append("user", this.select_user);
                params.append("type", this.select_type);
                params.append("useDate", this.select_useDate);
                params.append("returnDate", this.select_returnDate);
                this.$axios.post('/assets/fixedAssets/selectAssetsUseByCondition', params, {
                    headers:
                        {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                }).then((res) => {
                    this.tableData = res.data.data;
                })
            },
            //配置分页查询
            handleSizeChange: function (size) {
                this.pageSize = size;
            },
            handleCurrentChange: function (currentPage) {
                this.currentPage = currentPage;
            },
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

    .date-input {
        width: 200px;
        display: inline-block;
    }

</style>
