package com.kuo.kuoresume.thread;

import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kuo.kuoresume.view.AbsResumeView;

/**
 * Created by Kuo on 2016/4/18.
 */
public class ResumeThread extends Thread {

    private AbsResumeView absResumeView;

    private SurfaceHolder surfaceHolder;

    private boolean loop = true;

    public ResumeThread(AbsResumeView absResumeView, SurfaceHolder surfaceHolder) {
        this.absResumeView =  absResumeView;
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        super.run();

        Canvas canvas = null;

        while (loop) {

            try {
                Surface surface = surfaceHolder.getSurface();
            /* Check availability of surface */
                if (surface != null && surface.isValid() && surfaceHolder != null) {

                    canvas = surfaceHolder.lockCanvas();

                    synchronized (surfaceHolder) {
                        if (canvas != null && absResumeView.isLoaded) {
                            absResumeView.onResumeDraw(canvas);
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Thread Resume Run", "[Drawing Thread]", e);
            } finally {
                /**
                 * Do this in a finally so that if an exception is thrown during the above,
                 * we don't leave the Surface in an inconsistent state
                 */
                if (canvas != null && surfaceHolder != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
