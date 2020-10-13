package com.willy.ct.powermock;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class TestDataSummaryService {
	@Test
	void simpleTest() {
		Assertions.assertTrue(new DataSummaryService().getServiceResult(1) == 1);
	}
	
	//PowerMock Test
	@InjectMocks
    private DataSummaryService service;
    
	@Test//一定要用org.junit.Test
	@PrepareForTest(DataSummaryService.class)
	public void powermockTest() throws Exception {
		//用PowerMock將原Dao取代
		PowerMockito.whenNew(DataSummaryDao.class).withNoArguments().thenReturn(new FakeDataSummaryDao());
		Assertions.assertTrue(service.getServiceResult(1) == 2);
	}
}

class FakeDataSummaryDao extends DataSummaryDao{
	@Override
	public int getResult(int result) {
		return result + 1;
	}
}