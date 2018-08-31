let config = {
    route: [


        {
            "path": "/work/launch",
            "redirect": "/work/launch/positive"
        },
        /*  {
              "path": "/work/reply",
              "component": "components/page/workflowUse/launch/workReply",
              "meta": {
                  "title": "审批批复"
              }
          },*/
            {
            "path": "/work/launch",
            "component": "components/page/workflowUse/launch",
            "meta": {
                "title": "发起流程"
            },
            children: [
                {
                    "path": "/work/launch/meetingApplication",
                    "component": "components/page/workflowUse/launch/MeetingApplicant",
                    "meta": {
                        "title": "会议室申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/leaveBill",
                    "component": "components/page/workflowUse/test/leaveBillForm",
                    "meta": {
                        "title": "请假信息",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/assetApplication",
                    "component": "components/page/workflowUse/launch/assetForm",
                    "meta": {
                        "title": "固定资产申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/bookApplication",
                    "component": "components/page/workflowUse/launch/bookApply",
                    "meta": {
                        "title": "图书申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/human",
                    "component": "components/page/workflowUse/launch/HumanDemand",
                    "meta": {
                        "title": "人员需求申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/positive",
                    "component": "components/page/workflowUse/launch/PositiveApplication",
                    "meta": {
                        "title": "转正申请",
                        noMenu: true
                    }
                },
                {
                    "path": "/work/launch/training",
                    "component": "components/page/workflowUse/launch/TrainingApply",
                    "meta": {
                        "title": "培训申请",
                        noMenu: true
                    }
                }
            ]
        }
    ],
    proxyTable: [
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
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "work/launch",
                    "menuName": "发起流程"
                },
                {
                    "icon": "el-icon-zoom-in",
                    "index": "approverTest",
                    "menuName": "是定人测试"
                }

            ]
        }
    ]
}
//  "path": "/work/launch",

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
    "component": "components/page/workflowUse/launch/workReply",
    "meta": {
        "title": "审批批复"
    },
    children: aa(launch.children, launch.path, "/work/reply")
})


module.exports = config;
