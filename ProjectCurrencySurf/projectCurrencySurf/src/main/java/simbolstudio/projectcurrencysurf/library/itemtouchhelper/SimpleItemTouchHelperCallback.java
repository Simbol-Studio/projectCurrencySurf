package simbolstudio.projectcurrencysurf.library.itemtouchhelper;

/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import simbolstudio.projectcurrencysurf.R;

/**
 * An implementation of {@link ItemTouchHelper.Callback} that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br/>
 * </br/>
 * Expects the <code>RecyclerView.Adapter</code> to listen for {@link
 * ItemTouchHelperAdapter} callbacks and the <code>RecyclerView.ViewHolder</code> to implement
 * {@link ItemTouchHelperViewHolder}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    //    public static final float ALPHA_FULL = 1.0f;
    private Context context;
    private final ItemTouchHelperAdapter mAdapter;
    private Paint paint;
    Drawable icon;
    int iconMargin;

    public SimpleItemTouchHelperCallback(Context context, ItemTouchHelperAdapter adapter) {
        this.context = context;
        this.mAdapter = adapter;
        this.paint = new Paint();
        this.iconMargin = (int) context.getResources().getDimension(R.dimen._16_dp);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the action
        if (i != ItemTouchHelper.START)
            mAdapter.onItemSwipedLeft(viewHolder.getAdapterPosition());
        else
            mAdapter.onItemSwipedRight(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }

        View itemView = viewHolder.itemView;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {


            if (dX > 0) {
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                paint.setColor(Color.parseColor("#FF9800"));
                c.drawRect(background, paint);
                icon = context.getResources().getDrawable(R.drawable.ico_plus);
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidthorHeight = icon.getIntrinsicWidth();

                int xMarkLeft = itemView.getLeft() + (int) Math.round(iconMargin * 3) - intrinsicWidthorHeight;
                int xMarkRight = itemView.getLeft() + (int) Math.round(iconMargin * 3);
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicWidthorHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicWidthorHeight;
                icon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                icon.draw(c);

            } else if (dX < 0) {
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                paint.setColor(Color.parseColor("#E91E63"));
                c.drawRect(background, paint);
                icon = context.getResources().getDrawable(R.drawable.ico_plus);
                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidthorHeight = icon.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - iconMargin - intrinsicWidthorHeight;
                int xMarkRight = itemView.getRight() - iconMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicWidthorHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicWidthorHeight;
                icon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                icon.draw(c);

            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

//        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
