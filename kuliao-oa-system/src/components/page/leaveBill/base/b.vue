<template>
    <div>
        <h1>BBBBB</h1>
        <h2>id = {{id}}</h2>
        <h1>{{a}}</h1>
        <el-form>
        <el-button
            @click.native.prevent="to('a')"
            type="text"
            size="mini">
            查看
        </el-button>
        </el-form>
    </div>
</template>
<script>
    export default {
        name: "b",
        props:['id','a'],
        mounted(){
            console.log(this.$route.query.id);
            console.log(this.$route.query.name);
        },
        methods: {
            to(send) {
                this.$router.push({path:'/baseTest/' + send,query:{
                        id:111
                    }});

            }
        }
    }
</script>

<style scoped>

</style>

