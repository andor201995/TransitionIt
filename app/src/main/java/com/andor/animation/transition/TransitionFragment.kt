package com.andor.animation.transition

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import androidx.fragment.app.Fragment


class TransitionFragment : Fragment() {

    private lateinit var scene0: Scene
    private lateinit var scene1: Scene
    private lateinit var scene2: Scene
    private lateinit var sceneRoot: Scene
    private lateinit var transition: TransitionSet
    private var tapCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_transition, container, false) as ViewGroup

        //second scene
        val screen0 = inflater.inflate(R.layout.screen_0, container, false)
        val screen1 = inflater.inflate(R.layout.screen_1, container, false)
        val screen2 = inflater.inflate(R.layout.screen_2, container, false)
        val screenRoot = inflater.inflate(R.layout.fragment_transition, container, false)

        sceneRoot = Scene(root, screenRoot)
        scene0 = Scene(root, screen0)
        scene1 = Scene(root, screen1)
        scene2 = Scene(root, screen2)

        //create transition, set properties
        transition = TransitionSet()
        transition.duration = 2000
        transition.addTransition(ChangeBounds())
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(p0: Transition?) {
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
                setTapHereButtonListener()
            }
        })
        return root
    }

    override fun onStart() {
        super.onStart()
        //button setup
        setTapHereButtonListener()
    }

    private fun setTapHereButtonListener() {
        val button = view?.findViewById<ImageButton>(R.id.btn1)
        button?.setOnClickListener {
            when (tapCount) {
                0 -> TransitionManager.go(scene0, transition)
                1 -> TransitionManager.go(scene1, transition)
                2 -> TransitionManager.go(scene2, transition)
                3 -> {
                    TransitionManager.go(sceneRoot, transition)
                    tapCount = -1
                }
            }
            tapCount++
        }
    }
}
