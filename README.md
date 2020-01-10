# [方案一](https://github.com/wbj1022/sp_apply_anr/blob/master/ActivityThreadHelper.java)
原理参考[今日头条方案](https://www.jianshu.com/p/9ae0f6842689)

调用方法：
<pre name="code" class="java">
ActivityThreadHelper.tryHackActivityThreadH();
</pre>

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

