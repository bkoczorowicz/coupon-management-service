package pl.koczorowicz.empik.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class IpAddressDeterminer {

    private String checkIpAddress;
    private RestTemplate restTemplate;

    private static final List<String> CANDIDATE_ADDRESS_HEADERS = List.of(
        "X-Forwarded-For",
        "X-Real-IP",
        "X-Client-IP",
        "X-Cluster-Client-IP",
        "Forwarded",
        "Forwarded-For"
    );

    public IpAddressDeterminer(@Value("${localhost.public-ip-check.url}") String checkIpAddress, @Autowired RestTemplate restTemplate) {
        this.checkIpAddress = checkIpAddress;
        this.restTemplate = restTemplate;
    }

    public String determineClientIpAddress(HttpServletRequest request) {
        for (String header : CANDIDATE_ADDRESS_HEADERS) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return "0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) ? getPublicIpForLocalhost() : request.getRemoteAddr();
    }

    private String getPublicIpForLocalhost() {
        String publicIp = restTemplate.getForObject(checkIpAddress, String.class).trim();
        return StringUtils.isNotBlank(publicIp) ? publicIp : "127.0.0.1";
    }

}
