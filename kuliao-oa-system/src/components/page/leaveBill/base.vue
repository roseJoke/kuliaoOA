<template>
    <div>
        <transition name="fade">
            <router-view></router-view>
        </transition>
    </div>
</template>

<script>
    export default {
        methods: {
            to(send) {
                this.mid = this.mid + 1;
                this.$router.push({
                    path: '/baseTest/' + send, query: {
                        id: 111,
                        name: 'aaa'
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>
