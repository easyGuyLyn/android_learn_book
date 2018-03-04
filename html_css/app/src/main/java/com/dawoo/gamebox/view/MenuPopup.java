package com.dawoo.gamebox.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dawoo.gamebox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票站-投注页菜单
 * Created by fei on 2017/9/20.
 */
public class MenuPopup implements BasePopupWindow.ViewInterface {
    private Context context;
    private BasePopupWindow popupWindow;
    private PopupItemClick popupItemClick;
    MenuAdapter adapter ;

    public MenuPopup(Context context) {
        this.context = context;
    }

    public void show(View view,String firstData) {
//        initPopupWindow();

        if (popupWindow != null && popupWindow.isShowing()) return;

        View popupView = LayoutInflater.from(context).inflate(R.layout.lottery_menu, null);
        ListView lvMenu = popupView.findViewById(R.id.lvMenu);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();
                popupItemClick.onPopupItemClick(i);

            }
        });
        adapter = new MenuAdapter();
        adapter.setFirstData(firstData);
        lvMenu.setAdapter(adapter);

        popupWindow = new BasePopupWindow.Builder(context).setView(popupView)
                .setWidthAndHeight(popupView.getWidth(), popupView.getHeight())
                .setOutsideTouchable(true)
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAsDropDown(view);
    }

    public void setPopupItemClick(PopupItemClick popupItemClick){
        this.popupItemClick = popupItemClick;
    }
    public void setListFirstData(String data){
        adapter.setFirstData(data);
    }

    public interface  PopupItemClick{
        void onPopupItemClick(int index);
    }

    @Override
    public void getChildView(View view, int layoutResId) {

    }

    private class MenuAdapter extends BaseAdapter {
        private List<String> data = initData();
        private LayoutInflater inflater;
        public void setFirstData(String  string){
            data.set(0,string);
        }

        private MenuAdapter() {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.popup_item, null);
                holder = new ViewHolder();

                holder.tvItem = view.findViewById(R.id.text1);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tvItem.setText(data.get(i));
            return view;
        }

        private List<String> initData() {
            List<String> data = new ArrayList<>();
            data.add("0.00");
            data.add("投注记录");
            data.add("开奖记录");
            data.add("会员中心");
            data.add("消息中心");
            data.add("账户充值");
            return data;
        }
    }
    private class ViewHolder {
        TextView tvItem;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
