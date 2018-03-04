package com.dawoo.lotterybox.view.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dawoo.lotterybox.R;
import com.hwangjr.rxbus.RxBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by archar on 2018.
 */
public class SscBFragmentManager {

    public static final String TOP_FRGMENT = "top_fragment";

    private Class<? extends Fragment> fromClass;

    private SscBFragmentManager() {
    }


    private static SscBFragmentManager mInstance = new SscBFragmentManager();

    public static SscBFragmentManager getInstance() {
        return mInstance;
    }


    private HashMap<String, Fragment> mFragmentHashMap = new HashMap<>();
    private FragmentManager mFragmentManager;

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public Fragment getFragment(Class<? extends Fragment> clazz) {
        if (clazz == null) {
            return null;
        }
        String key = clazz.getSimpleName();
        Fragment fragment = mFragmentHashMap.get(key);
        if (fragment == null) {
            synchronized (SscBFragmentManager.class) {
                try {
                    fragment = clazz.newInstance();
                    mFragmentHashMap.put(key, fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return fragment;
    }


    public void clear() {
        clearAllFragmentQuote();
        fromClass = null;
    }

    //清除所有fragment被栈的引用
    //再清除map对fragment的引用  很快就会释放掉所有的fragment
    private void clearAllFragmentQuote() {
        Iterator iterator = mFragmentHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Fragment fragment = (Fragment) entry.getValue();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commitAllowingStateLoss();
        }
        mFragmentHashMap.clear();
    }


    public void switchFragment(FragmentManager mFragmentManager, Class<? extends Fragment> clazz) {
        setFragmentManager(mFragmentManager);
        Fragment fromFragment = getFragment(fromClass);
        Fragment toFragment = getFragment(clazz);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom_ssc, R.anim.slide_out_top_ssc);
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
        RxBus.get().post(TOP_FRGMENT, toFragment.getId() + "");
    }
}
