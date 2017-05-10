package com.mzp.libreads.common.holder;

import java.util.HashMap;
import java.util.Map;

import com.mzp.libreads.common.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder {
	public final View convertView;
	private Map<Integer, View> views = new HashMap<>();

	private CommonViewHolder(View convertView) {
		this.convertView = convertView;
	}

	public static CommonViewHolder createCVH(View convertView, ViewGroup parent, int layoutRes) {
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
			convertView = layoutInflater.inflate(layoutRes, parent, false);
			CommonViewHolder cvh = new CommonViewHolder(convertView);
			convertView.setTag(cvh);
		}
		CommonViewHolder cvh = (CommonViewHolder) convertView.getTag();

		return cvh;
	}

	public View getView(int id) {
		if (views.get(id) == null) {
			views.put(id, convertView.findViewById(id));
		}
		return views.get(id);
	}

	public TextView getTv(int id) {
		return ((TextView) getView(id));
	}

	public ImageView getIv(int id) {
		return ((ImageView) getView(id));
	}

	public Button getBTN(int id) {
		return ((Button) getView(id));
	}

	public TextView getEt(int id) {
		return ((EditText) getView(id));
	}
	
	public CircleImageView getCiv(int id){
		return (CircleImageView) getView(id);
	}
	
	public CheckBox getCB(int id){
		return (CheckBox) getView(id);
	}
}
