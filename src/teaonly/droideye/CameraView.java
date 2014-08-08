package teaonly.droideye;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.*;

public class CameraView implements SurfaceHolder.Callback{
    public static interface CameraReadyCallback { 
        public void onCameraReady(); 
    }  

    private Camera camera_ = null;
    private SurfaceHolder surfaceHolder_ = null;
    private SurfaceView	  surfaceView_;
    CameraReadyCallback cameraReadyCb_ = null;
 
    private List<Camera.Size> supportedSizes; 
    private Camera.Size procSize_;
    private boolean inProcessing_ = false;

    public CameraView(SurfaceView sv){
        surfaceView_ = sv;

        surfaceHolder_ = surfaceView_.getHolder();
        surfaceHolder_.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder_.addCallback(this); 
    }

    public List<Camera.Size> getSupportedPreviewSize() {
        return supportedSizes;
    }

    public int Width() {
        return procSize_.width;
    }

    public int Height() {
        return procSize_.height;
    }

    public void setCameraReadyCallback(CameraReadyCallback cb) {
        cameraReadyCb_ = cb;
    }

    public void StartPreview(){
        if ( camera_ == null)
            return;
        camera_.startPreview();
    }
    
    public void StopPreview(){
        if ( camera_ == null)
            return;
        camera_.stopPreview();
    }

    public void AutoFocus() {
        camera_.autoFocus(afcb);
    }

    public void Release() {
        if ( camera_ != null) {
            camera_.stopPreview();
            camera_.release();
            camera_ = null;
        }
    }
    
    public void setupCamera(int camid, PreviewCallback cb) {
    	if( Camera.getNumberOfCameras() <= 1)
    		return;
    	
    	if ( camera_ != null) {
            camera_.stopPreview();
            camera_.release();
            camera_ = null;
        }
    	
    	// 0, back camera; 1 for front camera
    	camera_ = Camera.open(getFrontOrBackCam(camid));
        procSize_ = camera_.new Size(0, 0);
        Camera.Parameters p = camera_.getParameters();        
       
        supportedSizes = p.getSupportedPreviewSizes();
        procSize_ = supportedSizes.get( supportedSizes.size()/2 );
        p.setPreviewSize(procSize_.width, procSize_.height);
        
        camera_.setParameters(p);
        camera_.setPreviewCallback(cb);
        //camera_.setDisplayOrientation(90);
        try {
            camera_.setPreviewDisplay(surfaceHolder_);
        } catch ( Exception ex) {
            ex.printStackTrace(); 
        }
        
        camera_.startPreview();
    }
    
    public void setupCamera(int wid, int hei, PreviewCallback cb) {
        procSize_.width = wid;
        procSize_.height = hei;
        
        Camera.Parameters p = camera_.getParameters();        
        p.setPreviewSize(procSize_.width, procSize_.height);
        camera_.setParameters(p);
        
        camera_.setPreviewCallback(cb);
    }

    private void setupCamera() {
    	// default open the backcam
        camera_ = Camera.open(getFrontOrBackCam(0));
        procSize_ = camera_.new Size(0, 0);
        Camera.Parameters p = camera_.getParameters();        
       
        supportedSizes = p.getSupportedPreviewSizes();
        procSize_ = supportedSizes.get( supportedSizes.size()/2 );
        p.setPreviewSize(procSize_.width, procSize_.height);
        
        camera_.setParameters(p);
        //camera_.setDisplayOrientation(90);
        try {
            camera_.setPreviewDisplay(surfaceHolder_);
        } catch ( Exception ex) {
            ex.printStackTrace(); 
        }
        camera_.startPreview();    
    }
    
    // code, 0 for backcam; 1 for frontcam
    private int getFrontOrBackCam(int code)
    {
    	 int camid = 0;
    	 int cameraCount = 0;  
    	   
    	 Camera.CameraInfo cameraInfo = new Camera.CameraInfo();  
    	 cameraCount = Camera.getNumberOfCameras(); // get cameras number
    	 if(cameraCount <= 1)
    		 return camid;
    	         
    	 for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {  
    	     Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo  
    	     // find a backcam
    	     if ( code == 0 ) {
    	    	 if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
    	    	 {
    	    		 camid = camIdx;
        	    	 break;
    	    	 }
    	     }
    	     // find a frontcam
    	     else
    	     {
    	    	 if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
    	    	 {
    	    		 camid = camIdx;
        	    	 break;
    	    	 }
    	     }
    	 }
    	 
    	 return camid;
    }
    
    private Camera.AutoFocusCallback afcb = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder sh, int format, int w, int h){
    }
    
	@Override
    public void surfaceCreated(SurfaceHolder sh){        
        setupCamera();        
        if ( cameraReadyCb_ != null)
            cameraReadyCb_.onCameraReady();
    }
    
	@Override
    public void surfaceDestroyed(SurfaceHolder sh){
        Release();
    }
}
