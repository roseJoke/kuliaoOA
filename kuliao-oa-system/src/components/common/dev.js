/**
 *
 * */
module.exports = {
    isCommon:true,
    route: [
        {
            "path": "/",
            "redirect": "/dashboard"
        },

        {
            "path": "/login",
            "component": "components/page/Login"
        },
        {
            "path": "/404",
            "component": "components/page/404"
        },
        {
            "path": "/403",
            "component": "components/page/403"
        },
        {
            "path": "*",
            "redirect": "/404"
        }
    ],
    proxyTable:
        [
            {
                ip: "172.16.0.84",
                port: "8081",
                url: [
                    "/user/*"
                ]
            },



            {
                ip: "127.0.0.1:8082",
                port:
                    "8082",
                url:
                    [
                        "/message/*"
                    ]
            }

        ],
    menu:{

    }
}
