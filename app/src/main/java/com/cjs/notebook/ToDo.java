package com.cjs.notebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class ToDo extends Activity {

    private TextView todocount;
    private TextView donecount;
    private ListView todolv;
    private ListView donelv;
    private ImageButton btn_edittodo;//添加的按钮
    private CheckBox result;//复选框
    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);//显示布局为todo.xml

        result = findViewById(R.id.cbox);
        btn_edittodo = findViewById(R.id.btn_edittodo);
        btn_edittodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Log.d("ToDo","点击添加按钮，弹出提示框输入文字。");
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);//prompts.xml 里的文本编辑框。弹出框可输入文字

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String InputContent = userInput.getText().toString();
                                        result.setText(userInput.getText());
                                        Log.d("ToDo","保存输入的内容");
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                        Log.d("ToDo","取消输入内容");
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

    }

}