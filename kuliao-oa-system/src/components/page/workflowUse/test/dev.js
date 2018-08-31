
module.exports = {
    route: [
        {
            "path": "/assetApplication",
            "component": "components/page/workflowUse/test/assetForm",
            "meta": {
                "title": "固定资产申请"
            }
        },
        {
            "path": "/bookApplication",
            "component": "components/page/workflowUse/test/bookApply",
            "meta": {
                "title": "图书申请"
            }
        },
        {
            "path": "/leaveBill",
            "component": "components/page/workflowUse/test/leaveBillForm",
            "meta": {
                "title": "请假"
            }
        },
        {
            "path": "/newApproval",
            "component": "components/page/workflowUse/test/newApproval",
            "meta": {
                "title": "发起审批"
            }
        },
        {
            "path": "/positiveApplication1",
            "component": "components/page/workflowUse/test/PositiveApplication",
            "meta": {
                "title": "转正申请"
            }
        },
        {
            "path": "/MeetingApplicant1",
            "component": "components/page/workflowUse/test/MeetingApplicant",
            "meta": {
                "title": "会议室申请"
            }
        },
        {
            "path": "/HumanDemand1",
            "component": "components/page/workflowUse/test/HumanDemand",
            "meta": {
                "title": "人员需求申请"
            }
        },
        {
            "path": "/TrainingApply1",
            "component": "components/page/workflowUse/test/TrainingApply",
            "meta": {
                "title": "培训申请"
            }
        }
    ],
    proxyTable: [
        // {
        // ip: "172.20.0.155",
        // port: "8088",
        // url: [
        //     "/bookInfo/*",
        //     "/borrowInfo/*",
        //     "/workflow/meeting/*",
        //     "/workflow/meetingApproval/*",
        //     "/workflow/meetingRecord/*"
        //     ]
        // },
        // {
        //     ip: "172.17.0.3",
        //     port: "8081",
        //     url: [
        //         "/evaluate/*"
        //     ]
        // }
    ],
    menu: [
        {
            "icon": "el-icon-menu",
            "menuName": "詹明越中心",
            "index": "aa",
            "subs": [
                {
                    "icon": "el-icon-zoom-in",
                    "index": "newApproval",
                    "menuName": "发起审批"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "bookApplication",
                    "menuName": "图书申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "assetApplication",
                    "menuName": "固定资产申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill",
                    "menuName": "请假申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "positiveApplication1",
                    "menuName": "转正申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "MeetingApplicant1",
                    "menuName": "会议室申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "HumanDemand1",
                    "menuName": "人员需求申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "TrainingApply1",
                    "menuName": "培训申请"
                }
            ]
        }
    ]
}
