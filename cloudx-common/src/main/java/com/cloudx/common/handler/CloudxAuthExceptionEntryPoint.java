package com.cloudx.common.handler;

import cn.hutool.core.util.StrUtil;
import com.cloudx.common.base.ResponseMap;
import com.cloudx.common.util.HttpUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 令牌认证异常响应
 *
 * @author chachae
 * @since 2020/4/24 07:28
 */
@Slf4j
public class CloudxAuthExceptionEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    int status = HttpServletResponse.SC_UNAUTHORIZED;
    String message = "token不合法";
    String rawMessage = authException.getMessage();
    if (StrUtil.containsIgnoreCase(rawMessage, "Invalid access token")) {
      message = "访问令牌不正确";
    }
    if (StrUtil.containsIgnoreCase(rawMessage,
        "Full authentication is required to access this resource")) {
      message = "请求头client信息不正确，烦请核对";
      status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
    log.error(message, authException);
    HttpUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, status,
        new ResponseMap().message(message));
  }
}