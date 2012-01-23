package com.spicysoft.sample.dribble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

/** �Q�[���I�[�o�[��� */
final class OverScene implements Scene
{
  /** ����͂�����邽�߁A��ʂ��J���Ă����莞��(2�b)�́A���͑���𖳌��Ƃ��� */
  private final static long WAIT = 2000;
  /** ��莞��(10�b)���ƃ^�C�g����ʂɖ߂� */
  private final static long AUTO = 10000;
  /** �w�i�̕`���� */
  private static Paint backgroundPaint;
  /** �X�R�A�̕`���� */
  private Paint paintScore;
  /** ��ʂ�\���J�n�������� */
  private long start;

  /** ���������� */
  public void init(SurfaceView view) 
  {
    backgroundPaint = new Paint();
    backgroundPaint.setColor(Color.BLACK);

    paintScore = new Paint();
    paintScore.setTextAlign(Paint.Align.CENTER);
    paintScore.setAntiAlias(true);
    paintScore.setColor(Color.WHITE);
    paintScore.setTextSize(48);

  }

  /** ��ʂ̕\���J�n */
  public void start(SurfaceView view)
  {
    start = System.currentTimeMillis();
    Sounds.stopBgm();
  }

  /** 
   * �e�t���[�����̓��̓C�x���g��I�u�W�F�N�g�̈ړ��┻�菈��
   */
  public void process(SurfaceView view)
  {
    final long elapsed = System.currentTimeMillis() - start;
    final MotionEvent e = Main.event();
    if (WAIT < elapsed && e != null && e.getAction() == MotionEvent.ACTION_UP || AUTO <= elapsed) {
      Sounds.playTouch();
      Main.startScene(Main.TITLE);
    }
  }

  /** 
   * �e�t���[�����̕`�揈��
   */
  public void draw(Canvas canvas)
  {
    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

    final int centerX = canvas.getWidth() / 2;
    final int centerY = canvas.getHeight() / 2;
    canvas.drawText("�L�^ " + PlayLog.lastScore() + " ��", centerX , centerY, paintScore);
    if (PlayLog.isLastBest()) {
      canvas.drawText("!! �x�X�g�X�R�A !!", centerX , centerY + paintScore.ascent() * 2, paintScore);
    }
  }
}

