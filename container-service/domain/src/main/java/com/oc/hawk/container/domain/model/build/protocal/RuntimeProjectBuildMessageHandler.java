package com.oc.hawk.container.domain.model.build.protocal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RuntimeProjectBuildMessageHandler implements ProjectBuildMessageHandler {
    public final static String TAG_START = "=@@";
    public final static String TAG_CLOSE = "@@@";
    public final static String SUB_PROJECT_TAG = "subProject@";
    private final Pattern compile = Pattern.compile(TAG_START + "(?<tag>.*?)" + TAG_CLOSE);
    private final Consumer<ProtocolMessage> onDecodeMessage;
    private String buf = "";

    public RuntimeProjectBuildMessageHandler(Consumer<ProtocolMessage> onDecodeMessage) {
        this.onDecodeMessage = onDecodeMessage;
    }

    private void parse(String message) {

        ProtocolMessage protocolMessage = new ProtocolMessage();
        if (isPurText(message)) {
            protocolMessage.setText(message);
        } else {
            Matcher matcher = compile.matcher(message);
            while (matcher.find()) {
                String tag = matcher.group("tag");
                if (tag.contains(SUB_PROJECT_TAG)) {
                    protocolMessage.addProjects(tag.substring(SUB_PROJECT_TAG.length()));
                } else {
                    protocolMessage.addTag(tag);
                }
            }

            String text = matcher.replaceAll("");
            if (isPurText(text)) {
                protocolMessage.setText(text);
            } else {
                if (text.contains(TAG_START)) {
                    int index = text.indexOf(TAG_START);
                    buf = text.substring(index);
                    protocolMessage.setText(text.substring(0, index));
                }
            }
        }
        onDecodeMessage.accept(protocolMessage);
    }

    private boolean isPurText(String text) {
        return !text.contains(TAG_START) && !text.contains(TAG_CLOSE);
    }

    @Override
    public void append(String message) {
        if(StringUtils.isNotEmpty(buf)){
            System.err.println("buf is " + buf);
        }
        this.parse(buf + message);
    }

}
