package com.willy.ct.mock;

public class DataSummaryService {
	DataSummaryDao dao = new DataSummaryDao();
	public int getServiceResult(int result) {
		return dao.getResult(result);
	}
}
