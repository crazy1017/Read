package com.mzp.libreads.main.reminder;

public class ReminderSettings {
	/**
	 * 最大显示未读数
	 */
	public static final int MAX_UNREAD_SHOW_NUMBER = 99;
	/**
	 * 最大显示折叠消息
	 */
	public static final int MAX_FOLD_NUMBER = 999;

	public static int unreadMessageShowRule(int unread) {
		return Math.min(MAX_UNREAD_SHOW_NUMBER, unread);
	}

	public static int foldMessageShowRule(int msgNumber) {
		return Math.min(MAX_FOLD_NUMBER, msgNumber);
	}

}
