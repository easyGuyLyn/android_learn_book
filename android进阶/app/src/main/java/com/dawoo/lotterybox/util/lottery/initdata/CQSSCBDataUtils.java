package com.dawoo.lotterybox.util.lottery.initdata;


import com.dawoo.lotterybox.BoxApplication;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.playType.PlayDetailBean;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;
import com.dawoo.lotterybox.bean.playType.SSCBPlayChhooseBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by archar on 18-2-27.
 */

public class CQSSCBDataUtils {

    //初始化 左边导航选择项的数据源
    public static List<SSCBPlayChhooseBean> initChooseData() {
        List<SSCBPlayChhooseBean> sscbPlayChhooseBeanList = new ArrayList<>();
        List<String> stringList = Arrays.asList(BoxApplication.getContext().getResources().getStringArray(R.array.cqssc_b_playType));

        for (int i = 0; i < stringList.size(); i++) {
            SSCBPlayChhooseBean sscbPlayChhooseBean = new SSCBPlayChhooseBean();
            if (i == 0) {
                sscbPlayChhooseBean.setSelected(true);
            }
            sscbPlayChhooseBean.setBetName(stringList.get(i));
            sscbPlayChhooseBeanList.add(sscbPlayChhooseBean);
        }
        return sscbPlayChhooseBeanList;
    }

