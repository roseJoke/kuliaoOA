<template>
    <div id="app">
        <router-view  v-if="isRouterAlive"></router-view>
        <!--路由指定放入组件-->
    </div>
</template>
<script>
    export default {
        provide(){
            return{
                reload: this.reload
            }
        },
        data () {
            return {
                isRouterAlive: true
            }
        },
        methods: {
            reload () {
                this.isRouterAlive = false;
                this.$nextTick(() => (this.isRouterAlive = true))
            }
        }
    }
</script>
<style>
    @import "../static/css/main.css";
    /*@import "../static/css/color-dark.css"; */    /*深色主题*/
    @import "../static/css/theme-green/color-green.css";   /*浅绿色主题*/
</style>
