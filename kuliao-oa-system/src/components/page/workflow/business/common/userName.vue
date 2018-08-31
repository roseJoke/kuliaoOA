<template>
    <span>
        <span style="float: left">{{ user.username }}</span>
         <span style="float: right; color: #8492a6; font-size: 13px">{{ user.empNo }}</span>
         <span style="float: right;margin-right: 5px">{{ user.deptName }}</span>
    </span>
</template>

<script>
    export default {
        props: ['id', "empNo"],
        data() {
            return {
                user: {
                    code: "",
                    deptName: "",
                    empNo: "",
                    userId: null,
                    username: ""
                },

            }
        },
        created() {
            if (this.empNo) {
                let params = new URLSearchParams();
                params.append('empNo', this.empNo);
                this.$axios.post("/base/user/query", params).then((res) => {
                    if(res.data.data[0]) {
                        this.user = res.data.data[0];
                    }
                });
            }
        },
        watch:{
            empNo:function (val) {
                if (val) {
                    let params = new URLSearchParams();
                    params.append('empNo', val);
                    this.$axios.post("/base/user/query", params).then((res) => {
                        if(res.data.data[0]) {
                            this.user = res.data.data[0];
                        }
                    });
                }
            }
        }
    }
</script>

<style scoped>

</style>
