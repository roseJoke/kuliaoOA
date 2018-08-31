<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程中心</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 发起审批</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-collapse accordion>
                <template v-for="process in processArr">
                    <el-collapse-item>
                        <template slot="title">
                            {{process.groupName}}
                        </template>
                        <div>
                            <el-table :data="process.processList"style="width: 100%">
                                <el-table-column prop="processName" label="流程名称">
                                </el-table-column>
                                <el-table-column
                                    label="操作" center>
                                    <template slot-scope="scope">
                                        <el-button size="mini" type="text"
                                                   @click.native.prevent="startProcess(scope.row.processId)">发起流程
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </el-collapse-item>
                </template>
            </el-collapse>
        </div>
    </div>
</template>
<script>

    export default {
        components: {
        },
        name: "process",
        data() {
            return {
                processArr:[],//处理过后的数据
                AllProcess: [],//后台拿过来的数据
                idx: -1, //当前表格的索引
            }
        },
        beforeMount() {
            let that = this;
            this.$axios.post("/workflow/definition/queryAll").then(res => {
                that.AllProcess = res.data.data;
                //获取所有的分组并去重
                var allgroup = that.AllProcess.map(function (item) {
                    return item.groupName;
                }).filter(function (element, index, self) {
                    return self.indexOf(element) === index;
                });
                // 遍历整个分组，根据分组拿到相应分组的流程。
                for (let i = 0; i < allgroup.length; i++) {
                  let Listprocess = that.AllProcess.filter(function (item) {
                    return item.groupName === allgroup[i];
                  })
                    let processobj = {
                      groupName:allgroup[i],
                      processList:Listprocess
                    }
                  that.processArr.push(processobj);
                }
            }).catch(error => {
                console.log(error)
            })
        },
        methods: {
            // 发起流程
            startProcess(processId ) { //分别是 外层表的当前行数索引，内嵌表的当前行数索引，跳转路径
                let that = this;
                // 在发起流程的那个的时候，需要判断一下是否需要制定人
                // processType  === "D"  此时需要每一个审批人出现一个公共的页面
                //1.拿到流程实例id
                console.log('当前实例的id'+processId)
                let  item1 = that.AllProcess.filter(function (item) {
                    return item.processId === processId;
                })
                //2.接下来第二步拿到当前的数据对象。
                let processType = item1[0].processType;
                let nextPath = item1[0].relationUrl;
                console.log(nextPath)
                that.$router.push({path: '/work/launch'+nextPath, query: {'processId': processId, 'nextUrl': nextPath}})
               /* if (processType === 'S') {
                    //直接跳转到业务界面，需要包含流程id
                    that.$router.push({path: nextPath, query: {'processId': processId}});
                } else if (processType === 'D') {
                    //先跳转至指定人页面，也需要包含流程id,同时还需要保存填写业务信息的路径。
                    that.$router.push({path: '/pointAt', query: {'processId': processId, 'nextUrl': nextPath}})
                }else{
                    console.log('参数有误')
                }*/
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
