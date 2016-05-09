package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.Image;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLAbout extends ComputeRect{

    private static final int SEA_MIN_SIZE = 12;
    private static final int SEA_MAX_SIZE = 15;

    private ArrayList<Image> plants = new ArrayList<>();

    private Image levelSign, ticketStation, build85, signWood;

    public GLImageText glImageText1, glImageText2;

    public GLText signText, aboutText;

    public GLAbout(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        //PLANT_RANGE_SIZE = (int) (viewComputeListener.getViewCompute().getContentRect().width() / viewComputeListener.getViewCompute().getPlantSize()) + 1;

        PLANT_RANGE_SIZE = 27;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createPlants();
        createImages();

    }

    public void draw(float[] mvpMatrix) {

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        aboutText.draw(mvpMatrix);

        ticketStation.draw(mvpMatrix, 5);

        build85.draw(mvpMatrix, 8);

        glImageText1.draw(mvpMatrix);

        drawPlants(mvpMatrix);

    }

    public void drawPlants(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        int count = 0;
        for(Image image : plants) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                if (count >= SEA_MIN_SIZE && count <= SEA_MAX_SIZE)
                    image.draw(mvpMatrix, 9);
                else
                    image.draw(mvpMatrix, 2);
            }
            count++;
        }
    }

    private void createImages() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        levelSign = new Image(new RectF(0, plantHeight - plantSize * 4, plantSize * 4, plantHeight));

        signText = new GLText("Level 1", 0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4, plantHeight - plantSize * 2, plantSize * 6, plantHeight));

        aboutText = new GLText("about", 0, 0);

        ticketStation = new Image(new RectF(plantSize * 9, plantHeight - plantSize * 2, plantSize * 11, plantHeight));

        build85 = new Image(new RectF(plantSize * 13, plantHeight - plantSize * 4, plantSize * 15, plantHeight));

        glImageText1 = new GLImageText("Live in Kaohsiung City", plantSize * 14, plantHeight / 6);

        computeRect();
    }

    private void createPlants() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

            float left = dstRect.left + plantSize * i;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            plants.add(new Image(new RectF(left, top, right, bottom)));
        }
    }

    public void computeRect() {

        computePlants();
        computeAbout();
        computeLevelSign();
        computeTicketStation();
        computeBuild85();

        GLCharacter glCharacter = viewComputeListener.getGLCharacter();

        RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        if(glCharacter.dstRect.right > dstRect.right) {
            glCharacter.setCharacterStae(GLCharacter.CHARACTER_AIR);
        } else if(glCharacter.dstRect.left > dstRect.right) {
            glCharacter.setCharacterStae(GLCharacter.CHARACTER_DOWN);
        }

    }

    private void computeLevelSign() {

        RectF srcRect = levelSign.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        levelSign.setDstRect(left, top, right, bottom);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + srcRect.height() / 4);
    }

    private void computeAbout() {

        RectF srcRect = signWood.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        signWood.setDstRect(left, top, right, bottom);

        aboutText.setLocation(signWood.getDstRect().centerX() - signText.getWidth() / 2,
                signWood.getDstRect().top + aboutText.getHeight() / 2);

    }

    private void computeTicketStation() {

        RectF srcRect = ticketStation.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        ticketStation.setDstRect(left, top, right, bottom);

    }

    private void computeBuild85() {

        RectF srcRect = build85.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        build85.setDstRect(left, top, right, bottom);

        RectF textSrcRect = glImageText1.getSrcRect();

        glImageText1.setLocation(build85.getDstRect().centerX() - textSrcRect.width() / 2,
                dstRect.top + textSrcRect.top);
    }

    private void computePlants() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        int count = 0;
        for(Image image : plants) {

            float left = dstRect.left + plantSize * count;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            image.setDstRect(left, top, right, bottom);

            count++;
        }
    }

}
