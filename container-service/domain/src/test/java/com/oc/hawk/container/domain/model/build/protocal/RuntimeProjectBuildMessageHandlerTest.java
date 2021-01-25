package com.oc.hawk.container.domain.model.build.protocal;

import com.oc.hawk.container.domain.ContainerBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RuntimeProjectBuildMessageHandlerTest extends ContainerBaseTest {

    @Test
    public void append_testShouldParsedATag() {
        String message = "=@@Start 0 @@@Cloning into 'app'...";
        RuntimeProjectBuildMessageHandler runtimeProjectBuildMessageHandler = new RuntimeProjectBuildMessageHandler(msg -> {
            Assertions.assertThat(msg.getTags()).hasSize(1);

            ProtocolTag tag = msg.getTags().iterator().next();
            Assertions.assertThat(tag.getData()).isNull();
            Assertions.assertThat(tag.getTag()).isEqualTo("Start");
            Assertions.assertThat(tag.isSuccess()).isTrue();
        });

        runtimeProjectBuildMessageHandler.append(message);
    }

    @Test
    void append_testStartWithTag() {
        String t = "build file ./message/target/message-0.0.1-SNAPSHOT.jar /app";
        String m = "=@@Build 0 @@@" + t;
        RuntimeProjectBuildMessageHandler runtimeProjectBuildMessageHandler = new RuntimeProjectBuildMessageHandler(msg -> {
            Assertions.assertThat(msg.getTags()).hasSize(1);
            Assertions.assertThat(msg.getText()).isEqualTo(t);

        });
        runtimeProjectBuildMessageHandler.append(m);

    }

    @Test
    void append_testAppendTextAndTag() {
        String tag = "=@@Start 0 @@@";
        String text = "Cloning into 'app'...";
        RuntimeProjectBuildMessageHandler runtimeProjectBuildMessageHandler = new RuntimeProjectBuildMessageHandler(msg -> {
            Assertions.assertThat(msg.getTags()).hasSize(1);
            Assertions.assertThat(msg.getTags().iterator().next().getTag()).isEqualTo("Start");
            Assertions.assertThat(msg.getText()).isEqualTo(text);
            Assertions.assertThat(msg.getSubApps()).isEmpty();

        });
        runtimeProjectBuildMessageHandler.append(tag + text);
    }

    @Test
    void append_testAppendOnlyText() {
        String text = "Sending build context to Docker daemon  557.1kB";
        RuntimeProjectBuildMessageHandler runtimeProjectBuildMessageHandler = new RuntimeProjectBuildMessageHandler(msg -> {
            Assertions.assertThat(msg.getText()).isEqualTo(text);
        });
        runtimeProjectBuildMessageHandler.append(text);
    }


    @Test
    void append_textEndWithTag() {
        String t = "digest: sha256:c37018b6a5aa0db59c3e8a0c6ebf85ebd0555c68270312aae0e588cc8f15312d size: 1361 \r\n";
        String text = t + "=@@DockerPush 0 @@@=@@End 0 @@@#";
        RuntimeProjectBuildMessageHandler runtimeProjectBuildMessageHandler = new RuntimeProjectBuildMessageHandler(msg -> {
            Assertions.assertThat(msg.getText()).isEqualTo(t + "#");
            Assertions.assertThat(msg.getTags()).hasSize(2);

        });
        runtimeProjectBuildMessageHandler.append(text);
    }
}
