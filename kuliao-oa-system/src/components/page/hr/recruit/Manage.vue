<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 简历管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="input_sex"  placeholder="性别" class="handle-input mr10" clearable></el-input>
                <el-input v-model="input_education"  placeholder="学历" class="handle-input mr10" clearable></el-input>
                <el-select v-model="select_post" placeholder="岗位" class="handle-select mr10">
                    <el-option  v-for="item in options" :key="item.value" :label="item.value" :value="item.value"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search">搜索</el-button>
                <el-button type="primary" icon="el-icon-add">添加</el-button>
                <el-dialog title="面试安排" :visible.sync="dialogFormVisible" width="390px">
                    <el-form ref="form" :model="form" width="90px">
                        <el-carousel :interval="5000" arrow="always">
                            <el-carousel-item v-for="item in 4" :key="item">
                                <h3>{{ item }}</h3>
                            </el-carousel-item>
                        </el-carousel>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
                    </div>
                </el-dialog>
                <el-button type="primary" icon="el-icon-Import">批量导入</el-button>
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
                <el-table-column prop="education" label="学历" align="center">
                </el-table-column>
                <el-table-column prop="major" label="专业" align="center">
                </el-table-column>
                <el-table-column prop="university" label="毕业院校" align="center">
                </el-table-column>
                <el-table-column prop="phone" label="电话" align="center">
                </el-table-column>
                <el-table-column prop="email" label="邮箱" align="center">
                </el-table-column>
                <el-table-column prop="des" label="备注" align="center">
                </el-table-column>
                <el-table-column label="操作" width="210">
                    <template slot-scope="scope">
                        <el-button  type="text" size="small"  @click="dialogFormVisible = true">查看简历</el-button>
                        <el-dialog title="面试安排" :visible.sync="dialogFormVisible" width="390px">
                            <el-form ref="form" :model="form" width="90px">
                                <el-carousel :interval="5000" arrow="always">
                                    <el-carousel-item v-for="item in 4" :key="item">
                                        <h3>{{ item }}</h3>
                                    </el-carousel-item>
                                </el-carousel>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogFormVisible = false">取 消</el-button>
                                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
                            </div>
                        </el-dialog>
                        <el-button
                            size="mini"
                            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
                    education :'本科',
                    age:'2',
                    major:'软件工程',
                    university:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    education :'本科',
                    age:'2',
                    major:'软件工程',

                    university:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    education :'本科',
                    age:'2',
                    major:'软件工程',
                    university:'管他什么大学',
                    post:'java工程师',
                    des:'没用',
                },{
                    time:'2018-02-03',
                    transactor_name:'王大澳',
                    name:'丽萨',
                    sex:'女',
                    phone:'137xxxxxx',
                    email:'10464543374@qq.com',
                    education :'本科',
                    age:'2',
                    major:'软件工程',
                    university:'管他什么大学',
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
            },

            handleEdit(index, row) {
                console.log(index, row);
            },
            handleDelete(index, row) {
                console.log(index, row);
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
    .el-carousel__item h3 {
        color: #475669;
        font-size: 18px;
        opacity: 0.75;
        line-height: 300px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

   .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }
</style>
