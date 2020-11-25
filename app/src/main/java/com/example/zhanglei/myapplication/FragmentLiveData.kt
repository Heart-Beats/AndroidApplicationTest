package androidx.lifecycle

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.internal.SafeIterableMap

/**
 * @Author  张磊  on  2020/11/25 at 13:33
 * Email: 913305160@qq.com
 *
 * 仅为测试 Fragment 时使用，实际测试得出
 * Fragment 在 onDestroyView 之前:
 *   1. getViewLifecycleOwner().getLifecycle().getCurrentState() ---> DESTROYED
 *   2. 添加的 LifecycleEventObserver 得到的 Lifecycle.Event  -----> ON_DESTROY
 */

class FragmentLiveData<T> : LiveData<T>() {

    companion object {
        private const val TAG = "FragmentLiveData"
    }

    @SuppressLint("RestrictedApi")
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        assertMainThread("observe")
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }
        val wrapper = MyLifecycleBoundObserver(owner, observer)
        val mObserversField = this::class.java.getDeclaredField("mObservers")
        mObserversField.isAccessible = true
        val mObservers = mObserversField.get(this) as SafeIterableMap<Observer<in T>, Any>

        val existing = mObservers.putIfAbsent(observer, wrapper)
        require(existing == null) {
            ("Cannot add the same observer"
                    + " with different lifecycles")
        }
        owner.lifecycle.addObserver(wrapper)
    }

    private inner class MyLifecycleBoundObserver(owner: LifecycleOwner, observer: Observer<in T>) : LifecycleBoundObserver(owner, observer) {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            Log.d(TAG, "onStateChanged: 当前状态事件 == $event")

            if (event == Lifecycle.Event.ON_DESTROY) {
                removeObserver(mObserver)
                return
            }
            activeStateChanged(shouldBeActive())
        }
    }
}