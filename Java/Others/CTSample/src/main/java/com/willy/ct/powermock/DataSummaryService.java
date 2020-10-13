package com.willy.ct.powermock;

public class DataSummaryService {
	public int getServiceResult(int result) {
		return new DataSummaryDao().getResult(result);
	}
}
