<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>办公用品统计记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                搜索条件：
                <el-input type="text" value-format="yyyyMM" v-model="select_loginDate" clearable placeholder="统计日期（201806）"
                          class="date-input"></el-input>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border style="width: 100%"
                      ref="multipleTable">
                <el-table-column label="序号" sortable width="80">
                    <template slot-scope="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="countId" v-if="countId>0" sortable width="150">
                </el-table-column>
                <el-table-column prop="loginDate" label="统计日期" width="120">
                </el-table-column>
                <el-table-column prop="goodsName" label="物品名称" width="120">
                </el-table-column>
                <el-table-column prop="format" label="规格" width="200">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="80">
                </el-table-column>
                <el-table-column prop="location" label="存放地点" width="80">
                </el-table-column>
                <el-table-column prop="originalCount" label="月初数量" width="80">
                </el-table-column>
                <el-table-column prop="instock" label="本月入库" width="80">
                </el-table-column>
                <el-table-column prop="outstock" label="本月出库" width="80">
                </el-table-column>
                <el-table-column prop="leastCount" label="剩余量" width="80">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="230">
                </el-table-column>
                <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[6,10, 20, 50, 100]"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="tableData.length">
                </el-pagination>
            </div>
        </div>
        <!--编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="100px">
                <el-form-item label="统计日期">
                    <el-input type="text" disabled="true" v-model="form.loginDate"></el-input>
                </el-form-item>
                <el-form-item label="物品名称">
                    <el-input type="text" disabled="true" v-model="form.goodsName"></el-input>
                </el-form-item>
                <el-form-item label="规格">
                    <el-input type="text" disabled="true" v-model="form.format"></el-input>
                </el-form-item>
                <el-form-item label="单位">
                    <el-input type="text" disabled="true" v-model="form.unit"></el-input>
                </el-form-item>
                <el-form-item label="存放地点">
                    <el-input type="text" disabled="true" v-model="form.location"></el-input>
                </el-form-item>
                <el-form-item label="月初量">
                    <el-input type="text" disabled="true" v-model="form.originalCount"></el-input>
                </el-form-item>
                <el-form-item label="本月入库">
                    <el-input type="text" disabled="true" v-model="form.instock"></el-input>
                </el-form-item>
                <el-form-item label="本月出库">
                    <el-input type="text" disabled="true" v-model="form.outstock"></el-input>
                </el-form-item>
                <el-form-item label="本月剩余">
                    <el-input disabled="true" v-model="form.leastCount"></el-input>
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

    import ElInput from "../../../../../node_modules/element-ui/packages/input/src/input.vue";

    export default {
        components: {ElInput},
        data() {
            return {
                tableData: [],
                cur_page: 1,
                multipleSelection: [],
                select_loginDate: '',
                del_list: [],
                is_search: false,
                editVisible: false,
                pageSize: 6,
                currentPage: 1,
                form: {
                    countId: '0',
                    loginDate: '',
                    goodsName: '',
                    format: '',
                    unit: '',
                    location: '',
                    originalCount: '',
                    instock: '',
                    outstock: '',
                    leastCount: '',
                    remark: ''
                },
                idx: -1
            }
        },
        created() {
            this.getData();
        },
        methods: {
            // 获取初始数据
            getData() {
                this.$axios.post('/assets/officeGoods/count').then((res) => {
                        this.tableData = res.data.data;
                })
            },
            handleEdit(index, row) {
                this.idx = (this.currentPage-1)*this.pageSize+index;
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    countId: item.countId,
                    loginDate: item.loginDate,
                    goodsName: item.goodsName,
                    format: item.format,
                    unit: item.unit,
                    location: item.location,
                    originalCount: item.originalCount,
                    instock: item.instock,
                    outstock: item.outstock,
                    leastCount: item.leastCount,
                    remark: item.remark
                }
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit() {
                let params = new URLSearchParams();
                params.append("countId",this.form.countId);
                params.append("remark",this.form.remark);
                this.$axios.post('/assets/officeGoods/updateCountMess',params).then((res) => {
                    this.$set(this.tableData, this.idx, this.form);
                    this.editVisible = false;
                    this.$message.success(`修改第 ${this.idx + 1} 行信息成功`);
                })

            },
            formClear(formName) {
                this.$refs[formName].resetFields();
            },
            OnSearch() {
                if(this.select_loginDate==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("loginDate", this.select_loginDate);
                this.$axios.post('/assets/officeGoods/selectOfficeGoodsCountByCondition', params, {
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
    .date-input {
        width: 200px;
        display: inline-block;
    }
</style>
