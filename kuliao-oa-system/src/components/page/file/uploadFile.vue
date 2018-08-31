<template>
    <div>
        <el-upload
            class="upload-demo"
            action="/upload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-error="uploadError"
            multiple
            :data="{dirId:this.id}"
            :on-exceed="handleExceed"
            :file-list="fileList">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">啥都可以上传，限制400MB</div>
        </el-upload>
    </div>
</template>
<!--这是一个上传文件的组件   示例： <uoload-file :id="file.id"></uoload-file>     id为文件夹的ID，  -->
<script>
    export default {
        data() {
            return {
                fileList: [/*{
                    name: 'food.jpeg',
                    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                }, {
                    name: 'food2.jpeg',
                    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                }*/]
            };
        },
        props:['id'],
        methods: {
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
            beforeRemove(file, fileList) {
             //   return this.$confirm(`确定移除 ${ file.name }？`);
            },
            uploadError(err, file, fileList){
                console.log(err);
                this.$message({
                    showClose: true,
                    message:file.name+"上传失败！",
                    type: "error"
                });
            }
        }
    }
</script>

<style scoped>

</style>
