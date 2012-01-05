package com.spicysoft.sample.dribble;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

/**
 * �A�v���P�[�V�����̋N��
 */
public final class DribbleActivity extends Activity
{
  @Override public void onCreate(Bundle savedInstanceState) {
    Log.d("Dribble", "onCreate");
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(new DribbleView(this));
  }

  @Override protected void onPause() {
    Log.d("Dribble", "onPause");
    super.onPause();
  }

  @Override protected void onResume() {
    Log.d("Dribble", "onResume");
    super.onResume();
  }

  @Override protected void onDestroy() {
    Log.d("Dribble", "onDestroy");
    super.onDestroy();
  }
}

/**
 * �Q�[�����I�u�W�F�N�g�̏����A�^�b�`�̏����A��ʂ̕\�����̃Q�[���̊�{�������s���N���X
 */
class DribbleView extends SurfaceView implements SurfaceHolder.Callback
{
  /** �R���X�g���N�^ */
  public DribbleView(final Context context) {
    super(context);
    setFocusable(true);
    getHolder().addCallback(this);
  }

  /** ���ۂ�SurfaceView�������������ꂽ�ۂ�API����R�[���o�b�N����� */
  @Override public void surfaceCreated(SurfaceHolder holder) {
    Log.d("Dribble", "surfaceCreated");
    Main.createThread(this);
  }

  /** �\�����(��ʃT�C�Y���]���)���ύX���ꂽ�ۂ�API����R�[���o�b�N����� */
  @Override public void surfaceChanged(SurfaceHolder holder, int format,
      int width, int height) {
    Log.d("Dribble", "surfaceChanged");
  }

  /** SurfaceView���j�����ꂽ�ۂɁAAPI����R�[���o�b�N����� */
  @Override public void surfaceDestroyed(SurfaceHolder holder) {
    Log.d("Dribble", "surfaceDestroyed");
  }

  /**
   * �^�b�`���ꂽ�ۂ�API����R�[���o�b�N����� �Q�[�����[�v���ŏ����ł���悤�ɁA ���[�U���^�b�`�������̓t���b�N�̃A�N�V�������������ƁA
   * ���̍ۂɂǂꂮ�炢�{�[���ɉe����^����A�N�V���������������L�^����
   */
  @Override public boolean onTouchEvent(final MotionEvent event) {
    Main.queue(event);
    return true;
  }

}
