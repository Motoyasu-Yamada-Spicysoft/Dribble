package com.spicysoft.sample.dribble;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

/**
 * �Ō�̃X�R�A��x�X�g�X�R�A�Ȃǂ̃v���C�L�^���Ǘ�����
 */
final class PlayLog
{
  /** �v���C�L�^��ۊǂ���t�@�C���� */
  private static final String FILENAME_PLAYLOG = "playlog.dat";
  /** �Q�� */
  private static Context context;
  
  /** �Ō�̃v���C�̃X�R�A */
  private static int lastScore = 0;
  /** �Ō�̃v���C���x�X�g�X�R�A�ł��������ǂ���? */
  private static boolean isLastBest = false;

  /** �x�X�g�X�R�A */
  private static int bestScore = 0;
  /** �x�X�g�X�R�A���L�^�������� */
  private static Date bestPlayedAt;

  /**
   * �v���C�L�^�Ǘ��@�\�̏�����
   * @param context
   */
  public static void init(final Context context)
  {
    PlayLog.context = context;
    load();
  }

  /**
   * �X�R�A���L�^����B
   * �X�R�A���ߋ��̃x�X�g�X�R�A������ꍇ��
   * �x�X�g�X�R�A���X�V���ăf�[�^��ۑ�����B
   *
   * @param score �X�R�A
   * @return �x�X�g�X�R�A�Ȃ�true��Ԃ�
   */
  public static void logScore(final int score) {
    lastScore = score;
    isLastBest = bestScore < score;
    if (isLastBest) {
      bestScore = score;
      bestPlayedAt = new Date();
      save();
    }
  }
  
  /**
   * �Ō�Ƀv���C�̃X�R�A��Ԃ��B
   * @return �X�R�A
   */
  public static int lastScore() {
    return lastScore;
  }

  /** �Ō�̃v���C���x�X�g�X�R�A���H */
  public static boolean isLastBest() {
    return isLastBest;
  }

  /**
   * �x�X�g�X�R�A��Ԃ��B
   * @return �x�X�g�X�R�A
   */
  public static int bestScore() {
    return bestScore;
  }

  /**
   * �x�X�g�X�R�A���l����������
   */
  public static Date bestPlayedAt() {
    return bestPlayedAt;
  }

  /**
   * �x�X�g�X�R�A���Z�[�u����
   */
  public static void save()
  {
    try {
      final FileOutputStream fos = context.openFileOutput(FILENAME_PLAYLOG, Context.MODE_PRIVATE);
      final DataOutputStream dos = new DataOutputStream(fos);
      dos.writeInt(bestScore);
      dos.writeUTF(DateFormat.getDateInstance().format(bestPlayedAt));
      dos.flush();
      fos.close();
    } catch (IOException e) {
      Log.e("Dribble", "PlayLog.save: bestScore=" + bestScore + ",bestPlayedAt=" + bestPlayedAt, e);
    }
  }

  /**
   * �x�X�g�X�R�A�����[�h����
   */
  public static void load()
  {
    FileInputStream fis;
    try {
      fis = context.openFileInput(FILENAME_PLAYLOG);

    } catch(final FileNotFoundException e) {
      bestScore = 0;
      bestPlayedAt = new Date(0);
      return;
    }
    
    int tempLastScore;
    Date tempLastScorePlayed;
    try {
      final DataInputStream dis = new DataInputStream(fis);
      tempLastScore = dis.readInt();
      tempLastScorePlayed = DateFormat.getDateInstance().parse(dis.readUTF());
      fis.close();
    } catch(final Exception e) {
      Log.e("Dribble", "PlayLog.load:bestScore=" + bestScore + ",bestPlayedAt=" + bestPlayedAt, e);
      return;
    }
    bestScore = tempLastScore;
    bestPlayedAt = tempLastScorePlayed;
  }
}
