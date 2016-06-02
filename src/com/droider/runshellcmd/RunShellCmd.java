package com.droider.runshellcmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class RunShellCmd extends Activity {

	private EditText et_cmd;
	private TextView tv_runResult;
	ScrollView sv_result;
	private Button bt_runCmd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run_shell_cmd);
		et_cmd = (EditText) findViewById(R.id.et_cmd);
		tv_runResult = (TextView) findViewById(R.id.tv_runResult);
		tv_runResult.setText(tv_runResult.getText(), TextView.BufferType.EDITABLE);
		sv_result = (ScrollView) this.findViewById(R.id.sv_result);
		bt_runCmd = (Button) findViewById(R.id.btn_run_cmd);
		bt_runCmd.setVisibility(View.VISIBLE);
		bt_runCmd.setOnClickListener(new runCmdBtnListener());
	}

	private final class runCmdBtnListener implements View.OnClickListener {
		public void onClick(View view) {
			switch(view.getId()) {
			case R.id.btn_run_cmd:
				String cmd = et_cmd.getText().toString();
				cmd = cmd.trim();
				if(cmd.length() == 0) {
					Log.e(getPackageName(), "Command is none!");
					Toast toast=Toast.makeText(getApplicationContext(), R.string.cmd_none, Toast.LENGTH_SHORT);
					toast.show();
					break;
				}
				Log.d(getPackageName(), "cmd: " + cmd);
				List<String> results = runCmd(cmd);
				String result = "";
				for(String line : results) {
					result += line + "\n";
				}
				tv_runResult.setText(result);
				sv_result.scrollTo(0, tv_runResult.getBottom());
				break;
			default:
				break;
			}
		}
	}

	private List<String> runCmd(String cmd) {
		Process process = null;
		List<String> list = new ArrayList<String>();
		try {
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(cmd);
			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
