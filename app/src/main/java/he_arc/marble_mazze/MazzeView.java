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


    private  SurfaceHolder mSurfaceHolder;
    private DrawingThread mThread;

    private Paint mPaint;
    private List<Block> Blocks = null;



    public MazzeView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        //On declare un Thread pour afficher et dessine le jeux
        mThread = new DrawingThread();

        //On declare le pinceau pour dessiner
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void draw(Canvas pCanvas) {
        // Dessiner le fond de l'ecran en premier
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
                pCanvas.drawRect(b.getRectangle(), mPaint);
            }
        }
        // Dessiner la boule
        if(ball != null) {
            mPaint.setColor(ball.getColor());
            pCanvas.drawCircle(ball.getX(), ball.getY(), ball.RAYON, mPaint);
        }
        //Dessiner les points de vie
        mPaint.setColor(Color.RED);
        for (int i = 0; i<ball.vie; i++){
            pCanvas.drawCircle(50,getHeight()-50-i*80,40,mPaint);
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.keepDrawing = true;
        mThread.start();
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
            ball.reset();
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

                try {
                    //60fps
                    Thread.sleep(20);
                } catch (InterruptedException e) {}
            }
        }
    }
}
