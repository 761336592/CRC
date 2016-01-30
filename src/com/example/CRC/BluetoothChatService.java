package com.example.CRC;

import java.io.IOException;




import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 这类做所有的工作，建立和管理蓝牙 连接其他装置。它有一个线程，监听 传入连接，螺纹连接装置，和一个 执行数据传输线连接时。
 */
public class BluetoothChatService {
	// 调试
	
    // Debugging
    private static final String TAG = "BluetoothComm";
    private static final boolean D = true;
	private static final String NAME= "BluetoothComm";
	//SPP协议UUID
	private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// Member fields
    private final BluetoothAdapter mAdapter;
    private Handler mHandler;
    private int mState;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       //do nothing
    public static final int STATE_LISTEN = 1;     //监听连接
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  //已连接上远程设备
    private String BTName="";
    public BluetoothChatService(Context context, Handler handler){
    	mAdapter = BluetoothAdapter.getDefaultAdapter();
    	//mHandler = handler;
    	mState = STATE_NONE;
    }
    
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state,String str) {
        if (D) Log.d(TAG,BTName+"状态 " + mState + " -> " + state);
        mState = state;
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE,state,1);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.DEVICE_NAME,BTName);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
      //  setState(STATE_CONNECTED,BTName);
    }
    
    public void setHandler(Handler handler){
    	
    	mHandler=handler;
    }
    /**
     * Return the current connection state. 
     */
    public synchronized int getState() {
        return mState;
    }
    
    /**
     * Start the service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     * 开启监听线程
     */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        // Start the thread to listen on a BluetoothServerSocket
      /*  if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();//开启监听线程
        }*/
        setState(STATE_LISTEN,BTName);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {//正在连接状态
            if (mConnectThread != null) {//有ConnectThread，结束
            	mConnectThread.cancel(); 
            	mConnectThread = null;
            }
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {//有处于连接中的线程，结束
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        BTName=device.getName();
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();//开启新的连接请求线程
        setState(STATE_CONNECTING,BTName);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }

     /*   // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
        	mAcceptThread.cancel(); 
        	mAcceptThread = null;
        }*/

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();//和客户端开始通信

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        BTName=device.getName();
        bundle.putString(MainActivity.DEVICE_NAME,BTName);
      //  BTName=device.getName();
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_CONNECTED,BTName);  //已经连接了设备
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        if (mConnectThread != null) {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
       /* if (mAcceptThread != null) {
        	mAcceptThread.cancel(); 
        	mAcceptThread = null;
        }*/
        setState(STATE_NONE,BTName);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;//得到连接线程
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_LISTEN,BTName);

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Unable to connect device");
        bundle.putString("NAME", BTName);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_LISTEN,BTName);

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }
    
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;//远程设备

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
            	//获得一个BluetoothSocket对象
                tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            //取消搜索
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                BluetoothChatService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }

            // Start the connected thread，已连接上，管理连接
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private BufferedReader mBufferedReadersever ;
        private PrintWriter mPrintWritersever ; 
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            try {
				mBufferedReadersever=new BufferedReader(new InputStreamReader(mmInStream,"GBK"));
				mPrintWritersever=new PrintWriter(new OutputStreamWriter(mmOutStream));
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
     //       byte[] buffer = new byte[1024];
            StringBuffer buffer=new StringBuffer();
            int bytes;
            // Keep listening to the InputStream while connected
            while(true){
                try{
                	/*try{
						ConnectedThread.sleep(20);
					}catch (InterruptedException e) {
						e.printStackTrace();
					}*/
                	
                    buffer.append(mBufferedReadersever.readLine())	;
                 //   bytes = mmInStream.read(buffer);
                    mHandler.obtainMessage(MainActivity.MESSAGE_READ, buffer.toString().length(), -1, buffer.toString()).sendToTarget();
                    buffer=new StringBuffer();
                    /*try{
						ConnectedThread.sleep(100);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}*/
                }catch(IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e){
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try{
                mmSocket.close();
            } catch (IOException e){
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
