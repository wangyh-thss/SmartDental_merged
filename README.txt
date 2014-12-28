关于一些合并的接口：

LoginActivity在登录成功后会在sharedPreference里记录用户id和用户名。
在其它Activity获取sharedPreference信息的方式为：
import android.content.SharedPreferences;
SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
int userid = preferences.getInt("userid", -1);
String username = preferences.getString("username", "");

医生可以看到每个病人的详情。每个圈子的所有病人的列表在AllPatientActivity中实现，单击每个病人会跳转到其详情页面PatientInfoActivity。此病人的id保存在Intent的Extra中，通过getIntent().getIntExtra("patient_id",  -1)获取其用户id。
详见PatientInfoActivity.java

合作愉快，祝好！


nezharen

