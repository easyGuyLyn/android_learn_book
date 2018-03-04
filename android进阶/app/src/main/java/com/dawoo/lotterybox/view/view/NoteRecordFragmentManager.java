package com.dawoo.lotterybox.view.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.dawoo.lotterybox.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by archar on 2018.
 */
public class NoteRecordFragmentManager {

    private Class<? extends Fragment> fromClass;

    private NoteRecordFragmentManager() {
    }

    private static NoteRecordFragmentManager mInstance = new NoteRecordFragmentManager();

    public static NoteRecordFragmentManager getInstance() {
        return mInstance;
    }

    private Map<String, Fragment> mFragments = new HashMap<>();


    public Fragment getFragment(Class<? extends Fragment> clazz) {
        if (clazz == null) {
            return null;
        }
        String key = clazz.getSimpleName();
        Fragment fragment = mFragments.get(key);
        if (fragment == null) {
            synchronized (NoteRecordFragmentManager.class) {
                try {
                    fragment = clazz.newInstance();
                    mFragments.put(key, fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return fragment;
    }


    public void clear() {
        mFragments.clear();
        fromClass = null;
    }

    public void switchFragment(FragmentManager mFragmentManager, Class<? extends Fragment> clazz) {
        Fragment fromFragment = getFragment(fromClass);
        Fragment toFragment = getFragment(clazz);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
     //   transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        if (fromFragment != null && !fromFragment.isHidden() && fromFragment != toFragment) {
            if (!toFragment.isAdded()) {
                transaction.hide(fromFragment).add(R.id.fragment_content, toFragment);
            } else {
                transaction.hide(fromFragment).show(toFragment);
            }
        } else {
            if (!toFragment.isAdded()) {
                transaction.add(R.id.fragment_content, toFragment);
            } else {
                transaction.show(toFragment);
            }
        }
        transaction.commitAllowingStateLoss();
        fromClass = clazz;
    }
}
