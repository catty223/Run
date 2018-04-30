package com.mdzz.run;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by HYH on 2018/4/18.
 */

public class Main extends AppCompatActivity {
    private PackageManager pm;
    private ImageView paypic;
    private TextView pay;
    private TextView updatelog;
    private LinearLayout line1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        paypic = (ImageView) findViewById(R.id.paypic);
        pay = (TextView) findViewById(R.id.paytext);
        updatelog = (TextView) findViewById(R.id.updatelog);
        line1 = (LinearLayout) findViewById(R.id.line);
        line1.requestFocus();
        pm = getPackageManager();

        paypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypic.setImageDrawable(null);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypic.setImageDrawable(getResources().getDrawable(R.drawable.pay));
            }
        });
        updatelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setTitle("这是基本没人看的更新日志");
                builder.setMessage(getString(R.string.updatelogcontent));
                builder.setPositiveButton("确定", null);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (isEnable()) {
            menu.findItem(R.id.menu).setTitle("隐藏图标");
        }else{
            menu.findItem(R.id.menu).setTitle("显示图标");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("显示图标")) {
            item.setTitle("隐藏图标");
        } else {
            item.setTitle("显示图标");
        }
        pm.setComponentEnabledSetting(getCN(), isEnable() ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (paypic.isFocusableInTouchMode()) {
            paypic.setImageDrawable(null);
        } else {
            super.onBackPressed();
        }
    }

    private ComponentName getCN(){
        return new ComponentName(this, "com.mdzz.run.MainAlias");
    }
    private boolean isEnable()
    {
        if (pm.getComponentEnabledSetting(getCN()) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
            return false;
        else
            return true;
    }
}
