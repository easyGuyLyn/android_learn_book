package com.dawoo.lotterybox.util;

import android.text.TextUtils;

import com.dawoo.lotterybox.BoxApplication;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by archar on 18-2-12.
 * <p>
 * 玩法界面 对玩法数据的分组构造,构造成想要的数据模型
 */

public class JsonToPlayBean {

    public static List<PlayTypeBean> getPlayBeansFromJson() {
        if (!TextUtils.isEmpty(SharePreferenceUtil.getPlayBeanListJson(BoxApplication.getContext()))) {
            return GsonUtil.jsonToList(SharePreferenceUtil.getPlayBeanListJson(BoxApplication.getContext()), PlayTypeBean.class);
        }
        List<PlayTypeBean> playBeans = new ArrayList<>();

        String json = SharePreferenceUtil.getPlayTypeListJson(BoxApplication.getContext());
        List<PlayTypeBean.PlayBean> playTypeBeans = GsonUtil.jsonToList(json, PlayTypeBean.PlayBean.class);

        LinkedHashMap<String, List<PlayTypeBean.PlayBean>> schemeMap = new LinkedHashMap<>();
        for (PlayTypeBean.PlayBean playTypeBean : playTypeBeans) {
            List<PlayTypeBean.PlayBean> tempList = schemeMap.get(playTypeBean.getScheme());
            if (tempList == null) {
                tempList = new ArrayList<>();
                tempList.add(playTypeBean);
                schemeMap.put(playTypeBean.getScheme(), tempList);
            } else {
                tempList.add(playTypeBean);
            }
        }


        for (String scheme : schemeMap.keySet()) {
            PlayTypeBean playBean = new PlayTypeBean();
            playBean.setPlayBeans(schemeMap.get(scheme));
            playBean.setType(scheme);
            playBean.setParentTitle(schemeMap.get(scheme).get(0).getMainType());
            playBeans.add(playBean);
        }

        LinkedHashMap<String, List<PlayTypeBean>> mainTypeMap = new LinkedHashMap<>();
        for (PlayTypeBean playBean : playBeans) {
            List<PlayTypeBean> tempList = mainTypeMap.get(playBean.getParentTitle());
            if (tempList == null) {
                tempList = new ArrayList<>();
                tempList.add(playBean);
                mainTypeMap.put(playBean.getParentTitle(), tempList);
            } else {
                tempList.add(playBean);
                playBean.setParentTitle("");
            }
        }
        SharePreferenceUtil.savePlayBeanJson(BoxApplication.getContext(), GsonUtil.GsonString(playBeans));
        return playBeans;
    }


}
