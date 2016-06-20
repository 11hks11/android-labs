package com.example.dictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OfflineQuery extends Activity implements OnClickListener,
		TextWatcher {
	public static final int MENU_ABOUT = 1;
	public static final int MENU_TUICHU = MENU_ABOUT + 2;
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/dictionary";
	private AutoCompleteTextView actvWord;
	private final String DATABASE_FILENAME = "dictionary.db";
	private SQLiteDatabase database;
	private Button btnSelectWord;
	private Button Clear;
	private TextView mTextView;
	private String result;
	private ArrayList<HashMap<String, String>> arrayList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_query);
		database = openDatabase();
		btnSelectWord = (Button) findViewById(R.id.ButtonGo);
		actvWord = (AutoCompleteTextView) findViewById(R.id.seek);
		Clear = (Button) findViewById(R.id.clearButton);
		btnSelectWord.setOnClickListener(this);
		actvWord.addTextChangedListener(this);

		Clear.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				actvWord.setText("");
			}
		});
	}

	public class DictionaryAdapter extends CursorAdapter {
		private LayoutInflater layoutInflater;

		public CharSequence convertToString(Cursor cursor) {
			return cursor == null ? "" : cursor.getString(cursor
					.getColumnIndex("_id"));
		}

		private void setView(View view, Cursor cursor) {
			TextView tvWordItem = (TextView) view;
			tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
			tvWordItem.setPadding(15, 10, 10, 15);
			tvWordItem.setTextSize(18);
		}

		public DictionaryAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			// TODO Auto-generated constructor stub
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			setView(view, cursor);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = new TextView(OfflineQuery.this);
			setView(view, cursor);
			return view;
		}

	}

	private SQLiteDatabase openDatabase() {
		try {
			// ���dictionary.db�ļ��ľ���·��
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			// ���/sdcard/dictionaryĿ¼�д��ڣ��������Ŀ¼
			if (!dir.exists())
				dir.mkdir();
			// �����/sdcard/dictionaryĿ¼�в�����
			// dictionary.db�ļ������res\rawĿ¼�и�������ļ���
			// SD����Ŀ¼��/sdcard/dictionary��
			if (!(new File(databaseFilename)).exists()) {
				// ��÷�װdictionary.db�ļ���InputStream����
				InputStream is = getResources().openRawResource(
						R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// ��ʼ����dictionary.db�ļ�
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			// ��/sdcard/dictionaryĿ¼�е�dictionary.db�ļ�
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		} catch (Exception e) {
		}
		return null;
	}

	public void afterTextChanged(Editable s) {
		// ���뽫english�ֶεı�����Ϊ_id
		Cursor cursor = database.rawQuery(
				"select english as _id from t_words where english like ?",
				new String[] { s.toString() + "%" });
		DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
				cursor, true);

		actvWord.setAdapter(dictionaryAdapter);
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void onClick(View view) {

		// TODO Auto-generated method stub
		String sql = "select chinese from t_words where english=?";
		Cursor cursor = database.rawQuery(sql, new String[] { actvWord
				.getText().toString() });
		result = "δ�ҵ��õ���.";
		// ������ҵ��ʣ���ʾ�����ĵ���˼
		if (cursor.getCount() > 0) {
			// ����ʹ��moveToFirst��������¼ָ���ƶ�����1����¼��λ��
			cursor.moveToFirst();
			result = cursor.getString(cursor.getColumnIndex("chinese"));
		}
		if (actvWord.getText().toString().equals("")) {
			Toast.makeText(OfflineQuery.this, "��ѯ���ݲ���Ϊ�գ�", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// ��ʾ��ѯ����Ի���
		new AlertDialog.Builder(this)
				.setIcon(R.drawable.dialog_icon)
				.setTitle("��ѯ���")
				.setMessage(result)
				.setPositiveButton("�������ʱ�",
						new DialogInterface.OnClickListener() {

							@SuppressWarnings("unchecked")
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = getIntent();
								if (!result.equals("δ�ҵ��õ���.")) {
									String word = actvWord.getText().toString()
											.trim();
									String explain = result;
									arrayList = (ArrayList<HashMap<String, String>>) new ObjectFile()
											.readObjectFile();
									if (null == arrayList) {
										arrayList = new ArrayList<HashMap<String, String>>();
									}
									if (!TextUtils.isEmpty(word)
											&& !TextUtils.isEmpty(result)) {
										HashMap<String, String> map = new HashMap<String, String>();
										map.put("word", word);
										map.put("explain", result);
										arrayList.add(map);
										new ObjectFile()
												.writeObjectFile(arrayList);
									}
								}
							}

						}).setNegativeButton("�ر�", null).show();
		SimpleAdapter adapter = new SimpleAdapter(this, arrayList,
				android.R.layout.simple_list_item_2, new String[] { "word",
						"explain" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
	}

	// ��������
	private static final String APPID = "appid=4f2d3a06";
	private String text = "";

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
				actvWord.setText(text);
			}

		}
	};

	@Override
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
