package com.gz.jey.realestatemanager.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemClickSupport {
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        /**
         * ON CLICK
         * @param v View
         */
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    /**
     * ITEM CLICK SUPPORT
     * @param recyclerView RecyclerView
     * @param itemID Int
     */
    private ItemClickSupport(RecyclerView recyclerView, int itemID) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(itemID, this);
        RecyclerView.OnChildAttachStateChangeListener mAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (mOnItemClickListener != null) {
                    view.setOnClickListener(mOnClickListener);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        };
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    /**
     * TO ADD CLICK ELEMENT TO ITEM CLICK SUPPORT
     * @param view RecyclerView
     * @param itemID int
     * @return ITEM CLICK SUPPORT
     */
    public static ItemClickSupport addTo(RecyclerView view, int itemID) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(itemID);
        if (support == null) {
            support = new ItemClickSupport(view, itemID);
        }
        return support;
    }

    /**
     * TO SET ON ITEM CLICK LISTENER
     * @param listener OnItemClickListener
     * @return ITEM CLICK SUPPORT
     */
    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * INTERFACE FOR ON ITEM CLICKED
     */
    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }
}