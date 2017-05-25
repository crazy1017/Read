package com.mzp.libreads.books;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.common.utils.StringUtil;
import com.mzp.libreads.common.utils.TimerUtil;
import com.mzp.libreads.read.info.BookInfo;

public class BooksCache {

	private BooksCache(){}
	
	private static BooksCache booksCache = new BooksCache();
	
	public static BooksCache getInstance(){
		return booksCache;
	}
	
	private List<BookInfo> listBookCache = null;
	
	
	public List<BookInfo> getAllBooks(){
		if (listBookCache == null) {
			listBookCache = new ArrayList<>();
			BookInfo info = null;
			for (int i = 0;i< 50; i++) {
				info = new BookInfo();
				info.bookAuthor ="张三 "+i;
				info.bookName ="JAVA编程思想  "+i;
				info.bookId =StringUtil.getUUID();
				info.lastTime =TimerUtil.getCurrentTime();
				listBookCache.add(info);
			}
		}
		return listBookCache;
	}
	
	public void reset(){
		listBookCache = null;
	}
}
