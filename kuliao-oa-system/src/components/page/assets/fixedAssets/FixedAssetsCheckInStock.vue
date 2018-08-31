<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>固定资产入库签收</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="checkAll" >批量签收</el-button>
                <el-button @click="toggleSelection()">取消选择</el-button>
                <el-select v-model="select_type" placeholder="固定资产类型" clearable class="handle-select mr10">
                    <el-option v-for="item in types" :key="item.typeId" :label="item.typeName"
                               :value="item.typeId"></el-option>
                </el-select>
                <el-input v-model="select_name" placeholder="物品名称" clearable class="handle-input mr10"></el-input>
                <el-date-picker type="date" v-model="select_date" clearable value-format="yyyy年MM月dd日" placeholder="入库日期" class="date-input"></el-date-picker>
                <el-input v-model="select_user" placeholder="入库人" clearable class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border style="width: 100%"
                      ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="序号" sortable width="100">
                    <template slot-scope="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="assetsId" v-if="assetsId>0" width="150">
                </el-table-column>
                <el-table-column prop="instockDate" label="入库日期" width="150">
                </el-table-column>
                <el-table-column prop="buyDate" label="购买日期" width="150">
                </el-table-column>
                <el-table-column prop="assetsNumber" label="固定资产编号" width="200">
                </el-table-column>
                <el-table-column prop="assetsName" label="物品名称" width="150">
                </el-table-column>
                <el-table-column prop="type" label="类别" width="150">
                </el-table-column>
                <el-table-column prop="format" label="规格" width="150">
                </el-table-column>
                <el-table-column prop="price" label="单价" width="150">
                </el-table-column>
                <el-table-column prop="originalPrice" label="原值" width="150">
                </el-table-column>
                <el-table-column prop="instockPerson" label="入库人" width="150">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" @click="handleCheck(scope.$index)">签收入库</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[8,10, 20, 50, 100]"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="tableData.length">
                </el-pagination>
            </div>
        </div>
        <!-- 签收提示框 -->
        <el-dialog title="签收提示" :visible.sync="checkVisible" width="350px" center>
            <div class="del-dialog-cnt">是否确定签收？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="checkVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveCheck">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import ElButton from "../../../../../node_modules/element-ui/packages/button/src/button.vue";

    export default {
        components: {ElButton},
        data() {
            return {
                tableData: [],
                cur_page: 1,
                multipleSelection: [],
                checkVisible: false,
                select_date: '',
                select_name: '',
                select_type: '',
                select_user: '',
                check_list: [],
                is_search: false,
                types: [],
                pageSize: 8,
                currentPage: 1,
                form: {
                    assetsId: '0',
                    assetsNumber: '0',
                    assetsName: '冰箱',
                    type: '台式机',
                    format: '20L',
                    buyDate: '2018/07/02',
                    price: '2',
                    originalPrice: '库房',
                    instockDate: '',
                    instockPerson: '人事',
                }
            }
        },
        created() {
            this.getData();
        },
        mounted(){
            this.getType();
        },
        methods: {
            getType(){
                this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation', {
                    headers:
                        {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then((res) => {
                    console.log(res.data.list)
                    this.types = res.data.data.type;
                })
            },
            // 获取初始化数据
            getData() {
                // 开发环境使用 easy-mock 数据，正式环境使用 json 文件
                this.$axios.post('/assets/fixedAssets/checkInStock', {
                    page: this.cur_page
                }).then((res) => {
                    console.log(res.data.data)
                    this.tableData = res.data.data;
                })
            },
            handleCheck(index) {
                const item = this.tableData[(this.currentPage-1)*this.pageSize+index];
                this.form = {
                    assetsId: item.assetsId
                }
                this.checkVisible = true;
            },
            saveCheck() {
                let params = new URLSearchParams();
                params.append('assetsId', this.form.assetsId);
                this.$axios.post('/assets/fixedAssets/singingOneCheckMess', params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then((res) => {
                    this.$message.success("成功签收入库记录");
                    this.getData();
                })
                this.checkVisible = false;
            },
            checkAll() {
                const length = this.multipleSelection.length;
                let str = '';
                this.check_list = this.check_list.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].assetsId + ' ';
                }
                let params = new URLSearchParams();
                params.append('listId', str);
                this.$axios.post("/assets/fixedAssets/batchSingingCheckMess", params).then(res => {
                    this.getData();
                });
                this.$message.success('签收了' + length + "条入库申请");
                this.multipleSelection = [];
            },
            OnSearch() {
                if(this.select_name==''&&this.select_user==''&&this.select_type==''&&this.select_date==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("assetsName", this.select_name);
                params.append("type", this.select_type);
                params.append("instockDate", this.select_date);
                params.append("inStockPerson", this.select_user);
                this.$axios.post('/assets/fixedAssets/selectNeedCheckAssetsByCondition', params, {
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
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
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
        width: 100px;
        display: inline-block;
    }

    .date-input {
        width: 200px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }
</style>
