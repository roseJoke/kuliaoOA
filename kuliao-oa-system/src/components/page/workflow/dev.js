/**
 *
 * */
let config = {
    route: [


      /*  {
            "path": "/work/manage",
            "component": "components/page/workflow/flowmanage/process",
            "meta": {
                "title": "流程管理"
            }
        },
*/
        {
            "path": "/work/forMySend",
            "component": "components/page/workflow/forMySend",
            "meta": {
                "title": "我发起的"
            }
        },
        {
            "path": "/work/allWork",
            "component": "components/page/workflow/allprocess",
            "meta": {
                "title": "所有流程"
            }
        },

        {
            "path": "/work/forMyApproval",
            "component": "components/page/workflow/forMyApproval",
            "meta": {
                "title": "待我审批"
            }
            //配套的子页面自动和流程发起一致
        },
        //发起流程页面
        {
            "path": "/work/newApproval",
            "component": "components/page/workflow/newApproval",
            "meta": {
                "title": "发起流程"
            }
        },
        {
            "path": "/work/launch",
            "redirect": "/work/newApproval"
        },
        {
            "path": "/work/launch",
            "component": "components/page/workflow/launch",
            "meta": {
                "title": "发起流程"
            },
            children: [
                {
                    "path": "/work/launch/meetingApplication",
                    "component": "components/page/workflow/business/MeetingApplicant",
                    "meta": {
                        "title": "会议室申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/leaveBill",
                    "component": "components/page/workflow/business/leaveBillForm",
                    "meta": {
                        "title": "请假信息",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/assetApplication",
                    "component": "components/page/workflow/business/assetForm",
                    "meta": {
                        "title": "固定资产申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/bookApplication",
                    "component": "components/page/workflow/business/bookApply",
                    "meta": {
                        "title": "图书申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/human",
                    "component": "components/page/workflow/business/HumanDemand",
                    "meta": {
                        "title": "人员需求申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/positive",
                    "component": "components/page/workflow/business/PositiveApplication",
                    "meta": {
                        "title": "转正申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/training",
                    "component": "components/page/workflow/business/TrainingApply",
                    "meta": {
                        "title": "培训申请",
                        noMenu: true
                    }
                }
            ]
        }
    ],
    proxyTable: [{
        ip: "172.16.0.85",
        port: "8083",
        url: [
            "/workflow/*",
        ]
    }

    ],
    menu: [
        {
            "icon": "el-icon-menu",
            "menuName": "工作流",
            "index": "ss",
            "subs": [
                {
                    "icon": "el-icon-document",
                    "index": "flowmanage",
                    "menuName": "流程定义",
                    "subs": [
                        {
                            "icon": "el-icon-document",
                            "index": "work/manage",
                            "menuName": "流程管理",
                        }
                    ]
                },
                {
                    "icon": "el-icon-setting",
                    "index": "flowcenter",
                    "menuName": "流程中心",
                    "subs": [
                        {
                            "icon": "el-icon-zoom-in",
                            "index": "work/newApproval",
                            "menuName": "发起审批"
                        },
                        {
                            "icon": "el-icon-zoom-in",
                            "index": "work/allWork",
                            "menuName": "所有流程"
                        },
                        {
                            "icon": "el-icon-zoom-in",
                            "index": "work/forMyApproval",
                            "menuName": "待我审批"
                        },
                        {
                            "icon": "el-icon-zoom-in",
                            "index": "work/forMySend",
                            "menuName": "我发起的"
                        }
                    ]
                }
            ]
        }
    ]
}


let launch = config.route.filter(function (item) {
    return item.path === "/work/launch" && item.component;
})[0];

function aa(children, path, newPath) {

    let newChildren = [];
    for (let i = 0; i < children.length; i++) {
        newChildren.push({
            path: children[i].path.replace(path, newPath),
            component: children[i].component,
            meta: children[i].meta
        })
    }

    return newChildren;
}

config.route.push({
    "path": "/work/reply",
    "component": "components/page/workflow/workReply",
    "meta": {
        "title": "审批批复"
    },
    children: aa(launch.children, launch.path, "/work/reply")
})


module.exports = config;
