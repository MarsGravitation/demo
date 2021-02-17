package com.microwu.cxd.tool.log;

import com.microwu.cxd.tool.util.JacksonUtil;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/18   9:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public abstract class HttpBodyRecorderFilter extends OncePerRequestFilter {

    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 1024 * 512;

    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;

    private String codeRegex = "^[0-9,]*$";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);

        HttpServletRequest requestToUse = request;

//                && (request.getMethod().equals(HttpMethod.PUT.name())
//                || request.getMethod().equals(HttpMethod.POST.name())
        boolean isCacheRequest = isFirstRequest && !(request instanceof ContentCachingRequestWrapper)
                && isJsonRequest(request);
        if (isCacheRequest) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }

        HttpServletResponse responseToUse = response;
//        && (request.getMethod().equals(HttpMethod.PUT.name())
//                || request.getMethod().equals(HttpMethod.POST.name())
        boolean isCacheResponse = !(response instanceof ContentCachingResponseWrapper);

        if (isCacheResponse) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        boolean hasException = false;

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } catch (Exception e) {
            hasException = true;
            throw e;
        } finally {
            int code = hasException ? 500 : response.getStatus();
            boolean isRecord = !isAsyncStarted(requestToUse)
                    && (this.codeMatched(code, recordCode()));

            if (isRecord) {
                recordBody(createRequest(requestToUse), createResponse(responseToUse));
            } else {
                writeResponseBack(responseToUse);
            }
        }

    }

    protected String createRequest(HttpServletRequest request) {
        String payload = "";
        if (isJsonRequest(request)) {
            ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                payload = genPayload(payload, buf, wrapper.getCharacterEncoding());
            }
        } else {
            Map<String, String[]> map = request.getParameterMap();
            if (!CollectionUtils.isEmpty(map)) {
                payload = JacksonUtil.obj2String(map);
            }
        }

        return payload;
    }

    protected String createResponse(HttpServletResponse resp) {
        String response = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(resp, ContentCachingResponseWrapper.class);

        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            try {
                wrapper.copyBodyToResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }

            response = genPayload(response, buf, wrapper.getCharacterEncoding());
        }

        return response;
    }

    protected void writeResponseBack(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);

        if (wrapper != null) {
            try {
                wrapper.copyBodyToResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String genPayload(String payload, byte[] buf, String characterEncoding) {
        if (buf.length > 0 && buf.length < getMaxPayloadLength()) {
            try {
                payload = new String(buf, 0, buf.length, characterEncoding);
            } catch (UnsupportedEncodingException e) {
                payload = "[un-know]";
            }
        }
        return payload;
    }

    public int getMaxPayloadLength() {
        return maxPayloadLength;
    }

    private boolean codeMatched(int responseStatus, String statusCode) {
        if (statusCode.matches(codeRegex)) {
            String[] filteredCode = statusCode.split(",");
            return Stream.of(filteredCode).map(Integer::parseInt).collect(Collectors.toList()).contains(responseStatus);
        } else {
            return false;
        }
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().contains(APPLICATION_JSON_VALUE)
                && (request.getMethod().equals(HttpMethod.PUT.name()) || request.getMethod().equals(HttpMethod.POST.name()));
    }
    protected abstract void recordBody(String payload, String response);

    protected abstract String recordCode();
}