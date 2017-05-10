package com.mzp.libreads.main.reminder;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

/**
 * TAB红点提醒管理器
 */
public class ReminderManager {
    // callback
    public static interface UnreadNumChangedCallback {
        public void onUnreadNumChanged(ReminderItem item);
    }

    // singleton
    private static ReminderManager instance;

    public static synchronized ReminderManager getInstance() {
        if (instance == null) {
            instance = new ReminderManager();
        }

        return instance;
    }

    // observers
    private SparseArray<ReminderItem> items = new SparseArray<>();

    private List<UnreadNumChangedCallback> unreadNumChangedCallbacks = new ArrayList<>();

    private ReminderManager() {
        populate(items);
    }

    // interface
    public final void updateSessionUnreadNum(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.READ);
    }

    public final void updateSessionDeltaUnreadNum(int delta) {
        updateUnreadMessageNum(delta, true, ReminderId.READ);
    }

    public final void updateTeamUnreadNum(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.MESSAGE);
    }

    public final void updateContactUnreadNum(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.MESSAGE);
    }

    public void registerUnreadNumChangedCallback(UnreadNumChangedCallback cb) {
        if (unreadNumChangedCallbacks.contains(cb)) {
            return;
        }

        unreadNumChangedCallbacks.add(cb);
    }

    public void unregisterUnreadNumChangedCallback(UnreadNumChangedCallback cb) {
        if (!unreadNumChangedCallbacks.contains(cb)) {
            return;
        }

        unreadNumChangedCallbacks.remove(cb);
    }

    // inner
    private final void populate(SparseArray<ReminderItem> items) {
        items.put(ReminderId.READ, new ReminderItem(ReminderId.READ));
        items.put(ReminderId.MESSAGE, new ReminderItem(ReminderId.MESSAGE));
        items.put(ReminderId.ME, new ReminderItem(ReminderId.ME));
    }

    private final void updateUnreadMessageNum(int unreadNum, boolean delta, int reminderId) {
        ReminderItem item = items.get(reminderId);
        if (item == null) {
            return;
        }

        int num = item.getUnread();

        // 增量
        if (delta) {
            num = num + unreadNum;
            if (num < 0) {
                num = 0;
            }
        } else {
            num = unreadNum;
        }

        item.setUnread(num);
        item.setIndicator(false);

        for (UnreadNumChangedCallback cb : unreadNumChangedCallbacks) {
            cb.onUnreadNumChanged(item);
        }
    }
}
