package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.config.MonitorBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kangta123
 */
class CstTimestampConverterTest extends MonitorBaseTest {

    @Test
    void testConvert() {
        final CstTimestampConverter extractor = new CstTimestampConverter();
        double st1 = 1611559980.483;
        String[][] value = {new String[]{String.valueOf(st1), "100.23456"}};
        value = extractor.convert(value);

        Assertions.assertThat(value).isEqualTo(new String[][]{new String[]{"1611588780", "100.23456"}});
    }


}
