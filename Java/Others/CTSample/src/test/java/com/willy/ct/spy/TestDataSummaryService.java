package com.willy.ct.spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.willy.ct.mock.DataSummaryDao;
import com.willy.ct.mock.DataSummaryService;

@RunWith(MockitoJUnitRunner.class)
public class TestDataSummaryService {
	@InjectMocks//被注入SPY的對象
    private DataSummaryService localService;
    @Spy//注入SPY的對象
    private DataSummaryDao dao;

    //如果不使用上述注解，可以使用@Before方法来手动进行mock对象的创建和注入，但会几行很多代码
    /*
    private LocalServiceImpl localService;
    private RemoteServiceImpl remoteService;

    @Before
    public void setUp() throws Exception {
        localService = new LocalServiceImpl();
        remoteService = Mockito.mock(RemoteServiceImpl.class);  //创建Mock对象
        Whitebox.setInternalState(localService, "remoteService", remoteService); //注入依赖对象
    }
    */

    @Test
    public void testSpy() throws Exception {
    	int target = 3;
        Mockito.when(dao.getResult(1)).thenReturn(target); //指定Mock的回傳值
        int result = localService.getServiceResult(1);
        assertEquals(target, result);
    }
    
    /**
     * Mock與Spy的不同
     * Spy碰到Mockito未指定的條件時，會按原程式邏輯返回值
     * Mock碰到Mockito未指定的條件時，會按返回值型別的預設值返回(0,false,null)
     * @throws Exception 
     */
    @Test
    public void testBetweenMockAndSpy() throws Exception {
    	int target = 3;
        Mockito.when(dao.getResult(1)).thenReturn(target); //指定Mock的回傳值
        int result = localService.getServiceResult(3);
        assertEquals(target, result);
    }
}
