package com.fileencryptor;

import com.fileencryptor.SplashScreenActivity;
import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	public final int REQ_CD_CHOOSE_FILE = 101;
	
	private ArrayList<String> listString = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> updatemap = new ArrayList<>();
	
	private LinearLayout linear_main;
	private LinearLayout linear_toolbar;
	private LinearLayout linear_path;
	private LinearLayout linear_password;
	private LinearLayout linear_choosefile;
	private LinearLayout linear_encrypt;
	private LinearLayout linear_decrypt;
	private TextView dev_by_abnsafita;
	private LinearLayout linear_protect_toolbar;
	private ImageView imageview_exit;
	private TextView ToolBar_AES;
	private LinearLayout linear_protect_filepath;
	private HorizontalScrollView hscroll_path;
	private EditText b01_filepath;
	private LinearLayout linear_protect_password;
	private HorizontalScrollView hscroll_password;
	private EditText b01_password;
	private LinearLayout linear_protect_choosefile;
	private Button button_choosefile;
	private LinearLayout linear_protect_encrypt;
	private Button button_encrypt;
	private LinearLayout linear_protect_decrypt;
	private Button button_decrypt;
	
	private Intent CHOOSE_FILE = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog.Builder B01_Dialog;
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			} else {
				initializeLogic();
			}
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear_main = findViewById(R.id.linear_main);
		linear_toolbar = findViewById(R.id.linear_toolbar);
		linear_path = findViewById(R.id.linear_path);
		linear_password = findViewById(R.id.linear_password);
		linear_choosefile = findViewById(R.id.linear_choosefile);
		linear_encrypt = findViewById(R.id.linear_encrypt);
		linear_decrypt = findViewById(R.id.linear_decrypt);
		dev_by_abnsafita = findViewById(R.id.dev_by_abnsafita);
		linear_protect_toolbar = findViewById(R.id.linear_protect_toolbar);
		imageview_exit = findViewById(R.id.imageview_exit);
		ToolBar_AES = findViewById(R.id.ToolBar_AES);
		linear_protect_filepath = findViewById(R.id.linear_protect_filepath);
		hscroll_path = findViewById(R.id.hscroll_path);
		b01_filepath = findViewById(R.id.b01_filepath);
		linear_protect_password = findViewById(R.id.linear_protect_password);
		hscroll_password = findViewById(R.id.hscroll_password);
		b01_password = findViewById(R.id.b01_password);
		linear_protect_choosefile = findViewById(R.id.linear_protect_choosefile);
		button_choosefile = findViewById(R.id.button_choosefile);
		linear_protect_encrypt = findViewById(R.id.linear_protect_encrypt);
		button_encrypt = findViewById(R.id.button_encrypt);
		linear_protect_decrypt = findViewById(R.id.linear_protect_decrypt);
		button_decrypt = findViewById(R.id.button_decrypt);
		CHOOSE_FILE.setType("*/*");
		CHOOSE_FILE.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		B01_Dialog = new AlertDialog.Builder(this);
		
		imageview_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_MaterialDialog("شرح التطبيق", "يمكنك هذا التطبيق من حماية ملفاتك وتشفيرها بحيث لايمكن الوصول اليها او فتحها ابدا الى عن طريق فك تشفيرها بكلمة المرور التي قمت بتعيينها", "Exit", "Ok");
			}
		});
		
		button_choosefile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(CHOOSE_FILE, REQ_CD_CHOOSE_FILE);
			}
		});
		
		button_encrypt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if ("".equals(b01_password.getText().toString())) {
					((EditText)b01_password).setError("ادخل كلمة السر");
				}
				else {
					if (30 > b01_filepath.getText().toString().length()) {
						ApplicationUtil.showMessage(getApplicationContext(), "يرجى اختيار ملف");
					}
					else {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/AES-256/Encrypted/"));
						_AES256FileEncryption(b01_filepath.getText().toString(), FileUtil.getExternalStorageDir().concat("/AES-256/Encrypted/".concat(Uri.parse(b01_filepath.getText().toString()).getLastPathSegment())), b01_password.getText().toString());
						ApplicationUtil.showMessage(getApplicationContext(), FileUtil.getExternalStorageDir().concat("/AES-256/Encrypted/".concat(Uri.parse(b01_filepath.getText().toString()).getLastPathSegment())));
					}
				}
			}
		});
		
		button_decrypt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if ("".equals(b01_password.getText().toString())) {
					((EditText)b01_password).setError("ادخل كلمة السر");
				}
				else {
					if (30 > b01_filepath.getText().toString().length()) {
						ApplicationUtil.showMessage(getApplicationContext(), "يرجى اختيار ملف");
					}
					else {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/AES-256/Decrypted/"));
						_AES256FileDecryption(b01_filepath.getText().toString(), FileUtil.getExternalStorageDir().concat("/AES-256/Decrypted/".concat(Uri.parse(b01_filepath.getText().toString()).getLastPathSegment())), b01_password.getText().toString());
						ApplicationUtil.showMessage(getApplicationContext(), FileUtil.getExternalStorageDir().concat("/AES-256/Decrypted/".concat(Uri.parse(b01_filepath.getText().toString()).getLastPathSegment())));
					}
				}
			}
		});
	}
	
	private void initializeLogic() {
		
		_Gradient_Text(dev_by_abnsafita, "برمجة وتطوير Ali");
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_CHOOSE_FILE:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				b01_filepath.setText(_filePath.get((int)(0)));
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		B01_Dialog.setTitle("اغلاق التطبيق");
		B01_Dialog.setIcon(R.drawable.exit_b01);
		B01_Dialog.setMessage("هل تريد الخروج من التطبيق؟");
		B01_Dialog.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
						ApplicationUtil.CustomToastWithIcon(getApplicationContext(), "Exit !!!", 0xFF00FF00, 20, 0x33000000, 15, ApplicationUtil.BOTTOM, R.drawable.exit_b01);
						finishAffinity();
				}
		});
		B01_Dialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
						ApplicationUtil.CustomToastWithIcon(getApplicationContext(), "Cancel !!!", 0xFFFF0000, 20, 0x33000000, 15, ApplicationUtil.BOTTOM, R.drawable.cancel_b01);
				}
		});
		B01_Dialog.create().show();
	}
	public void _AES256FileEncryption(final String _input, final String _output, final String _password) {
		FileEncryption.encryptFile(_input, _output, _password);
	}
	
	
	public void _AES256FileDecryption(final String _input, final String _output, final String _password) {
		FileEncryption.decryptFile(_input, _output, _password);
	}
	
	
	public void _MaterialDialog(final String _title, final String _message, final String _button1text, final String _button2text) {
		final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this).create();
		View inflate = getLayoutInflater().inflate(R.layout.dialog,null); 
		dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog1.setView(inflate);
		TextView t1 = (TextView) inflate.findViewById(R.id.t1);
		
		TextView t2 = (TextView) inflate.findViewById(R.id.t2);
		
		TextView b1 = (TextView) inflate.findViewById(R.id.b1);
		
		TextView b2 = (TextView) inflate.findViewById(R.id.b2);
		
		LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);
		
		LinearLayout linear3 = (LinearLayout) inflate.findViewById(R.id.linear3);
		t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/font.ttf"), 0);
		b1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/tajawal_medium.ttf"), 0);
		b2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/tajawal_medium.ttf"), 0);
		t1.setText(_title);
		t2.setText(_message);
		b1.setText(_button1text);
		b2.setText(_button2text);
		if (_button1text.equals("HIDE")) {
			b1.setVisibility(View.GONE);
		}
		
		
		
		b1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				dialog1.dismiss();
				if (_button2text.equals("موافق")) {
					
				}
			}
		});
		b2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				dialog1.dismiss();
				if (_button1text.equals("خروج")) {
					
				}
				else {
					
				}
				finishAffinity();
			}
		});
		dialog1.setCancelable(false);
		dialog1.show();
	}
	
	
	public void _Gradient_Text(final TextView _view, final String _msg) {
		_view.setText(_msg);
		
		TextPaint paint = _view.getPaint();
		
		float width = paint.measureText(_msg); 
		
		Shader textShader = new LinearGradient(0, 0, width,_view.getTextSize(), new int[]{ Color.parseColor("#F97C3C"), Color.parseColor("#FDB54E"), Color.parseColor("#64B678"), Color.parseColor("#478AEA"), Color.parseColor("#8446CC"), }, null, Shader.TileMode.CLAMP); 
		
		/* Programmed by Ali To request help, contact me on Telegram: @ali_r_1997 */
		
		_view.getPaint().setShader(textShader);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
