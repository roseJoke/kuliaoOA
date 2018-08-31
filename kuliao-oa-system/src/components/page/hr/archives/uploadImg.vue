<template>
    <div>
        <div class="crop-demo">
            <!-- <img :src="cropImg" class="pre-img">-->
            <div class="crop-demo-btn"><img :src="cropImg" class="pre-img">
                <input class="crop-input pre-img" type="file" name="image" accept="image/*" @change="setImage"/>
            </div>
        </div>

        <el-dialog title="裁剪图片" :visible.sync="dialogVisible" width="30%">
            <vue-cropper ref='cropper' :src="imgSrc" :ready="cropImage" :zoom="cropImage" :cropmove="cropImage"
                         style="width:100%;height:300px;"></vue-cropper>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="cancelCrop">取 消</el-button>
                    <el-button type="primary" @click="upImg">确 定</el-button>
                </span>
        </el-dialog>
    </div>
</template>

<script>
    import VueCropper from 'vue-cropperjs';

    export default {
        props: ['img_url', 'img_id', 'img_path'],
        data: function () {
            return {
                //defaultSrc: './static/img/timg.jpg',
                defaultSrc: "",//this.img,
                fileList: [],
                imgSrc: null,
                cropImg: '',
                dialogVisible: false,
                targetImg: null,
            }
        },
        components: {
            VueCropper
        },
        created() {
            console.log(this.img_url)
            let that = this;
            if (this.img_url) {
                console.log(this.img_url)
                this.cropImg = this.img_url;
            } else if (this.img_path) {
                console.log(this.img_path)
                this.$axios.get("/file/getFile", {
                    params: {
                        path: this.img_path
                    }
                }).then(res => {
                    that.cropImg = res.data.data.downloadUrl;
                })

            } else if (this.img_id) {
                console.log(this.img_id)
                this.$axios.get("/file/getFile", {
                    params: {
                        id: that.img_id
                    }
                }).then(res => {
                    console.log(res.data);
                    console.log(res.data.data.downloadUrl);

                    that.cropImg = res.data.data.downloadUrl;
                    console.log(that.cropImg);
                })
            } else {
                console.log("啥都没");
                this.cropImg = this.defaultSrc;
            }
        },
        methods: {
            setImage(e) {
                const file = e.target.files[0];
                if (!file.type.includes('image/')) {
                    return;
                }
                const reader = new FileReader();
                reader.onload = (event) => {
                    this.dialogVisible = true;
                    this.imgSrc = event.target.result;
                    this.$refs.cropper && this.$refs.cropper.replace(event.target.result);
                };
                reader.readAsDataURL(file);
            },
            cropImage() {
                this.cropImg = this.$refs.cropper.getCroppedCanvas().toDataURL();
            },
            cancelCrop() {
                this.dialogVisible = false;
                this.cropImg = this.defaultSrc;
            },
            imageuploaded(res) {
                console.log(res)
            },
            upImg() {

                var arr = this.cropImg.split(','), mime = arr[0].match(/:(.*?);/)[1],
                    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
                while (n--) {
                    u8arr[n] = bstr.charCodeAt(n);
                }
                let file = new Blob([u8arr], {type: mime});
                file.name = '人事档案图片';
                this.dialogVisible = false;
                this.targetImg = file;

                this.$emit('input', this.targetImg);
                //file = this.cropImg;
                /* console.log(file);
                 let param = new FormData(); //创建form对象
                 param.append('file', file);//通过append向form对象添加数据
                 console.log(param.get('file'));
                 //param.append('isFile', true);
                 param.append('dirId', 0);
                 let config = {
                     headers: {'Content-Type': 'multipart/form-data'}
                 };
                 this.$axios.post('/upload', param, config)
                     .then(response => {
                         console.log(response.data);
                     })*/
            },
            handleError() {
                this.$notify.error({
                    title: '上传失败',
                    message: '图片上传接口上传失败，可更改为自己的服务器接口'
                });
            }
        },
        watch: {
            value: function (val) {
                this.form = val;
            },
            img_url: function (img_url) {
                let that = this;
                if (this.img_url) {
                    this.cropImg = this.img_url;
                }
            },
            img_id: function (img_id) {
                let that = this;
                if (img_id) {

                    this.$axios.get("/file/getFile", {
                        params: {
                            id: that.img_id
                        }
                    }).then(res => {
                        that.cropImg = res.data.data.downloadUrl;
                    })
                }
            },
            img_path: function (img_path) {
                let that = this;
                if (img_path) {
                    this.$axios.get("/file/getFile", {
                        params: {
                            path: img_path
                        }
                    }).then(res => {
                        that.cropImg = res.data.data.downloadUrl;
                    })

                }
            }
        }
    }
</script>

<style scoped>
    .content-title {
        font-weight: 400;
        line-height: 50px;
        margin: 10px 0;
        font-size: 22px;
        color: #1f2f3d;
    }

    .pre-img {
        width: 100px;
        height: 100px;
        background: #f8f8f8;
        border: 1px solid #eee;
        border-radius: 5px;
    }

    .crop-demo {
        display: flex;
        align-items: flex-end;
    }

    /*  .crop-demo-btn {
          position: relative;
          width: 100px;
          height: 40px;
          line-height: 40px;
          padding: 0 20px;
          margin-left: 30px;
          background-color: #409eff;
          color: #fff;
          font-size: 14px;
          border-radius: 4px;
          box-sizing: border-box;
      }*/

    .crop-input {
        position: absolute;
        width: 100px;
        height: 100px;
        left: 0;
        top: 0;
        opacity: 0;
        cursor: pointer;
    }
</style>
