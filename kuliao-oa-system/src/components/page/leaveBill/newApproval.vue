<template>
    <div>
    <div class="crumbs">
        <el-breadcrumb separator="/">
            <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
            <el-breadcrumb-item><i class="el-icon-tickets"></i> 发起审批</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
    <div class="container">
        <!--根据流程的列表创建面板-->
        <template v-for="(item,index) in listCatagory">
            <el-collapse accordion>
                <el-collapse-item :title="item.category" name='index'>
                    <div>
                        <el-table :data="item.arr" style="width: 100%">
                            <el-table-column prop="processName" label="流程名称">
                            </el-table-column>
                            <el-table-column
                                label="操作" center>
                                <template slot-scope="scope">
                                    <el-button size="mini"type="text"
                                               @click.native.prevent="startProcess()">发起流程
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-collapse-item>
            </el-collapse>
        </template>
    </div>
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
                listCatagory: [
                    {
                        category: '人力资源 ',
                        arr: [
                            {
                                processName: '请假',
                                processDefinitionVersion: ''
                            }, {
                                processName: '出差',
                                processDefinitionVersion: ''
                            }, {
                                processName: '取款',
                                processDefinitionVersion: ''
                            }, {
                                processName: '赈灾',
                                processDefinitionVersion: ''
                            }
                        ]
                    }
                ],
            }
        },
        beforeMount() {
            let params = new URLSearchParams();
            let that = this;
            params.append('empId', "1");
            this.$axios.post("/workflow/findLastVersionProcessDefinition", params,{
                headers:{
                    'Content-Type' : 'application/x-www-form-urlencoded'
                }
            }).then(res => {
                console.log(res.data);
                that.listCatagory=res.data.data;
            })
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
            // edit(item) {
            //     this.$router.push({path:'/baseProcess/flowWWWs',query:{processKey:item.processKey
            //         }});
            //     console.log('当前流程定义key '+item.processKey);
            // },
            startProcess() { //分别是 外层表的当前行数索引，内嵌表的当前行数索引，跳转路径
                let that = this;
                // console.log(index+'|||'+propsIndex+'|||'+send);
                // let processKey = this.listCatagory[propsIndex].arr[index].processKey;
                this.$router.push({path:'/leaveBill_leaveBillForm'});
                const h = that.$createElement;
                that.$notify({
                    title:'温馨提示',
                    message:h('i',{style:'color:teal'},'请完善具体的请假信息。')
                });
                // this.$router.push('/'+send);
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
