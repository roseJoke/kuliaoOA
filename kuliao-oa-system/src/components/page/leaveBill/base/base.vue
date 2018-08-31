<template>
    <div>
        <transition name="fade">
            <router-view :id="id" :a="true"></router-view>
        </transition>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                id: 3
            }
        },
        methods: {
            to(send) {
                this.mid = this.mid + 1;
                this.$router.push({path:'/baseTest/' + send,query:{
                        id:111,
                        name:'aaa'
                    }});

            }
        },
        props: ['id'],
        watch: {
            mid(val) {
                console.log(val)
                this.$emit("on-id-change", val);//③组件内对myResult变更后向外部发送事件通知
            }
        }
    }
</script>

<style scoped>

</style>
