package ulyne.com.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    ServiceAct.MyBinder myBinder ;
    AidlService aidlService;
    /**
     * 连接服务(普通服务)
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (ServiceAct.MyBinder) iBinder;
            myBinder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * 连接服务(远程服务)
     */
    private ServiceConnection remoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlService = AidlService.Stub.asInterface(iBinder);
            try
            {
                int result = aidlService.plus(3, 5);
                String upperStr = aidlService.toUpperCase("hello world");
                Log.d("TAG", "result is " + result);
                Log.d("TAG", "upperStr is " + upperStr);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService();
        /**
         * 开启远程服务暂时禁止绑定服务，因为不在同一个进程
         */
        //bindService();
        Log.d("线程ID", "MainActivity thread id is " + Thread.currentThread().getId());
    }

    /**
     * 启动service
     */
    void startService()
    {
        Intent startService = new Intent(this, ServiceAct.class);
        startService(startService);
    }

    /**
     * 停止service
     */
    void stopService()
    {
        Intent stopService = new Intent(this, ServiceAct.class);
        stopService(stopService);
    }

    /**
     * 绑定service
     */
    void bindService()
    {
        Intent bindIntent = new Intent(this, ServiceAct.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 解绑service
     */
    void unBindService()
    {
        unbindService(serviceConnection);
    }
}
