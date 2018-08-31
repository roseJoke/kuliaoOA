<template>
    <div class="container">
        <el-form label-width="80px" :model="processform" :inline="true" :rules="rules" ref="processform">
            <el-form-item label="流程类别" prop="category">
                <el-input v-model="processform.category" type="text"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit('processform')">创建</el-button>
            </el-form-item>
            <router-link to="/TZChart?id=2">
                <el-button type="default">+新建流程</el-button>
            </router-link>
        </el-form>
        <!--根据流程的列表创建面板-->
        <template v-for="(item,index) in listCatagory">
            <el-collapse accordion>
                <el-collapse-item :title="item.category" name='index'>
                    <div>
                        <el-table :data="item.arr" style="width: 100%">
                            <el-table-column prop="processName" label="流程名称" width="180">
                            </el-table-column>
                            <el-table-column
                                prop="processDefinitionVersion"
                                label="流程版本号"
                                width="180">
                            </el-table-column>
                            <el-table-column
                                label="操作" center>
                                <template slot-scope="scope">
                                    <router-link to="/TZChart">
                                        <el-button size="small" @click="edit(scope.row)">查看并编辑</el-button>
                                    </router-link>
                                    <el-button size="small" type="danger"
                                               @click="handleDelete(scope.$index, scope.row)">删除
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-collapse-item>
            </el-collapse>
        </template>
    </div>

</template>

<script>
    import ElButton from "element-ui/packages/button/src/button";
    import ElForm from "element-ui/packages/form/src/form";
    import ElFormItem from "element-ui/packages/form/src/form-item";
    import ElInput from "element-ui/packages/input/src/input";

    export default {
        components: {
            ElInput,
            ElFormItem,
            ElForm,
            ElButton
        },
        name: "process",
        data() {
            return {
                rules: {
                    category: [
                        {
                            required: true, message: '名称不能为空', trigger: 'change'
                        },
                    ]
                },
                activeNames: '',
                idex: -1,
                processform: {
                    category: '',
                },
                listCatagory: [],

            }
        },
        beforeMount() {
            let that = this;
            console.log('接受后台数据')
            let params = new URLSearchParams();
            params.append('empId', "*");
            that.$axios.all([
                that.$axios.post("/workflow/getProcessGroup"),

                that.$axios.post("/workflow/findLastVersionProcessDefinition", params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })

            ]).then(that.$axios.spread(function (processGroup, processDefinition) {

                // 上面两个请求都完成后，才执行这个回调方法
                for(let i = 0; i < processGroup.data.data.length; i++) {

                    let node = {
                        category: processGroup.data.data[i].groupName,
                        arr: []
                    };
                    that.listCatagory.push(node);

                }


                for(let j = 0; j < that.listCatagory.length; j++) {
                    for(let k = 0; k < processDefinition.data.data.length; k++) {
                        if(processDefinition.data.data[k].category == that.listCatagory[j].category) {
                            let obj = {
                                processName: processDefinition.data.data[k].arr[0].processName,
                                processDefinitionVersion: processDefinition.data.data[k].arr[0].processDefinitionVersion,
                                processKey: processDefinition.data.data[k].arr[0].processKey
                            };
                            that.listCatagory[j].arr.push(obj);
                        }
                    }

                }
            }));
        },
        methods: {
            unique1(array){     // 对象去重方法
                let allArr = [array[0]];    //建立新的临时数组
                for(let i=0;i<array.length;i++){
                    let flag = true;
                    for(let j=0;j<allArr.length;j++){
                        if (array[i].category===null){
                            flag = true;
                        }
                        if(array[i].category ===allArr[j].category){
                            console.log(array[i].arr.toString());
                            //如果属性相同，那么就将对应的对象里的arr里的所有东西添加到分类里面。
                            flag = false;
                        };
                    };
                    if(flag){
                        allArr.push(array[i]);
                    };
                };
                return allArr;
            },

            //查看并编辑流程定义
            edit(item) {
                this.$router.push({path:'/baseProcess/flowWWWs',query:{processKey:item.processKey
                    }});
                console.log('当前流程定义key '+item.processKey);
            },

            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let that = this;
                        // 在这里需要判读一下添加进来的类别是否已知的类别有冲突。
                        for (let i = 0; i < that.listCatagory.length; i++) {
                            if (that.listCatagory[i].category === that.processform.category) {
                                return
                            }
                        }

                        let parameter = {
                            name: that.processform.category
                        }

                        this.$axios.post('/workflow/createProcessGroup',parameter).then(res => {
                            console.log(res.data);

                            let processObj = {
                                category: that.processform.category,
                                arr: []
                            };
                            that.listCatagory.push(processObj);

                        }).catch(error => console.log(error));


                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            handleDelete(index, row) {
                let that = this;
                that.idx = index;
                that.listCatagory[that.idx].arr.splice(that.idx, 1);
                this.$message.success('删除成功');
            }
        },
        computed: {},
        watch: {},
    }
</script>

<style scoped>
    .el-row {
        margin-bottom: 20px;
    }
    :last-child {
        margin-bottom: 0;
    }
    .el-col {
        border-radius: 4px;
    }

    .bg-purple-dark {

    }

    .bg-purple {
        background: #d3dce6;
    }

    .bg-purple-light {
        background: #e5e9f2;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 36px;
    }

    .row-bg {
        padding: 10px 0;
        background-color: #f9fafc;
    }
</style>
