<template>
    <el-steps>
        <el-step v-for="(index, i) in process.stepResult" :key="i" :title="index.stepName"
                 :status="getStatus(index.status)">
        </el-step>

    </el-steps>
</template>

<script>
    export default {
        props: ['instanceId'],
        data() {
            return {
                status: '',
                process: {
                    status: 0,
                    stepResult: []
                }
            }
        },
        created() {
            if (this.instanceId) {
                this.onLoad();
            }
        },
        methods: {
            getStatus(status) {
                console.log("节点" + status)
                if (this.status == 2) return 'success';
                //if(this.status == 3)return'success';
                switch (status) {
                    case '3':
                        return 'error ';
                    case '2':
                        return 'success';
                    default:
                        return 'wait ';
                }

            },
            onLoad() {
                let params = new URLSearchParams();
                params.append('instanceId', this.instanceId);
                this.$axios.post("/workflow/activate/queryByInstanceId", params, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }
                ).then((res) => {
                    console.log("流程查看")
                    console.log(res);
                    this.process = res.data.data;
                });
            }
        },
        watch: {
            instanceId: function (val) {
                this.onLoad();
            }
        }
    }
</script>

<style scoped>

</style>
