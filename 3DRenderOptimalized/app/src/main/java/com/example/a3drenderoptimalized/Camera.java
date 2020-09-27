package com.example.a3drenderoptimalized;

import android.util.Log;

public class Camera {
    Functions f = new Functions();
    Vec vUp = new Vec();
    Vec vTarget = new Vec();
    Vec vCamera = new Vec();
    Vec vLookDir = new Vec();
    Vec vStraight = new Vec();

    float ratio;
    float fVovRad;

    public float turnY = 0f, turnX = 0; //(float) -(Math.PI/2- 0.05)
    public float tx = 0, ty = 0;
    private static float screenX, screenY;

    //turn and speed coefficient
    public float turnKX, turnKY, turnSpeed = 2;
    private float k = 1;

    Matrix matView = new Matrix();
    Matrix matProj;
    Matrix matCameraRot;

    Camera(float x1, float y1, float z1, float x2, float y2, float z2, float rad, float ratio, float fNear, float fFar, float screenX, float screenY) {
        this.ratio = ratio;
        this.fVovRad = rad;
        vUp.setVec(x1,y1,z1);
        vTarget.setVec(x2,y2,z2);
        matProj = f.makeProjection(rad, ratio, fNear, fFar);

        turnKX = (float) -(rad / screenX * turnSpeed);
        turnKY = (float) -(rad*ratio / screenY * turnSpeed);
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public void move() {
        Matrix matCameraRotX = f.makeRotX(turnX + tx);
        Matrix matCameraRotY = f.makeRotY(turnY + ty);
        matCameraRot = f.multiplyMatrixMatrix(matCameraRotX, matCameraRotY);

        vLookDir = f.multiplyMatrixVec(matCameraRot, vTarget);
        vStraight = f.multiplyMatrixVec(matCameraRotY, vTarget);
        Matrix matCamera = f.pointAtMatrix(vCamera, new Vec(vLookDir), vUp);

        matView = f.quickInverseMatrix(matCamera);
        matCameraRot = f.quickInverseMatrix(matCameraRot);
    }

    public void turnCamera(float tx, float ty) {
        tx *= turnKX; ty *= turnKY;
        this.ty = tx;

        if (Math.abs(turnX + ty) < Math.PI/2-0.05) {
            this.tx = ty;
        } else {
            this.tx = 0;
            turnX = (float) (Math.PI/2 -0.05) * (turnX/Math.abs(turnX)); // * (turnX/Math.abs(turnX))
        }
    }

    public void setCamera() {
        turnY += ty;
        turnX += tx;
        tx = 0;
        ty = 0;
    }
}