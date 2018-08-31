<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>固定资产盘库</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                搜索条件：
                <el-select v-model="select_department" placeholder="部门名称" clearable class="handle-select mr10">
                    <el-option v-for="item in departments" :key="item.deptId" :label="item.deptName"
                               :value="item.deptName"></el-option>
                </el-select>
                <el-input v-model="select_number" placeholder="固定资产编号" clearable class="handle-select"></el-input>
                <el-input v-model="select_name" placeholder="物品名称" clearable class="handle-input mr10"></el-input>
                <el-select v-model="select_location" placeholder="存放地点" clearable class="handle-select mr10">
                    <el-option v-for="item in locations" :key="item.locationId" :label="item.locationName"
                               :value="item.locationId"></el-option>
                </el-select>
                <el-select v-model="select_status" placeholder="使用状态" clearable class="handle-select mr10">
                    <el-option key="1" label="闲置" value="1"></el-option>
                    <el-option key="2" label="使用中" value="2"></el-option>
                    <el-option key="3" label="维修中" value="3"></el-option>
                </el-select>
                <el-input v-model="select_user" placeholder="使用人" clearable class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="OnSearch">搜索</el-button>
            </div>
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)" border ref="multipleTable">
                <el-table-column label="序号" sortable width="100">
                    <template slot-scope="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="assetsId" v-if="assetsId>0" width="150">
                </el-table-column>
                <el-table-column prop="assetsNumber" label="固定资产编号" width="210">
                </el-table-column>
                <el-table-column prop="assetsName" label="名称" width="150">
                </el-table-column>
                <el-table-column prop="format" label="规格" width="220">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="150">
                </el-table-column>
                <el-table-column prop="location" label="存放地" width="150">
                </el-table-column>
                <el-table-column prop="department" label="部门名称" width="150">
                </el-table-column>
                <el-table-column prop="user" label="使用人" width="150">
                </el-table-column>
                <el-table-column prop="status" label="使用状态" width="150">
                </el-table-column>
                <el-table-column prop="remark" label="备注" width="287">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination class="pull-right clearfix"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[5,10, 20, 50, 100]"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="tableData.length">
                </el-pagination>
            </div>
        </div>
        <div class="container">
            <h4>汇总信息：</h4>
            <el-table :data="statistics" border style="width: 100%" ref="multipleTable">
                <el-table-column prop="department" label="部门名称" width="150">
                </el-table-column>
                <el-table-column prop="assetsName" label="物品名称" width="150">
                </el-table-column>
                <el-table-column prop="type" label="类型" width="150">
                </el-table-column>
                <el-table-column prop="count" label="总数量" width="150">
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="150">
                </el-table-column>
                <el-table-column prop="idle" label="闲置" width="150">
                </el-table-column>
                <el-table-column prop="using" label="使用中" width="150">
                </el-table-column>
                <el-table-column prop="service" label="维修中" width="150">
                </el-table-column>
            </el-table>
            <hr style="height: 10px;">
            <el-table :data="allCount" border style="width: 100%" ref="multipleTable">
                <el-table-column prop="assetsName" label="物品名称" width="150">
                </el-table-column>
                <el-table-column prop="count" label="总数量" width="150">
                </el-table-column>
                <el-table-column prop="idle" label="闲置" width="150">
                </el-table-column>
                <el-table-column prop="using" label="使用中" width="150">
                </el-table-column>
                <el-table-column prop="service" label="维修中" width="150">
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>

    export default {
        data() {
            return {
                tableData: [],
                statistics: [],
                allCount:[],
                cur_page: 1,
                multipleSelection: [],
                select_name: '',
                select_number:'',
                select_department: '',
                select_status: '',
                select_user: '',
                select_location: '',
                del_list: [],
                is_search: false,
                editVisible: false,
                delVisible: false,
                pageSize: 5,
                currentPage: 1,
                locations: [],
                departments:['java研发','C++研发','人事部','财务部','运维部','其他'],
                form: {
                    assetsId: '',
                    assetsName: '',
                    assetNumber: '',
                    util: '',
                    format: '',
                    location: '',
                    user: '',
                    department: '',
                    status: '',
                    remark: ''
                },
                idx: -1
            }
        },
        created() {
            this.getData();
        },
        mounted(){
            this.getDepartment();
            this.getTypeAndLocation();
        },
        methods: {
            getDepartment(){
                this.$axios.get('/base/dept/selectAll').then((res) => {
                    console.log(res.data.list)
                    this.departments = res.data.data;
                })
            },
            getTypeAndLocation(){
                this.$axios.post('/assets/fixedAssets/findAllTypeAndLocation', {
                    headers:
                        {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then((res) => {
                    console.log(res.data.list)
                    this.types = res.data.data.type;
                    this.locations = res.data.data.location;
                })
            },
            // 获取初始数据
            getData() {
                this.$axios.post('/assets/fixedAssets/stock').then((res) => {
                    console.log(res.data)
                    this.tableData = res.data.data.list;
                    this.statistics = res.data.data.count;
                    this.allCount = res.data.data.status;
                })
            },
            OnSearch() {
                if(this.select_department==''&&this.select_number==''&&this.select_name==''&&this.select_location==''&&this.select_status==''&&this.select_user==''){
                    this.getData();
                    return;
                }
                let params = new URLSearchParams();
                params.append("department", this.select_department);
                params.append("assetsNumber", this.select_number);
                params.append("assetsName", this.select_name);
                params.append("location", this.select_location);
                params.append("status", this.select_status);
                params.append("user", this.select_user);
                this.$axios.post('/assets/fixedAssets/selectAssetsStockByCondition', params, {
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

    .handle-input {
        width: 100px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }
</style>
