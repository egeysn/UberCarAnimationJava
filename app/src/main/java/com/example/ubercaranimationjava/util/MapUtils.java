package com.example.ubercaranimationjava.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.example.ubercaranimationjava.R;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.lang.Math.atan;

public  class MapUtils {


    public static Bitmap getCarBitmap( Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_car);
        return Bitmap.createScaledBitmap(bitmap, 50, 100, false);

    }


    @NotNull
    public static Bitmap getOriginDestinationMarkerBitmap() {
        int height = 20;
        int width = 20;
        Bitmap bitmap = Bitmap.createBitmap(height, width, Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawRect(0.0F, 0.0F, (float)width, (float)height, paint);
        return bitmap;
    }

    public static float getRotation(@NotNull LatLng start, @NotNull LatLng end) {

        double latDifference = Math.abs(start.latitude- end.latitude);
        double lngDifference = Math.abs(start.longitude - end.longitude);
        float rotation = -1F;

        double degrees = Math.toDegrees(atan(lngDifference / latDifference));
        if (start.latitude < end.latitude && start.longitude < end.longitude) {
                 rotation = (float) degrees;
        } else {
            if (start.latitude >= end.latitude && start.longitude < end.longitude) {

                rotation = (float)(90 - degrees + 90);
            } else if (start.latitude >= end.latitude && start.longitude >= end.longitude) {
                rotation = (float)(degrees + 180);
            } else if (start.latitude < end.latitude && start.longitude >= end.longitude) {
                rotation =
                        (float)(90 - degrees + 270);
            }
        }

        return rotation;
    }

    @NotNull
    public static ArrayList<LatLng> getListOfLocations() {
        ArrayList<LatLng> locationList = new ArrayList<LatLng>();
        locationList.add(new LatLng(28.436970000000002D, 77.11272000000001D));
        locationList.add(new LatLng(28.43635D, 77.11289000000001D));
        locationList.add(new LatLng(28.4353D, 77.11317000000001D));
        locationList.add(new LatLng(28.435280000000002D, 77.11332D));
        locationList.add(new LatLng(28.435350000000003D, 77.11368D));
        locationList.add(new LatLng(28.4356D, 77.11498D));
        locationList.add(new LatLng(28.435660000000002D, 77.11519000000001D));
        locationList.add(new LatLng(28.43568D, 77.11521D));
        locationList.add(new LatLng(28.436580000000003D, 77.11499D));
        locationList.add(new LatLng(28.436590000000002D, 77.11507D));
        return locationList;
    }




}
