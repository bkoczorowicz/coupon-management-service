package pl.koczorowicz.empik.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IpAddressDeterminer {

    private static final List<String> CANDIDATE_ADDRESS_HEADERS = List.of(
        "X-Forwarded-For",
        "X-Real-IP",
        "X-Client-IP",
        "X-Cluster-Client-IP",
        "Forwarded",
        "Forwarded-For"
    );

    public String determineClientIpAddress(HttpServletRequest request) {
        for (String header : CANDIDATE_ADDRESS_HEADERS) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return "0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) ? "127.0.0.1" : request.getRemoteAddr();
    }

}
