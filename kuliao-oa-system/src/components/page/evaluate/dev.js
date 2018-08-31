/**
 *
 * */
module.exports = {
    route: [
        {
            "path":"/evaluate_performance",
            "component": "components/page/evaluate/Performance",
            "meta": {
                "title": "绩效考核"
            }
        },{
            "path":"/evaluate_positive",
            "component": "components/page/evaluate/Positive",
            "meta": {
                "title": "转正考评"
            }
        },{
            "path":"/evaluate_askForLeave",
            "component": "components/page/evaluate/AskForLeave",
            "meta": {
                "title": "请假记录"
            }
        },{
            "path":"/evaluate_attendance",
            "component": "components/page/evaluate/Attendance",
            "meta": {
                "title": "考勤统计"
            }
        }
    ],
    proxyTable: [
        {
            ip: "127.0.0.1",
            port: "8081",
            url: [
                "/evaluate/*"
            ]
        },
        {
            ip: "www.gov.cn",
            port: "80",
            url: [
                "/zhengce/*"
            ]
        },

    ],
    menu:[
        {
            "icon": "el-icon-menu",
            "menuName": "考勤考评",
            "index": "2",
            "subs": [
                {
                    "icon": "el-icon-tickets",
                    "index": "3",
                    "menuName": "考评管理",
                    "subs":[
                        {
                            "icon": "el-icon-document",
                            "index": "evaluate_performance",
                            "menuName": "绩效考核"
                        },
                        {
                            "icon": "el-icon-document",
                            "index": "evaluate_positive",
                            "menuName": "转正考评"
                        }
                    ]
                },
                {
                    "icon": "el-icon-tickets",
                    "index": "4",
                    "menuName": "考勤管理",
                    "subs":[
                        {
                            "icon": "el-icon-document",
                            "index": "evaluate_askForLeave",
                            "menuName": "请假记录"
                        },
                        {
                            "icon": "el-icon-document",
                            "index": "evaluate_attendance",
                            "menuName": "考勤统计"
                        }
                    ]
                }
            ]
        }
    ]
}
