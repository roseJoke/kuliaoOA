<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 员工福利</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-select v-model="welfareType" placeholder="请选择福利类型">
                    <el-option value=""></el-option>
                    <el-option label="生日福利" value="生日福利"></el-option>
                    <el-option label="节假日福利" value="节假日福利"></el-option>
                </el-select>

                <el-button type="primary" size="mini" icon="el-icon-search" @click="selectWelfare">搜索</el-button>
            </div>

            <div class="handle-box">
                <el-button type="primary" @click="addwelfare('addform')"
                           style="float:right;margin-bottom: 20px;margin-right: 4%">[ + ] 新建福利
                </el-button>
            </div>
            <el-table :data="tabledata.slice((currentPage-1)*pageSize,currentPage*pageSize)" border stripe
                      style="width: 100%" v-loading="loading">
                <template v-for="i1 in data1">
                    <template v-if="i1.des==='mycomponent'">
                        <el-table-column
                            label="操作"
                            width="200"
                            data-date-picker="activated">
                            <template slot-scope="scope">
                                <el-button
                                    :prop="i1.des"
                                    size="mini"
                                    @click="handleEdit(scope.$index, scope.row)">编辑
                                    <!--updateWelfareById(scope.row.welfare_id)-->
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="danger"
                                    @click="handleDelete(scope.$index)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </template>
                    <template v-else slot-scope="scope">
                        <el-table-column
                            :show-overflow-tooltip="true"
                            :type="i1.type"
                            :format="i1.fmat"
                            v-bind:label="i1.table_data_item"
                            v-bind:width="i1.len"
                            v-bind:prop="i1.des">
                        </el-table-column>
                    </template>
                </template>
                <!--   <el-table-column label="操作" width="200">
                       <template slot-scope="scope">
                           <el-button
                               size="mini"
                               @click="updateWelfareById(i1.welfare_id)">编辑
                           </el-button>
                           <el-button
                               size="mini"
                               type="danger"
                               @click="changeWelfareState(welfare_id)">删除
                           </el-button>
                       </template>
                   </el-table-column>
   -->
            </el-table>

            <div class="pagination">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[7,8,9,10]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="tabledata.length">
                </el-pagination>
            </div>

            <el-row :gutter="20">
                <el-col :span="18" style="float: right; color:darkgray">
                    <el-col :span="5" :offset="5"><span><p>生日福利：<span
                        style="color:black;font-style: oblique">{{BirthdayCount}}</span>次</p> <p> 生日福利支出总额：<span
                        style="color:black;font-style: oblique">{{BirthdayCost}}</span>元</p> </span></el-col>
                    <el-col :span="5">
                        <p>节假日福利：
                            <span style="color:black;font-style: oblique">{{HolidayCount}}</span>次</p>
                        <p>节假日福利支出：
                            <span style="color:black;font-style: oblique">{{HolidayCost}}</span>元</p></el-col>
                    <el-col :span="5" :offset="2"><span>总支出：<span
                        style="color:black;font-style: oblique">{{sumCost}}</span>元   </span>
                    </el-col>
                </el-col>
            </el-row>
        </div>
        <!--  dialog 弹框-->
        <el-dialog title="填写员工福利信息" :visible.sync="dialogvalue1">
            <el-form ref="addform1" :rules="rrrr" :model="addform" label-width="80px">
                <!-- <el-form-item label="活动名称">
                     <el-input v-model="form.name"></el-input>
                 </el-form-item>-->
                <el-form-item prop="welfareType" label="福利类型">
                    <el-select v-model="addform.welfareType" placeholder="请选择福利类型">
                        <el-option label="生日福利" value="生日福利"></el-option>
                        <el-option label="节假日福利" value="节假日福利"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item prop="giftDescription" label="物品详情">
                    <el-input v-model="addform.giftDescription"></el-input>
                </el-form-item>

                <el-form-item prop="releaseTime" label="发放时间">
                    <el-date-picker
                        type="date"
                        v-model="addform.releaseTime"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日"
                        value-format="yyyyMMdd">
                    </el-date-picker>
                </el-form-item>

                <el-form-item prop="specificHoliday" label="节假日">
                    <el-input v-model="addform.specificHoliday"></el-input>
                </el-form-item>

                <el-form-item prop="unitPrice" label="单  价">
                    <el-input v-model.number="addform.unitPrice"></el-input>
                </el-form-item>

                <el-form-item prop="wNumber" label="数  量">
                    <el-input v-model.number="addform.wNumber"></el-input>
                </el-form-item>

                <el-form-item label="备  注">
                    <el-input type="textarea" v-model="addform.remark"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit('addform1')">立即创建</el-button>
                    <el-button @click="closedialogv1('addform1')">取消</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>

        <el-dialog title="编辑员工福利信息" :visible.sync="dialogvalue2">
            <el-form ref="addform2" :rules="rrrr" :model="addform" label-width="80px">
                <!-- <el-form-item label="活动名称">
                     <el-input v-model="form.name"></el-input>
                 </el-form-item>-->
                <el-form-item prop="welfareType" label="福利类型">
                    <el-select v-model="addform.welfareType">
                        <el-option label="生日福利" value="生日福利"></el-option>
                        <el-option label="节假日福利" value="节假日福利"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item prop="giftDescription" label="物品详情">
                    <el-input v-model="addform.giftDescription"></el-input>
                </el-form-item>

                <el-form-item prop="releaseTime" label="发放时间">
                    <el-date-picker
                        type="date"
                        v-model="addform.releaseTime"
                        placeholder="选择日期"
                        format="yyyy年MM月dd日"
                        value-format="yyyyMMdd">
                    </el-date-picker>
                </el-form-item>

                <el-form-item prop="specificHoliday" label="节假日">
                    <el-input v-model="addform.specificHoliday"></el-input>
                </el-form-item>


                <el-form-item prop="unitPrice" label="单  价">
                    <el-input v-model.number="addform.unitPrice"></el-input>
                </el-form-item>

                <el-form-item prop="wNumber" label="数  量">
                    <el-input v-model.number="addform.wNumber"></el-input>
                </el-form-item>


                <el-form-item label="备  注">
                    <el-input type="textarea" v-model="addform.remark"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="updateWelfareById('addform2')">提交更改</el-button>
                    <el-button @click="closedialogv2('addform2')">取消</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>


    </div>
