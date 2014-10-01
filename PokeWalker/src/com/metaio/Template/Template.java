// Copyright 2007-2014 metaio GmbH. All rights reserved.
package com.metaio.Template;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.AnnotatedGeometriesGroupCallback;
import com.metaio.sdk.jni.EVISUAL_SEARCH_STATE;
import com.metaio.sdk.jni.IAnnotatedGeometriesGroup;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.ILight;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IRadar;
import com.metaio.sdk.jni.IVisualSearchCallback;
import com.metaio.sdk.jni.ImageStruct;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.SensorValues;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.sdk.jni.VisualSearchResponseVector;
import com.metaio.tools.SystemInfo;
import com.metaio.tools.io.AssetsManager;

import java.io.FileOutputStream;

import java.util.Random;

public class Template extends ARViewActivity
{

    private IGeometry mPokeGeo;
    private IGeometry mEarthOcclusion;
    private IGeometry mEarthIndicators;
    private boolean mEarthOpened;
    private MetaioSDKCallbackHandler mSDKCallback;
    private VisualSearchCallbackHandler mVisualSearchCallback;

    private IAnnotatedGeometriesGroup mAnnotatedGeometriesGroup;

    private MyAnnotatedGeometriesGroupCallback mAnnotatedGeometriesGroupCallback;

    private IRadar mRadar;

    public GPSTracker gpsTracker;

    public float pokeLat, pokeLong;

    public ILight mPointLight;

    public int currentPokeId = 0;

    private int tapCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        gpsTracker = new GPSTracker(this);

        mEarthOpened = false;

        mSDKCallback = new MetaioSDKCallbackHandler();
        mVisualSearchCallback = new VisualSearchCallbackHandler();

        if (metaioSDK != null)
        {
            metaioSDK.registerVisualSearchCallback(mVisualSearchCallback);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mSDKCallback.delete();
        mSDKCallback = null;
        mVisualSearchCallback.delete();
        mVisualSearchCallback = null;
    }

    @Override
    protected int getGUILayout()
    {
        // Attaching layout to the activity
        return R.layout.template;
    }

    @Override
    protected void loadContents()
    {
        gpsTracker.updateGPSCoordinates();
        Log.v("COORD", "Lat: " + gpsTracker.latitude + " Long: " + gpsTracker.longitude);

        boolean result = metaioSDK.setTrackingConfiguration("GPS", false);
        MetaioDebug.log("Tracking data loaded: " + result);

        final String pokeModel = "imagemodel.obj";
        mPokeGeo = metaioSDK.createGeometry(pokeModel);

        //final String imagePath = AssetsManager.getAssetPath(getApplicationContext(), "charmander.png");
       //if (imagePath != null)
        //{

        //}


        mAnnotatedGeometriesGroup = metaioSDK.createAnnotatedGeometriesGroup();
        mAnnotatedGeometriesGroupCallback = new MyAnnotatedGeometriesGroupCallback();
        mAnnotatedGeometriesGroup.registerCallback(mAnnotatedGeometriesGroupCallback);

        // Clamp geometries' Z position to range [5000;200000] no matter how close or far they are away.
        // This influences minimum and maximum scaling of the geometries (easier for development).

        //metaioSDK.setLLAObjectRenderingLimits(5, 200);

        // Set render frustum accordingly
        metaioSDK.setRendererClippingPlaneLimits(10, 220000);

        // let's create LLA objects for known cities

        LLACoordinate pokeLoc = new LLACoordinate(gpsTracker.latitude, gpsTracker.longitude, 0, 0);

        // Load some POIs. Each of them has the same shape at its geoposition. We pass a string
        // (const char*) to IAnnotatedGeometriesGroup::addGeometry so that we can use it as POI title
        // in the callback, in order to create an annotation image with the title on it.

        mPokeGeo = createPOIGeometry(pokeLoc, pokeModel);
        //setRandomLocation(pokeModel);

        //mAnnotatedGeometriesGroup.addGeometry(mPokeGeo, "Pokemon");

        GetRandomPokemon(mPokeGeo);


        mRadar = metaioSDK.createRadar();
        mRadar.setBackgroundTexture(AssetsManager.getAssetPath(getApplicationContext(), "radar.png"));
        mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath(getApplicationContext(), "yellow.png"));
        mRadar.setRelativeToScreen(IGeometry.ANCHOR_TL);

        // add geometries to the radar
        mRadar.add(mPokeGeo);


        //LIGHT
        metaioSDK.setAmbientLight(new Vector3d(0.05f));

