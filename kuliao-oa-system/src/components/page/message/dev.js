/* "/message/!*": {
               //target: 'http://172.20.0.41:8082',
               target: 'http://127.0.0.1:8082',
               changeOrigin: true
           }*/
module.exports = {
    route: [
        {
            "path": "/addMsg",
            "component": "components/page/message/AddMsg",
            "meta": {
                "title": "添加消息"
            }
        },
        {
            "path": "/tabs",
            "component": "components/page/message/Tabs",
            "meta": {
                "title": "消息首页"
            }
        }
    ],
    proxyTable: [
        // {
        //     ip: "127.0.0.1",
        //     port: "8082",
        //     url: [
        //         "/message/*"
        //     ]
        // }
    ],
    menu: [

        {
            "icon": "el-icon-goods",
            "index": "message",
            "menuName": "通知管理",
            "subs": [
                {
                    "icon": "el-icon-edit-outline",
                    "index": "addMsg",
                    "menuName": "发送消息"
                },
                {
                    "icon": "el-icon-info",
                    "index": "tabs",
                    "menuName": "消息管理"
                }

            ]

        }
    ]
}
