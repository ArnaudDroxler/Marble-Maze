package he_arc.marble_mazze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Arnaud on 25.10.2016.
 */

public class MazzeView  extends SurfaceView implements SurfaceHolder.Callback {

    private Ball ball = null;
    private List<Block> blocks = null;

    SurfaceHolder mSurfaceHolder;
    DrawingThread mThread;

    Paint mPaint;
    private List<Block> Blocks = null;



    public MazzeView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new DrawingThread();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Ball getBoule() {
        return ball;
    }


    public void draw(Canvas pCanvas) {
        // Dessiner le fond de l'�cran en premier
        pCanvas.drawColor(Color.CYAN);
        if(blocks != null) {
            // Dessiner tous les blocs du labyrinthe
            for(Block b : blocks) {
                switch(b.getType()) {
                    case START:
                        mPaint.setColor(Color.BLUE);
                        break;
                    case END:
                        mPaint.setColor(Color.GREEN);
                        break;
                    case WALL:
                        mPaint.setColor(Color.BLACK);
                        break;
                    case HOLE:
                        mPaint.setColor(Color.RED);
                        break;
                }
                pCanvas.drawRect(b.getRectangle().left, b.getRectangle().top,b.getRectangle().right,b.getRectangle().bottom,mPaint);

                //pCanvas.drawRect(b.getRectangle(), mPaint);
            }
        }

        // Dessiner la boule
        if(ball != null) {
            mPaint.setColor(ball.getColor());
            pCanvas.drawCircle(ball.getX(), ball.getY(), ball.RAYON, mPaint);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.keepDrawing = true;
        mThread.start();
        // Quand on cr�e la boule, on lui indique les coordonn�es de l'�cran
        if(ball != null ) {
            this.ball.setHeight(getHeight());
            this.ball.setWidth(getWidth());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThread.keepDrawing = false;
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }

    private class DrawingThread extends Thread {
        boolean keepDrawing = true;

        @Override
        public void run() {
            Canvas canvas;
            while (keepDrawing) {
                canvas = null;

                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }

                // Pour dessiner � 50 fps
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {}
            }
        }
    }
}
