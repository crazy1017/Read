package com.mzp.libreads.message.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.message.info.MessageInfo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MessageListAdapter extends BaseAdapter {

	private List<MessageInfo> messageInfos;
	public MessageListAdapter(List<MessageInfo> messageInfos) {
		// TODO Auto-generated constructor stub
		this.messageInfos = messageInfos == null ? new ArrayList<MessageInfo>() : messageInfos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageInfos != null ? messageInfos.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageInfos != null ? messageInfos.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return messageInfos != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	public void notifyDataSetChanged(List<MessageInfo> messageInfos){
		this.messageInfos = messageInfos == null ? new ArrayList<MessageInfo>() : messageInfos;
		notifyDataSetChanged();
	}
}
