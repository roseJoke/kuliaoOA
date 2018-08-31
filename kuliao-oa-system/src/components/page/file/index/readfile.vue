<template v-if="url">
    <div>
        <canvas ref="ppp"></canvas>
        <el-pagination
            layout="prev, pager, next"
            :page-count="pageSize"
            @prev-click="onPrevPage"
            @next-click="onNextPage"
            @current-page="pageNum"
            @current-change="renderPage"
        >
        </el-pagination>
    </div>
</template>

<script>
    import PDFJS from 'pdfjs-dist'

    PDFJS.GlobalWorkerOptions.workerSrc = true;
    export default {
        props: ['url'],
        data() {
            return {
                pageSize: 0,
                pdfDoc: null,
                pageNum: 1,
                pageRendering: false,
                pageNumPending: null,
                scale: 1.0,
                canvas: null,
                ctx: null
            }
        },
        created() {

        },
        mounted() {
            let that = this;
            if (this.url) {
                this.showPDF(this.url);
            }
        },
        watch: {
            url: {
                handler: function (val, oldval) {
                    this.showPDF(val);
                },
            },

        },
        methods: {
            showPDF(url) {
                this.canvas = this.$refs.ppp;
                this.ctx = this.canvas.getContext('2d')
                let that = this;
                PDFJS.getDocument(url).then(function (pdfDoc_) {
                    that.pdfDoc = pdfDoc_;
                    //  document.getElementById('page_count').textContent = that.pdfDoc.numPages;

                    that.pageSize = that.pdfDoc.numPages;
                    that.renderPage(that.pageNum);
                });
            },

            renderPage(num) {
                if (num === undefined) {
                    num = this.pageNum;
                }
                this.pageRendering = true;
                let that = this;
                this.pdfDoc.getPage(num).then(function (page) {
                    var viewport = page.getViewport(that.scale);
                    that.canvas.height = viewport.height;
                    that.canvas.width = viewport.width;

                    var renderContext = {
                        canvasContext: that.ctx,
                        viewport: viewport
                    };
                    var renderTask = page.render(renderContext);

                    renderTask.promise.then(function () {
                        that.pageRendering = false;
                        if (that.pageNumPending !== null) {
                            // New page rendering is pending
                            renderPage(that.pageNumPending);
                            that.pageNumPending = null;
                        }
                    });
                });
            },
            queueRenderPage(num) {
                if (this.pageRendering) {
                    this.pageNumPending = num;
                } else {
                    this.renderPage(num);
                }
            },
            onPrevPage() {
                if (this.pageNum <= 1) {
                    return;
                }
                this.pageNum--;
                this.queueRenderPage(this.pageNum);
            },
            onNextPage() {
                if (this.pageNum >= this.pdfDoc.numPages) {
                    return;
                }
                this.pageNum++;
                this.queueRenderPage(this.pageNum);
            },

        }
    }
</script>

<style scoped>

</style>
