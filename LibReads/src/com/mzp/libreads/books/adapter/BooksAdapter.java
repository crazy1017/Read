package com.mzp.libreads.books.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.R;
import com.mzp.libreads.common.holder.CommonViewHolder;
import com.mzp.libreads.read.info.BookInfo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BooksAdapter extends BaseAdapter {

	private List<BookInfo> bookInfos;
	public BooksAdapter(List<BookInfo> bookInfos) {
		// TODO Auto-generated constructor stub
		this.bookInfos = bookInfos == null ? new ArrayList<BookInfo>() : bookInfos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bookInfos == null ? 0 : bookInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bookInfos == null ? 0 : bookInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return bookInfos == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CommonViewHolder createCVH = CommonViewHolder.createCVH(convertView, parent, R.layout.items_books);
		TextView tv_bookname = createCVH.getTv(R.id.tv_bookname);
		TextView tv_bookAuthor = createCVH.getTv(R.id.tv_bookAuthor);
		BookInfo bookInfo = bookInfos.get(position);
		tv_bookAuthor.setText(bookInfo.bookAuthor);
		tv_bookname.setText(bookInfo.bookName);
		return createCVH.convertView;
	}

}
