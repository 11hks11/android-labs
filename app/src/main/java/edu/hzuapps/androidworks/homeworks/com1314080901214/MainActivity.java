package com.example.dictionary;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �������ر���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		/*
		 * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		// ��ʾ�Ĳ�����dier
		setContentView(R.layout.dier);
		// ȡ��TabHost����
		mTabHost = getTabHost();
		// ����OfflineQuery����intent���Ա����ӵ����ҳ����
		Intent intent = new Intent().setClass(this, OfflineQuery.class);
		// �½�һ��newTabSpec(newTabSpec)
		// �������ǩ��ͼ��(setIndicator)
		// ��������(setContent)
		mTabHost.addTab(mTabHost.newTabSpec("tab1")
				.setIndicator("", getResources().getDrawable(R.drawable.img1))
				.setContent(intent));
		Intent intent1 = new Intent().setClass(this, OnlineQuery.class);
		mTabHost.addTab(mTabHost.newTabSpec("tab2")
				.setIndicator("", getResources().getDrawable(R.drawable.img2))
				.setContent(intent1));
		Intent intent2 = new Intent().setClass(this, NoteBook.class);
		mTabHost.addTab(mTabHost.newTabSpec("tab3")
				.setIndicator("", getResources().getDrawable(R.drawable.img3))
				.setContent(intent2));
		mTabHost.setCurrentTab(0);

	}
}
