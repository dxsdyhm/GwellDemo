package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.gwelldemo.R;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import Utils.RxBUSAction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.BindAlarmIdInfo;
import entity.DenfenceInfo;

public class AlarmSettingActivity extends BaseActivity {

    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.switch_alarm)
    Switch switchAlarm;
    @BindView(R.id.switch_defence)
    Switch switchDefence;
    @BindView(R.id.switch_motion)
    Switch switchMotion;

    private String idOrIp, password, userId;
    private String[] last_bind_data, new_data;
    private int max_alarm_count;
    private int count;
    private String TAG = "zxy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ButterKnife.bind(this);
        idOrIp = getIntent().getStringExtra("idOrIp");
        password = getIntent().getStringExtra("password");
        userId = getIntent().getStringExtra("userId");

        //获取各设置的初始状态
        P2PHandler.getInstance().getNpcSettings(idOrIp,
                password);
        P2PHandler.getInstance().getDefenceStates(idOrIp,
                password);
        P2PHandler.getInstance()
                .getBindAlarmId(idOrIp, password);

        switchAlarm.setEnabled(false);
        switchDefence.setEnabled(false);
        switchMotion.setEnabled(false);
    }

    boolean isBindAlarmId = false;

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_GET_BIND_ALARM_ID)
            }
    )
    public void getBindAlarmId(BindAlarmIdInfo bindAlarmIdInfo) {
        //获取绑定警报账号
        count = bindAlarmIdInfo.getData().length;
        last_bind_data = bindAlarmIdInfo.getData();
        max_alarm_count = bindAlarmIdInfo.getMaxCount();
        isBindAlarmId = false;

        for (int i = 0; i < count; i++) {
            if (last_bind_data[i].equals(userId)) {
                isBindAlarmId = true;
                break;
            }
        }

        if (count <= 0) {
            isBindAlarmId = false;
        }

        if (isBindAlarmId) {
            switchAlarm.setChecked(true);
            switchAlarm.setTag("1");
        } else {
            switchAlarm.setChecked(false);
            switchAlarm.setTag("0");
        }

        switchAlarm.setEnabled(true);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_SET_BIND_ALARM_ID)
            }
    )
    public void setBindAlarmId(BindAlarmIdInfo bindAlarmIdInfo) {
        //设置绑定警报账号
        if (bindAlarmIdInfo.getResult() == 0) {
            txt.append(getString(R.string.setting_succeed));
            txt.append("\n");
            if ("1".equals(switchAlarm.getTag())) {
                switchAlarm.setTag("0");
            } else {
                switchAlarm.setTag("1");
            }
        } else {
            txt.append(getString(R.string.setting_failure));
            txt.append("\n");
            if ("1".equals(switchAlarm.getTag())) {
                switchAlarm.setChecked(true);
            } else {
                switchAlarm.setChecked(false);
            }
        }
        switchAlarm.setEnabled(true);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_GET_REMOTE_DEFENCE)
            }
    )
    public void getRemoteDefence(DenfenceInfo denfenceInfo) {
        //获取布撤防状态
        if (!TextUtils.isEmpty(denfenceInfo.getContactId()) && idOrIp.equals(denfenceInfo.getContactId())) {
            int state = denfenceInfo.getState();
            if (state == 1) {
                switchDefence.setChecked(true);
                switchDefence.setTag("1");
            } else {
                switchDefence.setChecked(false);
                switchDefence.setTag("0");
            }
        }
        switchDefence.setEnabled(true);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_SET_REMOTE_DEFENCE)
            }
    )
    public void setRemoteDefence(DenfenceInfo denfenceInfo) {
        //设置布撤防状态
        if (denfenceInfo.getState() == 0) {
            txt.append(getString(R.string.setting_succeed));
            txt.append("\n");
            if ("1".equals(switchDefence.getTag())) {
                switchDefence.setTag("0");
            } else {
                switchDefence.setTag("1");
            }
        } else {
            txt.append(getString(R.string.setting_failure));
            txt.append("\n");
            if ("1".equals(switchDefence.getTag())) {
                switchDefence.setChecked(true);
            } else {
                switchDefence.setChecked(false);
            }
        }
        switchDefence.setEnabled(true);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_GET_MOTION)
            }
    )
    public void getMotionState(Integer state) {
        //获取移动侦测状态    state  0: off   1:on
        if (state == 1) {
            switchMotion.setChecked(true);
            switchMotion.setTag("1");
        } else {
            switchMotion.setChecked(false);
            switchMotion.setTag("0");
        }
        switchMotion.setEnabled(true);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_SET_MOTION)
            }
    )
    public void setMotionState(Integer result) {
        //获取移动侦测状态       result  0: off   1:on   255: 设备不支持
        if (result == 0) {
            txt.append(getString(R.string.setting_succeed));
            txt.append("\n");
            if ("1".equals(switchMotion.getTag())) {
                switchMotion.setTag("0");
            } else {
                switchMotion.setTag("1");
            }
        } else if (result == 255) {
            txt.append(getString(R.string.device_not_support) + ",");
            txt.append(getString(R.string.setting_failure));
            txt.append("\n");
            if ("1".equals(switchMotion.getTag())) {
                switchMotion.setChecked(true);
            } else {
                switchMotion.setChecked(false);
            }
        } else {
            txt.append(getString(R.string.setting_failure));
            txt.append("\n");
            if ("1".equals(switchMotion.getTag())) {
                switchMotion.setChecked(true);
            } else {
                switchMotion.setChecked(false);
            }
        }
        switchMotion.setEnabled(true);
    }


    private void setMotion(int value) {
        P2PHandler.getInstance().setMotion(idOrIp,
                password, value);
    }

    @OnClick({R.id.switch_alarm, R.id.switch_defence, R.id.switch_motion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_alarm:
                //关闭时绑定警报账号数组中移除当前账号，添加时绑定警报账号数组中添加当前账号
                switchAlarm.setEnabled(false);
                if ("1".equals(view.getTag())) {
                    txt.append(getString(R.string.alarm_on_to_off));
                    txt.append("\n");
                    //关闭
                    if (last_bind_data.length <= 0) {
                        new_data = new String[]{"0"};
                    } else {
                        new_data = new String[last_bind_data.length - 1];
                        if (new_data.length == 0) {
                            new_data = new String[]{"0"};
                        } else {
                            int num = 0;
                            for (int i = 0; i < last_bind_data.length; i++) {
                                if (!last_bind_data[i].equals(userId)) {
                                    new_data[num] = last_bind_data[i];
                                    num++;
                                }
                            }
                        }
                    }
                    // last_bind_data=new_data;
                    P2PHandler.getInstance().setBindAlarmId(idOrIp,
                            password, new_data.length, new_data);
                } else {
                    txt.append(getString(R.string.alarm_off_to_on));
                    txt.append("\n");
                    //开启
                    if (last_bind_data.length >= max_alarm_count) {
                        txt.append(getString(R.string.alarm_num_limit));
                        txt.append("\n");
                        return;
                    }
                    isBindAlarmId = false;
                    for (int i = 0; i < count; i++) {
                        if (last_bind_data[i].equals(userId)) {
                            isBindAlarmId = true;
                        }
                    }
                    if (isBindAlarmId) {
                        new_data = last_bind_data;
                    } else {
                        new_data = new String[last_bind_data.length + 1];
                        for (int i = 0; i < last_bind_data.length; i++) {
                            new_data[i] = last_bind_data[i];
                        }
                        new_data[new_data.length - 1] = userId;
                    }
                    // last_bind_data=new_data;

                    P2PHandler.getInstance().setBindAlarmId(idOrIp,
                            password, new_data.length, new_data);
                }

                break;
            case R.id.switch_defence:
                if ("1".equals(view.getTag())) {
                    txt.append(getString(R.string.denfence_to_not));
                    txt.append("\n");
                    P2PHandler.getInstance().setRemoteDefence(idOrIp, password,
                            0);
                } else {
                    txt.append(getString(R.string.not_to_denfence));
                    txt.append("\n");
                    P2PHandler.getInstance().setRemoteDefence(idOrIp, password,
                            1);
                }
                switchDefence.setEnabled(false);
                break;
            case R.id.switch_motion:
                if ("1".equals(view.getTag())) {
                    txt.append(getString(R.string.motion_on_to_off));
                    txt.append("\n");
                    setMotion(0);
                } else {
                    txt.append(getString(R.string.motion_off_to_on));
                    txt.append("\n");
                    setMotion(1);
                }
                switchMotion.setEnabled(false);
                break;
        }
    }

}
