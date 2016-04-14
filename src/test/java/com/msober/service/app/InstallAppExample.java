package com.msober.service.app;

import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;

public class InstallAppExample extends BaseTest{
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/AppExample.xml" })
	public void install() {
		
	}
}
