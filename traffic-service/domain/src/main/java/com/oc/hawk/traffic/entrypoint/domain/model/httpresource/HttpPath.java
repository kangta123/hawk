package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HttpPath {

    private String path;
    
    private Map<String,String> pathVariable;

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
    
    public Map<String,String> updatePathVariable(String targetPath){
        this.pathVariable = new HashMap<String,String>();
        List<String> pathList = Stream.of(path.split("\\/")).collect(Collectors.toList());
        List<String> configList = Stream.of(targetPath.split("\\/")).collect(Collectors.toList());
        for(int i=0;i<configList.size();i++) {
            String uri = configList.get(i);
            if(StringUtils.startsWith(uri,"{")) {
                String key = uri.replaceAll("\\{", "").replaceAll("\\}", "");
                String value = pathList.get(i);
                pathVariable.put(key, value);
            }
        }
        return pathVariable;
    }
    
    /**
    public static void main(String[] args) {
        HttpPath path = new HttpPath("/a1/b/2/c/3/?a=txt/");
        path.handlePath();
        path.updatePathVariable("/a1/b/{id1}/c/{id2}");
        System.out.println(path.getPathVariable());
    }
    */
}
