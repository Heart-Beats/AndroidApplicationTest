package com.example.zhanglei.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class MyAnimatorSet {

    private AnimatorSet animatorSet;
    private AnimatorSet.Builder animotorBuilder;

    public MyAnimatorSet() {
        if (animatorSet == null) {
            this.animatorSet = new AnimatorSet();
        }
    }

    public MyAnimatorSet(AnimatorSet animatorSet) {
        this.animatorSet = animatorSet;
    }

    public void setPlayWithAnimator(Animator... animators) {
        for (int i = 0; i < animators.length; i++) {
            if (i == 0) {
                this.animotorBuilder = animatorSet.play(animators[i]);
            } else {
                //和前面动画一起执行
                this.animotorBuilder.with(animators[i]);
            }
        }
    }

    /**
     *
     * @param animators 当前动画执行之前需要执行的动画
     */
    public void setBeforeAnimator(Animator... animators) {
        for (Animator animator : animators) {
            //在该动画之后执行前面的动画
            animotorBuilder.after(animator);
        }
    }

    /**
     *
     * @param animators 后续需要执行动画
     */
    public void setAfterAnimator(Animator... animators) {
        for (Animator animator : animators) {
            // 在该动画之前执行前面的动画
            animotorBuilder.before(animator);

        }
    }

    public void setListener(Animator.AnimatorListener animatorSetListener) {
        this.animatorSet.addListener(animatorSetListener);
    }

    public void removeListener(Animator.AnimatorListener animatorSetListener) {
        this.animatorSet.removeListener(animatorSetListener);
    }

    public void removeAllListeners() {
        this.animatorSet.removeAllListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setPauseListener(Animator.AnimatorPauseListener animatorPauseListener) {
        this.animatorSet.addPauseListener(animatorPauseListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void removePauseListener(Animator.AnimatorPauseListener animatorPauseListener) {
        this.animatorSet.removePauseListener(animatorPauseListener);
    }

    public void start() {
        this.animatorSet.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pause() {
        this.animatorSet.pause();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resume() {
        this.animatorSet.resume();
    }

    public void canCel() {
        this.animatorSet.cancel();
    }

    public void end() {
        this.animatorSet.end();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reverse() {
        this.animatorSet.reverse();
    }

    public boolean isStart() {
        return this.animatorSet.isStarted();
    }

    public boolean isRunning() {
        return this.animatorSet.isRunning();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isPause() {
        return this.animatorSet.isPaused();
    }

}
