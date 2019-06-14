package com.mingquan.yuejian.bean;

/**
 * Created by administrato on 2017/1/6.
 */

public class YueJianAppFrameBean {
  public String filename;
  public boolean rotate;
  public boolean trimmed;
  public Frame frame;
  public SpriteSourceSize spriteSourceSize;
  public SourceSize sourceSize;
  public Pivot pivot;
  public String imageSource;
  public String actionName;

  public String getImageSource() {
    return imageSource;
  }

  public void setImageSource(String imageSource) {
    this.imageSource = imageSource;
  }

  public class Frame {
    public int x;
    public int y;
    public int w;
    public int h;
  }

  public class SpriteSourceSize {
    public int x;
    public int y;
    public int w;
    public int h;

    @Override
    public String toString() {
      return "spriteSourceSize{"
          + "x='" + x + '\'' + ", y='" + y + '\'' + ", w='" + w + '\'' + ", h='" + h + '\'' + '}';
    }
  }

  public class SourceSize {
    public int w;
    public int h;
  }

  public class Pivot {
    public float x;
    public float y;
  }

  @Override
  public String toString() {
    return "YueJianAppFrameBean{"
        + "filename='" + filename + '\'' + ", rotate=" + rotate + ", trimmed=" + trimmed
        + ", frame=" + frame + ", spriteSourceSize=" + spriteSourceSize
        + ", sourceSize=" + sourceSize + ", pivot=" + pivot + '}';
  }
}
