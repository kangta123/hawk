package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HttpPath {

    private String path;

    public HttpPath(String path) {
        this.path = path;
    }
    
    
    public void handlePath() {
        int n = path.indexOf("?");
        if (n >= 0) {
            path = path.substring(0, n);
        }
        if (path.endsWith("/")) {
            n = path.lastIndexOf("/");
            if (n > 0) {
                path = path.substring(0, n);
            }
        }
    }
    
    public boolean matchPath(String targetPath) {
        String replacePath = targetPath.replaceAll("\\/", "\\\\/").replaceAll("\\{[a-zA-Z\\d]+\\}", "[a-zA-Z\\\\d]+");
        Pattern p = Pattern.compile(replacePath, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(path);
        return m.find();
    }
}
