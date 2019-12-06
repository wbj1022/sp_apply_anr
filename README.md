# sp_apply_anr
用来解决SharedPreferences apply引起的ANR.

#用法及原理
重写Application和Activity的getSharedPreferences方法，
@Override
public SharedPreferences getSharedPreferences(String name, int mode) {
    return SharedPreferencesWrapper.get(super.getSharedPreferences(name, mode));
}
返回封装后的SharedPreferencesWrapper实例，改写editor的apply方法如下：
@Override
public void apply() {
    sApplyHandler.post(new Runnable() {
        @Override
        public void run() {
            mInnerEditor.commit();
        }
    });
}

