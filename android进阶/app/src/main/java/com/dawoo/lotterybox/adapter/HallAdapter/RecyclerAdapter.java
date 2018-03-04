package com.dawoo.lotterybox.adapter.HallAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;

import java.util.List;

import static com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean.CHILD_ITEM;
import static com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean.PARENT_ITEM;


/**
 * 适配器
 */

public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    int showPosition = -1; //当前展开的item角标
    int showPositionParent = -1;//当前展开的item的父item角标

    private Context context;
    private List<TypeAndLotteryBean> dataBeanList;
    private LayoutInflater mInflater;
    private OnScrollListener mOnScrollListener;

    public RecyclerAdapter(Context context, List<TypeAndLotteryBean> mDataBean) {
        this.context = context;
        this.dataBeanList = mDataBean;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case PARENT_ITEM:
                view = mInflater.inflate(R.layout.recycleview_item_parent_, parent, false);
                return new ParentViewHolder(context, view);
            case CHILD_ITEM:
                view = mInflater.inflate(R.layout.recycleview_item_child_, parent, false);
                return new ChildViewHolder(context, view);
            default:
                view = mInflater.inflate(R.layout.recycleview_item_parent_, parent, false);
                return new ParentViewHolder(context, view);
        }
    }

    /**
     * 根据不同的类型绑定View
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case PARENT_ITEM:
                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                parentViewHolder.bindView(dataBeanList.get(position), position, itemClickListener);
                break;
            case CHILD_ITEM:
                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                childViewHolder.bindView(dataBeanList.get(position), position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataBeanList.get(position).getType();
    }

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onExpandChildren(TypeAndLotteryBean bean , int position) {
            int nowPosition = position;
            if (showPosition != -1){
                if (position > showPosition){
                    nowPosition = position - 1;
                }
                dataBeanList.get(showPositionParent).setExpand(false);
                onHideChildren();
            }


            TypeAndLotteryBean children = getChildDataBean(bean);
            if (children == null) {
                return;
            }

            if (nowPosition % 2 != 0){
                add(children, nowPosition + 1);
                showPosition = nowPosition + 1;
            }else {
                add(children, nowPosition + 2);
                showPosition = nowPosition + 2;
            }
            showPositionParent = position;

//            if (dataBeanList.size() - nowPosition < 4 && mOnScrollListener != null) { //如果点击的item为最后一个
//                mOnScrollListener.scrollTo(position + 1);//向下滚动，使子布局能够完全展示
//            }
        }

        @Override
        public void onHideChildren() {
            remove(showPosition);//删除
            showPosition = -1;
            showPositionParent = -1;
//            if (mOnScrollListener != null) {
//                mOnScrollListener.scrollTo(showPosition-1);
//            }
        }
    };

    /**
     * 在父布局下方插入一条数据
     * @param bean
     * @param position
     */
    public void add(TypeAndLotteryBean bean, int position) {
        dataBeanList.add(position, bean);
        notifyItemInserted(position);
    }

    /**
     *移除子布局数据
     * @param position
     */
    protected void remove(int position) {
        if (position!=-1){
            dataBeanList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 封装子布局数据对象并返回
     * 此处只是重新封装一个DataBean对象，为了标注Type为子布局数据，进而展开，展示数据
     * @param bean
     * @return
     */
    private TypeAndLotteryBean getChildDataBean(TypeAndLotteryBean bean){
        TypeAndLotteryBean child = new TypeAndLotteryBean();
        child.setType(CHILD_ITEM);
        child.setLotteries(bean.getLotteries());
        child.setTypeName(bean.getTypeName());
        child.setTypeCode(bean.getTypeCode());
        return child;
    }

    /**
     * 滚动监听接口
     */
    public interface OnScrollListener{
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.mOnScrollListener = onScrollListener;
    }
}
