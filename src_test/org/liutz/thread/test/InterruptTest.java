package org.liutz.thread.test;

import org.liutz.thread.Interrupt;

import android.test.AndroidTestCase;

public class InterruptTest extends AndroidTestCase {
	
	private static final String TAG ="InterruptTest";
	
	public void testRun() throws Throwable {
		Interrupt ir = new Interrupt();
		ir.run();
	}

}
