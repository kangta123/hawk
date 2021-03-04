package com.oc.hawk.transfer.entrypoint;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.oc.hawk.test.BaseTest;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;

@ExtendWith(MockitoExtension.class)
public abstract class EntryPointBaseTest extends BaseTest{
	
	@Mock
	protected EntryPointConfigRepository entryPointConfigRepository;
	
}
