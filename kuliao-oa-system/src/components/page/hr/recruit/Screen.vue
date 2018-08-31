<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 筛选简历</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_sex"  placeholder="性别" class="handle-input mr10" clearable></el-input>
                <el-input v-model="input_Education"  placeholder="学历" class="handle-input mr10" clearable></el-input>
                <el-select v-model="select_post" placeholder="岗位" class="handle-select mr10">
                    <el-option  v-for="item in options" :key="item.value" :label="item.value" :value="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
            </div>
            <el-table :data="performance_data" border stripe style="width: 100%" >
                <el-table-column type="index" :index="indexMethod" label="序号"align="center">
                </el-table-column>
                <el-table-column prop="name" label="姓名" align="center">
                </el-table-column>
                <el-table-column prop="time" label="面试时间"  align="center">
                </el-table-column>
                <el-table-column prop="sex" label="性别" align="center">
                </el-table-column>
                <el-table-column prop="age" label="年龄" align="center">
                </el-table-column>
                <el-table-column prop="post" label="应聘岗位" align="center">
                </el-table-column>
                <el-table-column prop="Education" label="学历" align="center">
                </el-table-column>
                <el-table-column prop="major" label="专业" align="center">
                </el-table-column>
                <el-table-column prop="University" label="毕业院校" align="center">
                </el-table-column>
                <el-table-column prop="level" label="面试轮数" align="center" >
                </el-table-column>
                <el-table-column prop="phone" label="电话" align="center">
                </el-table-column>
                <el-table-column prop="email" label="邮箱" align="center">
                </el-table-column>
                <el-table-column prop="des" label="备注" align="center">
                </el-table-column>
                <el-table-column label="面试意见"  align="center" type="expand" width="100px">
                    <template slot-scope="scope">
                        <el-form>
                            <el-form-item label="直属领导意见">
                                <span>{{scope.row.executor}}</span>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <!--<el-table-column label="最终考评结果" align="center">-->
                <!--</el-table-column>-->
                <el-table-column label="操作" width="160">
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" type="text" size="small">查看简历</el-button>
                        <el-button type="text" @click="dialogFormVisible = true">面试安排</el-button>

                        <el-dialog title="面试安排" :visible.sync="dialogFormVisible" width="390px">
                            <el-form ref="form" :model="form" label-width="90px">
                                <el-form-item label="面试负责人" label-width="85px">
                                    <el-select v-model="form.region" placeholder="请选择负责人">
                                        <el-option label="李四" value="shanghai"></el-option>
                                        <el-option label="王五" value="beijing"></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="面试时间"label-width="70px">
                                    <el-col :span="11">
                                        <el-date-picker type="date" placeholder="选择日期" v-model="form.date1" style="width: 100%;"></el-date-picker>
                                    </el-col>
                                    <el-col class="line" :span="1">-</el-col>
                                    <el-col :span="11">
                                        <el-time-picker type="fixed-time" placeholder="选择时间" v-model="form.date2" style="width: 100%;"></el-time-picker>
                                    </el-col>
                                </el-form-item>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogFormVisible = false">取 消</el-button>
                                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
                            </div>
                        </el-dialog>
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


                dialogTableVisible: false,
                dialogFormVisible: false,
                form: {
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: false,
                    type: [],
                    resource: '',
                    desc: ''
                },
                //部门下拉框
                options:[{
                    value:'Java'
                },{
                    value:'C++'
                },{
                    value:'C#'
                },],
                select_department:'',
                //输入框
                input_staff:'',
                //表格数据
                performance_data:[{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    Education :'本科',
                    age:'2',
                    major:'软件工程',
                    level:'2',
                    University:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    Education :'本科',
                    age:'2',
                    major:'软件工程',
                    level:'2',
                    University:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    Education :'本科',
                    age:'2',
                    major:'软件工程',
                    level:'2',
                    University:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    Education :'本科',
                    age:'2',
                    major:'软件工程',
                    level:'2',
                    University:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },
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

<style scoped>

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
