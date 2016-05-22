package com.kuo.kuoresume.object;

import android.graphics.RectF;

/**
 * Created by User on 2016/5/21.
 */
public class RectCollider {

    private ColliderListener colliderListener;

    private boolean isCollid;

    public void start(RectF otherRect, RectF dstRect) {

        if(colliderListener != null) {

            if(otherRect.right > dstRect.left
                    && otherRect.left < dstRect.left
                    && !isCollid) {

                isCollid = true;

                colliderListener.start(dstRect);

            } else if (otherRect.left < dstRect.right
                    && otherRect.right > dstRect.right
                    && !isCollid) {

                isCollid = true;

                colliderListener.start(dstRect);

            } else if(otherRect.left > dstRect.right
                    && isCollid) {

                isCollid = false;

                colliderListener.end(dstRect);

            } else if(otherRect.right < dstRect.left
                    && isCollid) {

                isCollid = false;

                colliderListener.end(dstRect);

            }
        }

    }

    public void setColliderListener(ColliderListener colliderListener) {
        this.colliderListener = colliderListener;
    }

    public interface ColliderListener {

        void start(RectF dstRect);
        void end(RectF dstRect);

    }

}
