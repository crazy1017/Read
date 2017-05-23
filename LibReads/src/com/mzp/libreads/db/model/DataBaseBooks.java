package com.mzp.libreads.db.model;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.common.log.OperLog;
import com.mzp.libreads.common.utils.EnCodedUtil;
import com.mzp.libreads.common.utils.StringUtil;
import com.mzp.libreads.db.manager.DataBaseOpenHelperCode;
import com.mzp.libreads.db.manager.DataBaseOpenHelperManager;
import com.mzp.libreads.read.info.BookInfo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 读书存储表db的处理逻辑
 * @author may
 */
public class DataBaseBooks {
	
	private DataBaseBooks(){}
	
	private static final String TAG = DataBaseBooks.class.getSimpleName();
	
	private static DataBaseBooks baseBooks = new DataBaseBooks();
	
	public static DataBaseBooks getInstance(){
		return baseBooks;
	}
	
	/**
	 * 获取所有的书籍
	 * @return
	 */
	public List<BookInfo> getAllBooks(){
		SQLiteDatabase initDataBase = null;
		try {
			initDataBase = initDataBase();
			String sql ="select * from books order by _id desc";
			Cursor cursor = initDataBase.rawQuery(sql, null);
			if (cursor == null || cursor.getCount() == 0) {
				return null;
			}
			List<BookInfo> infos = new ArrayList<>();
			BookInfo bookInfo = null;
			while (cursor.moveToNext()) {
				bookInfo = new BookInfo();
				bookInfo.bookAuthor = EnCodedUtil.decode(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.BOOKAUTHOR)));
				bookInfo.bookId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.BOOKID));
				bookInfo.bookImg = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.BOOKIMG));
				bookInfo.bookName =  EnCodedUtil.decode(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.BOOKNAME)));;
				bookInfo.lastTime = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.LASTTIME));
				bookInfo.MD5 = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.MD5));
				bookInfo.path = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.PATH));
				bookInfo.readPagers = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.READPAGERS));
				bookInfo.updateOver = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseOpenHelperCode.Books.UPDATEOVER));
				infos.add(bookInfo);
			}
			return infos;
		} catch (Exception e) {
			e.printStackTrace();
			OperLog.error(TAG+" :getAllBooks()", e.toString());
			return null;
		}finally {
			closed();
		}
	}
	
	/**
	 * 
	 * @param bookInfo
	 */
	public void saveBook(BookInfo bookInfo){
		if (bookInfo == null) {
			OperLog.error(TAG +" : saveBook", "传递的数据存在问题,请仔细检查后继续传递");
			return ;
		}
		removeBooks(bookInfo);
		saveBooks(bookInfo);
	}
	/**
	 * 存储书籍
	 * @param bookInfo
	 */
	public void saveBooks(BookInfo bookInfo){
		SQLiteDatabase initDataBase = null;
		try {
			initDataBase = initDataBase();
			String bookAuthor =EnCodedUtil.encode(bookInfo.bookAuthor) ;
			String bookId = bookInfo.bookId;
			String bookImg = bookInfo.bookImg;
			String bookName = EnCodedUtil.encode(bookInfo.bookName);
			String lastTime = bookInfo.lastTime;
			String mD5 = bookInfo.MD5;
			String path = bookInfo.path;
			int readPagers = bookInfo.readPagers;
			int updateOver = bookInfo.updateOver;
			
			ContentValues values = new ContentValues();
			
			if (!StringUtil.isEmpty(bookAuthor)) {
				values.put(DataBaseOpenHelperCode.Books.BOOKAUTHOR, bookAuthor);
			}
			if (!StringUtil.isEmpty(bookId)) {
				values.put(DataBaseOpenHelperCode.Books.BOOKID, bookId);
			}
			if (!StringUtil.isEmpty(bookImg)) {
				values.put(DataBaseOpenHelperCode.Books.BOOKIMG, bookImg);
			}
			if (!StringUtil.isEmpty(bookName)) {
				values.put(DataBaseOpenHelperCode.Books.BOOKNAME, bookName);
			}
			if (!StringUtil.isEmpty(lastTime)) {
				values.put(DataBaseOpenHelperCode.Books.LASTTIME,lastTime );
			}
			if (!StringUtil.isEmpty(mD5)) {
				values.put(DataBaseOpenHelperCode.Books.MD5, mD5);
			}
			if (!StringUtil.isEmpty(path)) {
				values.put(DataBaseOpenHelperCode.Books.PATH, path);
			}
			values.put(DataBaseOpenHelperCode.Books.READPAGERS, readPagers);
			values.put(DataBaseOpenHelperCode.Books.UPDATEOVER, updateOver);
			
			initDataBase.insert(DataBaseOpenHelperCode.TableCode.BOOKS, null, values);
		} catch (Exception e) {
			e.printStackTrace();
			OperLog.error(TAG +" : saveBooks()", e.toString());
		}finally {
			closed();
		}
	}
	
	/**
	 * 更新bookInfo的信息
	 * @param bookInfo
	 */
	public void updateBooks(BookInfo bookInfo){
		if (bookInfo == null) {
			OperLog.error(TAG +" : updateBooks()", "传递的数据存在问题,请仔细检查后继续传递");
			return;
		}
		SQLiteDatabase initDataBase = null;
		try {
			initDataBase = initDataBase();
			ContentValues values = new ContentValues();
			values.put(DataBaseOpenHelperCode.Books.READPAGERS, bookInfo.readPagers);
			int update = initDataBase.update(DataBaseOpenHelperCode.TableCode.BOOKS, values, DataBaseOpenHelperCode.Books.BOOKID+" = ?", new String[]{bookInfo.bookId});
			if (update > 0) {
				OperLog.error(TAG, "更新成功");
			}else {
				OperLog.error(TAG, "更新失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			OperLog.error(TAG +" : updateBooks()", e.toString());
		}finally {
			closed();
		}
	}
	
	/**
	 * 移除本地书籍
	 */
	public void removeBooks(BookInfo bookInfo){
		if (bookInfo == null) {
			OperLog.error(TAG +" : removeBooks()", "传递的数据存在问题,请仔细检查后继续传递");
			return;
		}
		SQLiteDatabase initDataBase = null;
		try {
			initDataBase = initDataBase();
			int delete = initDataBase.delete(DataBaseOpenHelperCode.TableCode.BOOKS, DataBaseOpenHelperCode.Books.BOOKID+" = ?", new String[]{bookInfo.bookId});
			if (delete >0) {
				OperLog.error(TAG+" : removeBooks()", "删除成功");
			}else {
				OperLog.error(TAG+" : removeBooks()", "删除失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			OperLog.error(TAG+" : removeBooks()", e.toString());
		}finally {
			closed();
		}
	}
	
	/**
	 * 获取db引用
	 * @return
	 */
	private SQLiteDatabase initDataBase(){
		return DataBaseOpenHelperManager.getInstance().getDataBase();
	}
	
	/**
	 * 关闭db
	 */
	private void closed(){
		DataBaseOpenHelperManager.getInstance().closeDataBase();
	}
}
