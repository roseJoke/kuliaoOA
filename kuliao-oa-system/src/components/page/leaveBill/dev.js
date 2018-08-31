
module.exports = {
    route: [
        {
            "path": "/leaveBill_newApproval",
            "component": "components/page/leaveBill/newApproval",
            "meta": {
                "title": "发起审批"
            }
        },
        {
            "path": "/leaveBill_test",
            "component": "components/page/leaveBill/test",
            "meta": {
                "title": "审批信息"
            }
        },

        {
            "path": "/leaveBill_leaveBillForm",
            "component": "components/page/leaveBill/leaveBillForm",
            "meta": {
                "title": "请假表单"
            }
        },
        {
            "path": "/leaveBill_forMyApproval",
            "component": "components/page/leaveBill/forMyApproval",
            "meta": {
                "title": "待我审批"
            }
        },
        {
            "path": "/leaveBill_approvalDone",
            "component": "components/page/leaveBill/approvalDone",
            "meta": {
                "title": "我已审批"
            }
        },
        {
            "path": "/leaveBill_forMySend",
            "component": "components/page/leaveBill/forMySend",
            "meta": {
                "title": "我发起的"
            }
        }
    ],
    proxyTable: [  {
        ip: "127.0.0.1",
        port: "8083",
        url: [
            "/workflow/*"
        ]
    }],
    menu: [
        {
            "icon": "el-icon-menu",
            "menuName": "流程中心",
            "index": "leave",
            "subs": [
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill_newApproval",
                    "menuName": "发起审批"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill_leaveBillForm",
                    "menuName": "请假信息"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill_test",
                    "menuName": "审批信息"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill_forMyApproval",
                    "menuName": "待我审批"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill_approvalDone",
                    "menuName": "我已审批"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "leaveBill_forMySend",
                    "menuName": "我发起的"
                }
            ]
        }
    ]
}
