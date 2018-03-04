package com.dawoo.lotterybox.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.dawoo.lotterybox.BoxApplication;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by archar on 18-2-11.
 */

public class AssetsReader {

    public static final String SSC_JSON_FILE = "cq_ssc_play_type.json";

    /**
     * 读 .csv  文件
     *
     * @param context
     */
    public static void readCsv(Context context) {

        //没有时时彩的 sp缓存，就重新读取 csv
        if (TextUtils.isEmpty(SharePreferenceUtil.getPlayTypeListJson(context))) {
            LinkedList<String> lineSscList = readAssetsCsv(context, "shishicai");
            resolveSscData(lineSscList);
            JsonToPlayBean.getPlayBeansFromJson();//构造玩法界面的数据体
            JsonToPlayExplainBean.getPlayBeansFromJson(context);//构造玩法说明界面的数据体
        }
    }

    /**
     * 填充时时彩数据 所有玩法 , 并永久序列化到sp中， 填充数据只有一次 .
     */
    private static void resolveSscData(LinkedList<String> lineSscList) {
        ArrayList<PlayTypeBean.PlayBean> typeBeans = new ArrayList<>();
        if (lineSscList != null) {
            for (int i = 0; i < lineSscList.size(); i++) {
                if (i != 0) { //第一个不要
                    String allItemContent = lineSscList.get(i);
                    String[] everyContent = allItemContent.split(",");
                    PlayTypeBean.PlayBean playTypeBean = new PlayTypeBean.PlayBean();
//                    playTypeBean.setId(everyContent[0]);
                    playTypeBean.setMainType(everyContent[1]);
                    playTypeBean.setScheme(everyContent[2]);
                    playTypeBean.setPlayTypeName(everyContent[3]);
//                    playTypeBean.setGroupNum(everyContent[4]);
                    playTypeBean.setTotalBet(everyContent[5]);
                    playTypeBean.setPrizeBet(everyContent[6]);
                    playTypeBean.setOdds(everyContent[7]);
                    playTypeBean.setBackAccount(everyContent[8]);
                    playTypeBean.setSingleBet(everyContent[9]);
                    playTypeBean.setSample(everyContent[10]);
                    playTypeBean.setPlayTypeExplain(everyContent[11]);
                    playTypeBean.setSingleExplain(everyContent[12]);
                    playTypeBean.setPrizeLevel(everyContent[13]);
                    playTypeBean.setPlayTypeAb(everyContent[14]);
                    playTypeBean.setBackAccStatus(everyContent[15]);
                    typeBeans.add(playTypeBean);
                }
            }
        }
        if (typeBeans.size() > 0) {
            String json = GsonUtil.GsonString(typeBeans);
            SharePreferenceUtil.savePlayTypeListJson(BoxApplication.getContext(), json);
        }
    }


    /**
     * 读取assets下的csv文件
     */
    private static LinkedList<String> readAssetsCsv(Context context, String fileName) {
        LinkedList<String> lineList = new LinkedList<>();
        try {
            InputStream is = context.getAssets().open(fileName + ".csv");
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String strLine = null;
            while ((strLine = br.readLine()) != null) {
                //   Log.e("lyn_readLine", strLine);
                lineList.add(strLine);
            }
            is.close();
            isr.close();
            br.close();
            return lineList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 得到json文件中的内容
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context,String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
