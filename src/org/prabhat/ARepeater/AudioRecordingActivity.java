package org.prabhat.ARepeater;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class AudioRecordingActivity extends Activity {
	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

	private MediaRecorder recorder = null;
	private int currentFormat = 0;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
			MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
			AUDIO_RECORDER_FILE_EXT_3GP };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setButtonHandlers();
		enableButtons(false);
	}

	private void setButtonHandlers() {
		((Button) findViewById(R.id.btnStartRecording)).setOnClickListener(btnClick);
		((Button) findViewById(R.id.btnStopRecording)).setOnClickListener(btnClick);
		((Button) findViewById(R.id.btnPlay)).setOnClickListener(btnClick);
		((Button) findViewById(R.id.btnStopPlay)).setOnClickListener(btnClick);
	}
	
	private void enableButton(int id, boolean isEnable) {
		((Button) findViewById(id)).setEnabled(isEnable);
	}

	private void enableButtons(boolean isRecording) {
		enableButton(R.id.btnStartRecording, !isRecording);
		enableButton(R.id.btnStopRecording, isRecording);
		enableButton(R.id.btnStopPlay, isRecording);
	}
	private void enablePlayerButtons(boolean isEnable)
	{
		((Button) findViewById(R.id.btnPlay)).setEnabled(isEnable);
		((Button) findViewById(R.id.btnStopPlay)).setEnabled(isEnable);
	}
	private void enableRecordPlayButtons(boolean isEnable)
	{
		((Button) findViewById(R.id.btnPlay)).setEnabled(isEnable);
		((Button) findViewById(R.id.btnStartRecording)).setEnabled(isEnable);
		((Button) findViewById(R.id.btnStopPlay)).setEnabled(!isEnable);
	}


	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);

		if (!file.exists()) {
			file.mkdirs();
		}

		return (file.getAbsolutePath() + "/" + "tobeRepeated" + file_exts[currentFormat]);	
	}

	private void startRecording() {
		recorder = new MediaRecorder();

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(output_formats[currentFormat]);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(getFilename());

		recorder.setOnErrorListener(errorListener);
		recorder.setOnInfoListener(infoListener);

		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void stopRecording() {
		if (null != recorder) {
			recorder.stop();
			recorder.reset();
			recorder.release();

			recorder = null;
		}
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(AudioRecordingActivity.this,
					"Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(AudioRecordingActivity.this,
					"Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
					.show();
		}
	};

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnStartRecording: {
/*				Toast.makeText(AudioRecordingActivity.this, "Start Recording",
						Toast.LENGTH_SHORT).show();*/

				enableButtons(true);
				enablePlayerButtons(false);
				startRecording();
				break;
			}
			case R.id.btnStopRecording: {
/*				Toast.makeText(AudioRecordingActivity.this, "Stop Recording",
						Toast.LENGTH_SHORT).show();*/
				enableButtons(false);
				enablePlayerButtons(true);
				((Button) findViewById(R.id.btnStopPlay)).setEnabled(false);
				stopRecording();

				break;
			}
			case R.id.btnPlay: {
				enableRecordPlayButtons(false);
				startPlaying();
				break;
			}
			case R.id.btnStopPlay: {
				enableRecordPlayButtons(true);
				stopPlaying();
				break;
			}
			}
		}
	};	
	
	MediaPlayer mp; 
	
	public void startPlaying()
	{
	    //set up MediaPlayer    
	    mp=new MediaPlayer();
	    mp.setLooping(true);

	    try {
	        mp.setDataSource(getFilename());
	        mp.prepare();
	        mp.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void stopPlaying()
	{
		mp.stop();
		mp.release();
	}
}