module.exports = {
    route: [
        {
            "path": "/table",
            "component": "components/page/demo/BaseTable",
            "meta": {
                "title": "基础表格"
            }
        },
       /* {
            "path": "/tabs",
            "component": "components/page/demo/Tabs",
            "meta": {
                "title": "tab选项卡"
            }
        },*/
        {
            "path": "/form",
            "component": "components/page/demo/BaseForm",
            "meta": {
                "title": "基本表单"
            }
        },
        {
            "path": "/editor",
            "component": "components/page/demo/VueEditor",
            "meta": {
                "title": "富文本编辑器"
            }
        },
        {
            "path": "/markdown",
            "component": "components/page/demo/Markdown",
            "meta": {
                "title": "markdown编辑器"
            }
        },
        {
            "path": "/upload",
            "component": "components/page/demo/Upload",
            "meta": {
                "title": "文件上传"
            }
        },
        {
            "path": "/charts",
            "component": "components/page/demo/BaseCharts",
            "meta": {
                "title": "schart图表"
            }
        },
        {
            "path": "/drag",
            "component": "components/page/demo/DragList",
            "meta": {
                "title": "拖拽列表"
            }
        },
        {
            "path": "/permission",
            "component": "components/page/demo/Permission",
            "meta": {
                "title": "权限测试",
                "permission": true
            }
        }

    ],
    proxyTable: [
        {
            ip: "192.168.0.2",
            port: "8081",
            url: [
                "/demo/get",
                "/demo/delete"
            ]
        },
        {
            ip: "192.168.0.2",
            port:
                "8082",
            url:
                [
                    "/demo/add",
                    "/demo/update"
                ]
        }

    ],
    //支持到三层
   menu: [
        {
            icon: 'el-icon-success',
            index: 'demo',
            menuName: '示例',
            subs: [
                {
                    "icon": "el-icon-setting",
                    "index": "dashboard",
                    "menuName": "系统首页"
                },
                {
                    "icon": "el-icon-edit",
                    "index": "addMsg",
                    "menuName": "添加消息"

                },
                {
                    "icon": "el-icon-tickets",
                    "index": "table",
                    "menuName": "基础表格"
                },
                {
                    "icon": "el-icon-message",
                    "index": "tabs",
                    "menuName": "tab选项卡"
                },
                {
                    "icon": "el-icon-date",
                    "index": "3",
                    "menuName": "表单相关",
                    "subs": [
                        {
                            "index": "form",
                            "menuName": "基本表单",
                            "subs": [
                                {
                                    "index": "form",
                                    "menuName": "基本表单"
                                }]
                        },
                        {
                            "index": "editor",
                            "menuName": "富文本编辑器"
                        },
                        {
                            "index": "markdown",
                            "menuName": "markdown编辑器"
                        }

                    ]
                },
                {
                    "icon": "el-icon-star-on",
                    "index": "charts",
                    "menuName": "schart图表"
                },
                {
                    "icon": "el-icon-rank",
                    "index": "drag",
                    "menuName": "拖拽列表"
                },
                {
                    "icon": "el-icon-warning",
                    "index": "permission",
                    "menuName": "权限测试"
                },
                {
                    "icon": "el-icon-error",
                    "index": "404",
                    "menuName": "404页面"
                }
            ]
        }
    ]
}


