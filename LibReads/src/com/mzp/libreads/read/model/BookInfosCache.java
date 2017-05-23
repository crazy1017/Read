package com.mzp.libreads.read.model;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.common.log.OperLog;
import com.mzp.libreads.common.utils.StringUtil;
import com.mzp.libreads.db.model.DataBaseBooks;
import com.mzp.libreads.read.info.BookInfo;

public class BookInfosCache {
	
	private static final String TAG = BookInfosCache.class.getSimpleName();

	private BookInfosCache (){}
	
	private static BookInfosCache bookInfosCache = new BookInfosCache();
	
	public static BookInfosCache getInstance(){
		return bookInfosCache;
	}
	
	private List<BookInfo> bookInfos = null;
	
	public List<BookInfo> getBookInfos (){
		bookInfos = DataBaseBooks.getInstance().getAllBooks();
		return bookInfos;
	}
	
	public void addBookInfos(BookInfo bookInfo){
		if (bookInfos != null) {
			for (int i=0 ; i<bookInfos.size() ;i++) {
				BookInfo info = bookInfos.get(i);
				if (info.bookId.equals(bookInfo.bookId)) {
					bookInfos.remove(i);
					break;
				}
			}
			bookInfos.add(0, bookInfo);
		}else {
			bookInfos = new ArrayList<>();
			bookInfos.add(bookInfo);
		}
	}
	
	public void removeBookInfos(BookInfo bookInfo){
		if (bookInfo == null || StringUtil.isEmpty(bookInfo.bookId)) {
			OperLog.error(TAG +" :removeBookInfos()", "传递的参数有误,请仔细检测");
			return;
		}
		if (bookInfos != null) {
			for (int i=0 ; i<bookInfos.size() ;i++) {
				BookInfo info = bookInfos.get(i);
				if (info.bookId.equals(bookInfo.bookId)) {
					bookInfos.remove(i);
					break;
				}
			}
		}
	}
	
	public void reset(){
		bookInfos = null;
	}
}
