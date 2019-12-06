package me.dingtone.app.im.manager;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesWrapper implements SharedPreferences {

    private final static HandlerThread sApplyThread = new HandlerThread("sp_apply_thread");
    static {
        sApplyThread.start();
    }
    private final static Handler sApplyHandler = new Handler(sApplyThread.getLooper());
    private final static HashMap<SharedPreferences, SharedPreferencesWrapper> sMap = new HashMap<>();

    private SharedPreferences mInnerSp;

    private SharedPreferencesWrapper(SharedPreferences sharedPreferences) {
        mInnerSp = sharedPreferences;
    }

    public static synchronized SharedPreferences get(SharedPreferences sharedPreferences) {
        SharedPreferencesWrapper spWrapper = sMap.get(sharedPreferences);
        if (spWrapper == null) {
            spWrapper = new SharedPreferencesWrapper(sharedPreferences);
            sMap.put(sharedPreferences, spWrapper);
        }
        return spWrapper;
    }

    @Override
    public Map<String, ?> getAll() {
        return mInnerSp.getAll();
    }

    @Override
    public String getString(String key, String defValue) {
        return mInnerSp.getString(key, defValue);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return mInnerSp.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mInnerSp.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mInnerSp.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mInnerSp.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mInnerSp.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return mInnerSp.contains(key);
    }

    @Override
    public Editor edit() {
        return EditorWrapper.get(mInnerSp.edit());
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mInnerSp.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mInnerSp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static class EditorWrapper implements Editor {

        private Editor mInnerEditor;

        private final static HashMap<Editor, EditorWrapper> sMap = new HashMap<>();


        public static synchronized EditorWrapper get(Editor editor) {
            EditorWrapper editorWrapper = sMap.get(editor);
            if (editorWrapper == null) {
                editorWrapper = new EditorWrapper(editor);
                sMap.put(editor, editorWrapper);
            }
            return editorWrapper;
        }

        private EditorWrapper(Editor editor) {
            mInnerEditor = editor;
        }

        @Override
        public Editor putString(String key, String value) {
            return mInnerEditor.putString(key, value);
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            return mInnerEditor.putStringSet(key, values);
        }

        @Override
        public Editor putInt(String key, int value) {
            return mInnerEditor.putInt(key, value);
        }

        @Override
        public Editor putLong(String key, long value) {
            return mInnerEditor.putLong(key, value);
        }

        @Override
        public Editor putFloat(String key, float value) {
            return mInnerEditor.putFloat(key, value);
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            return mInnerEditor.putBoolean(key, value);
        }

        @Override
        public Editor remove(String key) {
            return mInnerEditor.remove(key);
        }

        @Override
        public Editor clear() {
            return mInnerEditor.clear();
        }

        @Override
        public boolean commit() {
            return mInnerEditor.commit();
        }

        @Override
        public void apply() {
            sApplyHandler.post(new Runnable() {
                @Override
                public void run() {
                    mInnerEditor.commit();
                }
            });
        }
    }
}
