package com.example.pruebanegocio;

import android.app.Activity;
import android.widget.TableLayout;

import java.util.ArrayList;

public abstract class ObjectTable {

    public abstract void loadTable(TableLayout table, ArrayList<String> relatedWords, Activity activity);
}
