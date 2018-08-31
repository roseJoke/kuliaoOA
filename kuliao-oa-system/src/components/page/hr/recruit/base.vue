<template>

        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        methods:{
            to(send){
                this.$router.push('/hr_recruit/'+send);
            },
            update(e){
                let file = e.target.files[0];
                let param = new FormData(); //创建form对象
                param.append('file',file);//通过append向form对象添加数据
                console.log(param.get('file')); //FormData私有类对象，访问不到，可以通过get判断值是否传进去
                let config = {
                   // headers:{'Content-Type':'multipart/form-data'}
                }; //添加请求头
                this.$axios.post('/upload',param,config)
                    .then(response=>{
                        console.log(response.data);
                    })
            }
        }
    }
</script>

<style scoped>

</style>
