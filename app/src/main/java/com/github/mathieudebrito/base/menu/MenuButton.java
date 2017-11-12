package com.github.mathieudebrito.base.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mathieudebrito.base.R;
import com.github.mathieudebrito.base.utils.Styles;


public class MenuButton extends LinearLayout {

    protected ImageView imgItemWithImage;
    protected TextView txtItemWithImage;

    protected String text = null;
    protected Drawable icon = null;
    protected int tint = -1;

    public MenuButton(Context context) {
        super(context);
        bindViews();
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        bindViews();
        initFromXML(attrs);
    }

    public void bindViews() {
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.btn_menu, null);

        imgItemWithImage = (ImageView) layout.findViewById(R.id.imgItemWithImage);
        txtItemWithImage = (TextView) layout.findViewById(R.id.txtItemWithImage);

        addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void update(int text, int image) {
        update(text, image, -1);
    }

    public void update(int text, int image, int tint) {
        txtItemWithImage.setText(text);
        imgItemWithImage.setImageResource(image);
        if(tint != -1){
            this.tint = tint;
            imgItemWithImage.setColorFilter(tint);
        }
    }

    public void initFromXML(AttributeSet attrs) {

        try {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.MenuButton);

            text = Styles.getString(arr, R.styleable.MenuButton_menuButtonText);
            if (text == null) {
                text = "";
            }
            txtItemWithImage.setText(text);

            icon = arr.getDrawable(R.styleable.MenuButton_menuButtonIcon);
            if (icon == null) {
                imgItemWithImage.setImageDrawable(null);
                imgItemWithImage.setBackgroundColor(Color.TRANSPARENT);
            } else {
                imgItemWithImage.setImageDrawable(icon);
            }

            tint = arr.getColor(R.styleable.MenuButton_menuButtonTint, Color.TRANSPARENT);
            imgItemWithImage.setColorFilter(tint);

            arr.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}