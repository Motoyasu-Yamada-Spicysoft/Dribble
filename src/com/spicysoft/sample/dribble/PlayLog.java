package com.spicysoft.sample.dribble;


/**
 * �Ō�̃X�R�A��x�X�g�X�R�A�Ȃǂ̃v���C�L�^���Ǘ�����
 */
final class PlayLog
{
  /** �Ō�̃v���C�̃X�R�A */
  private static int lastScore = 0;

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
  }
  
  /**
   * �Ō�Ƀv���C�̃X�R�A��Ԃ��B
   * @return �X�R�A
   */
  public static int lastScore() {
    return lastScore;
  }
}
