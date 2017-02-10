package co.kr.hybridapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import co.kr.hybridapp.fregment.SlideViewFregment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	Context mContext;
	Activity av_;

	public SectionsPagerAdapter(Context context,Activity av , FragmentManager fm) {
		super(fm);
		mContext = context;
		av_ = av;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Log.e("SKY" , "ib_position  :: " + position);
		switch(position) {
		case 0:
			return new SlideViewFregment(mContext , av_ , 0);
		}
		return null;
	}
	@Override
	public int getCount() {
		// Show 3 total pages.
		return 1;
	}
}
