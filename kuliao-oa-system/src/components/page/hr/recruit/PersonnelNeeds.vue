<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 人员需求</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_name"  placeholder="员工编号/姓名" class="handle-input mr10" clearable></el-input>
                <el-select v-model="select_department" placeholder="部门" class="handle-select mr10">
                    <el-option  v-for="item in options" :key="item.value" :label="item.value" :value="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
            </div>
            <el-table :data="performance_data" border stripe style="width: 100%" >
                <el-table-column prop="apply_department" label="申请部门"  align="center">
                </el-table-column>
                <el-table-column prop="post_name" label="岗位名称" align="center">
                </el-table-column>
                <el-table-column prop="demand_number" label="需求人数" align="center">
                </el-table-column>
                <el-table-column prop="post_time" label="到岗时间" align="center">
                </el-table-column>
                <el-table-column prop="grade" label="等级" align="center">
                </el-table-column>
                <el-table-column prop="type" label="类型" align="center">
                </el-table-column>
                <el-table-column prop="executor" label="执行人" align="center" >
                </el-table-column>
                <el-table-column prop="person_liable" label=面试责任人 align="center" >
                </el-table-column>
                <el-table-column label="操作" width="290">
                    <template slot-scope="scope">
                        <el-popover
                            placement="right"
                            width="500"
                            trigger="click">
                            <el-table :data="tableData5"
                                      :show-header="false">
                                <el-table-column width="450" property="date">
                                    <template slot-scope="props">
                                        <el-form label-position="left" inline class="demo-table-expand">
                                            <el-form-item label="申请部门">
                                                <span>{{ props.row.dept }}</span>
                                            </el-form-item>
                                            <el-form-item label="岗位名称">
                                                <span>{{ props.row.post }}</span>
                                            </el-form-item>
                                            <el-form-item label="需求人数">
                                                <span>{{ props.row.count}}</span>
                                            </el-form-item>
                                            <el-form-item label="到岗时间">
                                                <span>{{ props.row.end_time }}</span>
                                            </el-form-item>
                                            <el-form-item label="需求等级">
                                                <span>{{ props.row.grade }}</span>
                                            </el-form-item>
                                            <el-form-item label="需求类型">
                                                <span>{{ props.row.type }}</span>
                                            </el-form-item>
                                            <el-form-item label="招聘执行人">
                                                <span>{{ props.row.executor }}</span>
                                            </el-form-item>
                                            <el-form-item label="岗位面试负责人">
                                                <span>{{ props.row.transactor_name }}</span>
                                            </el-form-item>
                                            <el-form-item label="岗位要求">
                                                <span>{{ props.row.require }}</span>
                                            </el-form-item>
                                            <el-form-item label="部门经理意见">
                                                <span>{{ props.row.opinion_dept_manager }}</span>
                                            </el-form-item>
                                            <el-form-item label="行政人事部意见">
                                                <span>{{ props.row.opinion_personnel }}</span>
                                            </el-form-item>
                                            <el-form-item label="总经理意见">
                                                <span>{{ props.row.opinion_top_manager }}</span>
                                            </el-form-item>
                                            <el-form-item label="备注">
                                                <span>{{ props.row.des }}</span>
                                            </el-form-item>
                                        </el-form>
                                    </template>

                                </el-table-column>
                            </el-table>
                    <el-button slot="reference" @click="editVisible = false">查看详情</el-button>
                            <el-button slot="reference" @click="to('Interview')">面试安排</el-button>
                            <el-button slot="reference" >需求完成</el-button>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :total="100">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    export default {

        data(){
            return{
                tableData5: [{
                    count: '2',
                    dept: '研发部',
                    grade: '紧急',
                    executor: '张三',
                    type: '缺员补充',
                    post: 'Java',
                    end_time: '2018-02-03',
                    transactor_name:'李四',
                    require:'适得府君书的环境氛围飞牛网范围内范围分为覅文件而非',
                    opinion_dept_manager:'同意，三天后开始',
                    opinion_personnel:'同意，三天后开始',
                    opinion_top_manager:'同意，三天后开始',
                    des:'无',
                }],
                //部门下拉框
                options:[{
                    value:'所有部门'
                },{
                    value:'人事部'
                },{
                    value:'财务部'
                },{
                    value:'研发部'
                }],
                select_department:'',
                //输入框
                input_staff:'',
                //表格数据
                performance_data:[{
                    apply_department:'研发部',
                    post_name:'java',
                    demand_number:'2',
                    post_time:'2018-02-03',
                    type:'缺员补充',
                    grade:'紧急',
                    executor:'张三',
                    person_liable:'李四'
                },{
                    apply_department:'研发部',
                    post_name:'java',
                    demand_number:'2',
                    post_time:'2018-02-03',
                    type:'缺员补充',
                    grade:'紧急',
                    executor:'张三',
                    person_liable:'李四'
                },{
                    apply_department:'研发部',
                    post_name:'java',
                    demand_number:'2',
                    post_time:'2018-02-03',
                    type:'缺员补充',
                    grade:'紧急',
                    executor:'张三',
                    person_liable:'李四'
                },{
                    apply_department:'研发部',
                    post_name:'java',
                    demand_number:'2',
                    post_time:'2018-02-03',
                    type:'缺员补充',
                    grade:'紧急',
                    executor:'张三',
                    person_liable:'李四'
                },{
                    apply_department:'研发部',
                    post_name:'java',
                    demand_number:'2',
                    post_time:'2018-02-03',
                    type:'缺员补充',
                    grade:'紧急',
                    executor:'张三',
                    person_liable:'李四'
                }
                ]
            }
        },
        methods:{
            to(send){
                this.$router.push('/hr_recruit/'+send);
            },

            update(e){
                let file = e.target.files[0];
                let param = new FormData(); //创建form对象
                param.append('file',file);//通过append向form对象添加数据
                console.log(param.get('file')); //FormData私有类对象，访问不到，可以通过get判断值是否传进去
                let config = {
                    // headers:{'Content-Type':'multipart/form-data'}
                }; //添加请求头
                this.$axios.post('/upload',param,config)
                    .then(response=>{
                        console.log(response.data);
                    })
            }
        }


    }
</script>

<style >

    .demo-table-expand {
        font-size: 0;
    }
    .demo-table-expand label {
        width: 120px;
        color: #99a9bf;
    }
    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 120px;
        display: inline-block;
    }
</style>
