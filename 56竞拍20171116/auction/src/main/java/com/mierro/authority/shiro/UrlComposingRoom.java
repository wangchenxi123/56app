package com.mierro.authority.shiro;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黄晓滨
 * @date 17/2/18
 */
public class UrlComposingRoom {

    public static String getUrl(HttpServletRequest request, String url){
        String resource ;
        String method = request.getMethod();
        String way="";
        switch (method) {
            case "GET":
            case "get":
                way = "get";
                break;
            case "POST":
            case "post":
                way = "post";
                break;
            case "DELETE":
            case "delete":
                way = "delete";
                break;
            case "PUT":
            case "put":
                way = "put";
                break;
        }

        String type = request.getParameter("type");
        if (type == null ||type.trim().equals("")||type.equals("null")){
            type="";
        }else {
            type="/"+type;
        }
        resource = way+url+type;
        return resource;
    }


}
