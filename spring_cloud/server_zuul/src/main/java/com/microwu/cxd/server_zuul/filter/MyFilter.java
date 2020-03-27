package com.microwu.cxd.server_zuul.filter;

//@Component
//public class MyFilter extends ZuulFilter {
//    @Override
//    public String filterType(){
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder(){return 0;}
//
//    @Override
//    public boolean shouldFilter(){
//        return true;
//    }
//
//    @Override
//    public Object run(){
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServeltContext hsc = requestContext.getRequest();
//        Object token = hsc.getParamter("token");
//        if(token == null){
//            requestContext.setSendZuulResponse(false);
//            requestContext.setResponseStatusCode(401);
//            try{
//                requestContext.getWriter().write("token is null");
//            }catch(Exception e){
//
//            }
//            return null;
//        }
//        return null;
//    }
//}
