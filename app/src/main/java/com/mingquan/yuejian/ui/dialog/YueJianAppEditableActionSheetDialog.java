package com.mingquan.yuejian.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mingquan.yuejian.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的仿ios的ActionSheetDialog
 */
public class YueJianAppEditableActionSheetDialog {
  private Context context;
  private Dialog dialog;
  private TextView txt_title;
  private TextView txt_cancel;
  private LinearLayout lLayout_content;
  private ScrollView sLayout_content;
  private boolean showTitle = false;
  private List<SheetItem> sheetItemList;
  private Display display;
  private boolean mCalcelableOnItemClick = true;

  public YueJianAppEditableActionSheetDialog(Context context) {
    this.context = context;
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    display = windowManager.getDefaultDisplay();
  }

  public YueJianAppEditableActionSheetDialog builder() {
    View view = LayoutInflater.from(context).inflate(R.layout.yue_jian_app_view_actionsheet, null);

    view.setMinimumWidth(display.getWidth());

    sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
    lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
    txt_title = (TextView) view.findViewById(R.id.txt_title);
    txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
    txt_cancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
    dialog.setContentView(view);
    Window dialogWindow = dialog.getWindow();
    dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    lp.x = 0;
    lp.y = 0;
    dialogWindow.setAttributes(lp);

    return this;
  }

  public YueJianAppEditableActionSheetDialog setTitle(String title) {
    showTitle = true;
    txt_title.setVisibility(View.VISIBLE);
    txt_title.setText(title);
    return this;
  }

  public YueJianAppEditableActionSheetDialog setCancelable(boolean cancel) {
    dialog.setCancelable(cancel);
    return this;
  }

  public YueJianAppEditableActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
    dialog.setCanceledOnTouchOutside(cancel);
    return this;
  }

  public void setCancelableOnItemClick(boolean cancelable) {
    mCalcelableOnItemClick = cancelable;
  }
  /**
   * 重新设置SheetItem
   */
  public void resetSheetItem(String name, SheetItemColor color, OnSheetItemClickListener listener) {

  }

  public void dismiss() {
    if (null != dialog && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  /**
   * @param strItem
   * @param color
   * @param listener
   * @return
   */
  public YueJianAppEditableActionSheetDialog addSheetItem(
      TextView textView, String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
    if (sheetItemList == null) {
      sheetItemList = new ArrayList<SheetItem>();
    }
    sheetItemList.add(new SheetItem(textView, strItem, color, listener));
    return this;
  }

  private void setSheetItems() {
    if (sheetItemList == null || sheetItemList.size() <= 0) {
      return;
    }

    int size = sheetItemList.size();

    if (size >= 7) {
      LayoutParams params = (LayoutParams) sLayout_content.getLayoutParams();
      params.height = display.getHeight() / 2;
      sLayout_content.setLayoutParams(params);
    }

    /**添加ActionSheetItem*/
    for (int i = 1; i <= size; i++) {
      final int index = i;
      SheetItem sheetItem = sheetItemList.get(i - 1);
      String strItem = sheetItem.name;
      SheetItemColor color = sheetItem.color;

      final OnSheetItemClickListener listener =
          (OnSheetItemClickListener) sheetItem.itemClickListener;

      sheetItem.textView.setText(strItem);
      sheetItem.textView.setTextSize(18);
      sheetItem.textView.setGravity(Gravity.CENTER);
      sheetItem.textView.setTag(sheetItem.name);

      /**添加不同的条目*/
      if (size == 1) {
        if (showTitle) {
          sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_bottom_selector);
        } else {
          sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_single_selector);
        }
      } else {
        if (showTitle) {
          if (i >= 1 && i < size) {
            sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_middle_selector);
          } else {
            sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_bottom_selector);
          }
        } else {
          if (i == 1) {
            sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_top_selector);
          } else if (i < size) {
            sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_middle_selector);
          } else {
            sheetItem.textView.setBackgroundResource(R.drawable.yue_jian_app_actionsheet_bottom_selector);
          }
        }
      }

      /**显示不同的颜色*/
      if (color == null) {
        sheetItem.textView.setTextColor(Color.parseColor(SheetItemColor.Blue.getName()));
      } else {
        sheetItem.textView.setTextColor(Color.parseColor(color.getName()));
      }

      float scale = context.getResources().getDisplayMetrics().density;
      int height = (int) (45 * scale + 0.5f);
      sheetItem.textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));

      /**添加点击事件*/
      sheetItem.textView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onClick(index);
          if (mCalcelableOnItemClick && dialog != null) {
            dialog.dismiss();
          }
        }
      });

      lLayout_content.addView(sheetItem.textView);
    }
  }

  public void show() {
    setSheetItems();
    dialog.show();
  }

  public void cancel() {
    dialog.closeOptionsMenu();
    dialog.cancel();
    dialog = null;
  }

  public interface OnSheetItemClickListener { void onClick(int which); }

  public class SheetItem {
    public String name;
    public OnSheetItemClickListener itemClickListener;
    public SheetItemColor color;
    public TextView textView;

    public SheetItem(TextView textView, String name, SheetItemColor color,
        OnSheetItemClickListener itemClickListener) {
      this.textView = textView;
      this.name = name;
      this.color = color;
      this.itemClickListener = itemClickListener;
    }

    public void changeListener(OnSheetItemClickListener listener) {
      this.itemClickListener = null;
      if (listener != null) {
        this.itemClickListener = listener;
      }
    }
  }

  public enum SheetItemColor {
    Blue("#037BFF"),
    Red("#FD4A2E"),
    Black("#000000");

    private String name;

    private SheetItemColor(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
