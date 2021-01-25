package com.oc.hawk.kubernetes;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class KubernetesTest {
    private static final String MASTER_URL = "https://192.168.24.207:6443";
    private static final String NAMESPACE = "build";
    static String podName = "builder-33322";

    public static void main(String[] args) throws InterruptedException, IOException {

        test2();
    }

    public static void test2() throws InterruptedException {
        Config config = new ConfigBuilder().withMasterUrl(MASTER_URL).build();
        try (final KubernetesClient client = new DefaultKubernetesClient(config)){
             final PodList list = client.pods().list();

        }
    }

    public static void test1() throws InterruptedException, IOException {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        PipedOutputStream pw = new PipedOutprutStream(new PipedInputStream());
//        final PipedInputStream pis = new PipedInputStream();
//        final PipedOutputStream pos = new PipedOutputStream(pis);
//
//        final PipedOutputStream cmd = new PipedOutputStream();
//        PipedInputStream in = new PipedInputStream(cmd);
//        KubernetesClient client = new DefaultKubernetesClient(MASTER_URL);
//        ExecWatch watch = client.pods().inNamespace(NAMESPACE).withName(podName)
//            .readingInput(in)
//            .writingOutput(pos)
//            .writingError(System.err)
//            .withTTY()
//            .exec();
//
//        cmd.write("bash build.sh\r\n".getBytes());
//        executorService.submit(() -> {
//            try {
//
//                byte[] buffer = new byte[512];
//
//                int len;
//
//                byte[] remain = null;
//
//                final ProjectBuildProtocol projectBuildProtocol = new RuntimeProjectBuildProtocol(msg->{
//                    System.out.print(msg.getText());
//                });
//                while ((len = pis.read(buffer)) != -1) {
//                    int last = 0;
//                    for (int i = 0; i < len; i++) {
//                        final char c = (char) buffer[i];
//                        if ((c == '\n') ) {
//                            String str = remain != null ? new String(remain) : "";
//
//                            projectBuildProtocol.append(str + new String(buffer, last, i - last));
//                            last = i;
//                            remain = null;
//                        }
//                    }
//                    remain = Arrays.copyOfRange(buffer, last, len );
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    pis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("thread close");
//        });
//
////        pos.write("ls\r\n".getBytes());
//
//
//        System.out.println("over");
//        Thread.sleep(60 * 1000);
    }
}
