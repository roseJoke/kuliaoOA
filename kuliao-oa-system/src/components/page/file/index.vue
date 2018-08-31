<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i>文件</el-breadcrumb-item>
                <el-breadcrumb-item>文件预览</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-row>
                <el-col :span="6">
                    <el-card class="box-card">

                    <el-tree
                        :props="defaultProps"
                        :data="file"
                        @node-click="openFile"
                        node-key="id"
                        lazy
                        :load="loadFile"
                        :expand-on-click-node="false">
                            <span class="custom-tree-node" slot-scope="{ node, data }">
                                <img :src="typeImg(data.type)" style="width: 20px">
                                <span>{{data.name}}</span>
                          </span>
                    </el-tree>

                    </el-card>
                </el-col>

                <el-col :span="18" >
                    <read-file :url="pdfURL"></read-file>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
    import readFile from "./index/readfile.vue"

    export default {

        data() {
            return {
                pdfURL:'/static/img/bbb.pdf',
                file: [],
                defaultProps: {
                    children: 'child',
                    label: 'name',
                    isLeaf: this.fileType
                }

            }
        },
        components: {
            readFile
        }  ,
        methods: {
            typeImg(type) {
                if (type && type.imgUrl) {
                    return type.imgUrl;
                } else {
                    return '/static/img/img.jpg';
                }
            }
            ,
            fileType(data, node) {
                if (data.type && data.type.dir) {
                    return false
                } else {
                    return true;
                }

            }
            ,

            loadFile(node, resolve) {
                let id = node.key ? node.key : 0;
                this.$axios.get("/file/fileList", {params: {id: id}})
                    .then((res) => {
                        if (res.data.data.child) {
                            resolve(res.data.data.child);
                        } else {
                            resolve([])
                        }
                    });
                return resolve;
            }
            ,
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            }
            ,
            typeImg(type) {
                if (type && type.imgUrl) {
                    return type.imgUrl;
                } else {
                    return '/static/img/img.jpg';
                }
            },
              openFile(data,node,dd){
                 console.log(data)
                  if(data.type.name=='PDF文件'){
                     console.log(data.downloadUrl);
                     this.pdfURL=data.downloadUrl;
                  }
              },

            getFileList(id) {
                id = id ? id : 0;
                this.$axios.get("/file/fileList", {params: {id: id}})
                    .then((res) => {
                        console.log(res);
                        this.file = res.data.data.child;
                    });
            }

        }
    }
</script>

<style scoped>

</style>
