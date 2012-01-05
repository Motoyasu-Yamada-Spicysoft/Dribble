package com.spicysoft.sample.dribble;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * �Q�[�����I�u�W�F�N�g�̏����A�^�b�`�̏����A��ʂ̕\�����̃Q�[���̊�{�������s���N���X
 */
final class Main
{
  /** FPS 1�b������̃t���[�����B�Q�[���̏����P�� */
  public static final int FRAMES_PER_SECOND = 50;
  /** �t���[��������̃~���b�� */
  public static final int MILLIS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
  /** �^�C�g����ʗp�V�[�� */
  public static final Scene TITLE = new TitleScene();
  /** �Q�[���v���C��ʗp�V�[�� */
  public static final Scene PLAY = new PlayScene();
  /** �Q�[���I�[�o�[��ʗp�V�[�� */
  public static final Scene OVER = new OverScene();

  /** �X���b�h */
  private static Thread thread;
  /** �V�[�� */
  private static Scene current;
  /** �C�x���g�L���[ */
  private static final Queue<MotionEvent> events = new LinkedList<MotionEvent>();
  /** ���ڂ̏��������H */
  private static boolean first = true;
  /** �r���[ */
  private static SurfaceView view;

  /**
   * �Q�[�����[�v�X���b�h�𐶐��� �Q�[�����[�v���J�n or �ĊJ
   */
  public static void createThread(final SurfaceView view) {
    Main.view = view;
    if (first) {
      Main.initScenes(view);
      startScene(Main.TITLE);
      first = false;
    }
    thread = new Thread() {
      @Override public void run() {
        Main.runLoop();
      }
    };
    thread.start();
  }

  /**
   * �Q�[�����[�v�X���b�h�𒆒f����
   */
  public static void interruptThread() {
    thread.interrupt();
  }

  /**
   * �Q�[�����[�v���̂P�t���[���̏���
   */
  public static void runLoop() {
    Log.d("Dribble", "Loop in thread will start.");
    for (;;) {
      if (thread.isInterrupted()) {
        Log.d("Dribble", "Thread is interrupted");
        break;
      }
      final long start = System.currentTimeMillis();
      final Scene scene = current;
      scene.process(view);

      // (1) �_�u���o�b�t�@�����O�J�n
      final SurfaceHolder holder = view.getHolder();
      final Canvas canvas = holder.lockCanvas();
      if (canvas != null) {
        try {
          scene.draw(canvas);
        } finally {
          // (3) �_�u���o�b�t�@�����O����
          holder.unlockCanvasAndPost(canvas);
        }
      }

      final long elapsed = System.currentTimeMillis() - start;
      final long towait = MILLIS_PER_FRAME - elapsed;
      if (0 < towait) {
        try {
          Thread.sleep(towait);
        } catch (final InterruptedException e) {
          Log.d("Dribble", "On calling Thread.sleep", e);
          break;
        }
      }
    }

    Log.d("Dribble", "Loop in thread exits");
    thread = null;
  }

  /**
   * �S�ẴV�[��������������
   */
  public static void initScenes(final SurfaceView view) {
    TITLE.init(view);
    PLAY.init(view);
    OVER.init(view);
  }

  /**
   * �V�[����ύX����
   */
  public static void startScene(final Scene scene) {
    Log.d("Dribble", "startScene: scene = " + scene);
    current = scene;
    events.clear();
    current.start(view);
  }

  /** �L���[����C�x���g��1���擾 */
  public static synchronized MotionEvent event() {
    return events.poll();
  }

  /** �L���[�ɃC�x���g���i�[ */
  public static synchronized void queue(final MotionEvent event) {
    events.add(event);
  }

}