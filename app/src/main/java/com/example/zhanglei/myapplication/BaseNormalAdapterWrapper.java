package com.example.zhanglei.myapplication;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseNormalAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    enum ITEM_TYPE {
        HEADER,
        FOOTER,
        NORMAL
    }

    private RecyclerView.Adapter mAdapter;
    private View mHeaderView;
    private View mFooterView;

    BaseNormalAdapterWrapper(RecyclerView.Adapter adapter) {
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

    abstract View getHeaderView();

    abstract View getFooterView();

/*    private class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }*/
}
