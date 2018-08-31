<template>
    <div>
        <el-transfer
            style="text-align: left; display: inline-block"
            v-model="assign"
            filterable
            :left-default-checked="[2]"
            :right-default-checked="[1]"
            :titles="['可选人员列表', '已选人员名单']"
            :button-texts="['取消', '指定']"
            :format="{
             noChecked: '${total}',
             hasChecked: '${checked}/${total}'}"
            :data="empdata">
        </el-transfer>
    </div>
</template>

<script>
    export default {
        data(){
            const generateData = _ => {
                const data = [];
                    data.push({
                        key: 0,// 员工编号
                        label: `员工`//员工姓名
                    });
                return data;
            };
            return {
                empdata: generateData(),
                assign: [0],
                empassign:{}
            };
        },
        methods:{
            //获取当前任务节点完成审批的人员姓名

            // 查询当前任务节点可以参与审批的人员列表
            //后台传过来的是员工编号，然后我们需要根据便好查出员工姓名。
            getAllTaskEmpList(){
                    let that = this;
                    that.$axios.post("/workflow/process/queryStep/").then((res)=>{
                        // 根据userId组织人员选择列表，若列表只有一个人则不能编辑.只能为此人
                    }).catch(error => {
                        console.log('有错误')
                    })
            }
        }
    }
</script>
<style>

</style>
