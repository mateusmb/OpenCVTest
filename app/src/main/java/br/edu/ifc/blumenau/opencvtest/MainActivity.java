package br.edu.ifc.blumenau.opencvtest;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2{

    private static final String TAG = "MYAPP::OPENCV";

    private CameraBridgeViewBase camera_view;
    private boolean isCamera = true;
    private MenuItem itemSwitchCamera = null;

    BaseLoaderCallback bl_callback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    camera_view.enableView();

                }break;
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        camera_view = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCVView);
        camera_view.setVisibility(SurfaceView.VISIBLE);
        camera_view.setCvCameraViewListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera_view != null) {
            camera_view.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera_view != null) {
            camera_view.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, bl_callback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            bl_callback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat capture = inputFrame.gray();
        Mat canny_edges = new Mat();

        Imgproc.Canny(capture, canny_edges, 10, 100);

        return canny_edges;
//        return inputFrame.rgba();
    }
}
