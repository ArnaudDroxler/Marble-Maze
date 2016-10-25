package he_arc.marble_mazze;

import android.graphics.RectF;

/**
 * Created by Arnaud on 25.10.2016.
 */

public class Block {

    enum  Type {WALL,HOLE,START,END};

    private float SIZE = Ball.RAYON * 2; //TO define

    private Type mType = null;
    private RectF mRectangle = null;

    public Block(Type pType, int pX, int pY) {
        this.mType = pType;
        this.mRectangle = new RectF(pX * SIZE, pY * SIZE, (pX + 1) * SIZE, (pY + 1) * SIZE);
    }

    public Type getType() {
        return mType;
    }

    public RectF getRectangle() {
        return mRectangle;
    }




}
