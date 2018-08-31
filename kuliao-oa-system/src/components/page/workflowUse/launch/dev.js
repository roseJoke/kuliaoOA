
module.exports = {
    route: [
        {
            "path": "/meetingApplication",
            "component": "components/page/workflowUse/launch/MeetingApplicant",
            "meta": {
                "title": "会议室申请"
            }
        },
        {
            "path": "/human",
            "component": "components/page/workflowUse/launch/HumanDemand",
            "meta": {
                "title": "人员需求申请"
            }
        },
        {
            "path": "/positive",
            "component": "components/page/workflowUse/launch/PositiveApplication",
            "meta": {
                "title": "转正申请"
            }
        },
        {
            "path": "/training",
            "component": "components/page/workflowUse/launch/TrainingApply",
            "meta": {
                "title": "培训申请"
            }
        }
    ],
    proxyTable: [
        {
            ip: "172.16.0.85",
            port: "8083",
            url: [
                "/bookInfo/*",
                "/borrowInfo/*",
                "/workflow/meeting/*",
                "/workflow/meetingApproval/*",
                "/workflow/meetingRecord/*"
            ]
        },
        {
            ip: "172.17.0.3",
            port: "8081",
            url: [
                "/evaluate/*"
            ]
        }
    ],
    menu: [
        {
            "icon": "el-icon-menu",
            "menuName": "使用中心",
            "index": "leave",
            "subs": [
                {
                    "icon": "el-icon-zoom-in",
                    "index": "meetingApplication",
                    "menuName": "会议室申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "human",
                    "menuName": "人员需求申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "positive",
                    "menuName": "转正申请"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "training",
                    "menuName": "培训申请"
                }
            ]
        }
    ]
}
