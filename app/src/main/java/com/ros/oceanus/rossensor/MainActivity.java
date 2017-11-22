package com.ros.oceanus.rossensor;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.hardware.camera2.*;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import org.ros.address.InetAddressFactory;
import org.ros.android.OrientationPublisher;
import org.ros.android.RosActivity;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;


public class MainActivity extends RosActivity {

    private OrientationPublisher orientationPublisher;
    private TextView mTextView;

    public MainActivity() {
        super("ROSSensor", "ROSSensor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.ip_address);
        orientationPublisher = new OrientationPublisher((SensorManager)getSystemService(SENSOR_SERVICE));
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        try {
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(
                    local_network_address.getHostAddress(), getMasterUri());
            nodeMainExecutor.execute(orientationPublisher, nodeConfiguration);
        } catch (IOException e) {
            Log.e("Camera Tutorial", "socket error trying to get networking information from the master uri");
        }
    }
}