       /* mPointLight = metaioSDK.createLight();
        mPointLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_POINT);
        mPointLight.setAmbientColor(new Vector3d(0, 0, 0));
        mPointLight.setAttenuation(new Vector3d(0, 0, 40));
        mPointLight.setDiffuseColor(new Vector3d(1, 1, 1)); // green-ish
        mPointLight.setCoordinateSystemID(1);

        mPointLight.setTranslation(new Vector3d(0,0,0));*/
    }

    void UpdateLocation(){


    }

    private IGeometry createPOIGeometry(LLACoordinate lla, String modelPath)
    {

        String path = AssetsManager.getAssetPath(getApplicationContext(), modelPath);
        if (path != null)
        {
            IGeometry geo = metaioSDK.createGeometry(path);
            //geo.setTranslationLLA(lla);
            geo.setLLALimitsEnabled(false);
            geo.setScale(1);


            SetRandomLocation(geo);

            return geo;
        }
        else
        {
            MetaioDebug.log(Log.ERROR, "Missing files for POI geometry");
            return null;
        }
    }

    private void GetRandomPokemon(IGeometry geo){

        Random rand = new Random();

        tapCount = 0;
        currentPokeId = rand.nextInt(150) + 1;
//        Log.d("TAG", (AssetsManager.getAssetPath(getApplicationContext(), "oddish.png")));

        geo.setTexture(AssetsManager.getAssetPath(getApplicationContext(), "pokemon_" + currentPokeId + ".png"));
        String pokename = "pokemon_" + currentPokeId + ".png";


    }

    private void SetRandomLocation(IGeometry geo){
        int randX, randY, randZ;

        Random rand = new Random();

        randX = rand.nextInt(100) + 50;
        if(rand.nextInt(2) < 1){
            randX *= -1;
        }
        randY = rand.nextInt(100) + 50;
        if(rand.nextInt(2) < 1){
            randY *= -1;
        }
        randZ = rand.nextInt(100) + 50;
        if(rand.nextInt(2) < 1){
            randZ *= -1;
        }

        geo.setTranslation(new Vector3d(randX,randY, randZ));//rand.nextInt(15) + 5));
        //geo.startAnimation("Idle_1", true);


        geo.setRotation(new Rotation(0,0,50));
    }

    private String getAnnotationImageForTitle(String title)
    {
        Bitmap billboard = null;

        try
        {
            final String texturepath = getCacheDir() + "/" + title + ".png";
            Paint mPaint = new Paint();

            // Load background image and make a mutable copy

            float dpi = SystemInfo.getDisplayDensity(getApplicationContext());
            int scale = dpi > 240 ? 2 : 1;
            String filepath = AssetsManager.getAssetPath(getApplicationContext(), "POI_bg" + (scale == 2 ? "@2x" : "") + ".png");
            Bitmap mBackgroundImage = BitmapFactory.decodeFile(filepath);

            billboard = mBackgroundImage.copy(Bitmap.Config.ARGB_8888, true);

            Canvas c = new Canvas(billboard);

            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(24);
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setTextAlign( Paint.Align.CENTER );

            float y = 40 * scale;
            float x = 30 * scale;

            // Draw POI name
            if (title.length() > 0)
            {
                String n = title.trim();

                final int maxWidth = 160 * scale;

                int i = mPaint.breakText(n, true, maxWidth, null);

                int xPos = (c.getWidth() / 2);
                int yPos = (int) ((c.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2)) ;
                c.drawText(n.substring(0, i), xPos, yPos, mPaint);

                // Draw second line if valid
                if (i < n.length())
                {
                    n = n.substring(i);
                    y += 20 * scale;
                    i = mPaint.breakText(n, true, maxWidth, null);

                    if (i < n.length())
                    {
                        i = mPaint.breakText(n, true, maxWidth - 20*scale, null);
                        c.drawText(n.substring(0, i) + "...", x, y, mPaint);
                    }
                    else
                    {
                        c.drawText(n.substring(0, i), x, y, mPaint);
                    }
                }
            }

            // Write texture file
            try
            {
                FileOutputStream out = new FileOutputStream(texturepath);
                billboard.compress(Bitmap.CompressFormat.PNG, 90, out);
                MetaioDebug.log("Texture file is saved to "+texturepath);
                return texturepath;
            }
            catch (Exception e)
            {
                MetaioDebug.log("Failed to save texture file");
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            MetaioDebug.log("Error creating annotation texture: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.DEBUG, e);
            return null;
        }
        finally
        {
            if (billboard != null)
            {
                billboard.recycle();
                billboard = null;
            }
        }

        return null;
    }

    final class MyAnnotatedGeometriesGroupCallback extends AnnotatedGeometriesGroupCallback
    {

        @Override
        public IGeometry loadUpdatedAnnotation(IGeometry geometry, Object userData,
                                               IGeometry existingAnnotation)
        {
            if (userData == null)
            {
                return null;
            }

            if (existingAnnotation != null)
            {
                // We don't update the annotation if e.g. distance has changed
                return existingAnnotation;
            }

            String title = (String)userData; // as passed to addGeometry
            String texturePath = getAnnotationImageForTitle(title);

            return metaioSDK.createGeometryFromImage(texturePath, true, false);
        }
    }


    @Override
    public void onDrawFrame()
    {
        if (metaioSDK != null && mSensors != null)
        {
            SensorValues sensorValues = mSensors.getSensorValues();

            float heading = 0.0f;
            if (sensorValues.hasAttitude())
            {
                float m[] = new float[9];
                sensorValues.getAttitude().getRotationMatrix(m);

                Vector3d v = new Vector3d(m[6], m[7], m[8]);
                v = v.normalize();

                heading = (float)(-Math.atan2(v.getY(), v.getX()) - Math.PI/2.0);
            }

            IGeometry geos[] = new IGeometry[] {mPokeGeo};
            Rotation rot = new Rotation((float)(Math.PI/2.0), 0.0f, -heading);
            for (IGeometry geo : geos)
            {
                if (geo != null)
                {
                    geo.setRotation(rot);
                }
            }
        }

        super.onDrawFrame();
    }

    @Override
    protected void onGeometryTouched(final IGeometry geometry)
    {
        MetaioDebug.log("Geometry selected: "+geometry);

        mSurfaceView.queueEvent(new Runnable()
        {

            @Override
            public void run()
            {
                mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath(getApplicationContext(), "yellow.png"));
                mRadar.setObjectTexture(geometry, AssetsManager.getAssetPath(getApplicationContext(), "red.png"));
            }
        });
//
//        geometry.setScale(0.8f);
//        tapCount++;
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                geometry.setScale(1f);
//            }
//        }, 100);
//
//        metaioSDK.requestScreenshot(Environment.getExternalStorageDirectory().toString() + "/pokescreen.jpg");

        /*
        MetaioDebug.log("Template.onGeometryTouched: " + geometry);

        if (geometry != mEarthOcclusion)
        {
            if (!mEarthOpened)
            {
                mEarth.startAnimation("Open", false);
                mEarthIndicators.startAnimation("Grow", false);
                mEarthOpened = true;
            }
            else
            {
                mEarth.startAnimation("Close", false);
                mEarthIndicators.startAnimation("Shrink", false);
                mEarthOpened = false;
            }
        }*/

    }

//    public Bitmap screenShot(View view) {
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
//                view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//        return bitmap;
//    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler()
    {
        return mSDKCallback;
    }

    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback
    {

        @Override
        public void onSDKReady()
        {
            MetaioDebug.log("The SDK is ready");
        }

        @Override
        public void onAnimationEnd(IGeometry geometry, String animationName)
        {
            MetaioDebug.log("animation ended" + animationName);
        }

        @Override
        public void onMovieEnd(IGeometry geometry, String name)
        {
            MetaioDebug.log("movie ended" + name);
        }

        @Override
        public void onNewCameraFrame(ImageStruct cameraFrame)
        {
            MetaioDebug.log("a new camera frame image is delivered" + cameraFrame.getTimestamp());
        }

        @Override
        public void onCameraImageSaved(String filepath)
        {
            MetaioDebug.log("a new camera frame image is saved to" + filepath);
        }

        @Override
        public void onScreenshotImage(ImageStruct image)
        {
            MetaioDebug.log("screenshot image is received" + image.getTimestamp());
        }

        @Override
        public void onScreenshotSaved(String filepath)
        {
            MetaioDebug.log("screenshot image is saved to" + filepath);
        }

        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues)
        {
            for (int i=0; i<trackingValues.size(); i++)
            {
                final TrackingValues v = trackingValues.get(i);
                MetaioDebug.log("Tracking state for COS "+v.getCoordinateSystemID()+" is "+v.getState());
            }
        }

        @Override
        public void onInstantTrackingEvent(boolean success, String file)
        {
            if (success)
            {
                MetaioDebug.log("Instant 3D tracking is successful");
            }
        }
    }

    final class VisualSearchCallbackHandler extends IVisualSearchCallback
    {

        @Override
        public void onVisualSearchResult(VisualSearchResponseVector response, int errorCode)
        {
            if (errorCode == 0)
            {
                MetaioDebug.log("Visual search is successful");
            }
        }

        @Override
        public void onVisualSearchStatusChanged(EVISUAL_SEARCH_STATE state)
        {
            MetaioDebug.log("The current visual search state is: " + state);
        }


    }


    public void TakeScreenshot(View V){

    }

}
