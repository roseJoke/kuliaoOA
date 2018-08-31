<template>
    <div>
        <h1 @click="$router.back(-1)">aaaaaa</h1>
        <h2>id = {{id}}</h2>
    </div>

</template>

<script>
    export default {
        name: "a",
        props:['id'],

        mounted(){
            console.log(this.$route.query.id);
            console.log(this.$route.query.name);
        },
    }
</script>

<style scoped>

</style>
