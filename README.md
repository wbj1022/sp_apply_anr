# [方案一](https://github.com/wbj1022/sp_apply_anr/blob/master/ActivityThreadHelper.java)
原理参考[今日头条方案](https://www.jianshu.com/p/9ae0f6842689)

调用方法：
<pre name="code" class="java">
ActivityThreadHelper.tryHackActivityThreadH();
</pre>

如果系统API有改变，比如Activity onStop方法在Android 10上通过下面方法回调:
<pre name="code" class="java">
case ON_STOP:
    mTransactionHandler.handleStopActivity(r.token, false /* show */,
            0 /* configChanges */, mPendingActions, false /* finalStateRequest */,
            "LIFECYCLER_STOP_ACTIVITY");
</pre>
而不是跟以前一样通过mH发送消息回调，那么该方案就无效了。

# [方案二](https://github.com/wbj1022/sp_apply_anr/blob/master/SharedPreferencesWrapper.java)
重写Application和Activity的getSharedPreferences方法，
<pre name="code" class="java">
@Override
public SharedPreferences getSharedPreferences(String name, int mode) {
    return SharedPreferencesWrapper.get(super.getSharedPreferences(name, mode));
}
</pre>
返回封装后的SharedPreferencesWrapper实例，改写editor的apply方法如下：
<pre name="code" class="java">
@Override
public void apply() {
    sApplyHandler.post(new Runnable() {
        @Override
        public void run() {
            mInnerEditor.commit();
        }
    });
}
</pre>
该方案对第三方SDK内部的sp无效。

