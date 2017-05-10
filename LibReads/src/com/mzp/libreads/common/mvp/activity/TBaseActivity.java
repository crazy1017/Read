package com.mzp.libreads.common.mvp.activity;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.common.mvp.fragment.TFragment;
import com.mzp.libreads.common.mvp.infre.Handlers;
import com.mzp.libreads.common.sys.ReflectionUtil;
import com.mzp.libreads.main.MainActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class TBaseActivity extends FragmentActivity{

	private boolean destroyed = false;
	private List<Activity> list = new ArrayList<>();

	@Override
	protected void onStart() {
		super.onStart();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list.add(this);
	}

	@Override
	public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyed = true;
		list.remove(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onNavigateUpClicked();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onNavigateUpClicked() {
		onBackPressed();
	}

	protected final Handler getHandler() {
		return Handlers.sharedHandler(this);
	}

	protected void showKeyboard(boolean isShow) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShow) {
			if (getCurrentFocus() == null) {
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			} else {
				imm.showSoftInput(getCurrentFocus(), 0);
			}
		} else {
			if (getCurrentFocus() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 延时弹出键盘
	 * 
	 * @param focus
	 *            ：键盘的焦点项
	 */
	protected void showKeyboardDelayed(View focus) {
		final View viewToFocus = focus;
		if (focus != null) {
			focus.requestFocus();
		}

		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (viewToFocus == null || viewToFocus.isFocused()) {
					showKeyboard(true);
				}
			}
		}, 200);
	}


	public boolean isDestroyedCompatible() {
		if (Build.VERSION.SDK_INT >= 17) {
			return isDestroyedCompatible17();
		} else {
			return destroyed || super.isFinishing();
		}
	}

	@TargetApi(17)
	private boolean isDestroyedCompatible17() {
		return super.isDestroyed();
	}

	/** fragment management */
	public TFragment addFragment(TFragment fragment) {
		List<TFragment> fragments = new ArrayList<TFragment>(1);
		fragments.add(fragment);

		List<TFragment> fragments2 = addFragments(fragments);
		return fragments2.get(0);
	}

	public List<TFragment> addFragments(List<TFragment> fragments) {
		List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		boolean commit = false;
		for (int i = 0; i < fragments.size(); i++) {
			// install
			TFragment fragment = fragments.get(i);
			int id = fragment.getFragmentId();

			// exists
			TFragment fragment2 = (TFragment) fm.findFragmentById(id);

			if (fragment2 == null) {
				fragment2 = fragment;
				transaction.add(id, fragment);
				commit = true;
			}

			fragments2.add(i, fragment2);
		}

		if (commit) {
			try {
				transaction.commitAllowingStateLoss();
			} catch (Exception e) {

			}
		}

		return fragments2;
	}

	public void switchContent(TFragment fragment) {
		switchContent(fragment, false);
	}

    protected TFragment switchContent(TFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragment.getFragmentId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }

        return fragment;
    }

	protected boolean displayHomeAsUpEnabled() {
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			return onMenuKeyDown();

		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	protected boolean onMenuKeyDown() {
		return false;
	}

    private void invokeFragmentManagerNoteStateNotSaved() {
        FragmentManager fm = getSupportFragmentManager();
        ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
    }

    protected void switchFragmentContent(TFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fragment.getFragmentId(), fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }
    
    protected void removeAllActivity(){
    	for(Activity activity: list){
    		if(activity instanceof MainActivity){
    			return;
    		}else{
    			activity.finish();
    		}
    	}
    	list = null;
    }
}
