package com.spicysoft.sample.dribble;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
  /** �l�b�g����擾�����w�i�摜��ۊǂ���t�@�C���� */
  private static final String FILENAME_BKGND= "bkgnd.image";
  /** �ǂݍ��񂾔w�i�摜 */
  private static Bitmap image = null;
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
    try {
      byte[] data;
      data = load(FILENAME_BKGND);
      if (data != null) {
        Log.i("Dribble","BackgroundImage is loaded from the file.");
      } else {
        data = httpGetBinary("http://farm4.staticflickr.com/3084/3206940931_156126f790_b.jpg");
        if (data != null) {
          Log.i("Dribble","BackgroundImage is got from the internet.");
          save(FILENAME_BKGND,data);
        } else {
          Log.i("Dribble","BackgroundImage can't be got from the internet.");
        }
      }
      image = BitmapFactory.decodeByteArray(data, 0, data.length);

    } catch(final Exception e) {
      Log.e("Dribble",e.getLocalizedMessage(),e);
    }
  }

  /** ���݂̔w�i�摜���擾���� */
  static Bitmap image()
  {
    return image;
  }

  /**
   * �w�肵��URL��GET���\�b�h��HTTP�ʐM���s�����ʂ��o�C�i���[�`���ŕԂ�
   */
  private static byte[] httpGetBinary(final String url) throws Exception {
      final URLConnection connection = new URL(url).openConnection();
      connection.setDoInput(true);

      final InputStream stream = connection.getInputStream();
      final byte[] data = toByteArray(stream);
      stream.close();
      return data;
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
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    final byte[] temp = new byte[1024];

    for(;;) {
      final int read = stream.read(temp);
      if (read <= 0) {
        break;
      }
      buffer.write(temp,0,read);
    }
    return buffer.toByteArray();
  }

}
