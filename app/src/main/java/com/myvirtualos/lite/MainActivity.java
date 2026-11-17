package com.myvirtualos.lite;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.*;
import android.view.View;

public class MainActivity extends Activity {
    private LinearLayout content;
    private final int dark = Color.rgb(24, 31, 48);
    private final int accent = Color.rgb(74, 112, 235);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(dark);
        getWindow().setNavigationBarColor(dark);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(244, 246, 251));
        TextView header = new TextView(this);
        header.setText("MY VIRTUAL OS  •  LITE");
        header.setTextColor(Color.WHITE);
        header.setTextSize(19);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setPadding(20, 22, 20, 22);
        header.setBackgroundColor(dark);
        root.addView(header);
        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(18, 18, 18, 18);
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        LinearLayout nav = new LinearLayout(this);
        nav.setBackgroundColor(Color.WHITE);
        for (String tab : new String[]{"Home", "Apps", "Instances", "Settings"}) {
            Button b = new Button(this);
            b.setText(tab); b.setTextSize(11); b.setAllCaps(false);
            b.setOnClickListener(v -> showPage(tab));
            nav.addView(b, new LinearLayout.LayoutParams(0, dp(54), 1));
        }
        root.addView(nav);
        setContentView(root);
        showPage("Home");
    }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density); }
    private void showPage(String page) {
        content.removeAllViews();
        title(page.equals("Home") ? "Welcome back" : page);
        if (page.equals("Home")) {
            card("Your virtual workspace", "A lightweight offline dashboard for managing demo virtual instances.");
            card("Status", "Ready • Offline mode");
            card("Membership", "Free access — no purchase required in this demo.");
            action("Start demo instance", () -> Toast.makeText(this, "Demo instance started", Toast.LENGTH_SHORT).show());
        } else if (page.equals("Apps")) {
            card("App library", "Your installed apps will appear here.");
            action("Refresh apps", () -> Toast.makeText(this, "App list refreshed", Toast.LENGTH_SHORT).show());
        } else if (page.equals("Instances")) {
            card("Virtual instances", "No real Android virtual machine is included in this Lite prototype.");
            action("Create demo instance", () -> Toast.makeText(this, "Demo instance created", Toast.LENGTH_SHORT).show());
        } else {
            card("Settings", "Version 1.0 • My Virtual OS Lite");
            card("Privacy", "This prototype does not send data to a server.");
            action("About", () -> Toast.makeText(this, "My Virtual OS Lite", Toast.LENGTH_SHORT).show());
        }
    }
    private void title(String value) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(25); t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setTextColor(dark); t.setPadding(2, 4, 2, 16);
        content.addView(t);
    }
    private void card(String heading, String body) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL); box.setPadding(18, 16, 18, 16);
        box.setBackgroundColor(Color.WHITE);
        TextView h = new TextView(this);
        h.setText(heading); h.setTextSize(17); h.setTypeface(Typeface.DEFAULT, Typeface.BOLD); h.setTextColor(dark);
        TextView d = new TextView(this);
        d.setText(body); d.setTextSize(14); d.setTextColor(Color.DKGRAY); d.setPadding(0, 8, 0, 0);
        box.addView(h); box.addView(d);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(12); content.addView(box, p);
    }
    private void action(String label, Runnable run) {
        Button b = new Button(this);
        b.setText(label); b.setAllCaps(false); b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(accent));
        b.setOnClickListener(v -> run.run());
        content.addView(b, new LinearLayout.LayoutParams(-1, dp(52)));
    }
}
