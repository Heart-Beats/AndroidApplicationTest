package com.example.zhanglei.myapplication.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter的包装类，用于添加 RecyclerView 的头尾布局
 *
 * @author zhanglei
 */

public abstract class BaseNormalAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum ITEM_TYPE {
        HEADER, FOOTER, NORMAL
    }

    private final RecyclerView.Adapter mAdapter;
    private final View mHeaderView;
    private final View mFooterView;

    protected BaseNormalAdapterWrapper(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        this.mHeaderView = getHeaderView();
        this.mFooterView = getFooterView();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.HEADER.ordinal();
        } else if (position == mAdapter.getItemCount() + 1) {
            return ITEM_TYPE.FOOTER.ordinal();
        } else {
            return ITEM_TYPE.NORMAL.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (0 < position && position < mAdapter.getItemCount() + 1) {
            mAdapter.onBindViewHolder(holder, position - 1);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.HEADER.ordinal()) {
            return new RecyclerView.ViewHolder(mHeaderView) {
            };
        } else if (viewType == ITEM_TYPE.FOOTER.ordinal()) {
            return new RecyclerView.ViewHolder(mFooterView) {
            };
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    /**
     * 给包装的 adapter 添加头布局
     *
     * @return 添加的头部view
     */
    protected abstract View getHeaderView();

    /**
     * 给包装的 adapter 添加尾布局
     *
     * @return 添加的尾部view
     */
    protected abstract View getFooterView();
}
