package com.andor.animation.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CircleToRectTransition extends Transition {
    private static final String TAG = CircleToRectTransition.class.getSimpleName();
    private static final String BOUNDS = "viewBounds";
    private static final String[] PROPS = {BOUNDS};

    @Override
    public String[] getTransitionProperties() {
        return PROPS;
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        Rect bounds = new Rect();
        bounds.left = view.getLeft();
        bounds.right = view.getRight();
        bounds.top = view.getTop();
        bounds.bottom = view.getBottom();
        transitionValues.values.put(BOUNDS, bounds);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }

        if (!(startValues.view instanceof ImageButton)) {
            Log.w(CircleToRectTransition.class.getSimpleName(), "transition view should be CircleRectView");
            return null;
        }

        ImageButton view = (ImageButton) (startValues.view);

        Rect startRect = (Rect) startValues.values.get(BOUNDS);
        final Rect endRect = (Rect) endValues.values.get(BOUNDS);

        Animator animator;

        //scale animator
        animator = ViewAnimationUtils.createCircularReveal(view, startRect.height(), startRect.width(), endRect.height(), endRect.width());


        //movement animators below
        //if some translation not performed fully, use it instead of start coordinate
        float startX = startRect.left + view.getTranslationX();
        float startY = startRect.top + view.getTranslationY();

        //somehow end rect returns needed value minus translation in case not finished transition available
        float moveXTo = endRect.left + Math.round(view.getTranslationX());
        float moveYTo = endRect.top + Math.round(view.getTranslationY());

        Animator moveXAnimator = ObjectAnimator.ofFloat(view, "x", startX, moveXTo);
        Animator moveYAnimator = ObjectAnimator.ofFloat(view, "y", startY, moveYTo);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, moveXAnimator, moveYAnimator);

        //prevent blinking when interrupt animation
        return animatorSet;
    }
}