</template>

<script>

    export default {
        data() {
            var checkNum = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('数量不能为空'));
                }
                setTimeout(() => {
                    if (!Number.isInteger(value)) {
                        callback(new Error('老妹儿，请输入整数'));
                    } else {
                        if (value < 0) {
                            callback(new Error('嘿，数量要大于0'));
                        } else {
                            callback();
                        }
                    }
                }, 1000);
            };
            return {
                data1: [
                    {
                        table_data_item: '福利编号',
                        //   len: 160,
                        des: 'id'
                    },
                    {
                        table_data_item: '福利类型',
                        //    len: 160,
                        des: 'welfareType',
                    },
                    {
                        table_data_item: '物品详情',
                        //  len: 200,
                        des: 'giftDescription',
                    },
                    {
                        table_data_item: '发放时间',
                        //  len: 165,
                        des: 'releaseTime',
                    },
                    {
                        table_data_item: '具体节假日',
                        //   len: 165,
                        des: 'specificHoliday',
                    },
                    {
                        table_data_item: '单价',
                        len: 140,
                        des: 'unitPrice',
                    },
                    {
                        table_data_item: '数量',
                        len: 140,
                        des: 'wNumber',
                    },
                    {
                        table_data_item: '备注',
                        //   len: 180,
                        des: 'remark',
                    },
                    {
                        table_data_item: '操作',
                        //    len: 200,
                        des: 'mycomponent'
                    }
                ],                                  //表数据名数组
                tabledata: [],                             //表数据集合
                dialogvalue1: false,                    //添加福利对话框
                dialogvalue2: false,                    //编辑福利对话框
                addform: {
                    welfareType: '',
                    giftDescription: '',
                    releaseTime: '',
                    specificHoliday: '',
                    unitPrice: '',
                    wNumber: '',
                    remark: ''

                },           // form表单数据
                formLabelWidth: '120px',
                rrrr: {
                    welfareType: [
                        {required: true, message: '请选择福利类型', trigger: 'blur'}
                    ],

                    giftDescription: [
                        {required: true, message: '请输入物品详情', trigger: 'blur'}
                    ],

                    releaseTime: [
                        {type: 'string', required: true, message: '请选择日期', trigger: 'change'}
                    ],
                    specificHoliday: [
                        {required: true, message: '请输入节假日', trigger: 'blur'}
                    ],
                    unitPrice: [
                        {required: true, message: '请输入单价', trigger: 'blur'},
                        {type: 'number', message: '请输入正确数字', trigger: 'blur'}
                    ],
                    wNumber: [
                        {validator: checkNum, trigger: 'blur'},
                    ]
                }, //表单验证规则
                pageSize: 7,
                currentPage: 1,
                loading: false,
                BirthdayCount: 0,
                HolidayCount: 0,
                BirthdayCost: 0,
                HolidayCost: 0,
                sumCost: 0,
                welfareType: '',

            }
        },
        created() {
            this.selectWelfare();
            this.CountWelfare();
        },
        methods: {

            //获取所有有价值记录
            selectWelfare() {
                this.loading = true;
                var oaWelfare = {};
                oaWelfare['welfareType'] = this.welfareType;     //节假日福利
                this.$axios.post('/hr/OaWelfare/selectWelfare', oaWelfare, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then((res) => {
                    this.loading = false;
                    console.log(res.data.data);
                    this.tabledata = res.data.data;
                })
            },


            /**
             * 新建福利内容
             * @param form
             */
            //进入员工福利填写页面
            addwelfare(form) {
                this.addform = {
                    welfareType: '',
                    giftDescription: '',
                    releaseTime: '',
                    specificHoliday: '',
                    unitPrice: '',
                    wNumber: '',
                    welfareRemark: ''
                }
                this.dialogvalue1 = true;
                //this.$refs[form].clearValidate();
            },
            //添加员工福利
            onSubmit(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        var oaWelfare = {};
                        oaWelfare['welfareType'] = this.addform.welfareType;
                        oaWelfare['giftDescription'] = this.addform.giftDescription;
                        oaWelfare['releaseTime'] = this.addform.releaseTime;
                        oaWelfare['specificHoliday'] = this.addform.specificHoliday;
                        oaWelfare['unitPrice'] = this.addform.unitPrice;
                        oaWelfare['wNumber'] = this.addform.wNumber;
                        oaWelfare['remark'] = this.addform.remark;
                        /* let oaWelfare = new URLSearchParams();
                         oaWelfare.append('welfareType', this.addform.welfare_type);
                         oaWelfare.append('giftDescription', this.addform.gift_description);
                         oaWelfare.append('releaseTime', this.addform.release_time);
                         oaWelfare.append('specificHoliday', this.addform.specific_holiday);
                         oaWelfare.append('unitPrice', this.addform.unit_price);
                         oaWelfare.append('wNumber', this.addform.w_number);
                         oaWelfare.append('remark', this.addform.welfare_remark);*/
                        console.log(oaWelfare);
                        this.$axios.post("/hr/OaWelfare/insertWelfare", oaWelfare, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(res => {
                            if (res.data.code == '000000') {
                                this.$message.success(res.data.msg);
                                //this.formClear('addform1');
                                this.selectWelfare();
                                this.CountWelfare();
                                // this.tabledata.push(this.addform1);
                                this.closedialogv1('addform1');

                            } else {
                                this.$message.error(res.data.data.msg);
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //关闭addform1
            closedialogv1(form) {
                this.form = {
                    welfareType: '',
                    giftDescription: '',
                    releaseTime: '',
                    specificHoliday: '',
                    unitPrice: '',
                    wNumber: '',
                    welfareRemark: ''
                };
                this.$refs[form].clearValidate();
                console.log(form + "添加验证去除")
                this.dialogvalue1 = false;
            },


            /**
             * 删除福利内容
             */
            //福利记录逻辑删除
            handleDelete(index) {
                this.idx = index;
                this.$confirm('您确定要删除该客户信息吗?', '温馨提示', {
                    cancelButtonText: '取消',
                    confirmButtonText: '确定',
                    type: 'warning'
                }).then(() => {
                    this.changeWelfareState(index);
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            changeWelfareState(index) {
                let id = this.tabledata[(this.currentPage - 1) * this.pageSize + index].id;
                this.$axios.post("hr/OaWelfare/updateWelfareStateById", id, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.selectWelfare();
                    this.CountWelfare();
                })
            },


            /**
             * 编辑福利内容
             * @param index
             * @param row
             */
            //回填员工福利记录内容
            handleEdit(index, row) {
                // alert(row.id);
                this.idx = row.id;
                //  const item = this.tabledata[index];
                const item = this.tabledata[(this.currentPage - 1) * this.pageSize + index];
                this.addform = {
                    welfareType: item.welfareType,
                    giftDescription: item.giftDescription,
                    releaseTime:item.releaseTime.substring(0,4)+item.releaseTime.substring(5,7)+item.releaseTime.substring(8,10),
                    specificHoliday: item.specificHoliday,
                    unitPrice: item.unitPrice,
                    wNumber: item.wNumber,
                    remark: item.remark
                }
                this.dialogvalue2 = true;

            },
            //update福利记录
            updateWelfareById(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        var oaWelfare = {};
                        oaWelfare['id'] = this.idx;
                        oaWelfare['welfareType'] = this.addform.welfareType;
                        oaWelfare['giftDescription'] = this.addform.giftDescription;
                        oaWelfare['releaseTime'] = this.addform.releaseTime;
                        oaWelfare['specificHoliday'] = this.addform.specificHoliday;
                        oaWelfare['unitPrice'] = this.addform.unitPrice;
                        oaWelfare['wNumber'] = this.addform.wNumber;
                        oaWelfare['remark'] = this.addform.remark;

                        this.$axios.post("hr/OaWelfare/updateWelfare", oaWelfare, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(res => {
                            if (res.data.code == '000000') {
                                this.selectWelfare();
                                this.CountWelfare();
                                this.$message.success(res.data.msg);
                                this.dialogvalue2 = false;

                            } else {
                                this.$message.error(res.data.data.msg);
                            }
                        })
                    } else {
                        console.log('error update!!');
                        return false;
                    }
                });
            },
            //关闭addform2
            closedialogv2(form) {
                // this.$refs[form].clearValidate();
                this.$refs[form].resetFields();
                console.log(form + "表单验证去除");
                this.dialogvalue2 = false;
                this.addform = {};
            },


            //重置表单
            formClear(form) {
                this.$refs[form].resetFields();
            },

            //配置分页查询
            handleSizeChange: function (size) {
                this.pageSize = size;
            },
            handleCurrentChange: function (currentPage) {
                this.currentPage = currentPage;
            },


            //福利数据统计
            CountWelfare() {
                this.$axios.post('/hr/OaWelfare/selectCount', {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then((res) => {
                    console.log(res.data.data);
                    this.BirthdayCount = res.data.data[1];
                    this.HolidayCount = res.data.data[3];
                    this.BirthdayCost = res.data.data[2];
                    this.HolidayCost = res.data.data[4];
                    this.sumCost = res.data.data[5];
                })
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
        width: 120px;
        display: inline-block;
    }

</style>
