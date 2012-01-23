package com.spicysoft.sample.dribble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

/** �Q�[���v���C��� */
final class PlayScene implements Scene
{
  /** �d�͉����x�F�P��(�`��P�� PER �b�̓��) */
  private static final float GRAVITY = 2000;
  /** �ő�p���`�͂ɂ��d�͂�15�{�̉����x��^���� */
  private static final float PUNCH_ACCELERATION = GRAVITY * 15;
  /** �ǂꂾ���̎��ԃp���`�͂��{�[���ɗ^�����邩? */
  private static final int PUNCH_MSECOND = 50;
  /** �{�[�������E�ǂɂ����������̑��x�̐��������̌����W�� */
  private static final float DECAY_VERTICAL = 0.7F;
  /** �{�[�������E�ǂɂ����������̑��x�̐��������̌����W�� */
  private static final float DECAY_HORIZONTAL = 0.9F;
  /** �{�[���̕\�����a */
  private static final float BALL_RADIUS = 32;
  /** �{�[���̃A�j���[�V�����̃p�^�[����(180�x��) */
  private static final int BALL_ANIMATION = 8;
  /** �{�[���̃A�j���[�V�����摜���̊e�p�^�[���̈ʒu */
  private static final Rect[] ballSrc = new Rect[BALL_ANIMATION];
  /** �{�[����\�������̋�` */
  private static final RectF ballDst = new RectF();
  /** �{�[���摜 */
  private static Bitmap ball;
  /** �{�[���摜��\������Ƃ��̕`���� */
  private static Paint ballPaint;
  /** �w�i�̕`���� */
  private static Paint backgroundPaint;
  /** �{�[���̈ʒu(X���W) */
  private static float ballX;
  /** �{�[���̈ʒu(X���W) */
  private static float ballY;
  /** �{�[���̑��x(�P��:�`��P�� PER �b) */
  protected static float ballVX;
  /** �{�[���̑��x(�P��:�`��P�� PER �b) */
  protected static float ballVY;
  /** �X�R�A(�h���u���� */
  private int dribbled;
  /** �v���C�c���� */
  private final static long TIME = 15;
  /** �Q�[���J�n���� */
  private long gameStarted;
  /** �c���� */
  private long remainedTime;
  /** �X�R�A */
  private Paint paintScore;
  /** �c�莞�� */
  private Paint paintRemainTime;
  /** �^�b�`�����u�Ԃ�X���W */
  private float downedX = 0;
  /** �^�b�`�����u�Ԃ�Y���W */
  private float downedY = 0;

  /** ������ */
  public void init(SurfaceView view) {
    ball = BitmapFactory.decodeResource(view.getResources(), R.drawable.ball);
    final int ballSrcHeight = ball.getHeight();
    for (int n = 0; n < BALL_ANIMATION; n++) {
      ballSrc[n] = new Rect(ballSrcHeight * n, 0, ballSrcHeight * (n + 1),
          ballSrcHeight);
    }

    backgroundPaint = new Paint();
    backgroundPaint.setColor(Color.BLACK);

    ballPaint = new Paint();

    paintScore = new Paint();
    paintScore.setTextAlign(Paint.Align.LEFT);
    paintScore.setAntiAlias(true);
    paintScore.setColor(Color.WHITE);
    paintScore.setTextSize(32);

    paintRemainTime = new Paint();
    paintRemainTime.setTextAlign(Paint.Align.RIGHT);
    paintRemainTime.setAntiAlias(true);
    paintRemainTime.setColor(Color.WHITE);
    paintRemainTime.setTextSize(32);
  }

  /** ��ʕ\���J�n */
  public void start(SurfaceView view) {
    ballX = view.getWidth() / 2;
    ballY = 0;
    ballVX = 0;
    ballVY = 0;
    dribbled = 0;
    gameStarted = System.currentTimeMillis();
    Sounds.playBgmPlay();
  }

