<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 所有流程</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-table :data="performance_data" border stripe style="width: 100%">
                <el-table-column prop="activateData.processName" label="流程名称">
                </el-table-column>
                <el-table-column prop="activateData.activityUserId" label="发起人">
                </el-table-column>
                <el-table-column prop="activateData.activityUserId" label="发起次数">
                </el-table-column>
                <el-table-column label="审批">
                    <template slot-scope="scope">
                        <el-button
                            @click.native.prevent="approval(scope.$index)"
                            type="text"
                            size="mini">
                            审批
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>
<script>
    export default {
        name: "allprocess",
        data(){
            return{
                performance_data:''
            }
        },
        created(){
            let that = this;

        },
    }
</script>

<style scoped>

</style>
