package com.deftskill.todoapplication.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deftskill.todoapplication.Model.Todo;
import com.deftskill.todoapplication.R;
import com.deftskill.todoapplication.db.TaskContract;
import com.deftskill.todoapplication.db.TaskDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private CustomAdapter mAdapter;
    private List<Todo> todoList= new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private TextView title,main_todo_empty;
    private String task,datestring;
     Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title);
        title.setText("To-Do-List");
        myCalendar = Calendar.getInstance();
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        main_todo_empty =findViewById(R.id.main_todo_empty);
        updateUI();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final TextView title = new TextView(MainActivity.this);
                title.setText("Title");
                layout.addView(title);
                final EditText titleEditText = new EditText(MainActivity.this);
                layout.addView(titleEditText);

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd, yyyy");
                datestring = sdf.format(new Date());
                final TextView tv_date = new TextView(MainActivity.this);
                tv_date.setText("Choose Date");
                layout.addView(tv_date);

                final EditText dateEditText = new EditText(MainActivity.this);
                dateEditText.setText(datestring);
                layout.addView(dateEditText);


                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        String myFormat = "MMMM, dd, yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        // edittext.setText(sdf.format(myCalendar.getTime()));
                        dateEditText.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                dateEditText.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            new DatePickerDialog(MainActivity.this, date,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                        return true;
                    }
                });

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("To-do-List")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 task = String.valueOf(titleEditText.getText());
                                 datestring = String.valueOf(dateEditText.getText());
                                 TaskDbHelper db =new TaskDbHelper(MainActivity.this);
                                 db.addTodo(new Todo(task,datestring,"0"));
                                 updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }


        });

    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    public void updateUI() {
        final TaskDbHelper myDB = new TaskDbHelper(MainActivity.this);
        todoList= myDB.getAllTodo();
        if (todoList.size() != 0) {
            main_todo_empty.setVisibility(View.GONE);
            mAdapter = new CustomAdapter(this, todoList);
            mTaskListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        } else {
            mAdapter = new CustomAdapter(this, todoList);
            mTaskListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            main_todo_empty.setVisibility(View.VISIBLE);
        }
        myDB.close();
    }

    public class CustomAdapter extends BaseAdapter {
        private Context context;
        List<Todo> todoList;
        private static final String TAG = "CustomAdapter";
        public CustomAdapter(Context context, List<Todo> todoList) {
            this.context = context;
            this.todoList = todoList;
        }



        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoList;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           final CustomAdapter.ViewHolder holder;
            final Todo todo = todoList.get(position);
            if(convertView==null){
                holder = new CustomAdapter.ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
                holder.task_title = (TextView) convertView.findViewById(R.id.task_title);
                holder.task_date = (TextView) convertView.findViewById(R.id.task_date);
                holder.statuspending = (CheckBox) convertView.findViewById(R.id.task_status_pending);
                holder.imageView =(ImageView)convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            }else{
                holder = (CustomAdapter.ViewHolder) convertView.getTag();
            }

            holder.task_title.setText(todo.getTitle());
            holder.task_date.setText(todo.getDate());
            Log.e(TAG, "getView: "+todo.getId()+todo.getTitle()+todo.getStatus() );
            if (todo.getStatus().contains("1")){
                holder.statuspending.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
            }else{
                holder.statuspending.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
            }
            holder.statuspending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.statuspending.isChecked())  {
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("status")
                                .setMessage("Select a status of this task")
                                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TaskDbHelper db =new TaskDbHelper(context);
                                        db.updateTodo(new Todo(todo.getTitle(),todo.getDate(),"1"));
                                        updateUI();
                                        holder.statuspending.setVisibility(View.GONE);
                                        holder.imageView.setVisibility(View.VISIBLE);


                                    }
                                })
                                .setNegativeButton("Pending", null)
                                .create();
                        dialog.show();

                    }
                }
            });

            return convertView;
        }

        private class ViewHolder{
            TextView task_title,task_date;
            CheckBox statuspending;
            ImageView imageView;

        }
    }

}

