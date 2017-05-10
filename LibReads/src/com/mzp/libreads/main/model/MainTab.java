package com.mzp.libreads.main.model;

import com.mzp.libreads.R;
import com.mzp.libreads.main.fragment.BooksFragment;
import com.mzp.libreads.main.fragment.MeFragment;
import com.mzp.libreads.main.fragment.MessageFragment;
import com.mzp.libreads.main.fragment.ReadFragment;
import com.mzp.libreads.main.reminder.ReminderId;

public enum MainTab {

	READ(0, ReminderId.READ, ReadFragment.class, R.string.main_tab_read, R.layout.fragment_read),
	BOOKS(1, ReminderId.BOOKS, BooksFragment.class, R.string.main_tab_books, R.layout.fragment_books),
	MESSAGE(2, ReminderId.MESSAGE,MessageFragment.class, R.string.main_tab_message, R.layout.fragment_message),
    ME(3, ReminderId.ME, MeFragment.class, R.string.main_tab_me, R.layout.fragment_me);

	public final int tabIndex;

	public final int reminderId;

	public final Class<? extends MainTabFragment> clazz;

	public final int resId;

	public final int fragmentId;

    public final int layoutId;

	private MainTab(int index, int reminderId, Class<? extends MainTabFragment> clazz, int resId, int layoutId) {
		this.tabIndex = index;
		this.reminderId = reminderId;
		this.clazz = clazz;
		this.resId = resId;
		this.fragmentId = index;
        this.layoutId = layoutId;
	}

	public static final MainTab fromReminderId(int reminderId) {
		for (MainTab value : MainTab.values()) {
			if (value.reminderId == reminderId) {
				return value;
			}
		}

		return null;
	}

	public static final MainTab fromTabIndex(int tabIndex) {
		for (MainTab value : MainTab.values()) {
			if (value.tabIndex == tabIndex) {
				return value;
			}
		}

		return null;
	}
}
