package com.nguyenthanhduy.yoga_d.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
    public static void HideKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null && isKeyboardVisible(activity)) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static boolean isKeyboardVisible(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        int keypadHeight = screenHeight - rect.bottom;
        return keypadHeight > screenHeight * 0.15;
    }
}
