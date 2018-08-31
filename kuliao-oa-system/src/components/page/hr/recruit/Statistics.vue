<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i> 图表</el-breadcrumb-item>
                <el-breadcrumb-item>基础图表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">

            <div class="schart">
                <div class="content-title">柱状图</div>
                <schart canvasId="bar" width="500" height="400" :data="data1" type="bar" :options="options1"></schart>
            </div>
            <div class="schart">
                <div class="content-title">饼状图</div>
                <schart canvasId="pie" width="500" height="400" :data="data2" type="pie" :options="options2"></schart>
            </div>
        </div>
    </div>
</template>

<script>
    import Schart from 'vue-schart';
    export default {
        components: {
            Schart
        },
        data: () => ({
            data1:[
                {name:'阶段一',value:11},
                {name:'阶段二',value:14},
                {name:'阶段三',value:22},

            ],
            data2 : [
                {name:'参加',value:40},
                {name:'未参加',value:10},

            ],
            options1: {
                title: '各阶段通过情况',
                bgColor: '#009688',
                titleColor: '#ffffff',
                fillColor: '#e0f2f1',
                axisColor: '#ffffff',
                contentColor: '#999'
            },
            options2: {
                title: '面试参与情况',
                bgColor: '#607d8b',
                titleColor: '#ffffff',
                legendColor: '#ffffff'
            }
        })
    }
</script>

<style scoped>
    .schart{
        width: 600px;
        display: inline-block;
    }
    .content-title{
        clear: both;
        font-weight: 400;
        line-height: 50px;
        margin: 10px 0;
        font-size: 22px;
        color: #1f2f3d;
    }

</style>
