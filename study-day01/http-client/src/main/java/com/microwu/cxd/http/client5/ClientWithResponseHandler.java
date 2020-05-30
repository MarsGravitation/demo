package com.microwu.cxd.http.client5;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/22   11:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientWithResponseHandler {

//    /**
//     * 演示如何使用响应处理程序处理HTTP响应。这是推荐的方法
//     * 这种方法使调用者可以专注于HTTP响应的过程，将系统资源释放任务交给HttpClient。
//     * HTTP响应处理程序的使用保证了所有情况下基础HTTP连接都会自动释放回连接管理器
//     *
//     * @author   chengxudong               chengxudong@microwu.com
//     * @date    2020/5/22  11:16
//     *
//     * @param   	args
//     * @return  void
//     */
//    public static void main(String[] args) throws Exception {
//        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            final HttpGet httpGet = new HttpGet("http://httpbin.org/get");
//            System.out.println("Executing request " + httpGet.getMethod() + " " + httpGet.getUri());
//
//            // 创建一个自定义响应处理器
//            final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {
//                @Override
//                public String handleResponse(ClassicHttpResponse classicHttpResponse) throws HttpException, IOException {
//                    final int status = classicHttpResponse.getCode();
//                    if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
//                        final HttpEntity entity = classicHttpResponse.getEntity();
//                        try {
//                            return entity != null ? EntityUtils.toString(entity) : null;
//                        } catch (final ParseException e) {
//                            throw new ClientProtocolException(e);
//                        }
//                    } else {
//                        throw new ClientProtocolException("Unexpected response status: " + status);
//                    }
//                }
//            };
//            final String responseBody = httpClient.execute(httpGet, responseHandler);
//            System.out.println("---------------------------------------");
//            System.out.println(responseBody);
//        }
//    }

}