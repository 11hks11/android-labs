package com.example.dictionary;

import java.util.ArrayList;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class OnlineQuery extends Activity{
	private Button ButtonGo;
	private AutoCompleteTextView seek;
	private WebView myWebView1;
	private Button clearButton;
	public static final int MENU_ABOUT = 1;
	public static final int MENU_TUICHU = MENU_ABOUT + 2;

public void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.online_query);
	ButtonGo = (Button)findViewById(R.id.ButtonGo);
	seek = (AutoCompleteTextView) findViewById(R.id.seek);
	clearButton = (Button)findViewById(R.id.clearButton);
	myWebView1 = (WebView)findViewById(R.id.myWebView1);
	
	ButtonGo.setOnClickListener(new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			String strURI = (seek.getText().toString());
			strURI = strURI.trim();
			if(strURI.length()==0)
			{
				Toast.makeText(OnlineQuery.this, "��ѯ���ݲ���Ϊ�գ�", Toast.LENGTH_LONG).show();
			}
			else 
			{
				String strURL = "http://dict.youdao.com/m/search?keyfrom=dict.mindex&q="+strURI;
				myWebView1.loadUrl(strURL);
			}
		}
		
	});
	http://dict.youdao.com/m/serch?keyfrom=dict.mindex&vendor=%24vendor&q=happy
	clearButton.setOnClickListener(new Button.OnClickListener()
			{
		public void onClick(View v){
			seek.setText("");
		}
			});
}
//��������
	private static final String APPID = "appid=4f2d3a06";
	private String text=""; 
	public void say(View view) {
		RecognizerDialog isrDialog = new RecognizerDialog(this, APPID);

		/*
		 * ��������Ŀǰ֧������ ��sms������ͨ�ı�תд ��poi������������ ��vsearch�����ȴ����� ��video������Ƶ��������
		 * ��asr���������ʶ��
		 */
		isrDialog.setEngine("sms", null, null);
		isrDialog.setListener(recoListener);
		isrDialog.show();

	}
	// ����ʶ�������������������
	RecognizerDialogListener recoListener = new RecognizerDialogListener() {

		@Override
		public void onResults(ArrayList<RecognizerResult> results,
				boolean isLast) {
			// ������ʶ����ɺ�᷵�ؼ��ϣ����������ֻ�õ���ƥ�����һ��
			text = results.get(0).text;
			System.out.println(text);
		}

		@Override
		public void onEnd(SpeechError error) {
			if (error == null) {
				// ��ɺ�Ͱѽ����ʾ��EditText��
				seek.setText(text);
			}

		}
  };
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, MENU_ABOUT, 0, "˵��");
		menu.add(0, MENU_TUICHU, 1, "�˳�");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ABOUT: {
			Intent intent = new Intent();
			intent.setClass(this, About.class);
			startActivity(intent);
			break;
			}
		case MENU_TUICHU: {
			finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
