package com.oc.hawk.traffic.entrypoint;

import com.oc.hawk.test.BaseTest;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class EntryPointBaseTest extends BaseTest{

    @Mock
	protected EntryPointConfigRepository entryPointConfigRepository;

}
