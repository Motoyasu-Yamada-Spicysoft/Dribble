package com.spicysoft.sample.dribble;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * �w�i�摜�Ǘ�
 * 
 */
class BackgroundImage
{
  /**
   * �w�i�摜���擾���Ă���URL��ݒ肵�Ă��܂��B
   * �z��̃C���f�b�N�X�ɉ����āA
   * �e���ԑ�(0:��,1:�ߑO,2:����,3:�ߌ�,4:�[��,5:��,6:�[��)��URL���i�[����Ă��܂��B
   * 
   * Flickr�Ō��J����Ă���摜�̂���
   * CreativeCommons���C�Z���X�̗��p����������Ă���摜�ɂ��āA
   * ���p�����Ă��������Ă��܂��B
   * 
   * �e�摜�̌����͈ȉ��ɋL�ڂ���Ă��܂�
   * http://www.flickr.com/photos/minicooper_dan/2576816778/
   * http://www.flickr.com/photos/pistachio_maplewood/4679460339/
   * http://www.flickr.com/photos/world9-1/5371346367/
   * http://www.flickr.com/photos/darkensiva/3818297731/
   * http://www.flickr.com/photos/clf/6770279955/
   * http://www.flickr.com/photos/7451276@N08/5761670299/
   * http://www.flickr.com/photos/yto/4611536747/
   * 
  */
  private static final String URL_IMAGE[] = {
    "http://farm4.staticflickr.com/3048/2576816778_907f9edf07_o.jpg",
    "http://farm5.staticflickr.com/4047/4679460339_37a40217aa_b.jpg",
    "http://farm6.staticflickr.com/5005/5371346367_2b6ae5af3e_z.jpg",
    "http://farm3.staticflickr.com/2639/3818297731_3092638846_b.jpg",
    "http://farm8.staticflickr.com/7030/6770279955_8ca9e52838_o.jpg",
    "http://farm4.staticflickr.com/3397/5761670299_1c05716ff9_b.jpg",
    "http://farm4.staticflickr.com/3364/4611536747_7ecbc3b28a_b.jpg"
  };

  /** �ǂݍ��񂾔w�i�摜 */
  private static Bitmap image = null;
  /** �ǂݍ��܂�Ă���摜�̎��ԑєԍ� */
  private static int hourly = -1;
  /** �Q�� */
  private static Context context;

  /**
   * �C���^�[�l�b�g����w�i�摜���擾���A
   * ���[�J���t�@�C���Ƃ��ĕۑ�����B
   * ����̋N���ȍ~�́A���[�J���t�@�C����ǂݍ��ށB
   */
  static void init(final Context context)
  {
    BackgroundImage.context = context;

    loadImage();
  }

  /** ���݂̔w�i�摜���擾���� */
  static Bitmap image()
  {
    loadImage();
    return image;
  }


  /** �摜�����[�h����B */
  private static void loadImage()
  {
    // ���ɁA���݂̎��ԑтɂ������摜���ǂݍ��܂�Ă���ꍇ��
    // �摜��ǂݍ��܂Ȃ�
    final int hourly = hourly();
    if (image != null && hourly == BackgroundImage.hourly) {
      Log.d("Dribble","Reused image");
      return;
    }
    // �摜���ǂݍ��܂�Ă��邪���ԑт��قȂ�̂Ŕj��
    if (image != null) {
      image.recycle();
      image = null;
      System.gc();
      Log.d("Dribble","Disposed image.");
    }
    BackgroundImage.hourly = hourly;

    byte[] data = load(fileNameOfBkgnd(hourly)); // (1)

    if (data != null) { // (2)
      Log.i("Dribble","BackgroundImage is loaded from the file.");
    } else {

      data = httpGetBinary(URL_IMAGE[hourly]); // (3)

      if (data != null) { // (4)
        Log.i("Dribble","BackgroundImage is got from the internet.");
        save(fileNameOfBkgnd(hourly),data);

      } else {
        Log.i("Dribble","BackgroundImage can't be got from the internet.");
      }
    }

    // (5)
    image = BitmapFactory.decodeByteArray(data, 0, data.length);
  }

  /**
   * �w�肵��URL��GET���\�b�h��HTTP�ʐM���s�����ʂ��o�C�i���[�`���ŕԂ�
   */
  private static byte[] httpGetBinary(final String stringUrl) {
      try {
        final URL url = new URL(stringUrl); // (1)
        final URLConnection connection = url.openConnection(); // (2)

        final InputStream stream = connection.getInputStream(); // (3)

        final byte[] data = toByteArray(stream); // (4)

        stream.close(); // (5)
        return data;

      } catch(final Exception e) { // (6)
        Log.e("Dribble", "URL_IMAGE:" + stringUrl, e);
        return null;
      }
  }

  /**
   * �t�@�C���Ƀo�C�i���[�f�[�^��ۑ�����
   */
  private static void save(final String filePath,final byte[] data)
  {
    try {
      final FileOutputStream fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
      fos.write(data);
      fos.close();
    } catch (IOException e) {
      Log.e("Dribble", "filePath:" + filePath, e);
    }
  }

  /**
   * �t�@�C������o�C�i���[�f�[�^�[��ǂݍ���
   */
  private static byte[] load(final String filePath)
  {
    try {
      final FileInputStream stream = context.openFileInput(filePath);
      final byte[] data = toByteArray(stream);
      stream.close();
      return data;
    } catch(final IOException e) {
      Log.e("Dribble", "filePath:" + filePath, e);
      return null;
    }
  }

  /**
   * �X�g���[�����炷�ׂẴf�[�^��ǂݍ���
   * �o�C�g�z��Ƀf�[�^�[���i�[���ĕԂ��B
   */
  private static byte[] toByteArray(final InputStream stream) throws IOException
  {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream(); // (1)
    final byte[] temp = new byte[1024]; // (2)

    for(;;) {
      final int read = stream.read(temp); // (3)
      if (read < 0) { // (4)
        break;
      } 
      buffer.write(temp,0,read); // (5)
    }

    return buffer.toByteArray(); // (6)
  }

  /**
   * ���ԑтɉ����ēK�؂ȉ摜��Ԃ����߂́A
   * ���ԑ�(0:��,1:�ߑO,2:����,3:�ߌ�,4:�[��,5:��,6:�[��)�ԍ���Ԃ�
   */
  private static int hourly()
  {
    final int hour = new Date().getHours();
    if (hour < 6) {
      return 6;
    }
    if (hour < 9) {
      return 0;
    }
    if (hour < 11) {
      return 1;
    }
    if (hour < 14) {
      return 2;
    }
    if (hour < 17) {
      return 3;
    }
    if (hour < 19) {
      return 4;
    }
    if (hour < 22) {
      return 5;
    }
    return 6;
  }

  /** �l�b�g����擾�����w�i�摜��ۊǂ���t�@�C���� */
  private static String fileNameOfBkgnd(final int hourly) 
  {
    return "bkgnd.image." + hourly;
  }
}
