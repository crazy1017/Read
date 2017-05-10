package com.mzp.libreads.read;

import static java.lang.String.format;

import java.io.File;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.ScrollBar;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.mzp.libreads.R;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ReadingActivity extends AppCompatActivity {

	
	private String pdfName = "demo.pdf";
//	Environment.getExternalStorageDirectory().getAbsolutePath()
	private File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/testf.pdf");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading);
		PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
		ScrollBar scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
		
		pdfView.setScrollBar(scrollBar);
		
		pdfView.fromFile(file)
		.defaultPage(1)
		.showMinimap(false)
		.enableSwipe(true).onLoad(new OnLoadCompleteListener() {
			@Override
			public void loadComplete(int nbPages) {
				Toast.makeText(ReadingActivity.this, "loadComplete "+nbPages,Toast.LENGTH_SHORT).show();
			}
		})
		.onPageChange(new OnPageChangeListener() {
			@Override
			public void onPageChanged(int page, int pageCount) {
				setTitle(format("%s %s / %s", pdfName, page, pageCount));
			}
		})
		.load();
	}
}
