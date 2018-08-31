/**
 *
 * */
module.exports = {
    route: [
        {
            "path": "/bookBorrow",
            "component": "components/page/assets/book/BookBorrows",
            "meta": {
                "title": "图书借阅记录"
            }
        },
        {
            "path": "/bookInfo",
            "component": "components/page/assets/book/BookInfo",
            "meta": {
                "title": "图书信息维护"
            }
        },
        {
            "path": "/BookInfoView",
            "component": "components/page/assets/book/BookInfoView",
            "meta": {
                "title": "图书信息查询"
            }
        },
        {
            "path": "/MeetingApplicated",
            "component": "components/page/assets/meeting/MeetingApplicated",
            "meta": {
                "title": "会议室申请记录"
            }
        },
        {
            "path": "/MeetingInfo",
            "component": "components/page/assets/meeting/MeetingInfo",
            "meta": {
                "title": "会议室信息维护"
            }
        },
        {
            "path": "/MeetingInfoQuery",
            "component": "components/page/assets/meeting/MeetingInfoQuery",
            "meta": {
                "title": "会议室信息查看"
            }
        },
        {
            "path": "/fixedAssetsInStock",
            "component": "components/page/assets/fixedAssets/FixedAssetsInStock",
            "meta": {
                "title": "固定资产入库"
            }
        },
        {
            "path": "/fixedAssetsCheckInStock",
            "component": "components/page/assets/fixedAssets/FixedAssetsCheckInStock",
            "meta": {
                "title": "固定资产签收入库"
            }
        },
        {
            "path": "/fixedAssetsAccount",
            "component": "components/page/assets/fixedAssets/FixedAssetsAccount",
            "meta": {
                "title": "固定资产台账信息"
            }
        }, ,
        {
            "path": "/fixedAssetsStock",
            "component": "components/page/assets/fixedAssets/FixedAssetsStock",
            "meta": {
                "title": "固定资产盘库信息"
            }
        },
        {
            "path": "/fixedAssetsUse",
            "component": "components/page/assets/fixedAssets/FixedAssetsUse",
            "meta": {
                "title": "固定资产领用信息"
            }
        },
        {
            "path": "/fixedAssetsService",
            "component": "components/page/assets/fixedAssets/FixedAssetsService",
            "meta": {
                "title": "固定资产维修管理"
            }
        },
        {
            "path": "/officeGoodsInStock",
            "component": "components/page/assets/officeGoods/OfficeGoodsInStock",
            "meta": {
                "title": "办公用品录入"
            }
        },
        {
            "path": "/officeGoodsAccount",
            "component": "components/page/assets/officeGoods/OfficeGoodsAccount",
            "meta": {
                "title": "办公用品台账信息"
            }
        },
        {
            "path": "/officeGoodsUse",
            "component": "components/page/assets/officeGoods/OfficeGoodsUse",
            "meta": {
                "title": "办公用品领用信息"
            }
        },
        {
            "path": "/officeGoodsCount",
            "component": "components/page/assets/officeGoods/OfficeGoodsCount",
            "meta": {
                "title": "办公用品统计信息"
            }
        }

    ],
    proxyTable: [
        {
            ip: "172.20.0.155",
            port: "8088",
            url: [
                "/assets/meeting/*",
                "/assets/meetingRecord/*"
            ]
        },
        {
            ip: "172.20.0.24",
            port: "8088",
            url: [
                "/bookInfo/*",
                "/borrowInfo/*",
                "/meetingInfo/*",
                "/meetingAppli/*"
            ]
        },
        {
            ip: "172.17.0.12",
            port: "8088",
            url: [
                "/assets/*"
            ]
        },
        {
            ip: "172.17.0.3",
            port: "8081",
            url: [
                "/evaluate/*"
            ]
        },


    ],
    menu: [
        {
            "icon": "el-icon-goods",
            "menuName": "资产管理",
            "index": "assets",
            "subs": [
                    {
                    "icon": "el-icon-mobile-phone",
                    "index": "fixedAssets",
                    "menuName": "固定资产管理",
                    "subs": [
                        {
                            "icon": "el-icon-edit-outline",
                            "index": "fixedAssetsInStock",
                            "menuName": "固定资产入库"
                        },
                        {
                            "icon": "el-icon-success",
                            "index": "fixedAssetsCheckInStock",
                            "menuName": "固定资产签收入库"
                        },
                        {
                            "icon": "el-icon-info",
                            "index": "fixedAssetsAccount",
                            "menuName": "固定资产台账"
                        },
                        {
                            "icon": "el-icon-message",
                            "index": "fixedAssetsStock",
                            "menuName": "固定资产盘库"
                        },
                        {
                            "icon": "el-icon-goods",
                            "index": "fixedAssetsUse",
                            "menuName": "固定资产领用"
                        },
                        {
                            "icon": "el-icon-warning",
                            "index": "fixedAssetsService",
                            "menuName": "固定资产维修"
                        }
                    ]
                },
                {
                    "icon": "el-icon-goods",
                    "index": "officeGoods",
                    "menuName": "办公用品管理",
                    "subs": [
                        {
                            "icon": "el-icon-edit-outline",
                            "index": "officeGoodsInStock",
                            "menuName": "办公用品入库"
                        },
                        {
                            "icon": "el-icon-info",
                            "index": "officeGoodsAccount",
                            "menuName": "办公用品台账"
                        },
                        {
                            "icon": "el-icon-goods",
                            "index": "officeGoodsUse",
                            "menuName": "办公用品领用"
                        },
                        {
                            "icon": "el-icon-search",
                            "index": "officeGoodsCount",
                            "menuName": "办公用品统计"
                        }
                    ]
                },


                {
                    "icon": "el-icon-tickets",
                    "index": "assets_meeting",
                    "menuName": "会议室管理",
                    "subs": [
                        {
                            "index": "MeetingApplicated",
                            "menuName": "会议室申请记录",
                        },
                        {
                            "index": "MeetingInfo",
                            "menuName": "会议室信息维护"
                        },
                        {
                            "index": "MeetingInfoQuery",
                            "menuName": "会议室信息查询"
                        }

                    ]
                },
                {
                    "icon": "el-icon-news",
                    "index": "assets_book",
                    "menuName": "图书管理",
                    "subs": [
                        {
                            "index": "bookBorrow",
                            "menuName": "图书借阅记录",
                        },
                        {
                            "index": "BookInfo",
                            "menuName": "图书信息维护"
                        },
                        {
                            "index": "BookInfoView",
                            "menuName": "图书信息查看"
                        }

                    ]
                }
            ]
        }

    ]
}
