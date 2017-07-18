package sj.keyboard.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.keyboard.view.R;

import static android.Manifest.permission.RECORD_AUDIO;

public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {
	private static final int DISTANCE_Y_CANCEL = 50;
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCEL = 3;
	
	private int mCurState = STATE_NORMAL;
	private boolean isRecording = false;
	private DialogManager mDialogManager;
	private AudioManager mAudioManager;
	private float mTime;
	private boolean mReady;
	public AudioRecorderButton(Context context) {
		this(context, null);
	}

	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDialogManager = new DialogManager(context);
		String dir = Environment.getExternalStorageDirectory()+"/recorder_audios";
		mAudioManager = AudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListener(this);
		setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}
	
	public interface AudioFinishRecorderListener {
		void onPermission();
		void onFinish(float seconds, String filePath);
	}
	private AudioFinishRecorderListener mListener;
	public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
		mListener = listener;
	}
	private Runnable mGetVoiceLevelRunnable = new Runnable() {
		
		public void run() {
			while (isRecording) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGED = 0x111;
	private static final int MSG_DIALOG_DIMISS = 0x112;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:
				mDialogManager.showRecordingDialog();
				isRecording = true;
				new Thread(mGetVoiceLevelRunnable).start();
				break;
			case MSG_DIALOG_DIMISS:
				mDialogManager.dimissDialog();
				break;
			case MSG_VOICE_CHANGED:
				mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
				break;
			}
		};
	};
	
	public void wellPrepared() {
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			if (checkPermission(RECORD_AUDIO)){
				changeState(STATE_RECORDING);
			}else {
				mListener.onPermission();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isRecording ) {
				if (wantToCancel(x, y)) {
					changeState(STATE_WANT_TO_CANCEL);
				} else {
					changeState(STATE_RECORDING);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (!mReady) {
				reset();
				return super.onTouchEvent(event);
			}
			if (!isRecording || mTime < 0.6f) {
				mDialogManager.tooShort();
				mAudioManager.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
			} else if (mCurState == STATE_RECORDING) {
				mDialogManager.dimissDialog();
				mAudioManager.release();
				if (mListener != null) {
					mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
				}
			} else if (mCurState == STATE_WANT_TO_CANCEL) {
				mDialogManager.dimissDialog();
				mAudioManager.cancel();
			}
			reset();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void reset() {
		isRecording = false;
		mTime = 0;
		mReady = false;
		changeState(STATE_NORMAL);
	}

	private boolean wantToCancel(int x, int y) {
		if (x < 0 || x > getWidth()) return true;
		if (y < -DISTANCE_Y_CANCEL || y > getHeight()+DISTANCE_Y_CANCEL) 
			return true;
		return false;
	}

	private void changeState(int state) {
		if (mCurState != state) {
			mCurState = state;
			switch (state) {
			case STATE_NORMAL:
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.str_recorder_normal);
				break;
			case STATE_RECORDING:
				setBackgroundResource(R.drawable.btn_recorder_recording);
				setText(R.string.str_recorder_recording);
				if (isRecording) {
					mDialogManager.recording();
				}
				break;
			case STATE_WANT_TO_CANCEL:
				setBackgroundResource(R.drawable.btn_recorder_recording);
				setText(R.string.str_recorder_want_cancel);
				mDialogManager.wantToCancel();
				break;
			}
		}
	}


	/**
	 * 检查权限
	 *
	 * @param permission
	 * @return
	 */
	protected boolean checkPermission(String permission) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)// Permission was added in API Level 16
		{
			return ContextCompat.checkSelfPermission(getContext(), permission)
					== PackageManager.PERMISSION_GRANTED;
		}
		return true;
	}
	
}
