//package cs371m.weatherwake;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.HapticFeedbackConstants;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
///**
// * Created by KC on 4/16/2016.
// */
//public class AlarmActivity implements BaseActivity {
//
//    ListView mAlarmList;
//    AlarmListAdapter mAlarmListAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.weather_wake_main);
//
//        mAlarmList = (ListView) findViewById(R.id.list);
//        mAlarmList.setLongClickable(true);
//        mAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
//                final Alarm alarm = (Alarm) mAlarmListAdapter.getItem(position);
//                AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmActivity.this);
//                dialog.setTitle("Delete");
//                dialog.setMessage("Delete this alarm?");
//                dialog.setPosititveButton("Yes", new onClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog) {
//                        database.init(AlarmActivity.this);
//                        database.deleteEntry(alarm);
////                        AlarmActivity.this.call
//
//                        updateAlarmList();
//                    }
//                });
//
//                return true;
//            }
//        });
//    }
//}
