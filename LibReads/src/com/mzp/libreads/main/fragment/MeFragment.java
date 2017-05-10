package com.mzp.libreads.main.fragment;

import com.mzp.libreads.main.model.MainTab;
import com.mzp.libreads.main.model.MainTabFragment;

public class MeFragment extends MainTabFragment {

	@Override
	protected void onInit() {
		this.setFragmentId(MainTab.ME.fragmentId);
	}
}
