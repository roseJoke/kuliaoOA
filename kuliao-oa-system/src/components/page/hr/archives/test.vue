<template>
    <div>
        <input v-model="staffId"/>
        <input v-model="staffName"/>
        <div class="block">
            <span class="demonstration">默认</span>
            <el-date-picker
                v-model="month"
                type="date"
                value-format="yyyyMM"
                placeholder="选择日期">
            </el-date-picker>
        </div>
        <button @click="onSubmit">提交</button>
        <el-upload
            class="upload-demo"
            action="/upload"




            multiple
            :data="{path:'主页/转正考评/转正表格'}"

            :on-success="aa"
           >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">啥都可以上传，限制400MB</div>
        </el-upload>
    </div>


</template>

<script>
    export default {
        data(){
            return{
                abc:true,
                name:null,
                sex:null,
                staffId:null,
                staffName:null,
                month :null,
                pageNum:null,
            }
        },
        methods:{
            aa(response, file, fileList){
               console.log(response);
            },
            onSubmit(){
                console.log(this.name)
                let params = new URLSearchParams();
                params.append('staffName',this.staffName);
                params.append('staffId',this.staffId);
                params.append('month',this.month);
                console.log(params.toString());
                this.$axios.post("/hr/archives/emp/",params,{
                    headers:{
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).then(res => {
                    console.log(res.data)
                });
            }
        }
    }
</script>

<style scoped>

</style>
