package he_arc.marble_mazze;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Created by Arnaud on 25.10.2016.
 */

public class Ball {

    private float posX = 0.0f;
    private float posY = 0.0f;

    private float speedX = 0.0f;
    private float speedY = 0.0f;

    private int mHeight = -1;
    private int mWidth = -1;

    private int color = Color.YELLOW;

    public static final int RAYON = 20;
    private static final float MAX_SPEED = 20.0f;
    private static final float COMPENSATEUR = 15.0f;
    private static final float REBOND = 1.75f;

    private RectF colliderBox = null;

    private RectF initialPos = null;

    public Ball() {
        colliderBox = new RectF();
    }

    public void setPosX(float pPosX) {
        posX = pPosX;

        // Si la boule sort du cadre, on rebondit
        if(posX < RAYON) {
            posX = RAYON;
            // Rebondir, c'est changer la direction de la balle
            speedY = -speedY / REBOND;
        } else if(posX > mWidth - RAYON) {
            posX = mWidth - RAYON;
            speedY = -speedY / REBOND;
        }
    }

    public void setPosY(float pPosY) {
        posY = pPosY;
        if(posY < RAYON) {
            posY = RAYON;
            speedX = -speedX / REBOND;
        } else if(posY > mHeight - RAYON) {
            posY = mHeight - RAYON;
            speedX = -speedX / REBOND;
        }
    }

    public void setInitialRectangle(RectF initialRectangle) {
        this.initialPos = initialRectangle;
        this.posX = initialPos.left + RAYON;
        this.posY = initialPos.top + RAYON;
    }

    public RectF putXAndY(float pX, float pY) {
        speedX += (pX-0.28) / COMPENSATEUR;
        if(speedX > MAX_SPEED)
            speedX = MAX_SPEED;
        if(speedX < -MAX_SPEED)
            speedX = -MAX_SPEED;

        speedY += (pY-0.18) / COMPENSATEUR;
        if(speedY > MAX_SPEED)
            speedY = MAX_SPEED;
        if(speedY < -MAX_SPEED)
            speedY = -MAX_SPEED;

        setPosX(posX + speedY);
        setPosY(posY + speedX);

        // Met � jour les coordonn�es du rectangle de collision
        colliderBox.set(posX - RAYON, posY - RAYON, posX + RAYON, posY + RAYON);

        return colliderBox;
    }

    public void reset() {
        speedX = 0;
        speedY = 0;
        this.posX = initialPos.left + RAYON;
        this.posY = initialPos.top + RAYON;
    }

    public int getColor(){
        return this.color;
    }

    public float getX() {
        return posX;
    }

    public float getY() {
        return posY;
    }

    public void changeXSpeed() {
        speedX = -speedX;
    }

    public void changeYSpeed() {
        speedY = -speedY;
    }

    public void setHeight(int pHeight) {
        this.mHeight = pHeight;
    }

    public void setWidth(int pWidth) {
        this.mWidth = pWidth;
    }

}
