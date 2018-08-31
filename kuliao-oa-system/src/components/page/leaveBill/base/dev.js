/**
 *
 * */
module.exports = {
    route: [
        {
            "path": "/baseTest",
            "redirect": "/baseTest/b"
        },
        {
            path: "/baseTest",
            component: "components/page/leaveBill/base/base",
            meta: {
                "title": "新建流程事务"
            },
            children: [
                {
                    "path": "/baseTest/a",
                    "component": "components/page/leaveBill/base/a",
                    "meta": {
                        "title": "aaaa",
                        noMenu: true
                    }
                }, {
                    "path": "/baseTest/b",
                    "component": "components/page/leaveBill/base/b",
                    "meta": {
                        "title": "bbbb",
                        noMenu: true
                    }
                },
                {
                    "path": "/baseTest/c",
                    "component": "components/page/leaveBill/base/c",
                    "meta": {
                        "title": "ccccc",
                        noMenu: true
                    }
                }]
        }
    ],
    proxyTable: [],
    menu: [
        {
            "icon": "el-icon-menu",
            "menuName": "我的事务11",
            "index": "base",
            "subs": [
                {
                    "icon": "el-icon-zoom-in",
                    "index": "baseTest",
                    "menuName": "测试"
                }
            ]
        }
    ]
}
