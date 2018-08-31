/**
 *
 * */
module.exports = {
    route: [
        {
            "path": "/dashboard",
            "component": "components/page/base/homepage",
            "meta": {
                "title": "系统首页"
            }
        },
        {
            "path": "/employer",
            "component": "components/page/base/Employer",
            "meta": {
                "title": "员工管理"
            }
        },
        {
            "path": "/role",
            "component": "components/page/base/RoleManage",
            "meta": {
                "title": "角色管理"
            }
        },
        {
            "path": "/dept",
            "component": "components/page/base/DeptManage",
            "meta": {
                "title": "部门管理"
            }
        },
        {
            "path": "/customer",
            "component": "components/page/base/Customer",
            "meta": {
                "title": "客户管理"
            }
        },
        {
            "path": "/addressBook",
            "component": "components/page/base/AddressBook",
            "meta": {
                "title": "电子通讯录"
            }
        }
    ],
    proxyTable: [
        // {
        //     ip: "172.17.0.7",
        //     port: "8085",
        //     url: [
        //         "/base/*"
        //     ]
        // },
        {
            ip: "172.20.0.24",
            port: "8080",
            url: [
                "/base/*"
            ]
        },
        {
            ip: "172.17.0.23",
            port: "9998",
            url: [
                "/hr/*"
            ]
        },
        {
            ip: "172.16.0.84",
            port:
                "8081",
            url:
                [
                    "/download/*"
                ]
        },

    ],
    menu:[
        {
            "icon": "el-icon-menu",
            "menuName": "用户管理",
            "index": "yhgl",
            "subs": [
                {
                    "icon": "el-icon-zoom-in",
                    "index": "hr_recruit",
                    "menuName": "招聘"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "customermanage",
                    "menuName": "客户管理",
                    "subs":[
                        {
                            "icon": "el-icon-zoom-in",
                            "index": "customer",
                            "menuName": "客户信息",
                        }
                    ]
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "employer",
                    "menuName": "员工管理"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "create",
                    "menuName": "流程管理"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "role",
                    "menuName": "角色管理"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "dept",
                    "menuName": "部门管理"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "addressBook",
                    "menuName": "电子通讯录"
                }
            ]
        },

    ]
}