    //刷新 左边导航选择项的数据源
    public static void refreshChooseData(List<SSCBPlayChhooseBean> list, int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setSelected(true);
            } else {
                list.get(i).setSelected(false);
            }
        }
    }


    //初始化  数字盘的基础数据源
    public static List<PlayTypeBean.PlayBean> initSZPData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("于万位自0~9任选1个号进行投注，当开奖结果与所选的定位与号码相同且顺序一致时，即为中奖。");
        playBean1.setSample("万位选了9，开奖号码万位是9，就中奖了");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.ww));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("于千位自0~9任选1个号进行投注，当开奖结果与所选的定位与号码相同且顺序一致时，即为中奖。");
        playBean2.setSample("千位选了9，开奖号码千位是9，就中奖了");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qw));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("于百位自0~9任选1个号进行投注，当开奖结果与所选的定位与号码相同且顺序一致时，即为中奖。");
        playBean3.setSample("百位选了9，开奖号码百位是9，就中奖了");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bw));

        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("于十位自0~9任选1个号进行投注，当开奖结果与所选的定位与号码相同且顺序一致时，即为中奖。");
        playBean4.setSample("十位选了9，开奖号码十位是9，就中奖了");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.sw));

        PlayTypeBean.PlayBean playBean5 = new PlayTypeBean.PlayBean();
        playBean5.setPlayTypeExplain("于个位自0~9任选1个号进行投注，当开奖结果与所选的定位与号码相同且顺序一致时，即为中奖。");
        playBean5.setSample("个位选了9，开奖号码个位是9，就中奖了");
        playBean5.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.gw));

        playBeanList.add(playBean1);
        playBeanList.add(playBean2);
        playBeanList.add(playBean3);
        playBeanList.add(playBean4);
        playBeanList.add(playBean5);
        return playBeanList;
    }


    //初始化 数字盘 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_SZP_0_9Data(String kind) {
        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.SZP));
            playDetailBean.setNum(i + "");
            playDetailBean.setKind(kind);
            playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_9_85));
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  双面的基础数据源
    public static List<PlayTypeBean.PlayBean> initSMData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("取万位为基准，大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean1.setSample("投注者购买万位小，当期开奖结果如为20352（2为小），则视为中奖");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.ww));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("取千位为基准，大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean2.setSample("投注者购买千位小，当期开奖结果如为20352（0为小），则视为中奖");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qw));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("取百位为基准，大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean3.setSample("投注者购买百位小，当期开奖结果如为20352（3为小），则视为中奖");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bw));

        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("取百位为基准，大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean4.setSample("投注者购买十位小，当期开奖结果如为20342（4为小），则视为中奖");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.sw));

        PlayTypeBean.PlayBean playBean5 = new PlayTypeBean.PlayBean();
        playBean5.setPlayTypeExplain("取个位为基准，大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean5.setSample("投注者购买个位小，当期开奖结果如为20352（2为小），则视为中奖");
        playBean5.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.gw));

        PlayTypeBean.PlayBean playBean6 = new PlayTypeBean.PlayBean();
        playBean6.setPlayTypeExplain(
                "开奖结果所有号码总和的为23、24、25、26、27、28、29、30、31、32、33、34、35、36、37、38、39、40、41、42、43、44、45时为“大”， 若为0、1、2、3、4、5、6、7、8、9、10、11、12、13、14、15、16、17、18、19、20、21、22时为“小”，当投注和数大小与开奖结果的和数大小相符时，即为中奖。 五颗球开出1，2，3，4，5 派彩为＄105\n" +
                        "五顆球开出1，2，3，5，5 派彩为＄105\n" +
                        "五顆球开出1，2，5，5，5 派彩为＄105\n" +
                        "前三：0~9任选1个号进行投注，当开奖结果[万位、千位、百位]任一数与所选的号码相同时，即为中奖。");
        playBean6.setSample("投注者购买总大，当期开奖结果如为20976（万2+千0+百9+十7+个6=24为大），则视为中奖。");
        playBean6.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.zh));


        playBeanList.add(playBean1);
        playBeanList.add(playBean2);
        playBeanList.add(playBean3);
        playBeanList.add(playBean4);
        playBeanList.add(playBean5);
        playBeanList.add(playBean6);

        return playBeanList;
    }


    //初始化 双面 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_SM_0_9Data(boolean isSignal, String kind) {
        String[] arrays;
        if (isSignal) {
            arrays = new String[]{"大", "小", "单", "双", "质", "和"};
        } else {
            arrays = new String[]{"总大", "总小", "总单", "总双"};
        }
        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.SM));
            playDetailBean.setNum(arrays[i]);
            playDetailBean.setKind(kind);
            playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_1_985));
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  一字定位的数据源
    public static List<PlayTypeBean.PlayBean> initYZDWData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("取万位为基准，自0~9、大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean1.setSample("万位选了9，开奖号码万位是9，就中奖了");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.ww));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("取千位为基准，自0~9、大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean2.setSample("千位选了9，开奖号码千位是9，就中奖了");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qw));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("取百位为基准，自0~9、大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean3.setSample("百位选了9，开奖号码百位是9，就中奖了");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bw));

        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("取十位为基准，自0~9、大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean4.setSample("十位选了9，开奖号码十位是9，就中奖了");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.sw));

        PlayTypeBean.PlayBean playBean5 = new PlayTypeBean.PlayBean();
        playBean5.setPlayTypeExplain("取个位为基准，自0~9、大小、单双、质合任选1个号进行投注，当开奖结果与所选号码相同时，即为中奖。");
        playBean5.setSample("个位选了9，开奖号码个位是9，就中奖了");
        playBean5.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.gw));

        playBeanList.add(playBean1);
        playBeanList.add(playBean2);
        playBeanList.add(playBean3);
        playBeanList.add(playBean4);
        playBeanList.add(playBean5);
        return playBeanList;
    }

    //初始化 一字定位 0-9 大小单双质和 的布局 的数据源
    public static List<PlayDetailBean> init_YZDW_0_9_DXDSZH_Data(boolean zonghe, String kind, String childType) {
        String[] arrays;
        if (!zonghe) {
            arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "大", "小", "单", "双", "质", "和"};
        } else {
            arrays = new String[]{"总大", "总小", "总单", "总双"};
        }

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.YZDW));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            if (zonghe) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_1_985));
            } else {
                if (i >= 10) {
                    playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_1_985));
                } else {
                    playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_9_85));
                }
            }
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  二字定位的数据源
    public static List<PlayTypeBean.PlayBean> initRZDWData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean1.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wq));

        PlayTypeBean.PlayBean playBean11 = new PlayTypeBean.PlayBean();
        playBean11.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean11.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean11.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wb));


        PlayTypeBean.PlayBean playBean12 = new PlayTypeBean.PlayBean();
        playBean12.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean12.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean12.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.ws));


        PlayTypeBean.PlayBean playBean13 = new PlayTypeBean.PlayBean();
        playBean13.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean13.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean13.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wg));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean2.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qb));

        PlayTypeBean.PlayBean playBean21 = new PlayTypeBean.PlayBean();
        playBean21.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean21.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean21.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qs));


        PlayTypeBean.PlayBean playBean22 = new PlayTypeBean.PlayBean();
        playBean22.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean22.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean22.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qg));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean3.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bs));

        PlayTypeBean.PlayBean playBean31 = new PlayTypeBean.PlayBean();
        playBean31.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean31.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean31.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bg));

        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("任选二位，自0~9任选2个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean4.setSample("选了 0,9，开奖号码对应位是0,9，就中奖了");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.sg));


        playBeanList.add(playBean1);
        playBeanList.add(playBean11);
        playBeanList.add(playBean12);
        playBeanList.add(playBean13);
        playBeanList.add(playBean2);
        playBeanList.add(playBean21);
        playBeanList.add(playBean22);
        playBeanList.add(playBean3);
        playBeanList.add(playBean31);
        playBeanList.add(playBean4);
        return playBeanList;
    }


    //初始化 二字定位 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_RZDW_0_9_Data(String kind, String childType) {
        String[] arrays;
        arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.RZDW));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_9_85));
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  三字定位的数据源
    public static List<PlayTypeBean.PlayBean> initSZDWData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean1.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wqb));

        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean2.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wqs));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean3.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wqg));


        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean4.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wbs));

        PlayTypeBean.PlayBean playBean5 = new PlayTypeBean.PlayBean();
        playBean5.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean5.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean5.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wbg));


        PlayTypeBean.PlayBean playBean6 = new PlayTypeBean.PlayBean();
        playBean6.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean6.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean6.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wsg));


        PlayTypeBean.PlayBean playBean7 = new PlayTypeBean.PlayBean();
        playBean7.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean7.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean7.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qbs));


        PlayTypeBean.PlayBean playBean8 = new PlayTypeBean.PlayBean();
        playBean8.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean8.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean8.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qbg));

        PlayTypeBean.PlayBean playBean9 = new PlayTypeBean.PlayBean();
        playBean9.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean9.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean9.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qsg));


        PlayTypeBean.PlayBean playBean10 = new PlayTypeBean.PlayBean();
        playBean10.setPlayTypeExplain("任选三位，自0~9任选3个号进行投注，当开奖结果与所选号码相同且顺序一致时，即为中奖");
        playBean10.setSample("选了 0,9，2,开奖号码对应位是0,9，2,就中奖了");
        playBean10.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bsg));

        playBeanList.add(playBean1);
        playBeanList.add(playBean2);
        playBeanList.add(playBean3);
        playBeanList.add(playBean4);
        playBeanList.add(playBean5);
        playBeanList.add(playBean6);
        playBeanList.add(playBean7);
        playBeanList.add(playBean8);
        playBeanList.add(playBean9);
        playBeanList.add(playBean10);
        return playBeanList;
    }


    //初始化 三字定位 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_SZDW_0_9_Data(String kind, String childType) {
        String[] arrays;
        arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.SZDW));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_9_85));
            list.add(playDetailBean);
        }
        return list;
    }

    //初始化  一字组合的数据源
    public static List<PlayTypeBean.PlayBean> initYZZHData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("0~9任选1个号进行投注，当开奖结果[万位、千位、百位、十位、个位]任一数与所选的号码相同时，即为中奖。同个号码出现多次时只计一次中奖");
        playBean1.setSample("下注一字【5号】＄100，一字賠率2.404,五颗球开出9，5，8，3，5 派彩为＄240.4");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.q5yzzh));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("0~9任选1个号进行投注，当开奖结果[万位、千位、百位]任一数与所选的号码相同时，即为中奖。");
        playBean2.setSample("下注万位【9号】，五颗球开出9，5，8，3，5 , 万位相同，中奖");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.q3yzzh));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("0~9任选1个号进行投注，当开奖结果[千位、百位、十位]任一数与所选的号码相同时，即为中奖。");
        playBean3.setSample("下注千【5号】，五颗球开出9，5，8，3，5 , 千位相同，中奖");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.z3yzzh));

        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("0~9任选1个号进行投注，当开奖结果[百位、十位、个位]任一数与所选的号码相同时，即为中奖。");
        playBean4.setSample("下注百【8号】，五颗球开出9，5，8，3，5 , 百位相同，中奖");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.h3yzzh));


        playBeanList.add(playBean1);
        playBeanList.add(playBean2);
        playBeanList.add(playBean3);
        playBeanList.add(playBean4);
        return playBeanList;
    }

    //初始化 一字组合 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_YZZH_0_9_Data(String kind, String childType) {
        String[] arrays;
        arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.YZZH));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            if (kind.equals(BoxApplication.getContext().getResources().getString(R.string.q5yzzh))) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_1_98));
            } else if (kind.equals(BoxApplication.getContext().getResources().getString(R.string.q3yzzh))) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_3_03));
            } else if (kind.equals(BoxApplication.getContext().getResources().getString(R.string.z3yzzh))) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_3_03));
            } else if (kind.equals(BoxApplication.getContext().getResources().getString(R.string.h3yzzh))) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_3_03));
            }
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  组选三的数据源
    public static List<PlayTypeBean.PlayBean> initZX3Data() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("会员可以挑选5~10个号码，当开奖结果[万位、千位、百位]中有且只有两个号码重复，则视为中奖。挑选不同个数的号码有其相对应的赔率。如果是选择(1、2、3、4、5)，则只要开奖结果[万位、千位、百位]中，有出现1、2、3、4、5中的任何两个号码，且其中有一个号码重复则中奖。 ");
        playBean1.setSample("※例如：112、344，若是开出豹子则不算中奖。 ");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.q3zx3));

        PlayTypeBean.PlayBean playBean11 = new PlayTypeBean.PlayBean();
        playBean11.setPlayTypeExplain("会员可以挑选5~10个号码，当开奖结果[千位、百位、十位]中有且只有两个号码重复，则视为中奖。挑选不同个数的号码有其相对应的赔率。如果是选择(1、2、3、4、5)，则只要开奖结果[千位、百位、十位]中，有出现1、2、3、4、5中的任何两个号码，且其中有一个号码重复则中奖。 ");
        playBean11.setSample("※例如：112、344，若是开出豹子则不算中奖。 ");
        playBean11.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.z3zx3));


        PlayTypeBean.PlayBean playBean12 = new PlayTypeBean.PlayBean();
        playBean12.setPlayTypeExplain("会员可以挑选5~10个号码，当开奖结果[百位、十位、个位]中有且只有两个号码重复，则视为中奖。挑选不同个数的号码有其相对应的赔率。如果是选择(1、2、3、4、5)，则只要开奖结果[百位、十位、个位]中，有出现1、2、3、4、5中的任何两个号码，且其中有一个号码重复则中奖。 ");
        playBean12.setSample("※例如：112、344，若是开出豹子则不算中奖。");
        playBean12.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.h3zx3));


        playBeanList.add(playBean1);
        playBeanList.add(playBean11);
        playBeanList.add(playBean12);

        return playBeanList;
    }


    //初始化 组选3 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_ZX3_0_9_Data(String kind, String childType) {
        String[] arrays;
        arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.ZXS));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_9_85));
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  组选6的数据源
    public static List<PlayTypeBean.PlayBean> initZX6Data() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("会员可以挑选4~8个号码，当开奖结果[万位、千位、百位]都出现在所下注的号码中且没有任何号码重复，则视为中奖。挑选不同个数的号码有其相对应的赔率，中奖赔率以所选号码中的最小赔率计算派彩。  ");
        playBean1.setSample("如果是选择(1、2、3、4)，则开奖结果[万位、千位、百位]为123、124、134、234都中奖，其他都是不中奖。例如：112、133、145、444等都是不中奖。");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.q3zx6));

        PlayTypeBean.PlayBean playBean11 = new PlayTypeBean.PlayBean();
        playBean11.setPlayTypeExplain("会员可以挑选4~8个号码，当开奖结果[千位、百位、十位]都出现在所下注的号码中且没有任何号码重复，则视为中奖。挑选不同个数的号码有其相对应的赔率，中奖赔率以所选号码中的最小赔率计算派彩。  ");
        playBean11.setSample("如果是选择(1、2、3、4)，则开奖结果[千位、百位、十位]为123、124、134、234都中奖，其他都是不中奖。例如：112、133、145、444等都是不中奖。");
        playBean11.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.z3zx6));


        PlayTypeBean.PlayBean playBean12 = new PlayTypeBean.PlayBean();
        playBean12.setPlayTypeExplain("会员可以挑选4~8个号码，当开奖结果[百位、十位、个位]都出现在所下注的号码中且没有任何号码重复，则视为中奖。挑选不同个数的号码有其相对应的赔率，中奖赔率以所选号码中的最小赔率计算派彩。");
        playBean12.setSample("如果是选择(1、2、3、4)，则开奖结果[百位、十位、个位]为123、124、134、234都中奖，其他都是不中奖。例如：112、133、145、444等都是不中奖。");
        playBean12.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.h3zx6));


        playBeanList.add(playBean1);
        playBeanList.add(playBean11);
        playBeanList.add(playBean12);

        return playBeanList;
    }


    //初始化 组选6 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_ZX6_0_9_Data(String kind, String childType) {
        String[] arrays;
        arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.ZXL));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_9_85));
            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  跨度的数据源
    public static List<PlayTypeBean.PlayBean> initKDData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("以开奖结果[万位、千位、百位]的最大差距（跨度），作为中奖依据。会员可以选择0~9的任一跨度。");
        playBean1.setSample("开奖结果为3、4、8、7、6。中奖的跨度为5。（最大号码8减最小号码3=5）。");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.q3kd));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("以开奖结果[千位、百位、十位]的最大差距（跨度），作为中奖依据。会员可以选择0~9的任一跨度。 ");
        playBean2.setSample("开奖结果为3、4、8、7、6。中奖的跨度为4。（最大号码8减最小号码4=4）");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.z3kd));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("以开奖结果[百位、十位、个位]的最大差距（跨度），作为中奖依据。会员可以选择0~9的任一跨度。 ");
        playBean3.setSample("开奖结果为3、4、8、7、6。中奖的跨度为2。（最大号码8减最小号码6=2）。");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.h3kd));


        playBeanList.add(playBean1);
        playBeanList.add(playBean2);
        playBeanList.add(playBean3);
        return playBeanList;
    }

    //初始化 跨度 0-9 的布局 的数据源
    public static List<PlayDetailBean> init_KD_0_9_Data(String kind, String childType) {
        String[] arrays;
        arrays = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.KD));
            playDetailBean.setKind(kind);
            playDetailBean.setChildType(childType);
            playDetailBean.setNum(arrays[i]);
            if (i == 0) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_71));
            } else if (i == 1) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_14_8));
            } else if (i == 2) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_8_1));
            } else if (i == 3) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_6_2));
            } else if (i == 4) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_5_4));
            } else if (i == 5) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_5_2));
            } else if (i == 6) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_5_4));
            } else if (i == 7) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_6_2));
            } else if (i == 8) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_8_1));
            } else if (i == 9) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_14_4));
            }

            list.add(playDetailBean);
        }
        return list;
    }


    //初始化  龙虎的数据源
    public static List<PlayTypeBean.PlayBean> initLHData() {
        List<PlayTypeBean.PlayBean> playBeanList = new ArrayList<>();

        PlayTypeBean.PlayBean playBean1 = new PlayTypeBean.PlayBean();
        playBean1.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean1.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！ ");
        playBean1.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wq));

        PlayTypeBean.PlayBean playBean11 = new PlayTypeBean.PlayBean();
        playBean11.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean11.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean11.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wb));


        PlayTypeBean.PlayBean playBean12 = new PlayTypeBean.PlayBean();
        playBean12.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean12.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean12.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.ws));


        PlayTypeBean.PlayBean playBean13 = new PlayTypeBean.PlayBean();
        playBean13.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean13.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean13.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.wg));


        PlayTypeBean.PlayBean playBean2 = new PlayTypeBean.PlayBean();
        playBean2.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean2.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean2.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qb));

        PlayTypeBean.PlayBean playBean21 = new PlayTypeBean.PlayBean();
        playBean21.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean21.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean21.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qs));


        PlayTypeBean.PlayBean playBean22 = new PlayTypeBean.PlayBean();
        playBean22.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean22.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean22.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.qg));


        PlayTypeBean.PlayBean playBean3 = new PlayTypeBean.PlayBean();
        playBean3.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean3.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean3.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bs));

        PlayTypeBean.PlayBean playBean31 = new PlayTypeBean.PlayBean();
        playBean31.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean31.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean31.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.bg));

        PlayTypeBean.PlayBean playBean4 = new PlayTypeBean.PlayBean();
        playBean4.setPlayTypeExplain("龙虎是以开奖结果的五个数字作为基准，取任意位置（万、千、百、十、个）的数字进行组合大小比对的一种玩法； 当投注龙/虎时，开奖结果为和局，那么押注龙/虎视为不中奖； 当投注“和”时，开奖结果为龙/虎，投注“和”视为不中奖；");
        playBean4.setSample("开奖结果为：2,1,3,5,2 万为龙、千为龙虎时：结果 龙(2）大于虎（1），即为开龙；如万为龙，个为虎时，结果一样大，即为开和局！");
        playBean4.setPlayTypeName(BoxApplication.getContext().getResources().getString(R.string.sg));


        playBeanList.add(playBean1);
        playBeanList.add(playBean11);
        playBeanList.add(playBean12);
        playBeanList.add(playBean13);
        playBeanList.add(playBean2);
        playBeanList.add(playBean21);
        playBeanList.add(playBean22);
        playBeanList.add(playBean3);
        playBeanList.add(playBean31);
        playBeanList.add(playBean4);
        return playBeanList;
    }

    //初始化 龙虎 龙和虎 的布局 的数据源
    public static List<PlayDetailBean> init_LH_Data(String kind) {
        String[] arrays;
        arrays = new String[]{"龙", "和", "虎"};
        List<PlayDetailBean> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            PlayDetailBean playDetailBean = new PlayDetailBean();
            playDetailBean.setType(BoxApplication.getContext().getResources().getString(R.string.LH));
            playDetailBean.setNum(arrays[i]);
            playDetailBean.setKind(kind);
            if (kind.equals(arrays[0])) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_1_98));
            } else if (kind.equals(arrays[1])) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_2_07));
            } else if (kind.equals(arrays[2])) {
                playDetailBean.setOdd(BoxApplication.getContext().getResources().getString(R.string.bv_1_98));
            }

            list.add(playDetailBean);
        }
        return list;
    }


}
