package com.github.mathieudebrito.base.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class Assets {

    public static String getAsString(Context context, String fileName) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

}
