package com.myvirtualos.lite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
    private static final int PICK_APK = 41;
    private final int bg = Color.rgb(12, 17, 31), panel = Color.rgb(25, 33, 52);
    private final int accent = Color.rgb(104, 132, 255), muted = Color.rgb(163, 174, 198);
    private LinearLayout body, nav;
    private String page = "Home";
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("myvirtualos", MODE_PRIVATE);
        getWindow().setStatusBarColor(bg); getWindow().setNavigationBarColor(bg);
        LinearLayout root = new LinearLayout(this); root.setOrientation(1); root.setBackgroundColor(bg);
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL); top.setPadding(dp(20),dp(18),dp(20),dp(14));
        top.setOrientation(1);
        TextView brand = text("MY VIRTUAL OS", 19, Color.WHITE, true); top.addView(brand);
        TextView sub = text("LITE  •  VIRTUAL DEVICE MANAGER", 10, muted, true); top.addView(sub);
        root.addView(top);
        ScrollView scroll = new ScrollView(this); body = new LinearLayout(this); body.setPadding(dp(18),dp(10),dp(18),dp(24)); body.setOrientation(1);
        scroll.addView(body); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        nav = new LinearLayout(this); nav.setPadding(dp(5),dp(6),dp(5),dp(6)); nav.setBackgroundColor(panel);
        String[] tabs={"Home","Apps","Instances","Settings"};
        for(String tab:tabs){ TextView t=text(tab,11,Color.WHITE,true); t.setGravity(Gravity.CENTER); t.setPadding(2,dp(13),2,dp(13));
            t.setOnClickListener(v->show(tab)); nav.addView(t,new LinearLayout.LayoutParams(0,-2,1)); }
        root.addView(nav); setContentView(root); show("Home");
    }
    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    private TextView text(String s,int size,int color,boolean bold){ TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    private GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private void show(String p){page=p;body.removeAllViews();
        if(p.equals("Home")) home(); else if(p.equals("Apps")) apps(); else if(p.equals("Instances")) instances(); else settings();
    }
    private void heading(String title,String detail){TextView h=text(title,27,Color.WHITE,true);h.setPadding(0,dp(8),0,dp(5));body.addView(h);TextView d=text(detail,13,muted,false);d.setPadding(0,0,0,dp(18));body.addView(d);}
    private void card(String title,String detail){LinearLayout c=new LinearLayout(this);c.setOrientation(1);c.setPadding(dp(16),dp(15),dp(16),dp(15));c.setBackground(shape(panel,18));
        TextView h=text(title,16,Color.WHITE,true);TextView d=text(detail,13,muted,false);d.setPadding(0,dp(7),0,0);c.addView(h);c.addView(d);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(12);body.addView(c,lp);}
    private void button(String label,Runnable action){TextView b=text(label,14,Color.WHITE,true);b.setGravity(Gravity.CENTER);b.setPadding(dp(14),dp(15),dp(14),dp(15));b.setBackground(shape(accent,14));b.setOnClickListener(v->action.run());LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(10);body.addView(b,lp);}
    private void home(){heading("Your workspace","A cleaner control center for Android environments.");
        card("DEVICE MANAGER","Manage launchable apps and local instance profiles from one place.");
        card("VM ENGINE","Not installed • This build does not boot a separate Android guest yet.");
        button("Manage instances",()->show("Instances"));button("Browse installed apps",()->show("Apps"));
        card("CURRENT BUILD","MyVirtualOS Lite 2.0 • Offline-first interface • No account required");
    }
    private void apps(){heading("Apps","Launch apps already installed on this Android phone.");
        button("Refresh app list",()->show("Apps"));
        PackageManager pm=getPackageManager();List<ApplicationInfo> list=pm.getInstalledApplications(PackageManager.GET_META_DATA);
        int count=0;
        for(ApplicationInfo ai:list){Intent launch=pm.getLaunchIntentForPackage(ai.packageName);if(launch==null)continue;count++;
            LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.setPadding(dp(12),dp(10),dp(12),dp(10));row.setBackground(shape(panel,14));
            TextView name=text(pm.getApplicationLabel(ai).toString(),14,Color.WHITE,true);row.addView(name,new LinearLayout.LayoutParams(0,-2,1));
            TextView open=text("OPEN",11,accent,true);row.addView(open);row.setOnClickListener(v->{try{startActivity(launch);}catch(Exception e){Toast.makeText(this,"Unable to open app",Toast.LENGTH_SHORT).show();}});
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(7);body.addView(row,lp);
        }
        if(count==0)card("No launchable apps","No apps with a launcher icon were found.");
    }
    private Set<String> getInstances(){return new HashSet<>(prefs.getStringSet("instances",new HashSet<>()));}
    private void saveInstances(Set<String> s){prefs.edit().putStringSet("instances",new HashSet<>(s)).apply();}
    private void instances(){heading("Instances","Create and manage local profiles. These are profiles, not bootable VMs.");
        button("+ Create instance",()->{EditText input=new EditText(this);input.setSingleLine(true);input.setHint("e.g. Android Test");new AlertDialog.Builder(this).setTitle("New profile").setView(input).setNegativeButton("Cancel",null).setPositiveButton("Create",(d,w)->{String n=input.getText().toString().trim();if(n.isEmpty())n="Android Profile";Set<String>s=getInstances();s.add(n);saveInstances(s);show("Instances");}).show();});
        Set<String> all=getInstances();if(all.isEmpty())card("No profiles yet","Tap Create instance to add your first local profile.");
        for(String name:all){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.setPadding(dp(14),dp(14),dp(14),dp(14));row.setBackground(shape(panel,14));
            row.addView(text(name,15,Color.WHITE,true),new LinearLayout.LayoutParams(0,-2,1));TextView del=text("DELETE",11,Color.rgb(255,125,135),true);row.addView(del);del.setOnClickListener(v->{Set<String>s=getInstances();s.remove(name);saveInstances(s);show("Instances");});
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(8);body.addView(row,lp);}
        button("Check VM engine status",()->new AlertDialog.Builder(this).setTitle("VM engine unavailable").setMessage("This app currently manages local profiles only. A compatible Android virtualization backend and supported device are required before a guest Android system can boot.").setPositiveButton("OK",null).show());
    }
    private void settings(){heading("Settings","App preferences and diagnostics.");
        card("VERSION","MyVirtualOS Lite 2.0");
        card("PRIVACY","Profiles are stored locally on this device. This app does not upload your app list.");
        button("Import APK file",()->{Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("application/vnd.android.package-archive");i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,PICK_APK);});
        button("Clear local profiles",()->new AlertDialog.Builder(this).setTitle("Clear profiles?").setMessage("This removes saved profile names from this app.").setNegativeButton("Cancel",null).setPositiveButton("Clear",(d,w)->{prefs.edit().remove("instances").apply();show("Settings");}).show());
        card("IMPORTANT","Import currently selects an APK file only; it does not install it into a virtual Android system. Guest Android execution is not yet implemented.");
    }
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(request==PICK_APK&&result==RESULT_OK&&data!=null){Uri uri=data.getData();String name=uri==null?"APK selected":uri.getLastPathSegment();new AlertDialog.Builder(this).setTitle("APK selected").setMessage("File selected: "+name+"\n\nThe VM engine is not available yet, so this file has not been installed.").setPositiveButton("OK",null).show();}}
}
