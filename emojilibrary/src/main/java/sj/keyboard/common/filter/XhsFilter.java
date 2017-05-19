package sj.keyboard.common.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.EditText;

import com.sj.emoji.EmojiDisplayListener;
import com.sj.emoji.EmojiSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sj.keyboard.interfaces.EmoticonFilter;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.widget.EmoticonSpan;

public class XhsFilter extends EmoticonFilter {

    public static final int WRAP_DRAWABLE = -1;
    private int emoticonSize = -1;
    public static final Pattern XHS_RANGE = Pattern.compile("\\[[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]");

    public static Matcher getMatcher(CharSequence matchStr) {
        return XHS_RANGE.matcher(matchStr);
    }

    @Override
    public void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emoticonSize = emoticonSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emoticonSize;
        clearSpan(editText.getText(), start, text.toString().length());
    }



    private void clearSpan(Spannable spannable, int start, int end) {
        if (start == end) {
            return;
        }
        EmoticonSpan[] oldSpans = spannable.getSpans(start, end, EmoticonSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }

    public static void emoticonDisplay(Context context, Spannable spannable, String emoticonName, int fontSize, int start, int end) {
        Drawable drawable = getDrawableFromAssets(context, emoticonName);
        if (drawable != null) {
            int itemHeight;
            int itemWidth;
            if (fontSize == WRAP_DRAWABLE) {
                itemHeight = drawable.getIntrinsicHeight();
                itemWidth = drawable.getIntrinsicWidth();
            } else {
                itemHeight = fontSize;
                itemWidth = fontSize;
            }

            drawable.setBounds(0, 0, itemHeight, itemWidth);
            EmojiSpan imageSpan = new EmojiSpan(drawable);
            spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }
}
