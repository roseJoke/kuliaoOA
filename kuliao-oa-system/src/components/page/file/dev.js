/**
 *
 * */
module.exports = {
    route: [


        /*path: "/file",
        component: "components/page/file/index",
        meta: {
            "title": "文件"
        },
        children: [*/
        {
            "path": "/file/read",
            "component": "components/page/file/read",
            "meta": {
                "title": "目录管理"
            }
        },
        {
            "path": "/file/index",
            "component": "components/page/file/index",
            "meta": {
                "title": "文件预览"
            }
        },
        {
            "path": "/file/xlsx",
            "component": "components/page/file/index/readXls",
            "meta": {
                "title": "表格文件预览"
            }
        }
        /* ]*/


    ],
    proxyTable: [

        {
            ip: "172.16.0.84",
            port:
                "8081",
            url:
                [
                    "/upload",
                    "/download/*",
                    "/file/*"
                ]
        }

    ],
    menu: [
        {
            "icon": "el-icon-document",
            "menuName": "文件",
            "index": "fileeee",
            "subs": [
                {
                    "icon": "el-icon-service",
                    "index": "file/index",
                    "menuName": "文件预览"
                },
                {
                    "icon": "el-icon-service",
                    "index": "file/read",
                    "menuName": "目录管理"
                },
                {
                    "icon": "el-icon-service",
                    "index": "file/xlsx",
                    "menuName": "表格预览"
                },
            ]
        }
    ]
}
