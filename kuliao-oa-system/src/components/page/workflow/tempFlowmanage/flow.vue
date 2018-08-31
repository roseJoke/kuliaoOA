<template>
    <div id="temp">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程管理</el-breadcrumb-item>
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 流程设计</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="jtk-demo-canvas canvas-wide statemachine-demo jtk-surface jtk-surface-nopan" id="canvas">
            <!--  <vue-context-menu :contextMenuData="AllmenuData"
                                :transferIndex="-1"
                                @del="delNode(index)"></vue-context-menu>-->
            <template v-for="(node,index) in flow">
                <div class="w" :id="'flow'+node.id" @contextmenu="showMenu(index)"
                     v-bind:style="{ left: node.style.x , top: node.style.y ,width:node.style.width?node.style.width:'10em' }">
                    {{ node.name}}
                    <div class="ep" :action="'flow'+node.id"></div>
                    <vue-context-menu :contextMenuData="menuData"
                                      :transferIndex="transferIndex"
                                      @refresh="getNodeIndex(node.id)"
                                      @del="delNode(node.id)"></vue-context-menu>
                    <el-dialog title="任务设置" :visible.sync="click" :modal-append-to-body='false' width="800px">
                        <el-form :model="form">
                            <el-form-item label="审批人列表">
                                <el-transfer @change="resetSignType()" v-model="selectEmpList" :data="empNo"
                                             :titles="['员工列表', '已选列表']" :button-texts="['取消', '添加']" filterable
                                             filter-placeholder="请输入搜索内容">
                                </el-transfer>
                            </el-form-item>
                            <el-form-item label="审批方式">
                                <el-radio v-model="form.sign1" label="S" :disabled="!flag">默认</el-radio>
                                <el-radio v-model="form.sign1" label="A" :disabled="flag">会签</el-radio>
                                <el-radio v-model="form.sign1" label="O" :disabled="flag">或签</el-radio>
                            </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="createProcessNode">完成</el-button>
                            <el-button @click="click = false">取 消</el-button>
                        </div>
                    </el-dialog>
                </div>
            </template>
            <!--弹出每个节点  录入后台工作流节点中所需的基础信息-->
            <!--提交时候弹出的文本框-->
            <!--流程图 https://jsplumbtoolkit.com/community/doc/connections.html#draganddrop -->
            <el-dialog title="流程提交" :visible.sync="show" width="500px">
                <el-form :model="form1" :rules="ruluProcess" ref="form1">
                    <el-form-item label="部署名称" prop="deployName">
                        <el-input v-model="form1.deployName" auto-complete="off" style="width: 215px"></el-input>
                    </el-form-item>
                    <el-form-item label="流程分类" label-width="77px">
                        <el-select v-model="form1.category" placeholder="请选择活动区域">
                            <el-option label="人力资源" value="1"></el-option>
                            <el-option label="客户关系" value="2"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="流程类型" prop="processType" label-width="77px">
                        <el-radio v-model="form1.processType" label="S">指定办理人</el-radio>
                        <el-radio v-model="form1.processType" label="D">动态指定办理人</el-radio>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="show = false,submit('form1')">确 定</el-button>
                    <el-button @click="show = false">取 消</el-button>
                </div>
            </el-dialog>
        </div>
        <div>
            <br/>
            <el-row type="flex">
                <el-col :offset="20">
                    <el-button type="primary" @click="show=true" :disabled="isSubmit">提交</el-button>
                    <el-button type="primary" :disabled="isSubmit" @click="reset">重置</el-button>
                </el-col>
            </el-row>
        </div>
    </div>
