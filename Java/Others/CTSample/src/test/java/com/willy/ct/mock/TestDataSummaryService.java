package com.willy.ct.mock;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestDataSummaryService {
	@InjectMocks//被注入MOCK的對象
    private DataSummaryService localService;
    @Mock//注入MOCK的對象
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
    public void testMock() throws Exception {
    	int target = 3;
        Mockito.when(dao.getResult(1)).thenReturn(target); //指定Mock的回傳值
        int result = localService.getServiceResult(1);
        assertEquals(target, result);
    }
    
    @Test
    public void testMock_Any() throws Exception {
    	int target = 3;

    	//用Any包含所有可能
        Mockito.when(dao.getResult(ArgumentMatchers.anyInt())).thenReturn(target); //指定Mock的回傳值
        int result = localService.getServiceResult(1);
        assertEquals(target, result);
    }

    //每次呼叫的回傳值不同，叫到第4次就拋exception
    @Test
    public void testMultipleReturnAndException() throws Exception {
    	int target = 3;

        Mockito.when(dao.getResult(ArgumentMatchers.anyInt())).thenReturn(target).thenReturn(target+1).thenReturn(target+2).thenThrow(new Exception()); //指定Mock的回傳值
        assertEquals(target, localService.getServiceResult(1));
        assertEquals(target+1, localService.getServiceResult(1));
        assertEquals(target+2, localService.getServiceResult(1));
    }
    
    //停用方法中的呼叫的回傳值為viod的方法[doSomthing]
    @Test
    public void testSuspendVoidMethod() throws Exception {
    	int target = 3;
    	Mockito.doNothing().when(dao).doSomthing();
        Mockito.when(dao.getResult(ArgumentMatchers.anyInt())).thenReturn(target); //指定Mock的回傳值
        assertEquals(target, localService.getServiceResult(1));
    }
    
    //驗證方法呼叫次數
    @Test
    public void testVerify() throws Exception {
    	int target = 3;
        Mockito.when(dao.getResult(1)).thenReturn(target); //指定Mock的回傳值
        Mockito.verify(dao, Mockito.never()).getResult(1); //mock方法未调用过
        int result = localService.getServiceResult(1);
        Mockito.verify(dao, Mockito.times(1)).getResult(1); //mock方法未调用过
        assertEquals(target, result);
    }
    
    //取得傳入該方法的參數，並調用真實對象
    @Test
    public void testVerifyCapture() throws Exception {
    	ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
    	
    	int target = 3;
    	//設定條件
        Mockito.when(dao.getResult(1)).thenReturn(target); //指定Mock的回傳值
        Mockito.when(dao.getResult(2)).thenReturn(2).thenCallRealMethod(); //指定Mock的回傳值，並調用真實對象
        
        //呼叫函數
        localService.getServiceResult(1);
        localService.getServiceResult(2);
        
      //設定capture取得最後一次呼叫時參數
        Mockito.verify(dao, Mockito.atLeastOnce()).getResult(captor.capture());
        
        assertEquals(2, captor.getValue().intValue());
        
        //列出所有參數
        List<Integer> list = captor.getAllValues();
        
    }
    
    /**
     * Mock與Spy的不同
     * Spy碰到Mockito未指定的條件時，會按原程式邏輯返回值
     * Mock碰到Mockito未指定的條件時，會按返回值型別的預設值返回(0,false,null)
     * @throws Exception 
     */
    @Test
    public void testBetweenMockAndSpy() throws Exception {
    	int target = 0;
        Mockito.when(dao.getResult(1)).thenReturn(target); //指定Mock的回傳值
        int result = localService.getServiceResult(3);
        assertEquals(target, result);
    }
}