  /**
   * �e�t���[�����̓��̓C�x���g��I�u�W�F�N�g�̈ړ��┻�菈��
   */
  public void process(SurfaceView view) {
    remainedTime = TIME - (System.currentTimeMillis() - gameStarted) / 1000;
    if (remainedTime < 0) {
      PlayLog.logScore(dribbled);
      Main.startScene(Main.OVER);
      return;
    }
    MotionEvent event;
    while ((event = Main.event()) != null) {
      switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downedX = event.getX();
        downedY = event.getY();
        break;
      case MotionEvent.ACTION_UP:
        final float movedX = event.getX() - downedX;
        final float movedY = event.getY() - downedY;
        final float moved = Math.abs(movedX) + Math.abs(movedY);
        final float actionedX;
        final float actionedY;
        if (moved < 32) {
          Log.d("Dribble", "PUSH");
          actionedX = 0;
          actionedY = PUNCH_ACCELERATION;
        } else {
          Log.d("Dribble", "MOVE");
          actionedX = PUNCH_ACCELERATION * movedX / view.getWidth();
          actionedY = PUNCH_ACCELERATION * movedY / view.getHeight();
        }
        float A = PUNCH_MSECOND / Main.MILLIS_PER_FRAME;
        ballVX += actionedX * A / Main.FRAMES_PER_SECOND;
        ballVY += actionedY * A / Main.FRAMES_PER_SECOND;
        break;
      }
    }

    final float floor = view.getHeight() - BALL_RADIUS;
    ballY = ballY + ballVY / Main.FRAMES_PER_SECOND;

    if (floor <= ballY) {
      // ���ɓ�������
      float bounce = (ballY - floor) * DECAY_VERTICAL * DECAY_VERTICAL;
      if (bounce < 1 && ballVY < 1) {
        // ���̏�Œ�~���Ă���
        ballY = floor;
        ballVY = 0;

      } else {
        // ���̏�Ńo�E���h
        ballY = floor - bounce;
        ballVY = -ballVY * DECAY_VERTICAL;
        dribbled++;
        Sounds.playBounce1();
        Log.v("Dribble","Bound on the floor: ballVY=" + ballVY + ",bounce=" + bounce);
  
        final float a = BALL_RADIUS / 2;
        final float b = GRAVITY / Main.FRAMES_PER_SECOND * 3;
        final float c = Math.abs(ballVY);
        Log.v("Dribble",(bounce <= a && c < b) + ":" + a + "," + b + "," + ballVY);
        if (bounce <= a && c < b) {
          // ����ȏ�o�E���h�������̏�Œ�~������
          ballY = floor;
          ballVY = 0;
          Log.v("Dribble","Stop on the floor.");
        }
      }
      ballVX = ballVX * DECAY_HORIZONTAL;

    } else {
      // ���R����
      ballVY += GRAVITY / Main.FRAMES_PER_SECOND;
      //Log.v("Dribble","Falling ballVY=" + ballVY);
    }

    final float rightWall = view.getWidth() - BALL_RADIUS;
    ballX = ballX + ballVX / Main.FRAMES_PER_SECOND;
    if (ballX <= BALL_RADIUS) {
      // ���̕ǂɏՓ�
      
      ballX = -(BALL_RADIUS - ballX) * DECAY_VERTICAL * DECAY_VERTICAL
          + BALL_RADIUS;
      ballVX = -ballVX * DECAY_VERTICAL;
      ballVY = ballVY * DECAY_HORIZONTAL;
      Sounds.playBounce2();

    } else if (rightWall <= ballX) {
      // �E�̕ǂɏՓ�
      ballX = rightWall - (ballX - rightWall) * DECAY_VERTICAL * DECAY_VERTICAL;
      ballVX = -ballVX * DECAY_VERTICAL;
      ballVY = ballVY * DECAY_HORIZONTAL;
      Sounds.playBounce2();

    }
  }

  /**
   * �e�t���[���ł̕`��
   */
  public void draw(Canvas canvas) {
    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
        backgroundPaint);
    ballDst.set(ballX - BALL_RADIUS, ballY - BALL_RADIUS, ballX + BALL_RADIUS,
        ballY + BALL_RADIUS);

    canvas.drawBitmap(ball, ballSrc[0], ballDst, ballPaint);

    canvas.drawText(dribbled + " ��", 0, 48, paintScore);
    canvas.drawText("���� " + remainedTime + "�b", canvas.getWidth(), 48,
        paintRemainTime);
  }

}
