/**
 *
 * */
module.exports = {
    route: [

        {
            path: "/hr_recruit",
            component: "components/page/hr/recruit/base",
            meta: {
                "title": "招聘管理"
            },
            children: [
                {
                    "path": "/hr_recruit/Personnelneeds",
                    "component": "components/page/hr/recruit/PersonnelNeeds",
                    "meta": {
                        "title": "人员需求",
                        noMenu: false
                    }
                }, {
                    "path": "/hr_recruit/Interview",
                    "component": "components/page/hr/recruit/Interview",
                    "meta": {
                        "title": "面试安排",
                        noMenu: true
                    }
                }, {
                    "path": "/hr_recruit/screen",
                    "component": "components/page/hr/recruit/Screen",
                    "meta": {
                        "title": "筛选简历",
                        noMenu: true
                    }

                }, {
                    "path": "/hr_recruit/manage",
                    "component": "components/page/hr/recruit/Manage",
                    "meta": {
                        "title": "简历管理",
                        noMenu: true
                    }

                },
                {
                    "path": "/hr_recruit/Entry",
                    "component": "components/page/hr/recruit/Entry",
                    "meta": {
                        "title": "简历录入",
                        noMenu: true
                    }

                }, {
                    "path": "/hr_recruit/Statistics",
                    "component": "components/page/hr/recruit/Statistics",
                    "meta": {
                        "title": "统计图",
                        noMenu: true
                    }

                },
            ]
        },
        {
            path: "/hr/archives/index",
            component: "components/page/hr/archives/index",
            meta: {
                "title": "档案管理"
            }

        },
        {
            path: "/hr/archives/index/edit",
            component: "components/page/hr/archives/edit",
            meta: {
                title: "档案修改",
                noMenu: true,
                enEdit: false
            }
        },
        {
            path: "/hr/archives/add",
            component: "components/page/hr/archives/add",
            meta: {
                "title": "档案录入"
            }

        },
        {
            path: "/hr_empWelfare",
            component: "components/page/hr/hr_empWelfare",
            meta: {
                "title": "员工福利记录"
            }
        },
        {
            path: "/hr_laborRelations",
            component: "components/page/hr/hr_laborRelations",
            meta: {
                "title": "劳动关系管理"
            }
        },
        {
            path: "/hr_train",
            component: "components/page/hr/hr_train",
            meta: {
                "title": "培训管理"
            }
        },
        {
            path: "/hr_test",
            component: "components/page/hr/archives/test",
            meta: {
                "title": "测试"
            }
        }


    ],
    proxyTable: [

        {
            ip: "172.16.0.84",
            port:
                "8081",
            url:
                [
                    "/upload"
                ]
        },
        {
            ip: "172.17.0.14",
            port:
                "9998",
            url:
                [
                    "/hr/*"
                ]
        },

       /* {
            ip: "172.17.0.23",
            port:
                "9998",
            url:
                [
                    "/hr/archives/!*",
                    "/hr/archives/",
                    "/test"
                ]

        }*/


    ],
    menu: [
        {
            "icon": "el-icon-menu",
            "menuName": "人力资源",
            "index": "5",
            "subs": [
                {
                    "icon": "el-icon-zoom-in",
                    "index": "hr_recruit",
                    "menuName": "招聘管理",
                    "subs": [
                        {
                            "icon": "el-icon-service",
                            "index": "hr_recruit/PersonnelNeeds",
                            "menuName": "人员需求"
                        },
                        {
                            "icon": "el-icon-service",
                            "index": "hr_recruit/Interview",
                            "menuName": "面试安排"
                        },
                        {
                            "icon": "el-icon-service",
                            "index": "hr_recruit/Screen",
                            "menuName": "筛选简历"
                        },
                        {
                            "icon": "el-icon-service",
                            "index": "hr_recruit/Manage",
                            "menuName": "简历管理"
                        },
                        {
                            "icon": "el-icon-service",
                            "index": "hr_recruit/Entry",
                            "menuName": "简历录入"
                        },
                        {
                            "icon": "el-icon-service",
                            "index": "hr_recruit/Statistics",
                            "menuName": "统计图"
                        },
                    ]
                },
                {
                    "icon": "el-icon-service",
                    "index": "hr_train",
                    "menuName": "培训"
                },
                {
                    "icon": "el-icon-goods",
                    "index": "hr_empWelfare",
                    "menuName": "员工福利"
                },
                {
                    "icon": "el-icon-document",
                    "index": "hr_archives",
                    "menuName": "人事档案",
                    "subs": [
                        {
                            "icon": "el-icon-tickets",
                            "index": "hr/archives/index",
                            "menuName": "档案管理"
                        },
                        {
                            "icon": "el-icon-edit",
                            "index": "hr/archives/add",
                            "menuName": "档案录入"
                        },
                    ]
                },
                {
                    "icon": "el-icon-share",
                    "index": "hr_laborRelations",
                    "menuName": "劳动关系"
                },
                {
                    "icon": "el-icon-tickets",
                    "index": "hr_bylawManage",
                    "menuName": "制度管理"
                },
                {
                    "icon": "el-icon-tickets",
                    "index": "hr_test",
                    "menuName": "测试"
                },

            ]
        }
    ]
}