</template>
<script>
    import FL from 'jsplumb'
    import 'vue-contextmenu'
    import ElOption from "element-ui/packages/select/src/option";
    import ElFormItem from "element-ui/packages/form/src/form-item";
    import ElInput from "element-ui/packages/input/src/input";
    import ElButton from "element-ui/packages/button/src/button";
    import ElCheckbox from "element-ui/packages/checkbox/src/checkbox";
    import ElTransfer from "element-ui/packages/transfer/src/main";
    export default {
        components: {
            ElTransfer,
            ElCheckbox,
            ElButton,
            ElInput,
            ElFormItem,
            ElOption
        },
        name: "FlowWWW",
        data() {
            const generateData = _ => {
                const data = [];
                // 试着在这里面调用一下
                let emplist = this.selectEmpName();
                for (let i = 0; i < emplist.length; i++) {
                    data.push({
                        key: i,
                        label: emplist[i],
                    });
                }
                return data;
            };
            return {
                // isEditable:true,//不可编辑为假
                isSubmit: true,
                processKey: '', //为空时，进行创建流程，不为空时，进行编辑
                empIds: [],
                empNo: generateData(),
                flag: true,
                selectEmpList: [],   //这个选项是已选列表的人员
                ruluProcess: {
                    deployName: [
                        {
                            required: true, message: '名称不能为空', trigger: 'blur'
                        }
                    ]
                },
                show: false,
                form1: {
                    deployName: "",
                    category: "",
                    processType: "S"
                },
                options: [{
                    value: '001',
                    label: '001'
                }, {
                    value: '002',
                    label: '002'
                }, {
                    value: '003',
                    label: '003'
                }, {
                    value: '004',
                    label: '004'
                }, {
                    value: '005',
                    label: '005'
                }],
                flow: [
                    /* {
                         id: 0,
                         name: "发起人",
                         nextNode: [
                             {
                                 node: 1
                             }
                         ],
                         style: {x: "5em", y: '10em', width: '10em'}
                     },

                     {
                         id: 1,
                         name: "一级审批人",
                         nextNode: [{
                             flowName: "小于三天",
                             condition: '${day<=3}',
                             node: 2
                         }, {
                             flowName: "小于五天",
                             condition: '${day<=5}',
                             node: 3
                         }, {
                             flowName: "小于十天",
                             condition: '${day<=10}',
                             node: 5
                         }],
                         style: {x: '25em', y: '10em'},
                     },
                     {
                         id: 2,
                         name: "小于三天",
                         nextNode: [{
                             node: 4
                         }],
                         style: {x: '45em', y: '10em'}
                     },
                     {
                         id: 4,
                         name: "结束",
                         nextNode: [],
                         style: {x: '65em', y: '10em'}
                     },
                     {
                         id: 3,
                         name: "小于五天",
                         nextNode: [{
                             node: 4
                         }],
                         style: {x: '45em', y: '5em'}
                     },
                     {
                         id: 5,
                         name: "小于十天",
                         nextNode: [{
                             node: 4
                         }],
                         style: {x: '45em', y: '15em'}
                    },*/
                ],
                instance: null,
                transferIndex: null,
                menuData: {
                    menuName: 'name1',
                    axios: {x: null, y: null},
                    menulists: [
                        {fnHandler: 'refresh', icoName: 'el-icon-setting', btnName: '设置'},
                        {fnHandler: 'del', icoName: 'el-icon-delete', btnName: '删除'},
                    ],
                },
                click: false,
                index: '',
                form: {
                    sign1: "S",
                },
            };
        },
        // 修改提交按钮和取消按钮的状态
        beforeUpdate() {
            let that = this;
            if (that.flow.length > 0) {
                that.isSubmit = false;
            }
            if (that.flow.length === 0) {
                that.isSubmit = true;
            }
        },
        computed: {
            offset: function () {
                var canvas = document.getElementById("canvas");
                return {
                    x: canvas.getBoundingClientRect().left,
                    y: canvas.getBoundingClientRect().top,
                }
            }
        },
        // 判断流程管理提交过来的参数。 new：新建流程。 modify :修改流程。 see 查看流程
        mounted() {
            let that = this;
            console.log('operate的值是什么：'+that.$route.query.operate)
            if('new'===that.$route.query.operate){
                console.log('新建流程');
                that.conn();
                return;
            }else if ('modify'===that.$route.query.operate){
                console.log('修改流程');
                that.$axios.get("./static/flow.json").then((res) => {
                    that.lookProcess(res.data.steps);
                    that.$nextTick(function () {
                        //数据加载完成的初始化
                        this.conn();
                    })
                }).catch(error => {
                    console.log(error);
                })
                return;
            }else if ('see'=== that.$route.query.operate){
                console.log('查看流程');
                that.$axios.get("./static/flow.json").then((res) => {
                    that.lookProcess(res.data.steps);
                    that.$nextTick(function () {
                        //数据加载完成的初始化
                        this.conn();
                    })
                }).catch(error => {
                    console.log(error);
                })
            }else {
                that.$message.error('参数有误');
                return; //结束函数的执行。
            }
        },
        //初始化
        created() {
            let that = this;
            that.instance = jsPlumb.getInstance({
                Endpoint: ["Dot", {radius: 2}],
                Connector: "StateMachine",
                HoverPaintStyle: {stroke: "#1e8151", strokeWidth: 2},
                ConnectionOverlays: [
                    ["Arrow", {
                        location: 1,
                        id: "arrow",
                        length: 14,
                        foldback: 0.8
                    }],
                    ["Label", {label: "", id: "label", cssClass: "aLabel"}]
                ],
                Container: "canvas"
            });
            that.instance.registerConnectionType("basic", {anchor: "Continuous", connector: "StateMachine"});
            window.jsp = that.instance;
        },
        methods: {
            // 查看流程
            lookProcess(flowDefination){
                let that = this;
                let x1 = 5;
                let y1 = 10;
                let arr = [];
                for (let i = 0; i < flowDefination.length; i++) {
                    if (flowDefination[i].stepId === 'S') {
                        flowDefination[i].stepId = 0;
                    }
                    let nextNodes = [];
                    for (let j = 0; j < flowDefination[i].links.length; j++) {
                        let next = {
                            node: flowDefination[i].links[j].targetStepId,
                            condition: flowDefination[i].links[j].condition,
                            flowName: flowDefination[i].links[j].linkName
                        }
                        nextNodes.push(next);
                    }

                    arr.push({
                        id: flowDefination[i].stepId,
                        name: flowDefination[i].stepName,
                        nextNode: nextNodes,
                        empNo: flowDefination[i].userId,
                        sign: flowDefination[i].approvalType,
                        style: {
                            x: x1 + 'em',
                            y: y1 + 'em'
                        }
                    });
                    x1 = x1 + 20;
                }
                //对数据进行处理
                let xlocation = 20;
                let num = 0;
                for (let j = 0; j < arr.length; j++) {
                    if (j > 0) {
                        if (arr[j].nextNode != undefined && arr[j].nextNode.length === 1) {
                            arr[j].style.x = 5 + xlocation * j + 'em';
                        } else if (arr[j].nextNode != undefined && arr[j].nextNode.length > 1) {
                            for (let k = 0; k < arr[j].nextNode.length; k++) {
                                let nextNodeId = arr[j].nextNode[k].node;//当前节点指向下一节点的节点id
                                for (let m = j + 1; m < arr.length; m++) {
                                    if (nextNodeId === arr[m].id) {
                                        if (arr[m].nextNode == '') { //下一节点为结束节点时的处理
                                            arr[m].style.x = 5 + xlocation * m + 'em';
                                            arr[m].style.y = 10 + 'em'; //纵坐标不变。
                                        } else {
                                            arr[m].style.x = 5 + xlocation * m + 'em';
                                            arr[m].style.y = 15 + 5 * num + 'em';
                                            num++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                that.flow = arr;
            },
            // 如果点击了重置按钮，清空流程面板
            reset() {
                let that = this;
                document.getElementById("canvas").innerHTML = '';
            },
            // 获取当前任务节点的索引
            getNodeIndex(i) {
                let that = this;
                console.log('当前索引' + that.index);
                that.click = true;
                that.index = i;
            },
            // 修改默认的审批方式
            resetSignType() {
                let that = this;
                if (that.selectEmpList.length > 1) {
                    that.form.sign1 = "A";
                    that.flag = false;
                }
                if (that.selectEmpList.length <= 1) {
                    that.flag = true;
                    this.form.sign1 = "S";
                }
            },
            //初始化节点上的参数。(完成流程时添加员工编号，添加审批方式)
            initParams(id) {
                let that = this;
                let currentNode = this.getNode(id);
                console.log('节点编号' + currentNode.id);
                let empNo = that.selectEmpList;
                // 在这里对任务审批方式的数据进行矫正
                let obj = {
                    id: id,
                    empId: empNo.toString(),
                    type: that.form.sign1 //会或签
                };
                that.empIds.push(obj); //todo
                that.selectEmpList = []; //清空选项列表
                that.form.sign1 = "S";  //将审批方式设为默认
            },
            //新建流程节点时需要提交的数据
            //当选中的人数大于1时，不能够是单人审批了，必须选择或签还是会签。
            createProcessNode() {
                // 保存表单上面录入的数据。
                let that = this;
                console.log('当前索引' + that.index);
                this.initParams(that.index);
                that.index = '';
                that.click = false;
            },
            //通过后台的接口拿到前台需要的员工信息。
            selectEmpName() {
                const data = ['001张三', '002李四', '003王五', '004赵六'];
                return data;
                // this.$axios.post(url).then(res=>{console.log(res.data)}).catch(error => {console.log(error)});
            },
            //提交 (提交的时候添加 deployName:'请假'，catagory:'人力资源')
            submit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        console.log(this.flow);
                        let that = this;
                        // 后台整合的就是
                        let originalUI = that.flow;
                        // 在这里需要向后台提交的数据
                        let processDefine = {
                            groupId: that.form1.category,
                            relationUrl: "/test",
                            processName: that.form1.deployName,
                            processType: that.form1.processType,
                            steps: []
                        };
                     //开始节点
                        let start = {
                            stepId:"S",
                            stepName: '开始',
                            links: [
                                {
                                    "linkName":"连接",
                                    "condition":"",
                                    "targetStepId":"S_0"
                                }
                            ]
                        };
                        processDefine.steps.push(start);
                        for (let i = 0; i < that.flow.length; i++) {
                            let end = '';
                            if (that.flow[i].nextNode.length === 0) { //结束任务节点。
                                end = {
                                    stepId: "E",
                                    stepName: '结束'
                                };
                                processDefine.steps.push(end);
                            } else {
                                let new_node = {
                                    stepId: 'S_'+that.flow[i].id,
                                    userId: that.getTypeAndEmpId(i).empId,
                                    stepName: that.flow[i].name,
                                    links: [],
                                    approvalType: that.getTypeAndEmpId(i).type
                                };
                                for (let j = 0; j < that.flow[i].nextNode.length; j++) {
                                    if (typeof(that.flow[i].nextNode[j].flowName) == "undefined" || that.flow[i].nextNode[j].flowName == null) {
                                        that.flow[i].nextNode[j].flowName = '';
                                    }
                                    if (typeof(that.flow[i].nextNode[j].condition) == "undefined" || that.flow[i].nextNode[j].condition == null) {
                                        that.flow[i].nextNode[j].condition = '';
                                    }
                                    if (that.getNode(that.flow[i].nextNode[j].node).nextNode.length ==0 ||that.getNode(that.flow[i].nextNode[j].node).nextNode === undefined) {
                                        that.flow[i].nextNode[j].node = 'E';
                                    }
                                    if (that.flow[i].nextNode[j].node === 'E'){
                                        let tempObj = {
                                            linkName: '连接',
                                            condition: that.flow[i].nextNode[j].condition,
                                            targetStepId: that.flow[i].nextNode[j].node
                                        }
                                        new_node.links.push(tempObj);
                                    }else {
                                        let tempObj = {
                                            linkName: '连接',
                                            condition: that.flow[i].nextNode[j].condition,
                                            targetStepId: 'S_'+that.flow[i].nextNode[j].node
                                        }
                                        new_node.links.push(tempObj);
                                    }
                                }
                                processDefine.steps.push(new_node);
                            }
                        }
                        let flowData = {
                            process: processDefine
                        };
                        console.log(flowData.process);
                        // 接下来是向后台发送请求
                        // 封装参数
                        this.$axios.post('/workflow/definition/create', flowData.process).then(res => console.log(res.data)).catch(error => console.log(error));
                    } else {
                        this.$message.error("部署名称不能为空！");
                        return false;
                    }
                });
            },
            //封装每个节点需要用户录入的信息。
            getTypeAndEmpId(index) {
                let that = this;
                let empId = '';
                let type = 0;
                console.log('empIds的长度' + that.empIds.length);
                console.log('empIds0的员工id' + that.empIds[0].empId.toString());
                for (let j = 0; j < that.empIds.length; j++) { //todo
                    if (index === that.empIds[j].id) {
                        type = that.empIds[j].type;
                        empId = that.empIds[j].empId;
                    }
                }
                return {
                    type: type,
                    empId: empId
                };
            },
            //删除节点 节点id
            delNode(id) {
                for (let i = 0; i < this.flow.length; i++) {
                    if (this.flow[i].id == id) {
                        this.flow.splice(i, 1);
                        return;
                    }
                }
            },
            //获取节点 根据ID
            getNode(id) {
                for (let i = 0; i < this.flow.length; i++) {
                    if (this.flow[i].id == id) {
                        console.log(this.flow[i]);
                        return this.flow[i];
                    }
                }
            },
            //显示菜单 根据ID
            showMenu(index) {
                this.transferIndex = index; // tranfer index to child component
                event.preventDefault();
                var x = event.clientX;
                var y = event.clientY;
                this.menuData.axios = {
                    x, y
                }
            },
            //开始把图线画出来
            conn() {
                let that = this;
                var canvas = document.getElementById("canvas");
                var windows = jsPlumb.getSelector(".statemachine-demo .w");
                //单击连线删除
                that.instance.bind("click", function (c) {
                    that.instance.deleteConnection(c);
                });
                //双击鼠标事件
                jsPlumb.on(canvas, "dblclick", function (e) {
                    that.newNode(e.offsetX, e.offsetY);
                });
                that.instance.batch(function () {
                    for (var i = 0; i < windows.length; i++) {
                        that.initNode(windows[i], true);
                    }
                    for (let i = 0; i < that.flow.length; i++) {
                        for (let j = 0; j < that.flow[i].nextNode.length; j++) {
                            console.log(that.flow[i].id);
                            that.instance.connect({
                                source: "flow" + that.flow[i].id,
                                target: "flow" + that.getNode(that.flow[i].nextNode[j].node).id,
                                type: "basic",
                                overlays: [
                                    ["Label", {label: that.flow[i].nextNode[j].condition}]
                                ],
                            });
                        }
                    }
                    //新建连线
                    console.log('设置事件')
                    that.instance.bind("connection", function (info) {
                        that.$prompt('请输入分支条件', '提示', {}).then(({value}) => {
                            let sourceId = info.sourceId.substring(4, info.sourceId.length);
                            let targetId = info.targetId.substring(4, info.targetId.length);
                            if (value) {
                                //弹出输入的值 value
                                info.connection.getOverlay("label").setLabel(value);
                                that.getNode(sourceId).nextNode.push({
                                    node: targetId,
                                    condition: value,
                                    flowName: that.getNode(targetId).name
                                });
                            } else {
                                that.getNode(sourceId).nextNode.push({
                                    node: targetId,
                                    condition: '' //todo
                                });
                            }
                            console.log('源' + info.sourceId);
                            console.log('源' + sourceId);
                            console.log('目的' + targetId);
                        })
                    });
                });
                jsPlumb.fire("jsPlumbDemoLoaded", that.instance);
            },
            //初始化节点
            initNode(el) {
                let that = this;
                //初始化元素，使其可以被连接
                that.instance.draggable(el);
                that.instance.makeSource(el, {
                    filter: ".ep",
                    anchor: "Continuous",
                    connectorStyle: {stroke: "#5c96bc", strokeWidth: 2, outlineStroke: "transparent", outlineWidth: 4},
                    connectionType: "basic",
                    extract: {
                        "action": "the-action"
                    },
                    maxConnections: -1,
                    onMaxConnections: function (info, e) {
                        alert("Maximum connections (" + info.maxConnections + ") reached");
                    }
                });
                that.instance.makeTarget(el, {
                    dropOptions: {hoverClass: "dragHover"},
                    anchor: "Continuous",
                    allowLoopback: true
                });
                that.instance.fire("jsPlumbDemoNodeAdded", el);
            },
            //添加新的节点（x,y）坐标
            newNode(x, y) {  //todo
                let that = this;
                var id = that.flow.length;
                //instance.getContainer().appendChild(d);
                this.$prompt('请输入任务名称', '提示', {}).then(({value}) => {
                    let that = this;
                    // var id1= 'task' + that.flow.length + 11;
                    //instance.getContainer().appendChild(d);
                    this.flow.push({
                        id: id,
                        name: value,
                        nextNode: [],
                        empNo: [],
                        sign: 'S',
                        style: {x: x + 'px', y: y + 'px'}
                    });
                    this.$nextTick(function () {
                        //数据加载完成的初始化
                        var windows = FL.jsPlumb.getSelector("#flow" + id);
                        that.initNode(windows);
                    })
                    return;
                });
            }
        }
    };
</script>

<style scoped>
    @import "../../../../../static/flow/jsplumbtoolkit-demo.css";
    @import "../../../../../static/flow/jsplumbtoolkit-defaults.css";
    /*去除搜索框的红色。*/
    input.el-input__inner {
        border-color: white;
    }
    .demo {
        touch-action: none;
    }
    .w {
        padding: 16px;
        position: absolute;
        z-index: 4;
        border: 1px solid #2e6f9a;
        box-shadow: 2px 2px 19px #e0e0e0;
        -o-box-shadow: 2px 2px 19px #e0e0e0;
        -webkit-box-shadow: 2px 2px 19px #e0e0e0;
        -moz-box-shadow: 2px 2px 19px #e0e0e0;
        -moz-border-radius: 8px;
        border-radius: 8px;
        opacity: 0.8;
        cursor: move;
        background-color: white;
        font-size: 11px;
        -webkit-transition: background-color 0.25s ease-in;
        -moz-transition: background-color 0.25s ease-in;
        transition: background-color 0.25s ease-in;
    }

    .w:hover {
        background-color: #5c96bc;
        color: white;

    }

    .aLabel {
        -webkit-transition: background-color 0.25s ease-in;
        -moz-transition: background-color 0.25s ease-in;
        transition: background-color 0.25s ease-in;
    }

    .aLabel.jtk-hover, .jtk-source-hover, .jtk-target-hover {
        background-color: #1e8151;
        color: white;
    }

    .aLabel {
        background-color: white;
        opacity: 0.8;
        padding: 0.3em;
        border-radius: 0.5em;
        border: 1px solid #346789;
        cursor: pointer;
    }

    .ep {
        position: absolute;
        bottom: 37%;
        right: 5px;
        width: 1em;
        height: 1em;
        background-color: orange;
        cursor: pointer;
        box-shadow: 0 0 2px black;
        -webkit-transition: -webkit-box-shadow 0.25s ease-in;
        -moz-transition: -moz-box-shadow 0.25s ease-in;
        transition: box-shadow 0.25s ease-in;
    }

    .ep:hover {
        box-shadow: 0 0 6px black;
    }

    .statemachine-demo .jtk-endpoint {
        z-index: 3;
    }

    #opened {
        left: 10em;
        top: 5em;
    }

    #phone1 {
        left: 35em;
        top: 12em;
        width: 7em;
    }

    #inperson {
        left: 12em;
        top: 23em;
    }

    #phone2 {
        left: 28em;
        top: 24em;
    }

    #rejected {
        left: 10em;
        top: 35em;
    }

    .dragHover {
        border: 2px solid orange;
    }

    path, .jtk-endpoint {
        cursor: pointer;
    }

    handel-input {
        width: 200px;
    }

    #temp {

    }
</style>
