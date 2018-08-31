<template>
    <div class="">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-message"></i> 消息管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-tabs v-model="message">
                <el-tab-pane :label="`未读消息(${messageContext.unread.length})`" name="first">
                    <el-table :data="messageContext.unread" :show-header="false" style="width: 100%">
                        <el-table-column>
                            <template slot-scope="scope">
                                <span class="message-title"
                                      @click="lookMessage(scope.row,scope.$index,1)">
                                    {{'['+msgType(scope.row.messageType)+']'}} {{scope.row.messageTopic}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="150">
                            <template slot-scope="scope">
                                <span>发布人：   {{scope.row.createUserName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="200">
                            <template slot-scope="scope">
                                <span>发布时间：   {{ scope.row.createUserDate | formatDate }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="120">
                            <template slot-scope="scope">
                                <el-button size="small" @click="handleRead(scope.$index)" >标为已读</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="handle-row">
                        <el-button type="primary"  @click="handleReadAll()">全部标为已读</el-button>
                    </div>
                </el-tab-pane>
                <el-tab-pane :label="`已读消息(${messageContext.read.length})`" name="second">
                    <template v-if="message === 'second' ">
                        <el-table :data="messageContext.read" :show-header="false" style="width: 100%">
                            <el-table-column>
                                <template slot-scope="scope">
                                    <span class="message-title"
                                          @click="lookMessage(scope.row,scope.$index,2)">
                                        {{'['+msgType(scope.row.messageType)+']'}} {{scope.row.messageTopic}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column width="150">
                                <template slot-scope="scope">
                                    <span>发布人：   {{scope.row.createUserName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column width="200">
                                <template slot-scope="scope">
                                    <span>发布时间：   {{ scope.row.createUserDate | formatDate }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column width="120">
                                <template slot-scope="scope">
                                    <el-button type="danger"   @click="handleDel(scope.$index,1)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="handle-row">
                            <el-button type="danger"  @click="handleDelAll()">删除全部 </el-button>
                        </div>
                    </template>
                </el-tab-pane>
                <el-tab-pane :label="`已发送(${messageContext.send.length})`" name="third">
                    <template v-if="message === 'third' ">
                    <el-table :data="messageContext.send" :show-header="false" style="width: 100%">
                        <el-table-column>
                            <template slot-scope="scope">
                                <span class="message-title"
                                      @click="lookMessage(scope.row,scope.$index,3)">
                                    {{'['+msgType(scope.row.messageType)+']'}} {{scope.row.messageTopic}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="150">
                            <template slot-scope="scope">
                                <span>收件人：   {{scope.row.userName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="200">
                            <template slot-scope="scope">
                                <span>发布时间：   {{ scope.row.createUserDate | formatDate }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="120">
                            <template slot-scope="scope">
                                <el-button @click="lookMessage(scope.row,scope.$index,3)">查看</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    </template>
                </el-tab-pane>
                <el-tab-pane :label="`回收站(${messageContext.recycle.length})`" name="forth">
                    <template v-if="message === 'forth'">
                        <el-table :data="messageContext.recycle" :show-header="false" style="width: 100%">
                            <el-table-column>
                                <template slot-scope="scope">
                                    <span class="message-title"
                                          >
                                        {{'['+msgType(scope.row.messageType)+']'}} {{scope.row.messageTopic}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column width="150">
                                <template slot-scope="scope">
                                    <span>发布人：   {{scope.row.createUserName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column width="200">
                                <template slot-scope="scope">
                                    <span>发布时间：   {{ scope.row.createUserDate | formatDate }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column width="120">
                                <template slot-scope="scope">
                                    <el-button @click="handleRestore(scope.$index)">还原</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="handle-row">
                            <el-button type="danger"  @click="handleClearAll()">清空回收站</el-button>
                        </div>
                    </template>
                </el-tab-pane>
            </el-tabs>
            <el-dialog :title="messageTopic" :visible.sync="recMessageVisible" width="30%" top="30vh">
                <span>{{messageContent}}</span>
                <el-row>
                    <el-col :span="8" :offset="16"><span>发布人：{{ userName }}</span></el-col>
                    <el-col :span="8" :offset="16"><span>时间：{{ createUserDate | formatDate }}</span></el-col>
                </el-row>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="recMessageVisible = false">确   定</el-button>
                </div>
            </el-dialog>
            <el-dialog :title="messageTopic" :visible.sync="sendMessageVisible" width="30%" top="30vh">
                <span>{{messageContent}}</span>
                <el-row>
                    <el-col :span="8" :offset="16"><span>收件人：{{ userName }}</span></el-col>
                    <el-col :span="8" :offset="16"><span>时间：{{ createUserDate | formatDate }}</span></el-col>
                </el-row>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="sendMessageVisible = false">确   定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>

</template>

<script>
    import {formatDate} from '../../common/date.js';
    export default {

        data() {
            return {
                message: 'first',
                showHeader: false,
                recMessageVisible: false,
                sendMessageVisible: false,
                messageTopic:'',
                messageContent: '',
                messageType: '',
                createUserDate: '',
                createUserId: '',
                createUserName: '',
                messageContext: {
                    //未读
                    unread: [],
                    //已读
                    read: [],
                    send: [],
                    //已删除
                    recycle: [],
                }

            }
        },
        created() {
           this.loadMessage();
        },
        filters: {
            formatDate(time) {
                let date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd hh:mm');
            }
        },
        methods: {
            msgType(type) {
                switch (type) {
                    case '1':
                        return '站内信';
                        break;
                    case '2':
                        return '审批提醒';
                        break;
                    default:
                        return '通知公告';
                }
            },
            lookMessage(row,index,type) {

                this.messageTopic = row.messageTopic;
                this.messageContent = row.messageContent;
                this.createUserDate = row.createUserDate;
                if(type === 1 || type === 2){
                    this.userName = row.createUserName;
                    this.recMessageVisible = true;
                    if(type === 1){
                        this.handleRead(index);
                    }
                }else if(type ===3){
                    this.userName = row.userName;
                    this.sendMessageVisible = true;
                }




            },
            sendMessage() {
              let sendParams = new URLSearchParams();
              sendParams.append("createUserId",localStorage.getItem('userId'));
                this.$axios.post("/message/findAllOaMessage", sendParams).then(res => {
                    this.messageContext.send = res.data.data;

                });
            },
            loadMessage() {
                let that = this;
                let unreadParams = new URLSearchParams();
                console.log(localStorage.getItem('empNo'));
                unreadParams.append('userId', localStorage.getItem('userId'));
                unreadParams.append('readStatus', 0);
                unreadParams.append('deleteStatus', 1);
                this.$axios.post("/message/findAllOaMessage", unreadParams).then(res => {
                    that.messageContext.unread = res.data.data;
                });
                let readParams = new URLSearchParams();
                readParams.append('userId', localStorage.getItem('userId'));
                readParams.append('readStatus', 1);
                readParams.append('deleteStatus', 1);
                this.$axios.post("/message/findAllOaMessage", readParams).then(res => {
                    that.messageContext.read = res.data.data;
                });
                let recycleParams = new URLSearchParams();
                recycleParams.append('userId', localStorage.getItem('userId'));
                recycleParams.append('deleteStatus', '0');
                this.$axios.post("/message/findAllOaMessage", recycleParams).then(res => {
                    that.messageContext.recycle = res.data.data;
                });
                this.sendMessage();
            },

            handleRead(index) {

                const item = this.messageContext.unread[index];

                let params = new URLSearchParams();
                params.append('listId', item.id);
                this.$axios.post("/message/updateOaMessageReadStatus", params).then(res => {
                    console.log(res);
                    this.messageContext.read.push(item);
                    this.messageContext.unread.splice(index, 1);
                });

            }
            ,
            handleDel(index,type) {
                let item = this.messageContext.read[index];
                console.log(this.messageContext.recycle);
                let params = new URLSearchParams();
                console.log(item);
                params.append('listId', item.id);
                params.append('deleteStatus', 0);
                this.$axios.post("/message/deleteOaMessage", params).then(res => {
                    this.messageContext.recycle.push(item);
                    this.messageContext.read.splice(index, 1);
                });
                console.log("abcd");


            },
            handleRestore(index) {
                const item = this.messageContext.recycle[index];
                let params = new URLSearchParams();
                console.log(item);
                params.append('listId', item.id);
                params.append('deleteStatus', 1);
                params.append('readStatus', 1);
                this.$axios.all([
                    this.$axios.post('/message/deleteOaMessage', params),
                    this.$axios.post('/message/updateOaMessageReadStatus', params)
                ]).then(this.$axios.spread((DResp, RResp) => {
                    // 上面两个请求都完成后，才执行这个回调方法
                    console.log('DResp', DResp.data);
                    console.log('RResp', RResp.data);
                    this.messageContext.read.push(item);
                    this.messageContext.recycle.splice(index, 1);
                }));


                 //const item = this.messageContext.recycle.splice(index, 1);
                //this.messageContext.read = item.concat(this.messageContext.read);
            },
            handleReadAll(){
                var listId = this.messageContext.unread.map(function(v){return v.id;});
                let params = new URLSearchParams();
                params.append('listId', listId);
                this.$axios.post("/message/updateOaMessageReadStatus", params).then(res => {
                    this.messageContext.read =  this.messageContext.read.concat(this.messageContext.unread);
                    this.messageContext.unread= [];
                });
            },
            handleDelAll(){
                var listId = this.messageContext.read.map(function(v){return v.id;});
                let params = new URLSearchParams();
                params.append('listId', listId);
                params.append('deleteStatus', 0);
                this.$axios.post("/message/deleteOaMessage", params).then(res => {
                    console.log(res);
                    this.messageContext.recycle =  this.messageContext.recycle.concat(this.messageContext.read);
                    this.messageContext.read = [];
                });
            },
            handleClearAll(){
                var listId = this.messageContext.recycle.map(function(v){return v.id;});
                let params = new URLSearchParams();
                params.append('listId', listId);
                params.append('deleteStatus', 2);
                this.$axios.post("/message/deleteOaMessage", params).then(res => {
                    this.messageContext.recycle = [];
                });
            },

        },
        computed: {
            unreadNum() {
                return this.messageContext.unread.length;
            }
        }
    }

</script>

<style>
    .message-title {
        cursor: pointer;
    }

    .handle-row {
        margin-top: 30px;
    }
</style>

